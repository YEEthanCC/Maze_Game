package wrapper;

import ca.MazeGame.model.CellLocation;
import ca.MazeGame.model.MazeGame;

import java.util.ArrayList;
import java.util.List;


public class ApiBoardWrapper {
    public int boardWidth;
    public int boardHeight;
    public ApiLocationWrapper mouseLocation;
    public ApiLocationWrapper cheeseLocation;
    public List<ApiLocationWrapper> catLocations;
    public boolean[][] hasWalls;
    public boolean[][] isVisible;

    // Accept whatever object(s) you need to populate this object.
    public static ApiBoardWrapper makeFromGame(MazeGame game) {
        ApiBoardWrapper wrapper = new ApiBoardWrapper();
        wrapper.catLocations = new ArrayList<>();
        wrapper.boardWidth = MazeGame.getMazeWidth();
        wrapper.boardHeight = MazeGame.getMazeHeight();
        wrapper.hasWalls = new boolean[wrapper.boardHeight][wrapper.boardWidth];
        wrapper.isVisible = new boolean[wrapper.boardHeight][wrapper.boardWidth];
        for (int i = 0; i < MazeGame.getMazeHeight(); i++) {
            for (int j = 0; j < MazeGame.getMazeWidth(); j++) {
                CellLocation cellLocation = new CellLocation(j, i);
                if (game.isMouseAtLocation(cellLocation)) {
                    wrapper.mouseLocation = new ApiLocationWrapper(cellLocation.getX(), cellLocation.getY());
                }
                if (game.isCheeseAtLocation(cellLocation)) {
                    wrapper.cheeseLocation = new ApiLocationWrapper(cellLocation.getX(), cellLocation.getY());
                }
                if (game.isCatAtLocation(cellLocation)) {
                    wrapper.catLocations.add(new ApiLocationWrapper(cellLocation.getX(), cellLocation.getY()));// Fill this in, along with all the other fields.
                }
                wrapper.hasWalls[i][j] = game.getCellState(cellLocation).isWall();
                wrapper.isVisible[i][j] = game.getCellState(cellLocation).isVisible();
            }
        }
        return wrapper;
    }
}
