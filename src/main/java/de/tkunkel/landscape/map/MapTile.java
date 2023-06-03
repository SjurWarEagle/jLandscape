package de.tkunkel.landscape.map;

import de.tkunkel.landscape.generator.BorderInfo;

import java.util.ArrayList;
import java.util.List;

public class MapTile {
    public List<MapTileCandidate> candidates=new ArrayList<>();
    public BorderInfo borderInfo;

    @Override
    public String toString() {
        return "MapTile{" +
                ", candidates=" + candidates +
                ", borderInfo=" + borderInfo +
                '}';
    }
}
