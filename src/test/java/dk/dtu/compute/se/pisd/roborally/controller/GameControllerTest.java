package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = LoadBoard.loadBoard(1);;
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

    /**
     * Test for Assignment V1 (can be delete later once V1 was shown to the teacher)
     */
    @Test
    void testV1() {
        Board board = gameController.board;

        Player player = board.getCurrentPlayer();
        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player, board.getSpace(0, 4).getPlayer(), "Player " + player.getName() + " should beSpace (0,4)!");
    }


        //The following tests should be used later for assignment V2



    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }


    /**
     * This test checks if moveForward correctly moves the player 1 spaces forward.
     * It also verifies that if there is a wall directly in front, it does not move forward.
     * @auther s224552
     */
    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(7,0));
        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(7, 1).getPlayer(), "Player " + current.getName() + " should beSpace (7,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(7, 0).getPlayer(), "Space (7,0) should be empty!");
        // sætter spilleren et sted med en væg
        current.setSpace(board.getSpace(2,1));
        gameController.moveForward(current);
        Assertions.assertEquals(current, board.getSpace(2, 1).getPlayer(), "Player " + current.getName() + " should beSpace (2,1)!");




    }

    /**
     * This test checks if fastForward correctly moves the player 3 spaces forward.
     * It also verifies that if there is a wall directly in front, it does not move forward.
     * Furthermore, it confirms that it can still move the player 1 space forward if the wall is placed afterwards
     * @auther s224552
     */
    @Test
    void fastForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        current.setSpace(board.getSpace(7,0));

        gameController.fastForward(current);
        //tjekke også her om man kan rykke en frem eller 2 eller 3
        Assertions.assertEquals(current, board.getSpace(7, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,3)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(7, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertNull(board.getSpace(7, 1).getPlayer(), "Space (0,1) should be empty!");
        Assertions.assertNull(board.getSpace(7, 2).getPlayer(), "Space (0,2) should be empty!");

       //Sætter spilleren ved væg
        current.setSpace(board.getSpace(2,1));
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(2, 1).getPlayer(), "Player " + current.getName() + " should beSpace (2,1)!");

        //Sætter spilleren 1 skridt væk fra væg
        current.setSpace(board.getSpace(2,0));
        gameController.fastForward(current);
        Assertions.assertEquals(current, board.getSpace(2, 1).getPlayer(), "Player " + current.getName() + " should beSpace (2,1)!");



    }

    /**
     * Test if the player is turn right by cheking the heading of the player, is goinge from South to West.
     */
    @Test
    void turnRight() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        gameController.turnRight(current);
        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading West!");
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
    }

    /**
     * Test if the player is turn left by cheking the heading of the player, is goinge from South to East.
     */
    @Test
    void turnLeft() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();
        gameController.turnLeft(current);
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading East!");
        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
    }

    /**
     * Testing the pushing mechanism in our game by calling moveforward, where it should push the player. Additionally,
     * it also checks if it would be possible to push a player through a wall, which should not be possible.
     * @auther s224552
     */
    @Test
    void moveToSpace() {
        Board board = gameController.board;
        Space space1 = board.getSpace(7,0);
        Space space2 = board.getSpace(7,1);
        Player current = board.getCurrentPlayer();
        Player other = board.getPlayer(2);
        current.setSpace(space1);
        other.setSpace(space2);
        gameController.moveForward(current);

        Assertions.assertEquals(other, board.getSpace(7,2).getPlayer(), "The other player should have been pushed");
        Assertions.assertEquals(current, board.getSpace(7,1).getPlayer(),"The player should have moved to the space");

        //sætter spillerne ved væg
        current.setSpace(board.getSpace(2,0));
        other.setSpace(board.getSpace(2,1));
        gameController.moveForward(current);
        Assertions.assertEquals(other, board.getSpace(2,1).getPlayer(), "The other player should not have been pushed");
        Assertions.assertEquals(current, board.getSpace(2,0).getPlayer(),"The player should have not moved to the space");
    }

    /**
     * @author s215698
     * This Test checks if the Uturn method works as intend
     * It does this by checking the player on getSpace(0,0)
     * The first Assertions.assertEquals asserts that the players starts out heading South
     * The Second then asserts that after calling the Uturn the player now has a Heading of North
     */
    @Test
    void uturn(){
        Board board = gameController.board;
        Player player = board.getSpace(0,0).getPlayer();
        Heading heading0 = player.getHeading();
        gameController.uturn(player);
        Assertions.assertEquals(Heading.SOUTH, heading0, "The players defualt heading is South");
        Assertions.assertEquals(Heading.NORTH, player.getHeading(), "North from a starting Heading of South");

    }





}