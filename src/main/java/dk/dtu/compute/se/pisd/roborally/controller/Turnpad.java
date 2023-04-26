package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author s215698
 * @author s224552
 * The class Turnpad is a subclass of FieldAction.
 * It represents the Action field pit that a robot can land on.
 * Pit contains a variable of the type space which is the space associated with a given pit
 * There is aditionally a getter and setter for the space variable
 * Turnpad also contains a type int variable called direction this is used to determin if the Actionfield
 * should move a robot left or move them right
 *
 * doAction for this Class is what updates the players Heading in moving it either left or right depending on the value of direction
 * It does this by using getPlayer on the space to find the player who shares a space
 * Then the getHeading takes the players current heading and depending on if direction is 1 or 2 moves the heading 1 to the left or right accordingly
 */
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
        /**
         * @author s215698
         * @author s224552
         */
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
