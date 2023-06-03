package de.tkunkel.landscape.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class BorderDetectorTest {

    @Test
    public void detectBorder_demoTileset() throws IOException {
        BorderDetector borderDetector = new BorderDetector();
        BorderInfo borderInfo = borderDetector.detectBorder("demo/left.png");
        Assertions.assertNotNull(borderInfo);
        assertNotNull(borderInfo.north);
        assertNotNull(borderInfo.east);
        assertEquals(3,borderInfo.east.length);
        assertNotNull(borderInfo.east[0]);
        assertNotNull(borderInfo.east[1]);
        assertNotNull(borderInfo.east[2]);
        assertNotNull(borderInfo.south);
        assertNotNull(borderInfo.west);
    }

    @Test
    public void detectBorder() throws IOException {
        BorderDetector borderDetector = new BorderDetector();
        BorderInfo borderInfo = borderDetector.detectBorder("50_x_50_red.png");
        assertNotNull(borderInfo);
        assertNotNull(borderInfo.north);
        assertNotNull(borderInfo.east);
        assertNotNull(borderInfo.south);
        assertNotNull(borderInfo.west);
        for (int i = 0; i < BorderInfo.cntAttachmentPoints; i++) {
            assertNotNull(borderInfo.north[i], "north " + i + " is null");
            assertNotNull(borderInfo.east[i], "east " + i + " is null");
            assertNotNull(borderInfo.south[i], "south " + i + " is null");
            assertNotNull(borderInfo.west[i], "west " + i + " is null");
        }

        for (int i = 0; i < BorderInfo.cntAttachmentPoints; i++) {
            assertEquals(borderInfo.north[i], new Color(255,0,0));
            assertEquals(borderInfo.east[i], new Color(255,0,0));
            assertEquals(borderInfo.south[i], new Color(255,0,0));
            assertEquals(borderInfo.west[i], new Color(255,0,0));
        }
    }

    @Test
    public void detectBorder_notFound() {
        BorderDetector borderDetector = new BorderDetector();
        assertThrows(IllegalArgumentException.class, () -> {
            borderDetector.detectBorder("notFound.png");
        });
    }
}
