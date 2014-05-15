/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.shader.Shader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.graphic.board.meshes.BoardMesh;
import kimble.logic.Game;
import kimble.logic.Team;
import kimble.logic.board.Board;
import kimble.logic.board.Square;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class BoardGraphic extends Model {

    public static final List<Vector3f> TEAM_COLORS;

    static {
        TEAM_COLORS = new ArrayList<>();
        TEAM_COLORS.add(new Vector3f(.8f, 0, 0));
        TEAM_COLORS.add(new Vector3f(0, 0, 0.8f));
        TEAM_COLORS.add(new Vector3f(0.8f, 0.8f, 0));
        TEAM_COLORS.add(new Vector3f(0, 0.6f, 0f));
        TEAM_COLORS.add(new Vector3f(1, 0, 1));
        TEAM_COLORS.add(new Vector3f(0, 0.8f, 0.8f));
        TEAM_COLORS.add(new Vector3f(1, 0.4f, 0));
        TEAM_COLORS.add(new Vector3f(.3f, 0.3f, 0.45f));
    }

    public static final Vector3f REGULAR_SQUARE_COLOR = new Vector3f(0.5f, 0.5f, 0.5f);

    private final Board board;
    private final List<Team> teams;
    private Map<Integer, SquareGraphic> squares;
    private Map<Integer, SquareGraphic> goalSquares;
    private Map<Integer, SquareGraphic> homeSquares;

    private float radius;

    private BoardSpecs specs;

    private float segmentAngle;
    private int vertexCount;
    private int firstGoalSquareIndex;

    public BoardGraphic(Board board, List<Team> teams, BoardSpecs specs) {

        this.board = board;
        this.teams = teams;

        if (teams.size() > TEAM_COLORS.size()) {
            throw new UnsupportedOperationException("Can't have more teams than: " + TEAM_COLORS.size());
        }

        this.specs = specs;

        radius = calcRadius();
        generateBoard();

        this.getMaterial().setDiffuse(new Vector4f(REGULAR_SQUARE_COLOR.x, REGULAR_SQUARE_COLOR.y, REGULAR_SQUARE_COLOR.z, 1));
        this.setMesh(new BoardMesh(teams, vertexCount, segmentAngle, goalSquares, firstGoalSquareIndex, specs));
    }

    private float calcRadius() {
        radius = (float) ((specs.squareSideLength + specs.squarePadding) * board.getSquares().size() / (2 * Math.PI));
        float radiusOfGoalSquares = (board.getGoalSquares(0).size() + 1) * (specs.squareSideLength
                + specs.goalSquarePadding) + specs.dieCupRadius;
        radius = Math.max(radius, radiusOfGoalSquares);
        return radius;
    }

    private void generateBoard() {
        int numberOfSquares = board.getSquares().size();

        this.segmentAngle = (float) (2 * Math.PI) / numberOfSquares;

        generateRegularSquares(radius, segmentAngle);
        generateGoalSquares(radius, segmentAngle);
        generateHomeSquares(radius, segmentAngle);
    }

    private void generateRegularSquares(float radius, float segmentAngle) {
        float currentAngle = 0;

        squares = new HashMap<>();
        for (Square s : board.getSquares()) {

            Vector3f squarePosition = new Vector3f((float) (radius * Math.cos(currentAngle)), 0f, (float) (radius
                    * Math.sin(currentAngle)));

            Vector3f squareColor = null;
            int squareID = s.getID();

            for (int teamID = 0; teamID < teams.size(); teamID++) {
                if (board.getStartSquare(teamID).getID() == squareID) {
                    float r = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).x;
                    float g = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).y;
                    float b = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).z;
                    squareColor = new Vector3f(r, g, b);
                    break;
                }
            }
            if (squareColor == null) {
                squareColor = REGULAR_SQUARE_COLOR;
            }

            SquareGraphic squareGraphic = new SquareGraphic(squarePosition, squareColor);
            squareGraphic.rotate(0, -currentAngle, 0);
            squares.put(squareID, squareGraphic);

            currentAngle += segmentAngle;
        }
    }

    private void generateGoalSquares(float radius, float segmentAngle) {
        goalSquares = new HashMap<>();
        for (int teamID = 0; teamID < teams.size(); teamID++) {

            Square startSquare = board.getStartSquare(teamID);
            float currentAngle = -squares.get(startSquare.getID()).getRotation().y;

            for (int i = 0; i < board.getGoalSquares(teamID).size(); i++) {

                Square goalSquare = board.getGoalSquares(teamID).get(i);

                float tempRadius = radius - ((i + 1) * (specs.squareSideLength + specs.goalSquarePadding));
                Vector3f goalPosition = new Vector3f((float) (tempRadius * Math.cos(currentAngle - 0.5f * segmentAngle)), 0, (float) (tempRadius
                        * Math.sin(currentAngle - 0.5f * segmentAngle)));

                int squareID = goalSquare.getID();
                if (firstGoalSquareIndex == 0) {
                    firstGoalSquareIndex = squareID;
                }

                float r = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).x;
                float g = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).y;
                float b = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).z;
                Vector3f color = new Vector3f(r, g, b);
                SquareGraphic squareGraphic = new SquareGraphic(goalPosition, color);
                squareGraphic.rotate(0, -(currentAngle - 0.5f * segmentAngle), 0);
                goalSquares.put(squareID, squareGraphic);
            }
        }
    }

    private void generateHomeSquares(float radius, float segmentAngle) {
        homeSquares = new HashMap<>();
        for (int teamID = 0; teamID < teams.size(); teamID++) {

            Square startSquare = board.getStartSquare(teamID);
            float currentAngle = -squares.get(startSquare.getID()).getRotation().y;

            float tempRadius = radius + specs.squareSideLength + specs.goalSquarePadding;
            Vector3f homePosition = new Vector3f((float) (tempRadius * Math.cos(currentAngle - 0.5f * segmentAngle)), 0, (float) (tempRadius
                    * Math.sin(currentAngle - 0.5f * segmentAngle)));
            Vector3f right = new Vector3f();
            Vector3f.cross(new Vector3f(0, 1, 0), homePosition, right);

            right.normalise().scale((specs.squareSideLength + specs.goalSquarePadding)
                    * teams.get(teamID).getPieces().size() / 2.0f - 0.5f * (specs.squareSideLength
                    + specs.goalSquarePadding));
            homePosition.translate(-right.x, -right.y, -right.z);

            right.normalise().scale(specs.squareSideLength + specs.goalSquarePadding);

            for (int i = 0; i < teams.get(teamID).getPieces().size(); i++) {
                int squareID = teams.get(teamID).getPieces().size() * teamID + i;

                float r = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).x;
                float g = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).y;
                float b = specs.specialSquareColorFadeFactor * TEAM_COLORS.get(teamID).z;
                Vector3f color = new Vector3f(r, g, b);

                SquareGraphic squareGraphic = new SquareGraphic(homePosition, color);
                squareGraphic.rotate(0, -(currentAngle - 0.5f * segmentAngle), 0);
                homeSquares.put(squareID, squareGraphic);

                homePosition = new Vector3f(homePosition.x + right.x, homePosition.y + right.y, homePosition.z + right.z);
            }
        }
    }

    @Override
    public void update(float dt) {
        super.update(dt);
        for (int squareIndex : squares.keySet()) {
            squares.get(squareIndex).update(dt);
        }
        for (int squareIndex : goalSquares.keySet()) {
            goalSquares.get(squareIndex).update(dt);
        }
        for (int squareIndex : homeSquares.keySet()) {
            homeSquares.get(squareIndex).update(dt);
        }
    }

    @Override
    public void render(Shader shader) {
        super.render(shader);
        for (int squareIndex : squares.keySet()) {
            squares.get(squareIndex).render(shader);
        }
        for (int squareIndex : goalSquares.keySet()) {
            goalSquares.get(squareIndex).render(shader);
        }
        for (int squareIndex : homeSquares.keySet()) {
            homeSquares.get(squareIndex).render(shader);
        }
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        for (int squareIndex : squares.keySet()) {
            squares.get(squareIndex).cleanUp();
        }
        for (int squareIndex : goalSquares.keySet()) {
            goalSquares.get(squareIndex).cleanUp();
        }
        for (int squareIndex : homeSquares.keySet()) {
            homeSquares.get(squareIndex).cleanUp();
        }
    }

    public Map<Integer, SquareGraphic> getSquares() {
        return squares;
    }

    public Map<Integer, SquareGraphic> getGoalSquares() {
        return goalSquares;
    }

    public Map<Integer, SquareGraphic> getHomeSquares() {
        return homeSquares;
    }

    public SquareGraphic getEmptyHomeSquare(int pieceID, int teamID) {
        return homeSquares.get(teamID * teams.get(teamID).getPieces().size() + pieceID);
    }

    public float getRadius() {
        return radius;
    }
}
