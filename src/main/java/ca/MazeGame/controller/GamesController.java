package ca.MazeGame.controller;

import ca.MazeGame.model.MazeGame;
import ca.MazeGame.model.MoveDirection;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import wrapper.ApiBoardWrapper;
import wrapper.ApiGameWrapper;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GamesController {
    private MazeGame game;
    private List<MazeGame> games = new ArrayList<>();
    List<ApiGameWrapper> gameWrappers = new ArrayList<>();

    @GetMapping("/api/about")
    public String about(){
        return "Ethan Chen";
    }

    @GetMapping("/api/games")
//    @ResponseStatus(HttpStatus.CREATED)
    public List<ApiGameWrapper> getAllGames() {
        return gameWrappers;
    }
//
    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getGame(@PathVariable("id") int gameNumber) throws FileNotFoundException {

        try {
            return gameWrappers.get(gameNumber);

        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }
//
    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper createNewGame() {
        game = new MazeGame();
        games.add(game);
        ApiGameWrapper gameWrapper = ApiGameWrapper.createApiGameWrapper(gameWrappers.size(), game);
        gameWrappers.add(gameWrapper);
        return gameWrapper;
    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getBoard(@PathVariable("id") int gameNumber) throws FileNotFoundException {
        game = findGame(gameNumber);
        ApiBoardWrapper boardWrapper = ApiBoardWrapper.makeFromGame(game);
        return boardWrapper;
    }

    public MazeGame findGame(int gameNumber) throws FileNotFoundException {
        if (gameNumber >= 0 && gameNumber < games.size()) {
            return games.get(gameNumber);
        }
        throw new FileNotFoundException();
    }

    public void movePlayer(MoveDirection playerMove) {
        if (game.isValidPlayerMove(playerMove)) {
            game.recordPlayerMove(playerMove);
            game.doCatMoves();
        } else {
            throw new IllegalArgumentException();
        }
    }


    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public void move(@PathVariable("id") int gameNumber,
                     @RequestBody String movement) throws FileNotFoundException, IllegalArgumentException {
        game = findGame(gameNumber);
        MoveDirection playerMove = MoveDirection.MOVE_UP;
        switch (movement) {
            case "MOVE_LEFT":
                playerMove = MoveDirection.MOVE_LEFT;
                movePlayer(playerMove);
                break;
            case "MOVE_RIGHT":
                playerMove = MoveDirection.MOVE_RIGHT;
                movePlayer(playerMove);
                break;
            case "MOVE_UP":
                playerMove = MoveDirection.MOVE_UP;
                movePlayer(playerMove);
                break;
            case "MOVE_DOWN":
                playerMove = MoveDirection.MOVE_DOWN;
                movePlayer(playerMove);
                break;
            case "MOVE_CATS":
                game.doCatMoves();
                break;
            default:
                throw new IllegalArgumentException();
        }
        ApiGameWrapper gameWrapper = ApiGameWrapper.createApiGameWrapper(gameNumber, game);
        // find existing gamewrapper object and remove and add new one
        try {
            gameWrappers.remove(gameWrappers.get(gameNumber));
            gameWrappers.add(gameWrapper);
        } catch (Exception e) {
            throw new FileNotFoundException();
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "player's move is invalid")
    @ExceptionHandler(IllegalArgumentException.class)
    public void invalidMoveExceptionHandler() {

    }

    @ResponseStatus(value = HttpStatus.ACCEPTED)
    @PostMapping("/api/games/{id}/cheatstate")
    public void cheat(@RequestBody String cheatStatus, @PathVariable("id") int gameNumber) throws FileNotFoundException {
        if (cheatStatus.equals("1_CHEESE")) {
            game = findGame(gameNumber);
            game.setNumberCheeseToCollect(1);
            gameWrappers.remove(gameWrappers.get(gameNumber));
            gameWrappers.add(ApiGameWrapper.createApiGameWrapper(gameNumber, game));
        } else if (cheatStatus.equals("SHOW_ALL")) {
            game = findGame(gameNumber);
            game.setMazeVisible();
        } else {
            throw new IllegalArgumentException();
        }
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(FileNotFoundException.class)
    public void fileNotFoundHandler() {

    }

}
