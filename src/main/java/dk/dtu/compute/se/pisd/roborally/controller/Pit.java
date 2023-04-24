package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Pit extends FieldAction{
    public static Board Space;

    private dk.dtu.compute.se.pisd.roborally.model.Space space;

    public void pit(){
        space = null;
    }

    public Space getSpace(){return space;}

    public void setSpace(Space space){this.space = space;}

    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space.getPit() != null) {
            return true;
        } else {
            return false;
        }
    }

}
