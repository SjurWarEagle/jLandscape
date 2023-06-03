package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.map.MapTile;
import de.tkunkel.landscape.map.MapTileCandidate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class LandscapeGenerator {

    private final String tileSet;
    private final BorderDetector borderDetector = new BorderDetector();
    private final ArrayList<MapTileCandidate> allPossibleMapTileCandidates = new ArrayList<>();

    public LandscapeGenerator(String tileSet) {
        this.tileSet = tileSet;
    }

    public Map createEmptyMap() throws URISyntaxException, IOException, NoTileCandidatesLeft {
        ArrayList<MapTileCandidate> mapTileCandidates = collectAllPossibleTiles(this.tileSet);

        Map map = new Map(3, 1);
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid[x].length; y++) {
                map.grid[x][y] = new MapTile();
                map.grid[x][y].candidates = new ArrayList<>(mapTileCandidates);
            }
        }
        updatePotentialCandidates(map);

        return map;
    }

    private ArrayList<MapTileCandidate> collectAllPossibleTiles(String tileSet) throws URISyntaxException, IOException {
        if (this.allPossibleMapTileCandidates.size() > 0) {
            return new ArrayList<>(this.allPossibleMapTileCandidates);
        }

        URL resource = getClass().getClassLoader().getResource(tileSet);
        if (resource == null) {
            throw new IllegalArgumentException("folder '" + tileSet + "' not found!");
        }

        ArrayList<MapTileCandidate> allCandidates = new ArrayList<>();

        File folder = new File(resource.toURI());
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".png")) {
                    MapTileCandidate mapTile = new MapTileCandidate();
                    mapTile.fileName = this.tileSet + "/" + file.getName();
                    mapTile.borderInfo = borderDetector.detectBorder(mapTile.fileName);
                    allCandidates.add(mapTile);
                }
            }
        }
        this.allPossibleMapTileCandidates.clear();
        this.allPossibleMapTileCandidates.addAll(allCandidates);
        return allCandidates;
    }

    public MapTile findNextTileToCollapse(Map map) {
        Optional<MapTile> next = Arrays.stream(map.grid).flatMap(Arrays::stream).filter(mapTile -> mapTile.candidates.size() > 1).min(Comparator.comparingInt(o -> o.candidates.size()));

        return next.orElse(null);
    }

    public boolean isUncollapsedTilesExists(Map map) {
        boolean rest = Arrays.stream(map.grid).flatMap(Arrays::stream).anyMatch((o1) -> o1.candidates.size() > 1);
        return rest;
    }

    public void collapseAll(Map map) throws URISyntaxException, IOException, NoTileCandidatesLeft {
        while (isUncollapsedTilesExists(map)) {
            MapTile nextTileToCollapse = findNextTileToCollapse(map);
            if (nextTileToCollapse == null) {
                return;
            }
            collapseSingle(nextTileToCollapse);
            updatePotentialCandidates(map);
        }
    }

    private void updatePotentialCandidates(Map map) throws URISyntaxException, IOException, NoTileCandidatesLeft {
        TileComparator tileComparator = new TileComparator();
        ArrayList<MapTileCandidate> allPossibleMapTileCandidates = collectAllPossibleTiles(this.tileSet);
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid[x].length; y++) {
                if (map.grid[x][y].candidates.size() <= 1) {
                    // already collapsed, no update of options needed
                    continue;
                }
                List<MapTileCandidate> neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.NORTH);
                removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.NORTH);

                neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.EAST);
                removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.EAST);

                neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.SOUTH);
                removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.SOUTH);

                neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.WEST);
                removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.WEST);

                map.grid[x][y].candidates = allPossibleMapTileCandidates;
                if (allPossibleMapTileCandidates.size() == 0) {
                    throw new NoTileCandidatesLeft("filling: " + x + "/" + y);
                }
            }
        }
    }


    private void removeNotAllowedCandidates(List<MapTileCandidate> sourceCandidates, List<MapTileCandidate> neighbours, Direction direction) {
        if (Objects.isNull(neighbours)) {
            return;
        }
        List<MapTileCandidate> toRemove = new ArrayList<>();
        TileComparator tileComparator = new TileComparator();

        for (MapTileCandidate sourceCandidate : sourceCandidates) {
            for (MapTileCandidate neighbour : neighbours) {
                if (!tileComparator.isPossibleNeighbour(sourceCandidate, neighbour, direction)) {
                    toRemove.add(sourceCandidate);
                }
            }
        }
        sourceCandidates.removeAll(toRemove);
    }

    private void collapseSingle(MapTile nextTileToCollapse) {
        long rounded = new Random().nextLong(nextTileToCollapse.candidates.size());
        MapTileCandidate selectedCandidate = nextTileToCollapse.candidates.get((int) rounded);

        nextTileToCollapse.candidates=new ArrayList<>();
        nextTileToCollapse.candidates.add(selectedCandidate);
    }
}
