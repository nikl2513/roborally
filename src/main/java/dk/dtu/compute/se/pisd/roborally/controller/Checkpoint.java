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
        return false;
    }

    public void executeAction(Player player1, Checkpoint checkpoint, Board board) {
        int value = player1.getCheckpointValue();
        switch (value) {
            case 0:
                if (player1.getCheckpointValue() == 0 && checkpoint.getCheckpointnumber() == 1) {
                    player1.setCheckpointValue(1);
                }
                break;
            case 1:
                if (player1.getCheckpointValue() == 1 && checkpoint.getCheckpointnumber() == 2) {
                    player1.setCheckpointValue(2);
                }
                break;
            case 2:
                if (player1.getCheckpointValue() == 2 && checkpoint.getCheckpointnumber() == 3) {
                    player1.setCheckpointValue(3);
                }
                break;
            case 3:
                if (player1.getCheckpointValue() == 3 && checkpoint.getCheckpointnumber() == 4) {
                    player1.setCheckpointValue(4);
                }
                break;
            case 4:
                if (player1.getCheckpointValue() == 4 && checkpoint.getCheckpointnumber() == 5) {
                    player1.setCheckpointValue(5);
                }
                break;
            case 5:
                if (player1.getCheckpointValue() == 5 && checkpoint.getCheckpointnumber() == 6) {
                    player1.setCheckpointValue(6);
                    board.setPhase(Phase.GAME_ENDING);
                    board.getStatusMessage();
                }
                break;
        }
    }
}
