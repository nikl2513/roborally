package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 *
 * @Author s215698
 * Wall is a subclass of Subject
 * This class contains the template used to create walls inside the game.
 * The wall method contains the variables board, heading and space
 * The board is a parameter when you create an instance of the wall object
 * This ensures that the walls are created on the correct board
 * another parameter is the heading which is given to every instance of the object this determinds the way the wall is facing
 * The Heading is then called in another method using the getHeading to check if the Wall is blocking the move of a robot
 *
 */


public class Wall extends Subject {
    public static Board Space;

     final public Board board;

    private Heading heading;

    private  Space space;
    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }


    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {


        if (space == null || space.board == this.board) {
            this.space = space;

            if (space != null) {

                space.setWall(this);

            }
            notifyChange();
        }


        }



    public Wall(Heading heading, Board board) {
        this.board = board;
        this.heading = heading;
        this.space = null;
    }
}




