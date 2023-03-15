package dk.dtu.compute.se.pisd.roborally.model;

/**
 *
 * @Author s215698
 *
 */


public class Wall {


    private Heading heading;

    private  Space space;
    private boolean ifWall;

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
    }

    public void wall() {
        heading = null;
        space = null;
    }
    public boolean iswall(Space space, Space neighbourghSpace, Heading heading){
        if (space.){

        }
        )
    }
    public void setWall(Space space, Heading heading){
        this.space = space;
        this.heading = heading;
        ifWall = true;
    }
}



