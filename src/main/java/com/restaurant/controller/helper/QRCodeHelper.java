package com.restaurant.controller.helper;

import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.util.ResourceUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeHelper {

    public static BufferedImage createImageFromQrCode(BitMatrix bitMatrix) throws IOException {
        BufferedImage image = ImageIO.read(ResourceUtils.getFile("classpath:photo/печать 3.jpg"));
        BufferedImage overlay = MatrixToImageWriter.toBufferedImage(bitMatrix);
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.drawImage(overlay, 381, 2198, null);
        return combined;
    }


    public static ByteArrayOutputStream createByteArrayFromQRCode(BitMatrix bitMatrix) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BufferedImage bufferedImage = createImageFromQrCode(bitMatrix);
        ImageIO.write(bufferedImage, "png", out);
        return out;
    }
}
