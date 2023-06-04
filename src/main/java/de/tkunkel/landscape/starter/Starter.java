package de.tkunkel.landscape.starter;

import de.tkunkel.landscape.generator.LandscapeGenerator;
import de.tkunkel.landscape.types.NoTileCandidatesLeft;
import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.renderer.MapRenderer;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Objects;

@SpringBootApplication(scanBasePackages = "de.tkunkel.landscape")
public class Starter {
    static Logger LOG = LoggerFactory.getLogger(Starter.class);

    private LandscapeGenerator landscapeGenerator;
    private         MapRenderer mapRenderer;


    public Starter(LandscapeGenerator landscapeGenerator, MapRenderer mapRenderer) {
        this.landscapeGenerator = landscapeGenerator;
        this.mapRenderer = mapRenderer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Starter.class, args);
    }

    @PostConstruct
    private void start() throws Exception {
        generateLandscape();
    }

    public void generateLandscape() throws Exception {
        landscapeGenerator.setConfigParameter("circuit", 20, 20);
        Map map = null;

        boolean problemOccured;
        int loop = 0;
        LOG.info("Lets start");
        do {
            loop++;
            problemOccured = false;
            try {
                map = landscapeGenerator.createEmptyMap();
            } catch (NoTileCandidatesLeft ex) {
                problemOccured = true;
                LOG.info("Looping (" + loop + ")");
                LOG.debug(ex.getMessage());
            }
            if (!problemOccured) {
                try {
                    landscapeGenerator.collapseAll(map);
                    checkIfBrokenFields(map);
                } catch (NoTileCandidatesLeft ex) {
                    problemOccured = true;
                    LOG.info("Looping (" + loop + ")");
                    LOG.debug(ex.getMessage());
                }
            }
            if (loop > 1_000) {
                throw new RuntimeException("Too many tries to fill the map!");
            }
        } while (problemOccured);

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
