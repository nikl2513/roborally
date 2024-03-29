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
 * @author s224576
 * @auther s224552
 * This test tests, that our conveyerbelt will move one player.
 * It also tests that if there is a player in the space that the conveyerbelt pushes another player,
 * then that player will get pushed one space too.
 * we extended this test so we can check if a player will get pushed through a wall.
 */
class ConveyorBeltTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = LoadBoard.loadBoard(1);
        gameController = new GameController(board);
        for (int i = 0; i < 2; i++) {
            Player player = new Player(board, null, "Player " + i);
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
        Player player2 = board.getPlayer(1);
        //we know there is a conveyerbelt on space x = 4 , y = 4
        player1.setSpace(board.getSpace(4, 4));
        player2.setSpace(board.getSpace(3, 4));
        gameController.executeActionspace();
        Assertions.assertEquals(player1.getSpace(), board.getSpace(3, 4), "The Player should move one space");
        Assertions.assertEquals(player2.getSpace(), board.getSpace(2, 4), "The Player should move one space");
        player1.setSpace(board.getSpace(2, 2));
        player2.setSpace(board.getSpace(3, 2));
        gameController.executeActionspace();
        Assertions.assertEquals(player1.getSpace(), board.getSpace(2, 2), "The player should not move");
        Assertions.assertEquals(player2.getSpace(), board.getSpace(3, 2), "The Player should not move");
    }
}