package io.github.tutorial;

import com.badlogic.gdx.utils.Array;
import io.github.tutorial.entity.Entity;

import java.util.HashMap;

public class GridManager {

    private final float cellSize;
    private final HashMap<Point, Array<Entity>> grid;

    public GridManager(boolean isBossLevel) {
        grid = new HashMap<>();
        if(isBossLevel) {
            cellSize = 2f;
        } else {
            cellSize = 1f;
        }
    }

    public void insert(Entity target) {
        int pX = (int) Math.floor(target.getX() / cellSize);
        int pY = (int) Math.floor(target.getY() / cellSize);
        Point point = new Point(pX, pY);
        if (grid.containsKey(point)) {
            grid.get(point).add(target);
        } else {
            grid.put(point, new Array<>());
            grid.get(point).add(target);
        }
    }

    public void clear() {
        grid.clear();
    }

    public Array<Entity> findNearbyEntities(float x, float y) {
        int pX = (int) Math.floor(x / cellSize);
        int pY = (int) Math.floor(y / cellSize);
        Array<Entity> nearbyEntities = new Array<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Point currPoint = new Point(pX + i, pY + j);
                if (grid.containsKey(currPoint)) {
                    nearbyEntities.addAll(grid.get(currPoint));
                }
            }
        }
        return nearbyEntities;
    }
}
