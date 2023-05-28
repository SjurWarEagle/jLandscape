package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.map.MapTile;
import de.tkunkel.landscape.map.MapTileCandidate;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.List;

public class LandscapeGenerator {

    private final String tileSet;

    public LandscapeGenerator(String tileSet) {
        this.tileSet = tileSet;
    }

    public Map createEmptyMap() throws URISyntaxException, IOException {
        ArrayList<MapTileCandidate> mapTileCandidates = collectPossibleTiles(this.tileSet);

        Map map = new Map(10, 10);
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid.length; y++) {
                map.grid[x][y] = new MapTile();
                map.grid[x][y].collapsed = false;
                map.grid[x][y].candidates = mapTileCandidates;
            }
        }
        return map;
    }

    private ArrayList<MapTileCandidate> collectPossibleTiles(String tileSet) throws URISyntaxException, IOException {
        URL resource = getClass().getClassLoader().getResource(tileSet);
        if (resource == null) {
            throw new IllegalArgumentException("folder '" + tileSet + "' not found!");
        }

        ArrayList<MapTileCandidate> allCandidates = new ArrayList<>();

        BorderDetector borderDetector = new BorderDetector();

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
        return allCandidates;
    }

    public MapTile findNextTileToCollapse(Map map) {
        Optional<MapTile> min = Arrays.stream(map.grid)
                .flatMap(Arrays::stream)
                .filter(mapTile -> mapTile.candidates.size() > 0)
                .min(Comparator.comparingInt(o -> o.candidates.size()));

        return min.orElse(null);
    }

    public boolean isUncollapsedTilesExists(Map map) {
        long count = Arrays.stream(map.grid)
                .flatMap(Arrays::stream)
                .filter((o1) -> o1.candidates.size() > 1)
                .count();
        ;
        return count != 0;
    }

    public void collapseAll(Map map) throws URISyntaxException, IOException {
        while (isUncollapsedTilesExists(map)) {
            MapTile nextTileToCollapse = findNextTileToCollapse(map);
            if (nextTileToCollapse == null) {
                return;
            }
            collapseSingle(nextTileToCollapse);
            updatePotentialCandidates(map);
        }
    }

    private void updatePotentialCandidates(Map map) throws URISyntaxException, IOException {
        ArrayList<MapTileCandidate> mapTileCandidates = collectPossibleTiles(this.tileSet);
        for (int x = 0; x < map.grid.length; x++) {
            for (int y = 0; y < map.grid.length; y++) {
//                map.grid[x][y].candidates = mapTileCandidates;
                if (x + 1 < map.width) {
                    removeNotAllowedCandidates(map.grid[x][y].candidates, map.grid[x + 1][y], mapTileCandidates);
                }
            }
        }
    }

    private void removeNotAllowedCandidates(List<MapTileCandidate> sourceCandidates, MapTile neighbour, ArrayList<MapTileCandidate> mapTileCandidates) {
        List<MapTileCandidate> toRemove = new ArrayList<>();

        for (MapTileCandidate sourceCandidate : sourceCandidates) {
            if (!isAllowed(sourceCandidate, neighbour.fileName, mapTileCandidates)) {
                toRemove.add(sourceCandidate);
            }
        }
        sourceCandidates.removeAll(toRemove);
    }

    public boolean isAllowed(MapTileCandidate sourceCandidate, String neighbourFileName, ArrayList<MapTileCandidate> neighbourCandidates) {
        //todo
        if (Objects.isNull(neighbourFileName)){
            return true;
        }

        Optional<MapTileCandidate> candidate = neighbourCandidates
                .stream()
                .filter(mapTileCandidate -> mapTileCandidate.fileName.equalsIgnoreCase(neighbourFileName))
                .findFirst();

        if (candidate.isEmpty()) {
            return true;
        }

        return true;
    }

    private void collapseSingle(MapTile nextTileToCollapse) {
        long rounded = Math.round(Math.random() * nextTileToCollapse.candidates.size() - 0.5);
        MapTileCandidate selectedCandidate = nextTileToCollapse.candidates.get((int) rounded);

        nextTileToCollapse.fileName = selectedCandidate.fileName;
        nextTileToCollapse.borderInfo = selectedCandidate.borderInfo;
        nextTileToCollapse.candidates = new ArrayList<>();
    }
}
