package de.tkunkel.landscape.types;

import java.awt.*;
import java.util.Arrays;

public class BorderInfo {
    public static final int cntAttachmentPoints = 5;
    public Color[] north = new Color[cntAttachmentPoints];
    public Color[] east = new Color[cntAttachmentPoints];
    public Color[] south = new Color[cntAttachmentPoints];
    public Color[] west = new Color[cntAttachmentPoints];

    @Override
    public String toString() {
        return "BorderInfo{" +
                "north=" + Arrays.toString(north) +
                ", east=" + Arrays.toString(east) +
                ", south=" + Arrays.toString(south) +
                ", west=" + Arrays.toString(west) +
                '}';
    }
}
