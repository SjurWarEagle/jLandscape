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
    private final int width;
    private final int height;
    private final BorderDetector borderDetector = new BorderDetector();
    private final ArrayList<MapTileCandidate> allPossibleMapTileCandidates = new ArrayList<>();

    public LandscapeGenerator(String tileSet,int width, int height) {
        this.tileSet = tileSet;
        this.width = width;
        this.height = height;
    }

    public Map createEmptyMap() throws URISyntaxException, IOException, NoTileCandidatesLeft {
        Set<MapTileCandidate> mapTileCandidates = collectAllPossibleTiles(this.tileSet);

        Map map = new Map(this.width, this.height);
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid[x].length; y++) {
                map.grid[x][y] = new MapTile();
                map.grid[x][y].candidates.clear();
                map.grid[x][y].candidates.addAll(mapTileCandidates);
            }
        }
        updatePotentialCandidates(map);

        return map;
    }

    private Set<MapTileCandidate> collectAllPossibleTiles(String tileSet) throws URISyntaxException, IOException {
        if (this.allPossibleMapTileCandidates.size() > 0) {
            return new HashSet<>(this.allPossibleMapTileCandidates);
        }

        URL resource = getClass().getClassLoader().getResource(tileSet);
        if (resource == null) {
            throw new IllegalArgumentException("folder '" + tileSet + "' not found!");
        }

        Set<MapTileCandidate> allCandidates = new HashSet<>();

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
        Optional<MapTile> next = Arrays.stream(map.grid).flatMap(Arrays::stream)
                .filter(mapTile -> mapTile.candidates.size() > 1)
                .min(Comparator.comparingInt(o -> o.candidates.size()));

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

    public void updatePotentialCandidates(Map map) throws URISyntaxException, IOException, NoTileCandidatesLeft {
        TileComparator tileComparator = new TileComparator();
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid[x].length; y++) {
                updateCell(map, tileComparator, x, y);
            }
        }
    }

    private void updateCell(Map map, TileComparator tileComparator, int x, int y) throws URISyntaxException, IOException, NoTileCandidatesLeft {
        Set<MapTileCandidate> allPossibleMapTileCandidates = collectAllPossibleTiles(this.tileSet);
        if (map.grid[x][y].candidates.size() <= 1) {
            // already collapsed, no update of options needed
            return;
        }
        List<MapTileCandidate> neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.NORTH);
        removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.NORTH);

        neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.EAST);
        removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.EAST);

        neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.SOUTH);
        removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.SOUTH);

        neighbours = tileComparator.getNeighbourInDirection(map, x, y, Direction.WEST);
        removeNotAllowedCandidates(allPossibleMapTileCandidates, neighbours, Direction.WEST);

        map.grid[x][y].candidates.clear();
        map.grid[x][y].candidates.addAll(allPossibleMapTileCandidates);
        if (allPossibleMapTileCandidates.size() == 0) {
            throw new NoTileCandidatesLeft("filling: " + x + "/" + y);
        }
    }


    private void removeNotAllowedCandidates(Set<MapTileCandidate> sourceCandidates, List<MapTileCandidate> neighbours, Direction direction) {
        if (Objects.isNull(neighbours)) {
            return;
        }
        Set<MapTileCandidate> toKeep = new HashSet<>();
        TileComparator tileComparator = new TileComparator();

        for (MapTileCandidate sourceCandidate : sourceCandidates) {
            for (MapTileCandidate neighbour : neighbours) {
                if (tileComparator.isPossibleNeighbour(sourceCandidate, neighbour, direction)) {
                    toKeep.add(sourceCandidate);
                }
            }
        }
        sourceCandidates.clear();
        sourceCandidates.addAll(toKeep);
    }

    private void collapseSingle(MapTile nextTileToCollapse) {
        long rounded = new Random().nextLong(nextTileToCollapse.candidates.size());
        MapTileCandidate selectedCandidate = nextTileToCollapse.candidates.stream().toList().get((int) rounded);

        nextTileToCollapse.candidates.clear();
        nextTileToCollapse.candidates.add(selectedCandidate);
    }
}
