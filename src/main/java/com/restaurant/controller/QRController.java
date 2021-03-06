package com.restaurant.controller;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.restaurant.controller.helper.QRCodeHelper;
import com.restaurant.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class QRController {

    private final static String androidUrl = "http://android.com";
    private final static String iosUrl = "http://ios.com";

    @Autowired
    private QRService qrService;

    @GetMapping(value = "/generateqr", produces="application/zip" )
    @ResponseBody
    public void generateqr(@RequestParam int id,  HttpServletResponse response) throws IOException, WriterException, NoSuchAlgorithmException {
        response.setHeader("Content-Disposition","attachment;filename=\"QRCodes.zip\"");
        List<BitMatrix> bitMatrices = qrService.getBitMatrixList(id);
        try (ZipOutputStream zout = new ZipOutputStream(response.getOutputStream())){
            for (int i = 0; i < bitMatrices.size(); i++) {
                ByteArrayOutputStream out = QRCodeHelper.createByteArrayFromQRCode(bitMatrices.get(i));

                ZipEntry entry = new ZipEntry(i + ".png");
                zout.putNextEntry(entry);
                zout.write(out.toByteArray());
                zout.closeEntry();
            }
        }
    }

    @ExceptionHandler(value = {IOException.class, WriterException.class, NoSuchAlgorithmException.class})
    public String bonusExceptionHandler(Exception e){
        return e.getMessage();
    }

    @GetMapping("/qr")
    @ResponseBody
    public ModelAndView qr(HttpServletRequest request, ModelAndView model) {
        String userAgent = request.getHeader("user-agent");
        if (userAgent.matches(".*Android.*")) {
            model.setViewName("redirect:" + androidUrl);
        } else if (userAgent.matches(".*iPhone.*") || userAgent.matches(".*iPad.*")){
            model.setViewName("redirect:" + iosUrl);
        } else {
            model.setViewName("/qr2");
            model.addObject("androidUrl", androidUrl);
            model.addObject("iosUrl", iosUrl);
        }
        return model;
    }



}
