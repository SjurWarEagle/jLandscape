package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.map.MapTileCandidate;
import de.tkunkel.landscape.types.BorderInfo;
import de.tkunkel.landscape.types.Direction;
import de.tkunkel.landscape.types.NoTileCandidatesLeft;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

class TileComparatorTest {

//    @Test
//    public void updatePotentialCandidates_normal_circuit() throws NoTileCandidatesLeft, URISyntaxException, IOException {
//        LandscapeGenerator landscapeGenerator = new LandscapeGenerator("circuit", 5, 5);
//        Map map = landscapeGenerator.createEmptyMap();
//        landscapeGenerator.updatePotentialCandidates(map);
//        Assertions.assertEquals(5, map.grid.length);
//
//        Assertions.assertEquals(5, map.grid[0].length);
//        Assertions.assertEquals(5, map.grid[1].length);
//        Assertions.assertEquals(5, map.grid[2].length);
//        Assertions.assertEquals(5, map.grid[3].length);
//        Assertions.assertEquals(5, map.grid[4].length);
//
//        Assertions.assertEquals(13, map.grid[0][0].candidates.size());
//        Assertions.assertEquals(13, map.grid[0][1].candidates.size());
//        //tile 5 cannot be o
//        Assertions.assertEquals(12, map.grid[1][0].candidates.size());
//        Assertions.assertEquals(13, map.grid[1][1].candidates.size());
//
//        landscapeGenerator.updatePotentialCandidates(map);
//
//        Assertions.assertEquals(13, map.grid[0][0].candidates.size());
//        Assertions.assertEquals(13, map.grid[0][1].candidates.size());
//        Assertions.assertEquals(13, map.grid[1][0].candidates.size());
//        Assertions.assertEquals(13, map.grid[1][1].candidates.size());
//    }

    @Test
    public void updatePotentialCandidates_normal_demo() throws NoTileCandidatesLeft, URISyntaxException, IOException {
        LandscapeGenerator landscapeGenerator = new LandscapeGenerator(new BorderDetector());
        landscapeGenerator.setConfigParameter("demo", 2, 2);
        Map map = landscapeGenerator.createEmptyMap();
        landscapeGenerator.updatePotentialCandidates(map);
        Assertions.assertEquals(2, map.grid.length);
        Assertions.assertEquals(2, map.grid[0].length);
        Assertions.assertEquals(2, map.grid[1].length);
        Assertions.assertEquals(5, map.grid[0][0].candidates.size());
        Assertions.assertEquals(5, map.grid[0][1].candidates.size());
        Assertions.assertEquals(5, map.grid[1][0].candidates.size());
        Assertions.assertEquals(5, map.grid[1][1].candidates.size());

        map.grid[0][0].candidates.clear();
        map.grid[0][0].candidates.add(loadMapTile("demo/blank.png"));

        landscapeGenerator.updatePotentialCandidates(map);
        Assertions.assertEquals(2, map.grid.length);
        Assertions.assertEquals(2, map.grid[0].length);
        Assertions.assertEquals(2, map.grid[1].length);
        Assertions.assertEquals(1, map.grid[0][0].candidates.size());
        Assertions.assertEquals(2, map.grid[0][1].candidates.size());
        Assertions.assertEquals(2, map.grid[1][0].candidates.size());
        Assertions.assertEquals(5, map.grid[1][1].candidates.size());

    }

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
    public void check_realImage_alignment() throws IOException {
        MapTileCandidate mapTileCandidateBlank = loadMapTile("demo/blank.png");
        MapTileCandidate mapTileCandidateNorth = loadMapTile("demo/up.png");
        MapTileCandidate mapTileCandidateEast = loadMapTile("demo/right.png");
        MapTileCandidate mapTileCandidateSouth = loadMapTile("demo/down.png");
        MapTileCandidate mapTileCandidateWest = loadMapTile("demo/left.png");

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

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateBlank, mapTileCandidateEast,
                Direction.EAST);
        Assertions.assertTrue(possibleNeighbour);

///////////////
        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateNorth, mapTileCandidateEast,
                Direction.EAST);
        Assertions.assertFalse(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(mapTileCandidateBlank, mapTileCandidateWest,
                Direction.EAST);
        Assertions.assertFalse(possibleNeighbour);
    }

    private static MapTileCandidate loadMapTile(String fileName) throws IOException {
        BorderDetector borderDetector = new BorderDetector();
        MapTileCandidate mapTileCandidateNorth = new MapTileCandidate();
        mapTileCandidateNorth.borderInfo = borderDetector.detectBorder(fileName);
        mapTileCandidateNorth.fileName = fileName;
        return mapTileCandidateNorth;
    }

    @Test
    public void getPossibleNeighbours() {
        TileComparator tileComparator = new TileComparator();
        MapTileCandidate meTile = generateFileCandidate(Color.BLUE, Color.RED, Color.GREEN, Color.BLUE, "a.png");

        List<MapTileCandidate> neighbours = new ArrayList<>();
        neighbours.add(generateFileCandidate(Color.WHITE, Color.WHITE, Color.BLUE, Color.WHITE, "a.png"));
        neighbours.add(generateFileCandidate(Color.WHITE, Color.WHITE, Color.WHITE, Color.RED, "b.png"));
        neighbours.add(generateFileCandidate(Color.GREEN, Color.WHITE, Color.WHITE, Color.WHITE, "c.png"));
        neighbours.add(generateFileCandidate(Color.WHITE, Color.BLUE, Color.WHITE, Color.WHITE, "d.png"));

        Collection<MapTileCandidate> possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, Direction.NORTH);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("a.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, Direction.EAST);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("b.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, Direction.SOUTH);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("c.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);

        possibleNeighbours = tileComparator.getPossibleNeighbours(meTile, neighbours, Direction.WEST);
        Assertions.assertNotNull(possibleNeighbours);
        Assertions.assertEquals(1, possibleNeighbours.size());
        Assertions.assertEquals("d.png", ((MapTileCandidate) possibleNeighbours.toArray()[0]).fileName);
    }

}
