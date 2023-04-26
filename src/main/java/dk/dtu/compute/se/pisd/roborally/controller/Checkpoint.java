package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * Checkpoint class
 * @author s224567
 */
public class Checkpoint extends FieldAction {

    public static Board Space;
    private dk.dtu.compute.se.pisd.roborally.model.Space space;

    public int getCheckpointnumber() {
        return checkpointnumber;
    }

    public void setCheckpointnumber(int checkpointnumber) {
        this.checkpointnumber = checkpointnumber;
    }

    public int checkpointnumber;


    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    /**
     * This method checks if the player can reach the next checkpoint by taking the player's checkpoint value and adding 1,
     * which should give the checkpoint number.
     * In other words, the player must have a checkpoint value of 1 to reach checkpoint 2.
     *
     * It also checks if a player has reached all checkpoints, and if so, the game will enter the game_ending phase.
     *
     *
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return true if the action has executed
     * @auther s224552
     * @auther s224567
     */
    @Override
    public boolean doAction(GameController gameController, dk.dtu.compute.se.pisd.roborally.model.Space space) {
        Player player1 = space.getPlayer();
        if (checkpointnumber == player1.getCheckpointValue()+1) {
            player1.setCheckpointValue(player1.getCheckpointValue()+1);
            if (player1.getCheckpointValue()==6){
                gameController.board.setPhase(Phase.GAME_ENDING);
                gameController.board.getStatusMessage();
            }
            return true;
        }


        return false;
    }

}
