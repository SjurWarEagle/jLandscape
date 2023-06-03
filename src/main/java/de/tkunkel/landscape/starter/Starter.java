package de.tkunkel.landscape.starter;

import de.tkunkel.landscape.generator.LandscapeGenerator;
import de.tkunkel.landscape.generator.NoTileCandidatesLeft;
import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.renderer.MapRenderer;

import java.util.Objects;

public class Starter {
    public static void main(String[] args) throws Exception {
        LandscapeGenerator landscapeGenerator = new LandscapeGenerator("circuit", 20, 20);
//        LandscapeGenerator landscapeGenerator = new LandscapeGenerator("demo", 10, 10);
        Map map = null;

        boolean problemOccured = false;
        int loop = 0;
        System.out.println("Lets start");
        do {
            loop++;
            problemOccured = false;
            try {
                map = landscapeGenerator.createEmptyMap();
            } catch (NoTileCandidatesLeft ex) {
                problemOccured = true;
                System.out.println("Looping (" + loop + ")");
                System.out.println(ex.getMessage());
            }
            if (!problemOccured) {
                try {
                    problemOccured = false;
                    landscapeGenerator.collapseAll(map);
                    checkIfBrokenFields(map);
                } catch (NoTileCandidatesLeft ex) {
                    problemOccured = true;
                    System.out.println("Looping (" + loop + ")");
                    System.out.println(ex.getMessage());
                }
            }
            if (loop > 1_000) {
                throw new RuntimeException("Too many tries to fill the map!");
            }
        } while (problemOccured);

        MapRenderer mapRenderer = new MapRenderer();
        mapRenderer.render(map);
    }

    private static void checkIfBrokenFields(Map testMap) throws NoTileCandidatesLeft {
        for (int x = 0; x < testMap.grid.length; x++) {
            for (int y = 0; y < testMap.grid[x].length; y++) {
                if (Objects.isNull(testMap.grid[x][y].candidates)) {
                    throw new NoTileCandidatesLeft("candidates is null: " + x + "/" + y);
                }
                if (testMap.grid[x][y].candidates.size() > 1) {
                    throw new NoTileCandidatesLeft("candidates too long");
                }
                if (testMap.grid[x][y].candidates.size() < 1) {
                    throw new NoTileCandidatesLeft("candidates too short");
                }
            }
        }
    }
}
