package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.MapTileCandidate;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LandscapeGeneratorTest {

    @Test
    public void isAllowed(){
        LandscapeGenerator generator = new LandscapeGenerator("demo");
        MapTileCandidate sourceCandidate=new MapTileCandidate();
        String neighbourFileName="blank.png";
        ArrayList<MapTileCandidate> neighbourCandidates=new ArrayList<>();
        boolean allowed = generator.isAllowed(sourceCandidate, neighbourFileName, neighbourCandidates);
        assertTrue(allowed);
    }

}
