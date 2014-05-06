/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.util.ArrayList;
import java.util.List;
import logic.Board;
import logic.Game;
import logic.Square;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BoardGraphic {

    private final Game game;
    private List<SquareGraphic> squares;

    public BoardGraphic(Game game, float squareWidth, float squarePadding) {
        this.game = game;
        setupSquaresInPolygon(squareWidth, squarePadding);
//        setupSquaresInCircle(squareWidth, squarePadding);
    }

    private void setupSquaresInCircle(float squareWidth, float squarePadding) {
        Board board = game.getBoard();
        int numberOfSquares = board.getSquares().size();

        float cornerAngle = 0;
        cornerAngle = (float) (2 * Math.PI) / numberOfSquares;

        System.out.println("Corner Angle: " + cornerAngle + " = " + Math.toDegrees(cornerAngle));

        float distance = squareWidth + squarePadding;
        float currentAngle = 0f;

        squares = new ArrayList<>();

        Vector3f directionVector;
        Vector3f position = new Vector3f();
        for (Square s : board.getSquares()) {
            directionVector = new Vector3f((float) Math.cos(currentAngle), 0f, (float) Math.sin(currentAngle));
            System.out.println("directionVector: " + directionVector);
            currentAngle += cornerAngle;
            position = new Vector3f(position.x + directionVector.x * distance, 0, position.z + directionVector.z * distance);
            squares.add(new SquareGraphic(s, squareWidth, position));
        }
    }

    private void setupSquaresInPolygon(float squareWidth, float squarePadding) {
        Board board = game.getBoard();
        int numberOfTeams = game.getNumberOfTeams();

        if (numberOfTeams < 3) {
            throw new UnsupportedOperationException("Can't play with less than three teams yet!");
        }

        float cornerAngle = 0;
        if (numberOfTeams == 3) {
            cornerAngle = (float) (Math.PI - Math.PI / numberOfTeams);
        } else {
            cornerAngle = (float) (2 * Math.PI) / numberOfTeams;
        }

        System.out.println("Corner Angle: " + cornerAngle + " = " + Math.toDegrees(cornerAngle));

        float distance = squareWidth + squarePadding;
        float currentAngle = 0f;

        squares = new ArrayList<>();

        Vector3f directionVector;
        Vector3f position = new Vector3f();
        for (Square s : board.getSquares()) {
            directionVector = new Vector3f((float) Math.cos(currentAngle), 0f, (float) Math.sin(currentAngle));
            System.out.println("directionVector: " + directionVector);
            for (int teamId = 0; teamId < game.getNumberOfTeams(); teamId++) {
                if (s.equals(board.getStartSquare(teamId))) {
                    currentAngle += cornerAngle;
                }
            }
            position = new Vector3f(position.x + directionVector.x * distance, 0f, position.z + directionVector.z * distance);
            squares.add(new SquareGraphic(s, squareWidth, position));
        }
    }

    public void render() {
        for (SquareGraphic s : squares) {
            s.render();
        }
    }
}
