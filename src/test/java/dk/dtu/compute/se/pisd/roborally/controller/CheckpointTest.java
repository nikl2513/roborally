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

class CheckpointTest {

    private GameController gameController;
    @BeforeEach
    void setUp() {
        Board board = LoadBoard.loadBoard(1);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player1 = new Player(board, null,"Player1 " + i);
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
        player1.setSpace(board.getSpace(2,5));
        int CheckpointValue = player1.getCheckpointValue();

        gameController.executeActionspace();
        Assertions.assertEquals(CheckpointValue+1,player1.getCheckpointValue());
    }
}