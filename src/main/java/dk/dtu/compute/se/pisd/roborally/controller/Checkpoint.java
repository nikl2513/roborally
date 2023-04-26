package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/*
checkpoint klasse, s224567
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
