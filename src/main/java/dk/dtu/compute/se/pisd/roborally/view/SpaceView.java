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
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.controller.ConveyorBelt;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
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

import java.awt.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 40; // 60; // 75;
    final public static int SPACE_WIDTH = 40;  // 60; // 75;

    public final Space space;


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

    @Override
    public void updateView(Subject subject) {
        if (subject == this.space) {
            updatePlayer();

            for (FieldAction action :space.getActions()){
                if (action instanceof ConveyorBelt){
                    ConveyorBelt conveyorBelt = (ConveyorBelt) action;
                    addConveyerbelt(conveyorBelt.getHeading());
                }
                if (action instanceof Checkpoint){
                    Checkpoint checkpoint = (Checkpoint) action;
                    checkpoint.getCheckpointnumber();
                    addCheckpoints(checkpoint.getCheckpointnumber());
                }

            }

            for (Heading heading :space.getWalls()){
                addwall(heading);
            }


        }
    }

    private void addCheckpoints(int Checkponitnumber) {

           switch (Checkponitnumber) {
               case 1 -> {
                   Text text   = createText("1");
                   Circle circle = new Circle(10, 10, 10);
                   circle.setFill(Color.YELLOW);
                   //this.getChildren().add(circle);
                   this.getChildren().addAll(circle, text);
                   break;
               }
               case 2 -> {
                   Text text   = createText("2");
                   Circle circle1 = new Circle(10, 10, 10);
                   circle1.setFill(Color.AQUA);
                   this.getChildren().addAll(circle1, text);
                   break;
               }
               case 3 -> {
                   Text text   = createText("3");
                   Circle circle2 = new Circle(10, 10, 10);
                   circle2.setFill(Color.RED);
                   this.getChildren().addAll(circle2, text);
                   break;
               }
               case 4 -> {
                   Text text   = createText("4");
                   Circle circle3 = new Circle(10, 10, 10);
                   circle3.setFill(Color.BROWN);
                   this.getChildren().addAll(circle3, text);
                   break;
               }
               case 5 -> {
                   Text text   = createText("5");
                   Circle circle4 = new Circle(10, 10, 10);
                   circle4.setFill(Color.DARKGREY);
                   this.getChildren().addAll(circle4, text);
                   break;
               }
               case 6 -> {
                   Text text   = createText("6");
                   Circle circle5 = new Circle(10, 10, 10);
                   circle5.setFill(Color.PURPLE);
                   this.getChildren().addAll(circle5, text);
                   break;
               }
           }


           }




    public void addwall(Heading heading) {


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



    public void addConveyerbelt (Heading heading) {

        switch (heading) {
            case NORTH: {
                Rectangle rectangleN = new Rectangle(35, 35);
                rectangleN.setStroke(Color.RED);
                this.getChildren().add(rectangleN);
                Polygon arrowN = new Polygon(0.0, 0.0,
                        17.5, 35.0,
                        35.0, 0.0);
                try {
                    arrowN.setFill(Color.valueOf("YELLOW"));
                } catch (Exception e) {
                    arrowN.setFill(Color.YELLOW);
                }

                arrowN.setRotate(180);
                this.getChildren().add(arrowN);
                break;
            }

            case EAST: {
                Rectangle rectangleE = new Rectangle(35, 35);
                rectangleE.setStroke(Color.RED);
                this.getChildren().add(rectangleE);
                Polygon arrowE = new Polygon(0.0, 0.0,
                        17.5, 35.0,
                        35.0, 0.0);
                try {
                    arrowE.setFill(Color.valueOf("YELLOW"));
                } catch (Exception e) {
                    arrowE.setFill(Color.YELLOW);
                }

                arrowE.setRotate(270);
                this.getChildren().add(arrowE);
                break;
            }

            case SOUTH: {
                Rectangle rectangleS = new Rectangle(35, 35);
                rectangleS.setStroke(Color.RED);
                this.getChildren().add(rectangleS);
                Polygon arrowS = new Polygon(0.0, 0.0,
                        17.5, 35.0,
                        35.0, 0.0);
                try {
                    arrowS.setFill(Color.valueOf("YELLOW"));
                } catch (Exception e) {
                    arrowS.setFill(Color.YELLOW);
                }

                arrowS.setRotate(0);
                this.getChildren().add(arrowS);
                break;
            }

            case WEST: {
                Rectangle rectangleW = new Rectangle(35, 35);
                rectangleW.setStroke(Color.RED);
                this.getChildren().add(rectangleW);
                Polygon arrowW = new Polygon(0.0, 0.0,
                        17.5, 35.0,
                        35.0, 0.0);
                try {
                    arrowW.setFill(Color.valueOf("YELLOW"));
                } catch (Exception e) {
                    arrowW.setFill(Color.YELLOW);
                }

                arrowW.setRotate(90);
                this.getChildren().add(arrowW);
                break;
            }
        }

    }


    /**
     * From: https://stackoverflow.com/questions/23258605/javafx-how-can-i-best-place-a-label-centered-in-a-shape
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





