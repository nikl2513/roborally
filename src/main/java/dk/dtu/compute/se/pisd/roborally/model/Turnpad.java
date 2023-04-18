package dk.dtu.compute.se.pisd.roborally.model;

public class Turnpad {
    private Space space;
    private String direction;
    public static Board Space;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }


    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void turnpad(){
    space = null;
    direction = null;

}


}
