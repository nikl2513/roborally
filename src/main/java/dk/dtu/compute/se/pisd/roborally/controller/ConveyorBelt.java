/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import org.jetbrains.annotations.NotNull;

/**
 * this a class that has the conveyorbelt action on it. it needs a heading and a space to tell where the conveyorbelt is.
 * there is a getter and a setter for both heading and space, so you can change and see where the conveyorbelt is.
 * conveyorbelt starts with a heading and space is set to null.
 * do action is the one that gamecontroller calss everytime someone moves. if the action on the space is a conveyorbelt.
 * then it goes in to the doaction in this class and moves the player.
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author s224549
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;

    private Space space;

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

    public ConveyorBelt(Heading heading) {
        this.heading = heading;
        space = null;
    }

    /**
     * @author s224549
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * it checks if the space next to the player is not null, then it goes in to the try statement and moves the player.
     * @return
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        Space space2 = gameController.board.getNeighbour(space,heading);
        if (space2 != null) {
            try {
                gameController.moveToSpace(player, space2, heading);
                player.setSpace(space2);
            } catch (ImpossibleMoveException e) {
            }
            return true;
        }
         else {
            return false;
        }
    }
}

