package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Turnpad extends FieldAction{
    private dk.dtu.compute.se.pisd.roborally.model.Space space;
    private int direction;
    public static Board Space;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }


    public int getDirection() {
        return direction;
    }



    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
       Player player = space.getPlayer();
       Heading heading =  player.getHeading();

       if (direction ==1 ){
           player.setHeading(heading.prev());
           return  true;
       }
       if (direction==2){
           player.setHeading(heading.next());
           return  true;
       }



        return  false;
    }


}
