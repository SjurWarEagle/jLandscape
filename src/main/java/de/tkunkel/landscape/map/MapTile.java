package de.tkunkel.landscape.map;

import java.util.HashSet;
import java.util.Set;

public class MapTile {
    public final Set<MapTileCandidate> candidates=new HashSet<>();

    @Override
    public String toString() {
        return "MapTile{" +
                ", candidates=" + candidates +
                '}';
    }
}
