package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 *
 *
 * @Author s215698
 *
 */
public class ImpossibleMoveException extends Exception {

        private Player player;
        private Space space;
        private Heading heading;

        public ImpossibleMoveException (
                Player player,
                Space space,
                Heading heading){

            super("Impossible move");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }

