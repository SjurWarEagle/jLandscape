package de.tkunkel.landscape.map;

import de.tkunkel.landscape.types.BorderInfo;

import java.util.HashSet;
import java.util.Set;

public class MapTile {
    public Set<MapTileCandidate> candidates=new HashSet<>();
    public BorderInfo borderInfo;

    @Override
    public String toString() {
        return "MapTile{" +
                ", candidates=" + candidates +
                ", borderInfo=" + borderInfo +
                '}';
    }
}
