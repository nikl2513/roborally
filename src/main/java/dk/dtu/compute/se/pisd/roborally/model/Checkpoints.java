package dk.dtu.compute.se.pisd.roborally.model;

public class Checkpoints {

    private Space space;

    public Space getSpace() {
        return space;
    }

    public void setSpace(Space space) {
        this.space = space;
    }

    public void Checkpoint(){
        space = null;
        boolean complete = false;
    }
hej
}
