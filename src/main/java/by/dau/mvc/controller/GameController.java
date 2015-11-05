package by.dau.mvc.controller;

import by.dau.data.engine.GameEngine;
import by.dau.data.entity.*;
import by.dau.data.service.*;
import by.dau.data.state.CurrentState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameEngine gameEngine;
    @Autowired
    MatchService matchService;
    @Autowired
    SetService setService;
    @Autowired
    UserService userService;
    @Autowired
    GameStateService gameStateService;
    @Autowired
    GameService gameService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public long create() {
        return gameEngine.create().getId();
    }

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void start() {
        gameEngine.start();
    }

    @RequestMapping(value = "/stop", method = RequestMethod.POST)
    public void stop() {
        gameEngine.stop();
    }

    @RequestMapping(value = "/state/{id}", method = RequestMethod.POST)
    public CurrentState state(@PathVariable("id") long id) {
        GameState gameState = gameStateService.read(id);
        Match match = matchService.getByState(gameState);
        List<Set> sets = setService.getAllByMatch(match);
        Set set = sets.get(sets.size());
        Game game = gameService.getLastBySet(set);
        String productName = set.getProduct().getName();
        float price = game.getPrice();
        CurrentState currentState = new CurrentState(gameState.getId(), match.getId(), set.getId(), game.getId(), productName, price);
        return currentState;
    }

    @RequestMapping(value = "/price/{id}/{price}", method = RequestMethod.POST)
    public void newPrice(@PathVariable("id") long id, @PathVariable("price") float price) {
        GameState gameState = gameStateService.read(id);
        Match match = matchService.getByState(gameState);
        List<Set> sets = setService.getAllByMatch(match);
        Set set = sets.get(sets.size());
        Game game = gameService.getLastBySet(set);
        game.setPrice(price);
        gameService.update(game);
    }

    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public List<User> users(@PathVariable("id") long id) {
        GameState gameState = gameStateService.read(id);
        List<User> users = userService.getUserByState(gameState);
        return users;
    }

    @RequestMapping(value = "/join/{name}", method = RequestMethod.POST)
    public void join(@PathVariable("name") String name) {
        gameEngine.join(name);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handlerError(Exception e) {
        e.printStackTrace();
    }
}
