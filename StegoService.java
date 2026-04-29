package com.project.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class StegoService {

    /**
     * Encodes a text message into a generated PNG image.
     * Each character is converted to an RGB pixel value.
     */
    public void encodeMessage(String message, File outputFile) {
        try {
            int width = 300;
            int height = 300;

            // Create a blank canvas
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            int[] pixels = message.chars().toArray();
            int x = 0, y = 0;

            for (int i = 0; i < pixels.length; i++) {
                int value = pixels[i];
                // Store the character value in R, G, and B channels
                int rgb = (value << 16) | (value << 8) | value;

                image.setRGB(x, y, rgb);

                x++;
                if (x >= width) {
                    x = 0;
                    y++;
                }
            }

            // Save using the File object provided
            ImageIO.write(image, "png", outputFile);
            System.out.println("Stego image saved: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Encoding Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Reads an image and extracts the hidden message.
     */
    public String decodeMessage(String imagePath) {
        StringBuilder message = new StringBuilder();

        try {
            BufferedImage image = ImageIO.read(new File(imagePath));

            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {

                    int rgb = image.getRGB(x, y);
                    // Extract the value from the Red channel
                    int value = (rgb >> 16) & 0xFF;

                    // If we hit a black pixel (0), it means the message ended
                    if (value == 0) return message.toString();

                    message.append((char) value);
                }
            }
        } catch (Exception e) {
            System.err.println("Decoding Error: " + e.getMessage());
        }

        return message.toString();
    }
}