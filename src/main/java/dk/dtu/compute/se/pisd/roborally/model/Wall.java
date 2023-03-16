package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 *
 * @Author s215698
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
        Space oldspace = this.space;

        if (space != oldspace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldspace != null) {
                // this should actually not happen
                oldspace.setWall(null);
            }
            if (space != null) {

                space.setWall(this);

            }
            notifyChange();
        }


        }





    public Wall(Heading heading, Board board) {
        this.board = board;
        this.heading = null;
        space = null;
    }
}




