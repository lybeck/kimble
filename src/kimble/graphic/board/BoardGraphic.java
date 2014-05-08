/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.Shader;
import kimble.graphic.VertexData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.logic.Game;
import kimble.logic.board.Square;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BoardGraphic extends Model {

    public static final List<Vector3f> teamColors;

    static {
        teamColors = new ArrayList<>();
        teamColors.add(new Vector3f(.8f, 0, 0));
        teamColors.add(new Vector3f(0, 0, 0.8f));
        teamColors.add(new Vector3f(0.8f, 0.8f, 0));
        teamColors.add(new Vector3f(0, 0.6f, 0f));
        teamColors.add(new Vector3f(1, 0, 1));
        teamColors.add(new Vector3f(0, 0.8f, 0.8f));
        teamColors.add(new Vector3f(1, 0.4f, 0));
        teamColors.add(new Vector3f(.3f, 0.3f, 0.45f));
    }

    private final Game game;
    private Map<Integer, SquareGraphic> squares;
    private Map<Integer, SquareGraphic> goalSquares;
    private Map<Integer, SquareGraphic> homeSquares;

    private float radius;
    private float squareSideLength;
    private float goalSquarePadding;
    private float dieCupRadius = 10;

    private float fade = 0.6f;

    private float boardOuterPadding = 1.15f;
    private float segmentAngle;
    private int vertexCount;
    private int firstGoalSquareIndex;

    public BoardGraphic(Game game, float squareSideLength, float squarePadding, float goalSquarePadding) {
        this.game = game;

        if (game.getTeams().size() > teamColors.size()) {
            throw new UnsupportedOperationException("Can't have more teams than: " + teamColors.size());
        }

        this.squareSideLength = squareSideLength;
        this.goalSquarePadding = goalSquarePadding;

        radius = calcRadius(squareSideLength, squarePadding, game, goalSquarePadding);

        generateBoard();

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {

        this.vertexCount = game.getNumberOfTeams() * 2;

        VertexData[] vertices = new VertexData[vertexCount + 1];

        float segmentLength = (float) (2 * Math.PI / game.getNumberOfTeams());

        float currentAngle = -0.5f * segmentAngle;

        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(0, -1, 0));
        vertices[0] = v0;

        int index = 1;
        for (int i = 0; i < game.getNumberOfTeams(); i++) {

            Vector3f tempPosition = goalSquares.get(firstGoalSquareIndex + i * game.getTeam(i).getPieces().size()).getPosition();
            float boardRadius = boardOuterPadding * (tempPosition.length() + 2.5f * (squareSideLength + goalSquarePadding));

            Vector3f startPoint = new Vector3f((float) (boardRadius * (Math.cos(currentAngle))), -1, (float) (boardRadius * (Math.sin(currentAngle))));

            Vector3f right = new Vector3f();
            Vector3f.cross(tempPosition, new Vector3f(0, 1, 0), right);
            right.normalise().scale((squareSideLength + goalSquarePadding) * game.getTeam(0).getPieces().size() / 1.5f);

            Vector3f leftPoint = new Vector3f();
            Vector3f rightPoint = new Vector3f();
            Vector3f.sub(startPoint, right, leftPoint);
            Vector3f.add(startPoint, right, rightPoint);

            VertexData leftVertex = new VertexData();
            leftVertex.setPosition(leftPoint);
            leftVertex.setColor(new Vector3f(0, 0, 0));
            VertexData rightVertex = new VertexData();
            rightVertex.setPosition(rightPoint);
            rightVertex.setColor(new Vector3f(0, 0, 0));

            vertices[index++] = leftVertex;
            vertices[index++] = rightVertex;

            currentAngle += segmentLength;
        }

        return vertices;
    }

    @Override
    public byte[] setupIndexData() {

        byte[] indices = new byte[vertexCount * 3];

        int index = 0;
        for (byte i = 1; i < vertexCount; i++) {
            indices[index++] = 0;
            indices[index++] = i;
            indices[index++] = (byte) (i + 1);
        }
        indices[index++] = 0;
        indices[index++] = (byte) vertexCount;
        indices[index++] = (byte) 1;

        return indices;
    }

    private float calcRadius(float squareSideLength, float squarePadding, Game game, float goalSquarePadding) {
        radius = (float) ((squareSideLength + squarePadding) * game.getBoard().getSquares().size() / (2 * Math.PI));
        float radiusOfGoalSquares = (game.getBoard().getGoalSquares(0).size() + 1) * (squareSideLength + goalSquarePadding) + dieCupRadius;
        radius = Math.max(radius, radiusOfGoalSquares);

        System.out.println("radius = " + radius);
        System.out.println("radiusOfGoalSquares = " + radiusOfGoalSquares);

        return radius;
    }

    private void generateBoard() {
        int numberOfSquares = game.getBoard().getSquares().size();

        this.segmentAngle = (float) (2 * Math.PI) / numberOfSquares;

        generateRegularSquares(radius, segmentAngle);
        generateGoalSquares(radius, segmentAngle);
        generateHomeSquares(radius, segmentAngle);
    }

    private void generateRegularSquares(float radius, float segmentAngle) {
        float currentAngle = 0;

        squares = new HashMap<>();
        for (Square s : game.getBoard().getSquares()) {

            Vector3f squarePosition = new Vector3f((float) (radius * Math.cos(currentAngle)), 0f, (float) (radius * Math.sin(currentAngle)));

            Vector3f squareColor = null;
            int squareID = s.getID();

            for (int teamID = 0; teamID < game.getTeams().size(); teamID++) {
                if (game.getBoard().getStartSquare(teamID).getID() == squareID) {
                    squareColor = new Vector3f(fade * teamColors.get(teamID).x, fade * teamColors.get(teamID).y, fade * teamColors.get(teamID).z);
                    break;
                }
            }
            if (squareColor == null) {
                squareColor = new Vector3f(0.5f, 0.5f, 0.5f);
            }

            SquareGraphic squareGraphic = new SquareGraphic(squarePosition, squareSideLength, squareColor);
            squareGraphic.rotate(0, -currentAngle, 0);
            squares.put(squareID, squareGraphic);

            currentAngle += segmentAngle;
        }
    }

    private void generateGoalSquares(float radius, float segmentAngle) {
        goalSquares = new HashMap<>();
        for (int teamID = 0; teamID < game.getTeams().size(); teamID++) {

            Square startSquare = game.getBoard().getStartSquare(teamID);
            float currentAngle = -squares.get(startSquare.getID()).getRotation().y;

            for (int i = 0; i < game.getBoard().getGoalSquares(teamID).size(); i++) {

                Square goalSquare = game.getBoard().getGoalSquares(teamID).get(i);

                float tempRadius = radius - ((i + 1) * (squareSideLength + goalSquarePadding));
                Vector3f goalPosition = new Vector3f((float) (tempRadius * Math.cos(currentAngle - 0.5f * segmentAngle)), 0, (float) (tempRadius * Math.sin(currentAngle - 0.5f * segmentAngle)));

                int squareID = goalSquare.getID();
                if (firstGoalSquareIndex == 0) {
                    firstGoalSquareIndex = squareID;
                }
                Vector3f color = new Vector3f(fade * teamColors.get(teamID).x, fade * teamColors.get(teamID).y, fade * teamColors.get(teamID).z);
                SquareGraphic squareGraphic = new SquareGraphic(goalPosition, squareSideLength, color);
                squareGraphic.rotate(0, -(currentAngle - 0.5f * segmentAngle), 0);
                goalSquares.put(squareID, squareGraphic);
            }
        }
    }

    private void generateHomeSquares(float radius, float segmentAngle) {
        homeSquares = new HashMap<>();
        for (int teamID = 0; teamID < game.getTeams().size(); teamID++) {

            Square startSquare = game.getBoard().getStartSquare(teamID);
            float currentAngle = -squares.get(startSquare.getID()).getRotation().y;

            float tempRadius = radius + squareSideLength + goalSquarePadding;
            Vector3f homePosition = new Vector3f((float) (tempRadius * Math.cos(currentAngle - 0.5f * segmentAngle)), 0, (float) (tempRadius * Math.sin(currentAngle - 0.5f * segmentAngle)));
            Vector3f right = new Vector3f();
            Vector3f.cross(new Vector3f(0, 1, 0), homePosition, right);

            right.normalise().scale((squareSideLength + goalSquarePadding) * game.getTeam(teamID).getPieces().size() / 2.0f - 0.5f * (squareSideLength + goalSquarePadding));
            homePosition.translate(-right.x, -right.y, -right.z);

            right.normalise().scale(squareSideLength + goalSquarePadding);

            for (int i = 0; i < game.getTeam(teamID).getPieces().size(); i++) {
                int squareID = game.getTeam(teamID).getPieces().size() * teamID + i;
                Vector3f color = new Vector3f(fade * teamColors.get(teamID).x, fade * teamColors.get(teamID).y, fade * teamColors.get(teamID).z);
                SquareGraphic squareGraphic = new SquareGraphic(homePosition, squareSideLength, color);
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
        return homeSquares.get(teamID * game.getTeam(teamID).getPieces().size() + pieceID);
    }

    public float getRadius() {
        return radius;
    }
}
