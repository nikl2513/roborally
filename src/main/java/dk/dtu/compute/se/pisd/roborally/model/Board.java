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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.Pit;
import dk.dtu.compute.se.pisd.roborally.controller.Turnpad;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.INITIALISATION;

/**
 * ...
 * this is where all the information about the game is.
 * the size of the board, the players in the game and the obstacles on the board.
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject {

    public final int width;

    public final int height;

    public final String boardName;

    private Integer gameId;

    private final Space[][] spaces;

    private final List<Player> players = new ArrayList<>();

    private Player current;

    private Phase phase = INITIALISATION;

    private int step = 0;

    private boolean stepMode;

    private int moveCounter = 0;


    /**
     * Vi opretter en privat variabel som er vores counter
     * Vi sætter den til 0
     * Vi opretter en getter og en setter for variablen
     * Setteren bruger notifyChange() til at orientere tilobserver at der er sket en ændring i programmet
     */
    public int getMoveCounter() {
        return moveCounter;
    }
    //

    public void setMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
        notifyChange();
    }

    /**
     * @param width how many spaces there should be on the x axis.
     * @param height how many spaces there should be on the y axis.
     * @param boardName the name on the board.
     * This method tells what a board should include from the start.
     */
    public Board(int width, int height, @NotNull String boardName) {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
    }

    /**
     * @param width how many spaces there should be on the x axis.
     * @param height how many spaces there should be on the y axis.
     * this method creates a board with a width, height and a name. the name is always defaulboard from the start.
     */
    public Board(int width, int height) {
        this(width, height, "defaultboard");
    }

    public Integer getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        if (this.gameId == null) {
            this.gameId = gameId;
        } else {
            if (!this.gameId.equals(gameId)) {
                throw new IllegalStateException("A game with a set id may not be assigned a new id!");
            }
        }
    }

    public Space getSpace(int x, int y) {
        if (x >= 0 && x < width &&
                y >= 0 && y < height) {
            return spaces[x][y];
        } else {
            return null;
        }
    }

    public Space getRandomSpace() {
        Random random = new Random();
        int xx = random.nextInt(width);
        int yy = random.nextInt(height);

        return spaces[xx][yy];
    }

    public int getPlayersNumber() {
        return players.size();
    }

    /**
     * @param player the player that is added to the board.
     * this method adds a player to the board.
     */
    public void addPlayer(@NotNull Player player) {
        if (player.board == this && !players.contains(player)) {
            players.add(player);
            notifyChange();
        }
    }

    public Player getPlayer(int i) {
        if (i >= 0 && i < players.size()) {
            return players.get(i);
        } else {
            return null;
        }
    }

    public Player getCurrentPlayer() {
        return current;
    }

    public void setCurrentPlayer(Player player) {
        if (player != this.current && players.contains(player)) {
            this.current = player;
            notifyChange();
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        if (phase != this.phase) {
            this.phase = phase;
            notifyChange();
        }
    }

    /**
     * This gives the current step of which programming card we are on with the individual player.
     *
     * @return the Current position in the programming cards
     */
    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        if (step != this.step) {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * @return
     */
    public boolean isStepMode() {
        return stepMode;
    }

    public void setStepMode(boolean stepMode) {
        if (stepMode != this.stepMode) {
            this.stepMode = stepMode;
            notifyChange();
        }
    }

    public int getPlayerNumber(@NotNull Player player) {
        if (player.board == this) {
            return players.indexOf(player);
        } else {
            return -1;
        }
    }

    /**
     * Returns the neighbour of the given space of the board in the given heading.
     * The neighbour is returned only, if it can be reached from the given space
     * (no walls or obstacles in either of the involved spaces); otherwise,
     * null will be returned.
     *
     * @param space   the space for which the neighbour should be computed
     * @param heading the heading of the neighbour
     * @return the space in the given direction; null if there is no (reachable) neighbour
     */


    // Sæt væg tjek ind her.
    public Space getNeighbour(@NotNull Space space, @NotNull Heading heading) {
        /**
         *
         * This Method returns the neighbouring space
         * It takes the current space of the player and a heading as Parameters
         * It then uses they these to determind the neighbouring space in a given direction.
         * In adition it checks for walls that are between the space and its neighbour.
         * heading1 checks for a wall on the players space that is facing the same way as the player
         * heading2 checks for an opposite facing wall on the neighbouring.
         * It does this by checking the if a List of wall the wall headings on the players currentfield contains a heading matching the players
         * And if the opposite field has a wall heading facing the opposite way.
         *
         */
        int x = space.x;
        int y = space.y;

        switch (heading) {
            case SOUTH:
                y = (y + 1) % height;
                break;
            case WEST:
                x = (x + width - 1) % width;
                break;
            case NORTH:
                y = (y + height - 1) % height;
                break;
            case EAST:
                x = (x + 1) % width;
                break;
        }
        Space space1 = this.getSpace(x, y);
        Heading heading2 = heading.prev().prev();

        if (space.getWalls().contains(heading)) {
            return null;
        }
        if (space1.getWalls().contains(heading2)) {
            return null;
        }

        return getSpace(x, y);
    }

    /**
     * Her har vi tilføjet antal slag til statuslinjen
     * Vi bruger getMoveCounter() til at vise hvor mange slag der er sket i spillet
     * Vi har også tilføjet et CheckpointValue til den så man kan se hvor mange chekpoints hver spiller har.
     */
    public String getStatusMessage() {
        Board board = current.board;

        // this is actually a view aspect, but for making assignment V1 easy for
        // the students, this method gives a string representation of the current
        // status of the game

        // XXX: V1 add the move count to the status message
        // XXX: V2 changed the status so that it shows the phase, the current player and the number of steps
        if (board.getPhase() == Phase.GAME_ENDING) {

            int i;
            for (i = 0; i < board.getPlayersNumber(); ++i) {
                Player player = board.getPlayer(i);
                if (player.getCheckpointValue() == 6) {
                    board.setCurrentPlayer(player);}
            }
            return winnermessage(board);





            }
       // return "Winner is: " + getCurrentPlayer().getName() + " Phase: " + getPhase().name();


        return "Phase: " + getPhase().name() +
                ", Player = " + getCurrentPlayer().getName() +
                ", AntalSlag = " + getMoveCounter() + ", Checkpoints = " + getCurrentPlayer().getCheckpointValue() +
                ", HP = " + getCurrentPlayer().getHp();


    }
    public String winnermessage(Board board) {
        switch (board.getPlayersNumber()){
            case 2:
                return "Winner is: " + getCurrentPlayer().getName() + "       Checkpoints: "  +
                        " " + board.getPlayer(0).getName()+ ": " + board.getPlayer(0).getCheckpointValue() +
                        " " + board.getPlayer(1).getName()+ ": " + board.getPlayer(1).getCheckpointValue();
            case 3:
                return "Winner is: " + getCurrentPlayer().getName() + "       Checkpoints: "  +
                        " " + board.getPlayer(0).getName()+ ": " + board.getPlayer(0).getCheckpointValue() +
                        " " + board.getPlayer(1).getName()+ ": " + board.getPlayer(1).getCheckpointValue() +
                        " " + board.getPlayer(2).getName()+ ": " + board.getPlayer(2).getCheckpointValue();
            case 4:
                return "Winner is: " + getCurrentPlayer().getName() + "       Checkpoints: " +
                        " " + board.getPlayer(0).getName()+ ": " + board.getPlayer(0).getCheckpointValue() +
                        " " + board.getPlayer(1).getName()+ ": " + board.getPlayer(1).getCheckpointValue() +
                        " " + board.getPlayer(2).getName()+ ": " + board.getPlayer(2).getCheckpointValue() +
                        " " + board.getPlayer(3).getName()+ ": " + board.getPlayer(3).getCheckpointValue();
            case 5:
                return "Winner is: " + getCurrentPlayer().getName() + "       Checkpoints: " +
                        " " + board.getPlayer(0).getName()+ ": " + board.getPlayer(0).getCheckpointValue() +
                        " " + board.getPlayer(1).getName()+ ": " + board.getPlayer(1).getCheckpointValue() +
                        " " + board.getPlayer(2).getName()+ ": " + board.getPlayer(2).getCheckpointValue() +
                        " " + board.getPlayer(3).getName()+ ": " + board.getPlayer(3).getCheckpointValue() +
                        " " + board.getPlayer(4).getName()+ ": " + board.getPlayer(4).getCheckpointValue();
            case 6:
                return "Winner is: " + getCurrentPlayer().getName() + "       Checkpoints: " +
                        " " + board.getPlayer(0).getName()+ ": " + board.getPlayer(0).getCheckpointValue() +
                        " " + board.getPlayer(1).getName()+ ": " + board.getPlayer(1).getCheckpointValue() +
                        " " + board.getPlayer(2).getName()+ ": " + board.getPlayer(2).getCheckpointValue() +
                        " " + board.getPlayer(3).getName()+ ": " + board.getPlayer(3).getCheckpointValue() +
                        " " + board.getPlayer(4).getName()+ ": " + board.getPlayer(4).getCheckpointValue() +
                        " " + board.getPlayer(5).getName()+ ": " + board.getPlayer(5).getCheckpointValue();
        }
        return null;
    }

}
