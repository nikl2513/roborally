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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.*;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

    public final Space space;


    /**
     * here we design the boards height and width. We design the colours on the board,
     * where the spaces mainly is made of black and white spaces.
     *
     * @param space
     */
    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * The updatePlayer() method basically designs what a player looks like in the game
     * So we check if there is a player, and if there is a player, we give the player a shape, a colour and a heading
     */

    private void updatePlayer() {
        this.getChildren().clear();

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }


    /**
     * @param subject the subject of the observer design pattern
     * @author s224576
     * @author s224552
     * UpdateView adds our objects to the board
     * so if we use the addCheckpoints() method to the UpdateView() it will then be added to the board
     */
    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();
            for (FieldAction action : space.getActions()) {
                if (action instanceof ConveyorBelt) {
                    ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                    addConveyerbelt(conveyorBelt.getHeading());
                }
                if (action instanceof Checkpoint) {
                    Checkpoint checkpoint = (Checkpoint) action;

                    addCheckpoints(checkpoint.getCheckpointnumber());
                }
                if (action instanceof Turnpad) {
                    Turnpad turnpad = (Turnpad) action;
                    addTurnpad(turnpad.getDirection());
                }
                if (action instanceof Pit) {
                    addPit();
                }

            }

            for (Heading heading : space.getWalls()) {
                addwall(heading);
            }


        }

    }

    /**
     * Here we design our Checkpoints. We design the colour, the shape, and assign a number
     * so we know, what checkpoints a player is supposed to pick up first.
     * The method can then be called in updateview() and be visible on the board
     *
     * @param Checkpointnumber the Checkpoints that are to be collected in the game.
     * @author s224552
     */

    private void addCheckpoints(int Checkpointnumber) {

        switch (Checkpointnumber) {
            case 1 -> {
                Text text = createText("1");
                Circle circle = new Circle(10, 10, 10);
                circle.setFill(Color.YELLOW);
                //this.getChildren().add(circle);
                this.getChildren().addAll(circle, text);
                break;
            }
            case 2 -> {
                Text text = createText("2");
                Circle circle1 = new Circle(10, 10, 10);
                circle1.setFill(Color.AQUA);
                this.getChildren().addAll(circle1, text);
                break;
            }
            case 3 -> {
                Text text = createText("3");
                Circle circle2 = new Circle(10, 10, 10);
                circle2.setFill(Color.RED);
                this.getChildren().addAll(circle2, text);
                break;
            }
            case 4 -> {
                Text text = createText("4");
                Circle circle3 = new Circle(10, 10, 10);
                circle3.setFill(Color.BROWN);
                this.getChildren().addAll(circle3, text);
                break;
            }
            case 5 -> {
                Text text = createText("5");
                Circle circle4 = new Circle(10, 10, 10);
                circle4.setFill(Color.DARKGREY);
                this.getChildren().addAll(circle4, text);
                break;
            }
            case 6 -> {
                Text text = createText("6");
                Circle circle5 = new Circle(10, 10, 10);
                circle5.setFill(Color.PURPLE);
                this.getChildren().addAll(circle5, text);
                break;
            }
        }


    }


    /**
     * We design our walls. We give them a heading on the board and assign a calour and a length.
     * Then the addwall() method can be called in updateView(), which will add them to the board.
     *
     * @param heading the heading of the walls
     */

    public void addwall(Heading heading) {
        /**
         * @author s224552
         * @author s215698
         *
         */

        Canvas canvas = new Canvas(SPACE_HEIGHT, SPACE_WIDTH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        switch (heading) {


            case NORTH:
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.strokeLine(2, SPACE_HEIGHT - 38, SPACE_WIDTH - 2, SPACE_HEIGHT - 38);

                break;
            //nord
            case EAST:


                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.strokeLine(38, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 38);

                break;

            //west
            case WEST:
                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 38, SPACE_HEIGHT - 38);

                break;
            //south

            case SOUTH:

                gc.setStroke(Color.RED);
                gc.setLineWidth(5);
                gc.setLineCap(StrokeLineCap.ROUND);
                gc.strokeLine(2, SPACE_HEIGHT - 2, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);

                break;

            default:
                break;
        }
        this.getChildren().add(canvas);

    }


    /**
     * Here we design our conveyerbelt. We give our conveyerbelts a heading, a design, and a colour.
     * Then the addConveyerbelt() method can be called in updateview() which will add our conveyerbelt to the board.
     *
     * @param heading the heading of the Conveyerbelt
     * @author s224552
     */
    public void addConveyerbelt(Heading heading) {

        switch (heading) {
            case NORTH: {
                Rectangle rectangleN = new Rectangle(35, 35);
                rectangleN.setStroke(Color.RED);
                //this.getChildren().add(rectangleN);
                Polygon arrowN = new Polygon(0.0, 0.0,
                        7.5, 15.0,
                        15.0, 0.0);
                try {
                    arrowN.setFill(Color.valueOf("CHOCOLATE"));
                } catch (Exception e) {
                    arrowN.setFill(Color.CHOCOLATE);
                }

                arrowN.setRotate(180);
                this.getChildren().add(arrowN);
                break;
            }

            case EAST: {
                Rectangle rectangleE = new Rectangle(35, 35);
                rectangleE.setStroke(Color.RED);
                //this.getChildren().add(rectangleE);
                Polygon arrowE = new Polygon(0.0, 0.0,
                        7.5, 15.0,
                        15.0, 0.0);
                try {
                    arrowE.setFill(Color.valueOf("CHOCOLATE"));
                } catch (Exception e) {
                    arrowE.setFill(Color.CHOCOLATE);
                }

                arrowE.setRotate(270);
                this.getChildren().add(arrowE);
                break;
            }

            case SOUTH: {
                Rectangle rectangleS = new Rectangle(35, 35);
                rectangleS.setStroke(Color.RED);
                //this.getChildren().add(rectangleS);
                Polygon arrowS = new Polygon(0.0, 0.0,
                        7.5, 15.0,
                        15.0, 0.0);
                try {
                    arrowS.setFill(Color.valueOf("CHOCOLATE"));
                } catch (Exception e) {
                    arrowS.setFill(Color.CHOCOLATE);
                }

                arrowS.setRotate(0);
                this.getChildren().add(arrowS);
                break;
            }

            case WEST: {
                Rectangle rectangleW = new Rectangle(35, 35);
                rectangleW.setStroke(Color.RED);
                //this.getChildren().add(rectangleW);
                Polygon arrowW = new Polygon(0.0, 0.0,
                        7.5, 15.0,
                        15.0, 0.0);
                try {
                    arrowW.setFill(Color.valueOf("CHOCOLATE"));
                } catch (Exception e) {
                    arrowW.setFill(Color.CHOCOLATE);
                }

                arrowW.setRotate(90);
                this.getChildren().add(arrowW);
                break;
            }
        }
    }

    /**
     * Here we design our turnpad. we design a shape, assign a colour and add text on the turnpad to indicate what function it has
     * Then the addTurnpad() method can be called in updateview() which will add our turnpad to the board
     *
     * @param d describes the type of turnpad it is, as we have 2 types.
     */
    public void addTurnpad(int d) {
        /**
         * @author s215698
         * @author s224552
         */
        if (d == 1) {
            Text text = createText("Left");
            Circle circle = new Circle(12, 12, 12);
            circle.setFill(Color.GREENYELLOW);
            this.getChildren().addAll(circle, text);
        }
        if (d == 2) {
            Text text = createText("Right");
            Circle circle = new Circle(12, 12, 12);
            circle.setFill(Color.GREENYELLOW);
            this.getChildren().addAll(circle, text);
        }

    }

    /**
     * Here we design our addPit. we design a shape and assign a colour to the pit.
     * Then the addPit() method can be called in updateview() which will add our pit to the board
     */
    public void addPit() {
        /**
         * @author s215698
         * @author s224552
         */
        Rectangle circle = new Rectangle(25, 25);
        circle.setFill(Color.GREY);
        this.getChildren().addAll(circle);
    }


    /**
     * From: https://stackoverflow.com/questions/23258605/javafx-how-can-i-best-place-a-label-centered-in-a-shape
     *
     * @param string
     * @return A texts string.
     */
    private Text createText(String string) {
        Text text = new Text(string);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setStyle(
                "-fx-font-family: \"Times New Roman\";" +
                        "-fx-font-style: italic;" +
                        "-fx-font-size: 12px;"
        );
        return text;
    }
}
