package dk.dtu.compute.se.pisd.roborally.fileaccess;

import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.Pit;
import dk.dtu.compute.se.pisd.roborally.controller.Turnpad;
import dk.dtu.compute.se.pisd.roborally.model.*;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.*;

public class Boards {

    public static Board createBoard(String boardName) {

        Board board = new Board(8, 8);

        Wall wall1 = new Wall(SOUTH, board);
        Wall wall2 = new Wall(NORTH, board);
        Wall wall3 = new Wall(EAST, board);
        Wall wall4 = new Wall(WEST, board);

        board.addwall(wall1);
        board.addwall(wall2);
        board.addwall(wall3);
        board.addwall(wall4);

        wall1.setSpace(board.getSpace(3, 2));
        wall2.setSpace(board.getSpace(7, 2));
        wall3.setSpace(board.getSpace(5, 3));
        wall4.setSpace(board.getSpace(1, 7));


        Conveyerbelt conveyerbelt1 = new Conveyerbelt();
        board.addConveyerbelt(conveyerbelt1);
        conveyerbelt1.setHeading(WEST);
        conveyerbelt1.setSpace(board.getSpace(1, 3));
        Space space4 = board.getSpace(1, 3);
        space4.setConveyerbelt(conveyerbelt1);

        Conveyerbelt conveyerbelt2 = new Conveyerbelt();
        board.addConveyerbelt(conveyerbelt2);
        conveyerbelt2.setHeading(NORTH);
        conveyerbelt2.setSpace(board.getSpace(4, 6));
        Space space6 = board.getSpace(4, 6);
        space6.setConveyerbelt(conveyerbelt2);

        Turnpad turnpad1 = new Turnpad();
        board.addTurnpad(turnpad1);

        turnpad1.setSpace(board.getSpace(4,7));
        Space space1t = board.getSpace(4,7);
        space1t.setTurnpad(turnpad1);

        Pit pit1 = new Pit();
        board.addpit(pit1);
        pit1.setSpace(board.getSpace(0,3));
        Space space1p = board.getSpace(0,3);
        space1p.setPit(pit1);


        Checkpoint checkpoint1 = new Checkpoint();
        Checkpoint checkpoint2 = new Checkpoint();
        Checkpoint checkpoint3 = new Checkpoint();
        Checkpoint checkpoint4 = new Checkpoint();
        Checkpoint checkpoint5 = new Checkpoint();
        Checkpoint checkpoint6 = new Checkpoint();
        checkpoint1.setCheckpointnumber(1);
        checkpoint2.setCheckpointnumber(2);
        checkpoint3.setCheckpointnumber(3);
        checkpoint4.setCheckpointnumber(4);
        checkpoint5.setCheckpointnumber(5);
        checkpoint6.setCheckpointnumber(6);
        board.addCheckpoint(checkpoint1);
        board.addCheckpoint(checkpoint2);
        board.addCheckpoint(checkpoint3);
        board.addCheckpoint(checkpoint4);
        board.addCheckpoint(checkpoint5);
        board.addCheckpoint(checkpoint6);
        checkpoint1.setSpace(board.getSpace(0, 1));
        checkpoint2.setSpace(board.getSpace(2, 5));
        checkpoint3.setSpace(board.getSpace(7, 7));
        checkpoint4.setSpace(board.getSpace(4, 1));
        checkpoint5.setSpace(board.getSpace(0, 6));
        checkpoint6.setSpace(board.getSpace(2, 7));
        Space space1c = board.getSpace(0, 1);
        Space space2c = board.getSpace(2, 5);
        Space space3c = board.getSpace(7, 7);
        Space space4c = board.getSpace(4, 1);
        Space space5c = board.getSpace(0, 6);
        Space space6c = board.getSpace(2, 7);
        space1c.setCheckpoint(checkpoint1);
        space2c.setCheckpoint(checkpoint2);
        space3c.setCheckpoint(checkpoint3);
        space4c.setCheckpoint(checkpoint4);
        space5c.setCheckpoint(checkpoint5);
        space6c.setCheckpoint(checkpoint6);

        return board;

    }

}
