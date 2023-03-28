/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;

import dk.dtu.compute.se.pisd.roborally.dal.GameInDB;
import dk.dtu.compute.se.pisd.roborally.dal.IRepository;
import dk.dtu.compute.se.pisd.roborally.dal.RepositoryAccess;
import dk.dtu.compute.se.pisd.roborally.model.*;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class
AppController implements Observer {

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;
    private Board board;


    IRepository repository = RepositoryAccess.getRepository();


    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    public void newGame() {
        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();




        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            Board board = new Board(8, 8);



            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));

            }


            Wall wall1 = new Wall(SOUTH, board);
            Wall wall2 = new Wall(NORTH, board);
            Wall wall3 = new Wall(EAST, board);
            Wall wall4 = new Wall(WEST, board);

            board.addwall(wall1);
            board.addwall(wall2);
            board.addwall(wall3);
            board.addwall(wall4);

            wall1.setSpace(board.getSpace(3, 2));
            wall2.setSpace(board.getSpace(7, 2));
            wall3.setSpace(board.getSpace(5, 3));
            wall4.setSpace(board.getSpace(1, 7));
           /* Space space =new Space(board,3,2);
            Space space1 =new Space(board,7,2);
            Space space2 =new Space(board,5,3);
            Space space3 =new Space(board,1,7);


            //space.setWall(wall1);
            //space1.setWall(wall2);
            //space2.setWall(wall3);
            //space3.setWall(wall4)
            */


            Conveyerbelt conveyerbelt1 = new Conveyerbelt();
            board.addConveyerbelt(conveyerbelt1);
            conveyerbelt1.setHeading(WEST);
            conveyerbelt1.setSpace(board.getSpace(1, 3));
            Space space4 = board.getSpace(1, 3);
            space4.setConveyerbelt(conveyerbelt1);

            Conveyerbelt conveyerbelt2 = new Conveyerbelt();
            board.addConveyerbelt(conveyerbelt2);
            conveyerbelt2.setHeading(NORTH);
            conveyerbelt2.setSpace(board.getSpace(4, 6));
            Space space6 = board.getSpace(4, 6);
            space6.setConveyerbelt(conveyerbelt2);


            Checkpoint checkpoint1 = new Checkpoint();
            Checkpoint checkpoint2 = new Checkpoint();
            Checkpoint checkpoint3 = new Checkpoint();
            Checkpoint checkpoint4 = new Checkpoint();
            Checkpoint checkpoint5 = new Checkpoint();
            Checkpoint checkpoint6 = new Checkpoint();
            checkpoint1.setCheckpointnumber(1);
            checkpoint2.setCheckpointnumber(2);
            checkpoint3.setCheckpointnumber(3);
            checkpoint4.setCheckpointnumber(4);
            checkpoint5.setCheckpointnumber(5);
            checkpoint6.setCheckpointnumber(6);
            board.addCheckpoint(checkpoint1);
            board.addCheckpoint(checkpoint2);
            board.addCheckpoint(checkpoint3);
            board.addCheckpoint(checkpoint4);
            board.addCheckpoint(checkpoint5);
            board.addCheckpoint(checkpoint6);
            checkpoint1.setSpace(board.getSpace(0, 1));
            checkpoint2.setSpace(board.getSpace(2, 5));
            checkpoint3.setSpace(board.getSpace(7, 7));
            checkpoint4.setSpace(board.getSpace(4, 1));
            checkpoint5.setSpace(board.getSpace(0, 6));
            checkpoint6.setSpace(board.getSpace(2, 7));
            Space space1c = board.getSpace(0, 1);
            Space space2c = board.getSpace(2, 5);
            Space space3c = board.getSpace(7, 7);
            Space space4c = board.getSpace(4, 1);
            Space space5c = board.getSpace(0, 6);
            Space space6c = board.getSpace(2, 7);
            space1c.setCheckpoint(checkpoint1);
            space2c.setCheckpoint(checkpoint2);
            space3c.setCheckpoint(checkpoint3);
            space4c.setCheckpoint(checkpoint4);
            space5c.setCheckpoint(checkpoint5);
            space6c.setCheckpoint(checkpoint6);


            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            repository.createGameInDB(board);
            roboRally.createBoardView(gameController);
        }
    }

    public void saveGame() {
        repository.updateGameInDB(board);

    }


    public void loadGame() {
        //
        if (gameController == null) {
            repository.getGames();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }
}

