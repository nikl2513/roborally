package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
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
}
