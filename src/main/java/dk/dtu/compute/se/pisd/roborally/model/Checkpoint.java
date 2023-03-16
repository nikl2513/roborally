package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoint {

    public static Board Space;
    private Space space;

   private boolean complete;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void Checkpoint(){
        space = null;
        complete = false;
    }

}
