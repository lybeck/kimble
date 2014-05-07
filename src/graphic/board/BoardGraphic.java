/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic.board;

import graphic.Model;
import graphic.Shader;
import java.util.ArrayList;
import java.util.List;
import logic.Game;
import logic.board.Square;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BoardGraphic extends Model {

    public static final List<Vector3f> teamColors;

    static {
        teamColors = new ArrayList<>();
        teamColors.add(new Vector3f(1, 0, 0));
        teamColors.add(new Vector3f(0, 1, 0));
        teamColors.add(new Vector3f(0, 0, 1));
        teamColors.add(new Vector3f(1, 1, 0));
        teamColors.add(new Vector3f(1, 0, 1));
        teamColors.add(new Vector3f(0, 1, 1));
        teamColors.add(new Vector3f(1, 1, 1));
    }

    private final Game game;
    private List<SquareGraphic> squares;

    private float squareSideLength;
    private float goalSquarePadding;

    public BoardGraphic(Game game, float squareSideLength, float squarePadding, float goalSquarePadding) {
        this.game = game;

        if (game.getTeams().size() > teamColors.size()) {
            throw new UnsupportedOperationException("Can't have more teams than: " + teamColors.size());
        }

        this.squareSideLength = squareSideLength;
        this.goalSquarePadding = goalSquarePadding;

        float radius = (float) ((squareSideLength + squarePadding) * game.getBoard().getSquares().size() / (2 * Math.PI));

        generateBoard(radius);
    }

    private void generateBoard(float radius) {
        int numberOfSquares = game.getBoard().getSquares().size();

        float segmentAngle = (float) (2 * Math.PI) / numberOfSquares;
        float currentAngle = 0;
        squares = new ArrayList<>();

        for (Square s : game.getBoard().getSquares()) {
            Vector3f squarePosition = new Vector3f((float) (radius * Math.cos(currentAngle)), 0f, (float) (radius * Math.sin(currentAngle)));

            Vector3f squareColor = null;
            // Loops over all teams and creates their goal squares and changes the color on the starting square.
            for (int i = 0; i < game.getTeams().size(); i++) {
                if (s.equals(game.getBoard().getStartSquare(game.getTeams().get(i).getId()))) {
                    Vector3f c = teamColors.get(i);
                    squareColor = new Vector3f(c.x * 0.7f, c.y * 0.7f, c.z * 0.7f);

                    // Creates the goal squares for team 'i'
                    for (int j = 0; j < game.getTeam(i).getPieces().size(); j++) {
                        float tempRadius = (radius - ((j + 1) * (squareSideLength + goalSquarePadding)));
                        Vector3f goalPosition = new Vector3f((float) (tempRadius * Math.cos(currentAngle - segmentAngle)), 0, (float) (tempRadius * Math.sin(currentAngle - segmentAngle)));
                        SquareGraphic goalSquareGraphic = new SquareGraphic(goalPosition, squareSideLength, squareColor);
                        goalSquareGraphic.rotate(0, -(currentAngle - segmentAngle), 0);
                        squares.add(goalSquareGraphic);
                    }
                }
            }
            if (squareColor == null) {
                squareColor = new Vector3f(0.5f, 0.5f, 0.5f);
            }

            SquareGraphic squareGraphic = new SquareGraphic(squarePosition, squareSideLength, squareColor);
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

    public List<SquareGraphic> getSquares() {
        return squares;
    }
}
