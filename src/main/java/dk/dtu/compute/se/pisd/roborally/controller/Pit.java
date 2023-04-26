package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;
/**
 * @author s215698
 * @author s224567
 * The class Pit is a subclass of FieldAction.
 * It represents the Action field pit that a robot can land on.
 * Pit contains a variable of the type space which is the space associated with a given pit
 * There is aditionally a getter and setter for the space variable
 *
 * The doAction method contains the specific code that does the action when i robot falls into the pit
 * In this case it will take the player that shares a space with the pit and subtract 1 from its hp variable and then
 * move the player to a randomized starting space on the board, it checks the random space to ensure that there is no player on that space already
 * This is to ensure that we don't accidentally overwrite another players robot.
 * If the players HP variable reachs 0 the player is "Dead" which in this case means they respawn but without the checkpoints they have previously
 * aquired
 */
public class Pit extends FieldAction{
    public static Board Space;

    private dk.dtu.compute.se.pisd.roborally.model.Space space;

    public void pit(){
        space = null;
    }

    public Space getSpace(){return space;}

    public void setSpace(Space space){this.space = space;}


    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        player.setHp(player.getHp() - 1);
        Space randomSpace = gameController.board.getRandomSpace();
        while (randomSpace.getPlayer()!=null){
            randomSpace = gameController.board.getRandomSpace();
        }
            player.setSpace(randomSpace);

        if (player.getHp() == 0) {
            player.setHp(3);
            player.setCheckpointValue(0);
        }
        return  true;
    }

}
