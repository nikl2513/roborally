package dk.dtu.compute.se.pisd.roborally.model;
/**
 *
 * @Author s215698
 *
 */


public class Wall {
    public static Board Space;
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
        this.space = space;
        space.setWall(this);
    }

    public Wall(Heading heading) {
        this.heading = null;
        space = null;
    }
}




