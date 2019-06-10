package com.restaurant.utils;


import org.springframework.http.MediaType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ResizeImage {

    /**
     * Resizes an image to a absolute width and height (the image may not be
     * proportional)
     * @param inputImage BufferedImage of the original image
//     * @param outputImagePath Path to save the resized image
     * @param scaledWidth absolute width in pixels
     * @param scaledHeight absolute height in pixels
     * @throws IOException
     */
    private static byte[] resize(BufferedImage inputImage,
                              int scaledWidth, int scaledHeight, String type)
            throws IOException {
        // reads input image
//        File inputFile = new File(inputImagePath);
//        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
//        String formatName = outputImagePath.substring(outputImagePath
//                .lastIndexOf(".") + 1);

        // writes to output file
//        ImageIO.write(outputImage, formatName, new File(outputImagePath));
        ByteArrayOutputStream baos=new ByteArrayOutputStream(1024 * 3);
        String formatName = "";
        switch (type) {
            case MediaType.IMAGE_JPEG_VALUE:
                formatName = "jpg";
                break;
            case MediaType.IMAGE_PNG_VALUE:
                formatName = "png";
                break;
        }
        ImageIO.write(outputImage, formatName, baos);
        baos.flush();
        return baos.toByteArray();
    }

    /**
     * Resizes an image by a percentage of original size (proportional).
     * @param inputStream InputStream of the original image
//     * @param outputImagePath Path to save the resized image
     * @param size a enum number specifies width of the output image
     * over the input image.
     * @throws IOException
     */
    public static byte[] resize(InputStream inputStream, Size size, String type) throws IOException {
        BufferedImage inputImage = ImageIO.read(inputStream);
        double percent = 1;
        if (size != Size.FULL) {
            percent = inputImage.getWidth() > size.getSize() ? (float) size.getSize() / inputImage.getWidth() : 1;
        }
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImage, scaledWidth, scaledHeight, type);
    }




    public enum Size {

        LOGO("", 400),
        PHOTO("", 1000),
        FULL("",0);

        private String description;
        private int size;

        Size(String description, int size) {
            this.description = description;
            this.size = size;
        }

        public int getSize() {
            return size;
        }
    }

}
