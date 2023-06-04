package de.tkunkel.landscape.map;

import de.tkunkel.landscape.types.BorderInfo;

public class MapTileCandidate {
    public String fileName;
    public BorderInfo borderInfo;

    @Override
    public String toString() {
        return "MapTileCandidate{" +
                "fileName='" + fileName + '\'' +
                ", borderInfo=" + borderInfo +
                '}';
    }
}
