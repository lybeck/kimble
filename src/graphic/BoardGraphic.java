/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import java.util.ArrayList;
import java.util.List;
import logic.board.Board;
import logic.board.Square;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BoardGraphic extends Model {

    private final Board board;
    private List<SquareGraphic> squares;

    public BoardGraphic(Board board, int numberOfTeams, float squareSideLength, float squarePadding) {
        this.board = board;

        float radius = (float) ((squareSideLength + squarePadding) * board.getSquares().size() / (2 * Math.PI));

        generateBoard(numberOfTeams, radius, squareSideLength);
    }

    private void generateBoard(int numberOfTeams, float radius, float squareSideLength) {
        int numberOfSquares = board.getSquares().size();

        float segmentAngle = (float) (2 * Math.PI) / numberOfSquares;
        float currentAngle = 0;
        squares = new ArrayList<>();

        for (Square s : board.getSquares()) {
            Vector3f squarePosition = new Vector3f((float) (radius * Math.cos(currentAngle)), 0f, (float) (radius * Math.sin(currentAngle)));

            SquareGraphic squareGraphic = new SquareGraphic(squarePosition, squareSideLength);
            squareGraphic.rotate(0, -currentAngle, 0);
            squares.add(squareGraphic);

            currentAngle += segmentAngle;
        }
    }

    @Override
    public void update(float dt) {
        for (SquareGraphic s : squares) {
            s.update(dt);
        }
    }

    @Override
    public void render(Shader shader) {
        for (SquareGraphic s : squares) {
            s.render(shader);
        }
    }

    @Override
    public void cleanUp() {
        for (SquareGraphic s : squares) {
            s.cleanUp();
        }
    }
}
