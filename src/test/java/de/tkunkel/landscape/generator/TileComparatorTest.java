package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.MapTileCandidate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class TileComparatorTest {

    @Test
    public void isPossibleNeighbour_normal() {
        TileComparator tileComparator = new TileComparator();
        MapTileCandidate me = generateFileCandidate(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, "a.png");
        MapTileCandidate other = generateFileCandidate(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, "b.png");
        boolean possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.NORTH);
        Assertions.assertTrue(possibleNeighbour);

        me = generateFileCandidate(Color.BLUE, Color.BLUE, Color.BLUE, Color.BLUE, "a.png");
        other = generateFileCandidate(Color.RED, Color.RED, Color.RED, Color.RED, "b.png");
        possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.NORTH);
        Assertions.assertFalse(possibleNeighbour);
    }

    @Test
    public void isPossibleNeighbour_normal_east() {
        TileComparator tileComparator = new TileComparator();
        MapTileCandidate me = generateFileCandidate(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, "a.png");
        MapTileCandidate other = generateFileCandidate(Color.GREEN, Color.YELLOW, Color.BLUE, Color.RED, "b.png");
        boolean possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.NORTH);
        Assertions.assertTrue(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.EAST);
        Assertions.assertTrue(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.SOUTH);
        Assertions.assertTrue(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.WEST);
        Assertions.assertTrue(possibleNeighbour);

    }

    @Test
    public void isPossibleNeighbour_nulls() {
        TileComparator tileComparator = new TileComparator();
        MapTileCandidate me = generateFileCandidate(Color.BLUE, null, null, null, "a.png");
        MapTileCandidate other = generateFileCandidate(null, null, null, null, "b.png");
        boolean possibleNeighbour = tileComparator.isPossibleNeighbour(me, other, Direction.NORTH);
        Assertions.assertFalse(possibleNeighbour);
    }

    private MapTileCandidate generateFileCandidate(Color north, Color east, Color south, Color west, String fileName) {
        MapTileCandidate mapTileCandidate = new MapTileCandidate();
        mapTileCandidate.borderInfo = new BorderInfo();
        mapTileCandidate.fileName = fileName;
        mapTileCandidate.borderInfo.north = new Color[]{north, north, north};
        mapTileCandidate.borderInfo.east = new Color[]{east, east, east};
        mapTileCandidate.borderInfo.south = new Color[]{south, south, south};
        mapTileCandidate.borderInfo.west = new Color[]{west, west, west};

        return mapTileCandidate;
    }

    @Test
    public void aaaaaaa() throws IOException {
        BorderDetector borderDetector = new BorderDetector();

        MapTileCandidate mapTileCandidateNorth = new MapTileCandidate();
        mapTileCandidateNorth.borderInfo = borderDetector.detectBorder("demo/up.png");
        mapTileCandidateNorth.fileName = "demo/up.png";

        MapTileCandidate mapTileCandidateEast = new MapTileCandidate();
        mapTileCandidateEast.borderInfo = borderDetector.detectBorder("demo/right.png");
        mapTileCandidateEast.fileName = "demo/right.png";

        MapTileCandidate mapTileCandidateSouth = new MapTileCandidate();
        mapTileCandidateSouth.borderInfo = borderDetector.detectBorder("demo/down.png");
        mapTileCandidateSouth.fileName = "demo/down.png";

        MapTileCandidate mapTileCandidateWest = new MapTileCandidate();
        mapTileCandidateWest.borderInfo = borderDetector.detectBorder("demo/left.png");
        mapTileCandidateWest.fileName = "demo/left.png";

        TileComparator tileComparator = new TileComparator();
        boolean possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateNorth, mapTileCandidateSouth,
                Direction.NORTH);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateEast, mapTileCandidateWest,
                Direction.EAST);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateWest, mapTileCandidateEast,
                Direction.WEST);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateSouth, mapTileCandidateNorth,
                Direction.SOUTH);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateNorth, mapTileCandidateWest,
                Direction.EAST);
        Assertions.assertTrue(possibleNeighbour);
///////////////
        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateNorth, mapTileCandidateEast,
                Direction.EAST);
        Assertions.assertFalse(possibleNeighbour);
    }

    @Test
    public void getPossibleNeighbours() {
        TileComparator tileComparator = new TileComparator();
        List<MapTileCandidate> allTiles = new ArrayList<>();
        MapTileCandidate meTile = generateFileCandidate(Color.BLUE, Color.RED, Color.GREEN, Color.BLUE, "a.png");

        List<MapTileCandidate> neighbours = new ArrayList<>();
        neighbours.add(generateFileCandidate(Color.WHITE, Color.WHITE, Color.BLUE, Color.WHITE, "a.png"));
        neighbours.add(generateFileCandidate(Color.WHITE, Color.WHITE, Color.WHITE, Color.RED, "b.png"));
        neighbours.add(generateFileCandidate(Color.GREEN, Color.WHITE, Color.WHITE, Color.WHITE, "c.png"));
        neighbours.add(generateFileCandidate(Color.WHITE, Color.BLUE, Color.WHITE, Color.WHITE, "d.png"));

        Collection<MapTileCandidate> possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, allTiles, Direction.NORTH);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("a.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, allTiles, Direction.EAST);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("b.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, allTiles, Direction.SOUTH);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("c.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, allTiles, Direction.WEST);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("d.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);
    }

}
