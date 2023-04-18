package dk.dtu.compute.se.pisd.roborally.model;



public class Pit {
    public static Board Space;

    private Space space;

    public void pit(){
        space = null;
    }

    public Space getSpace(){return space;}

    public void setSpace(Space space){this.space = space;}



}
