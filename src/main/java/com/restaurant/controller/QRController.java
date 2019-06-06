package com.restaurant.controller;

import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.restaurant.controller.helper.QRCodeHelper;
import com.restaurant.service.QRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
public class QRController {


    @Autowired
    private QRService qrService;

    @GetMapping(value = "/qr", produces="application/zip" )
    @ResponseBody
    public void qr(@RequestParam int id,  HttpServletResponse response) throws IOException, WriterException, NoSuchAlgorithmException {
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





}
