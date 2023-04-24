package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

public class Turnpad extends FieldAction{
    private dk.dtu.compute.se.pisd.roborally.model.Space space;
    private String direction;
    public static Board Space;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void turnpad(){
    space = null;
    direction = null;

}
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space.getTurnpad() != null) {
            return true;
        } else {
            return false;
        }
    }


}
