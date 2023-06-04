package de.tkunkel.landscape.renderer;

import de.tkunkel.landscape.map.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Service
public class MapRenderer {
    private final int cellSize = 25;
    Logger LOG = LoggerFactory.getLogger(MapRenderer.class);

    public void render(Map map) throws IOException {
        int width = map.width * cellSize;
        int height = map.height * cellSize;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        // Set background color
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, width, height);

        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid[x].length; y++) {
                String fileName = map.grid[x][y].candidates.stream().toList().get(0).fileName;
                BufferedImage tileImage = readImage(fileName);

                // we assume, that the tiles and the target are square
                double scaleFactor = (double) cellSize / tileImage.getWidth();
                g2d.drawImage(scaleImage(tileImage, scaleFactor), x * cellSize, y * cellSize, null);
            }
        }

        try {
            File output = new File("output/tmp.png");
            new File("output").mkdir();
            ImageIO.write(image, "png", output);
        } catch (IOException e) {
            LOG.error("Error saving image: " + e.getMessage());
        }
    }

    private static BufferedImage scaleImage(BufferedImage before, double scale) {
        int w = before.getWidth();
        int h = before.getHeight();
        // Create a new image of the proper size
        int w2 = (int) (w * scale);
        int h2 = (int) (h * scale);
        BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
        AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_BILINEAR);

        scaleOp.filter(before, after);
        return after;
    }

    private BufferedImage readImage(String fileName) throws IOException {
        URL resource = getClass().getClassLoader().getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file '" + fileName + "' not found!");
        }

        return ImageIO.read(resource);
    }
}
