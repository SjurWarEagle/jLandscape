package de.tkunkel.landscape.generator;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class BorderDetector {

    public BorderInfo detectBorder(String fileName) throws IOException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file '" + fileName + "' not found!");
        }
        BufferedImage bufferedImage = ImageIO.read(resource);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();

        BorderInfo borderInfo = new BorderInfo();

        int verticalSteps = width / BorderInfo.cntAttachmentPoints;
        int horizontalSteps = height / BorderInfo.cntAttachmentPoints;

        detectBorderNorth(bufferedImage, width, borderInfo, verticalSteps);
        detectBorderEast(bufferedImage, width, borderInfo, horizontalSteps, height);
        detectBorderSouth(bufferedImage, width, borderInfo, verticalSteps, height);
        detectBorderWest(bufferedImage, borderInfo, horizontalSteps, height);

        return borderInfo;
    }

    private static void detectBorderNorth(BufferedImage bufferedImage, int width, BorderInfo borderInfo, int stepSize) {
        int cnt =0;
        for (int x = stepSize; x <= width; x += stepSize) {
            int rgb = bufferedImage.getRGB(Math.min(x,width-1), 0);
            borderInfo.north[cnt++]=new Color(rgb);
        }
    }

    private static void detectBorderWest(BufferedImage bufferedImage, BorderInfo borderInfo, int stepSize, int height) {
        int cnt =0;
        for (int y = stepSize; y <= height; y += stepSize) {
            int rgb = bufferedImage.getRGB(0, Math.min(y,height-1));
            borderInfo.west[cnt++]=new Color(rgb);
        }
    }

    private static void detectBorderEast(BufferedImage bufferedImage, int width, BorderInfo borderInfo, int stepSize, int height) {
        int cnt =0;
        for (int y = stepSize; y <= height; y += stepSize) {
            int rgb = bufferedImage.getRGB(width-1, Math.min(y,height-1));
            borderInfo.east[cnt++]=new Color(rgb);
        }
    }

    private static void detectBorderSouth(BufferedImage bufferedImage, int width, BorderInfo borderInfo, int stepSize, int height) {
        int cnt =0;
        for (int x = stepSize; x <= width; x += stepSize) {
            int rgb = bufferedImage.getRGB(Math.min(x,width-1), height-1);
            borderInfo.south[cnt++]=new Color(rgb);
        }
    }
}
