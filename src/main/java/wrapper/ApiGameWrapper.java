package wrapper;

import ca.MazeGame.model.MazeGame;

public class ApiGameWrapper {
    public int gameNumber;
    public boolean isGameWon;
    public boolean isGameLost;
    public int numCheeseFound;
    public int numCheeseGoal;

    public ApiGameWrapper(int gameNumber, boolean isGameWon, boolean isGameLost, int numCheeseFound, int numCheeseGoal) {
        this.gameNumber = gameNumber;
        this.isGameWon = isGameWon;
        this.isGameLost = isGameLost;
        this.numCheeseFound = numCheeseFound;
        this.numCheeseGoal = numCheeseGoal;
    }

    public static ApiGameWrapper createApiGameWrapper(int id, MazeGame mazeGame) {
        ApiGameWrapper apiGameWrapper = new ApiGameWrapper(id, mazeGame.hasUserWon(),mazeGame.hasUserLost(), mazeGame.getNumberCheeseCollected(), mazeGame.getNumberCheeseToCollect());
        return apiGameWrapper;
    }
}
