package de.tkunkel.landscape.map;

import de.tkunkel.landscape.generator.BorderInfo;

import java.util.ArrayList;
import java.util.List;

public class MapTile {
    public boolean collapsed = false;
    public List<MapTileCandidate> candidates=new ArrayList<>();
    public String fileName;
    public BorderInfo borderInfo;

    @Override
    public String toString() {
        return "MapTile{" +
                "collapsed=" + collapsed +
                ", candidates=" + candidates +
                ", fileName='" + fileName + '\'' +
                ", borderInfo=" + borderInfo +
                '}';
    }
}
