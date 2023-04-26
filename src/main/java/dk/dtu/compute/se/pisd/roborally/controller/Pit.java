package dk.dtu.compute.se.pisd.roborally.controller;


import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
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
