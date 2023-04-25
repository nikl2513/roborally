package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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

    public void executeaction(Turnpad turnpad, Heading heading, Player player){
        if (Objects.equals(turnpad.getDirection(), "Right")) {
            player.setHeading(heading.next());
        }
        if (Objects.equals(turnpad.getDirection(), "Left")) {
            player.setHeading(heading.prev());
        }
    }
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        if (space.getTurnpad() != null) {
            return true;
        } else {
            return false;
        }
    }


}
