package de.tkunkel.landscape.map;

import java.util.Arrays;

public class Map {
    public final int width;
    public final int height;
    public final MapTile[][] grid;

    public Map(int width, int height) {
        grid = new MapTile[width][height];
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Map{" +
                "width=" + width +
                ", height=" + height +
                ", grid=" + Arrays.toString(grid) +
                '}';
    }
}
