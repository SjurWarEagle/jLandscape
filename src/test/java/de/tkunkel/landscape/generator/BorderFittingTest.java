package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.MapTileCandidate;
import de.tkunkel.landscape.types.Direction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class BorderFittingTest {

    private static MapTileCandidate loadMapTile(String fileName) throws IOException {
        BorderDetector borderDetector = new BorderDetector();
        MapTileCandidate mapTileCandidateNorth = new MapTileCandidate();
        mapTileCandidateNorth.borderInfo = borderDetector.detectBorder(fileName);
        mapTileCandidateNorth.fileName = fileName;
        return mapTileCandidateNorth;
    }

    @Test
    public void checkFitting_circuit() throws IOException {
        MapTileCandidate circuit0 = loadMapTile("circuit/0.png");
        MapTileCandidate circuit1 = loadMapTile("circuit/1.png");
        MapTileCandidate circuit2 = loadMapTile("circuit/2_rotated_0.png");
        MapTileCandidate circuit3 = loadMapTile("circuit/3_rotated_0.png");
        MapTileCandidate circuit4 = loadMapTile("circuit/4_rotated_0.png");
        MapTileCandidate circuit5 = loadMapTile("circuit/5_rotated_0.png");
        MapTileCandidate circuit6 = loadMapTile("circuit/6_rotated_0.png");
        MapTileCandidate circuit7 = loadMapTile("circuit/7.png");
        MapTileCandidate circuit8 = loadMapTile("circuit/8_rotated_0.png");
        MapTileCandidate circuit9 = loadMapTile("circuit/9_rotated_0.png");
        MapTileCandidate circuit10 = loadMapTile("circuit/10_rotated_0.png");
        MapTileCandidate circuit11 = loadMapTile("circuit/11_rotated_0.png");
        MapTileCandidate circuit12 = loadMapTile("circuit/12_rotated_0.png");

        TileComparator tileComparator = new TileComparator();
        boolean possibleNeighbour = tileComparator.isPossibleNeighbour(circuit0, circuit0, Direction.NORTH);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit4, circuit5, Direction.SOUTH);
        Assertions.assertTrue(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit4, circuit5, Direction.NORTH);
        Assertions.assertFalse(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit0, circuit4, Direction.EAST);
        Assertions.assertTrue(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit3, circuit2, Direction.EAST);
        Assertions.assertFalse(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit2, circuit3, Direction.WEST);
        Assertions.assertFalse(possibleNeighbour);

        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit12, circuit1, Direction.SOUTH);
        Assertions.assertTrue(possibleNeighbour);
        possibleNeighbour = tileComparator.isPossibleNeighbour(circuit12, circuit1, Direction.NORTH);
        Assertions.assertTrue(possibleNeighbour);
    }
}
