package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *  This class is created to test turnPad, which it does in the doAction method.
 *  In doAction, the player is placed on a Turnpad that rotates to the right,
 *  and it is checked that this action is performed. You call executeActionSpace(),
 *  which in turn calls all the actions that the players are standing on, and thus the player should be rotated.
 * @auther s224552
 */



class TurnpadTest {

    private GameController gameController;
    @BeforeEach
    void setUp() {
        Board board = LoadBoard.loadBoard(1);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"Player " + i);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }


    @Test
    void doAction() {
        Board board = gameController.board;
        Player player1 = board.getCurrentPlayer();
        //we know there is a turnpad on space x = 7 , y = 4
        player1.setSpace(board.getSpace(7,4));
        gameController.executeActionspace();
        Assertions.assertEquals(Heading.WEST, player1.getHeading(), "Player should be heading WEST!");
        Assertions.assertEquals(player1.getSpace(),board.getSpace(7,4),"The Player should not have moved");
    }

}