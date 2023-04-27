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

class PitTest {

    /**
     * @author s224567
     * This test class is created to test Pits, which it does in the doAction method.
     * It verifies whether the player hitting the Pit, is sent to a new position that is not on another player or on the Pit's position.
     * It Checks that a players HP is going down by 1
     */
    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = LoadBoard.loadBoard(1);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player1 = new Player(board, null, "Player1 " + i);
            board.addPlayer(player1);
            player1.setSpace(board.getSpace(i, i));
            player1.setHeading(Heading.values()[i % Heading.values().length]);
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
        //We know that there is a Pit on space x = 4 y = 7
        player1.setSpace(board.getSpace(4, 7));
        int hp = player1.getHp();
        Player player2 = board.getPlayer(1);
        gameController.executeActionspace();
        //We put a new player on the board
        Assertions.assertNotEquals(player1.getSpace(), board.getSpace(4, 7), "Player1 should be moved from the pits position");
        Assertions.assertNotEquals(player2.getSpace(), player1.getSpace(), "Player 1 should not be put in the same position as Player2");
        Assertions.assertEquals(player1.getHp(), hp - 1, "the old hp - 1 should be the same as the new player hp");

    }
}