package de.tkunkel.landscape.generator;

import de.tkunkel.landscape.map.Map;
import de.tkunkel.landscape.map.MapTile;
import de.tkunkel.landscape.map.MapTileCandidate;
import de.tkunkel.landscape.types.Direction;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.List;
import java.util.*;

@Service
public class TileComparator {

    public List<MapTileCandidate> getPossibleMapTilesWithNeighbour(Map map, int x, int y, Direction direction) {
        switch (direction) {

            case NORTH -> {
                if (y - 1 < 0) {
                    return null;
                }
                return extractMaptileFromMap(map, x, y - 1);
            }
            case EAST -> {
                if (x + 1 >= map.width) {
                    return null;
                }
                return extractMaptileFromMap(map, x + 1, y);
            }
            case SOUTH -> {
                if (y + 1 >= map.height) {
                    return null;
                }
                return extractMaptileFromMap(map, x, y + 1);
            }
            case WEST -> {
                if (x - 1 < 0) {
                    return null;
                }
                return extractMaptileFromMap(map, x - 1, y);
            }
        }
        return null;
    }

    private static List<MapTileCandidate> extractMaptileFromMap(Map map, int x, int y) {
        MapTile mapTile = map.grid[x][y];
        if (Objects.isNull(mapTile)) {
            return null;
        }
        return new ArrayList<>(mapTile.candidates);
    }

    public Collection<MapTileCandidate> getPossibleNeighbours(MapTileCandidate meTile, List<MapTileCandidate> neighbour, Direction direction) {
        Collection<MapTileCandidate> rc = new HashSet<>();
        for (MapTileCandidate tileCandidate : neighbour) {
            if (isPossibleNeighbour(meTile, tileCandidate, direction)) {
                rc.add(tileCandidate);
            }
        }

        return rc;
    }

    public boolean isPossibleNeighbour(MapTileCandidate me, MapTileCandidate other, Direction direction) {
        if (Objects.isNull(me) || Objects.isNull(me.borderInfo)) {
            return false;
        }
        if (Objects.isNull(other) || Objects.isNull(other.borderInfo)) {
            return false;
        }

        Color[] otherBorder = null;
        Color[] meBorder = null;
        switch (direction) {
            case NORTH -> {
                meBorder = me.borderInfo.north;
                otherBorder = other.borderInfo.south;
            }
            case EAST -> {
                meBorder = me.borderInfo.east;
                otherBorder = other.borderInfo.west;
            }
            case SOUTH -> {
                meBorder = me.borderInfo.south;
                otherBorder = other.borderInfo.north;
            }
            case WEST -> {
                meBorder = me.borderInfo.west;
                otherBorder = other.borderInfo.east;
            }
        }

        if (Objects.isNull(meBorder) || Objects.isNull(otherBorder)) {
            return false;
        }

        if (meBorder.length != otherBorder.length) {
            return false;
        }

        for (int i = 0; i < otherBorder.length; i++) {
            if (!isColorMatchesGoodEnough(meBorder[i], otherBorder[i])) {
                return false;
            }
        }

        return true;
    }

    private boolean isColorMatchesGoodEnough(Color color1, Color color2) {
        if (Objects.isNull(color1)) {
            return false;
        }
        if (Objects.isNull(color2)) {
            return false;
        }
        double factor=20;
        if (Math.abs(color1.getRed()- color2.getRed())>factor) {
            return false;
        }
        if (Math.abs(color1.getGreen()- color2.getGreen())>factor) {
            return false;
        }
        //noinspection RedundantIfStatement
        if (Math.abs(color1.getBlue()- color2.getBlue())>factor) {
            return false;
        }
        return true;
    }
}
