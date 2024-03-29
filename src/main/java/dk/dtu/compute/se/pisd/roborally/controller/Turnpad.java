package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * The class Turnpad is a subclass of FieldAction.
 * It represents the Action field pit that a robot can land on.
 * Pit contains a variable of the type space which is the space associated with a given pit
 * There is aditionally a getter and setter for the space variable
 * Turnpad also contains a type int variable called direction this is used to determin if the Actionfield
 * should move a robot left or move them right
 * <p>
 * doAction for this Class is what updates the players Heading in moving it either left or right depending on the value of direction
 * It does this by using getPlayer on the space to find the player who shares a space
 * Then the getHeading takes the players current heading and depending on if direction is 1 or 2 moves the heading 1 to the left or right accordingly
 * @author s215698
 * @author s224552
 */
public class Turnpad extends FieldAction {
    private int direction;
    public int getDirection() {
        return direction;
    }

    /**
     * @param gameController the gameController of the respective game
     * @param space          the space this action should be executed for
     * turns the player depending on which way the player choose.
     * @return
     * @author s215698
     * @author s224552
     */
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {

        Player player = space.getPlayer();
        Heading heading = player.getHeading();

        if (direction == 1) {
            player.setHeading(heading.prev());
            return true;
        }
        if (direction == 2) {
            player.setHeading(heading.next());
            return true;
        }


        return false;
    }


}
