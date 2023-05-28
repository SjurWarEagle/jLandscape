package de.tkunkel.landscape.starter;

import de.tkunkel.landscape.generator.LandscapeGenerator;
import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.renderer.MapRenderer;

public class Starter {
    public static void main(String[] args) throws Exception {
        LandscapeGenerator landscapeGenerator = new LandscapeGenerator("demo");
        Map testMap = landscapeGenerator.createEmptyMap();
        landscapeGenerator.collapseAll(testMap);

        MapRenderer mapRenderer = new MapRenderer();
        mapRenderer.render(testMap);
    }
}
