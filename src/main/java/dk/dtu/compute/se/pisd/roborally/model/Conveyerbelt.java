package dk.dtu.compute.se.pisd.roborally.model;

public class Conveyerbelt {
    public static Board Space;
    private Space space;
    private Heading heading;
    public void conveyerbelt(){
        space = null;
        heading = null;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public Heading getHeading() {
        return heading;
    }

    public Space getSpace() {
        return space;
    }

}
