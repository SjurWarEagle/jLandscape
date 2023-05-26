package de.tkunkel.landscape.map;

public class Map {
    public MapTile[][] grid;

    public Map(int width, int height){
        grid = new MapTile[width][height];
    }
}
