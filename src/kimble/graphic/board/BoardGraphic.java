/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

import kimble.graphic.Model;
import kimble.graphic.shader.Shader;
import kimble.graphic.model.VertexData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kimble.graphic.model.Mesh;
import kimble.graphic.shader.Material;
import kimble.logic.Game;
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

    private final Game game;
    private Map<Integer, SquareGraphic> squares;
    private Map<Integer, SquareGraphic> goalSquares;
    private Map<Integer, SquareGraphic> homeSquares;

    private Material material;

    private float radius;
    private float squareSideLength;
    private float goalSquarePadding;
    private float dieCupRadius;

    private float fade = 0.6f;

    private float boardOuterPadding;
    private float segmentAngle;
    private int vertexCount;
    private int firstGoalSquareIndex;

    public BoardGraphic(Game game, float squareSideLength, float squarePadding, float goalSquarePadding, float boardOuterPadding, float dieCupRadius) {

        this.game = game;

        if (game.getTeams().size() > TEAM_COLORS.size()) {
            throw new UnsupportedOperationException("Can't have more teams than: " + TEAM_COLORS.size());
        }

        this.squareSideLength = squareSideLength;
        this.goalSquarePadding = goalSquarePadding;

        this.boardOuterPadding = boardOuterPadding;
        this.dieCupRadius = dieCupRadius;

        radius = calcRadius(squareSideLength, squarePadding, game, goalSquarePadding);

        generateBoard();

        this.material = new Material();
        this.material.setDiffuse(new Vector4f(REGULAR_SQUARE_COLOR.x, REGULAR_SQUARE_COLOR.y, REGULAR_SQUARE_COLOR.z, 1));

        this.setMesh(new BoardMesh(game, vertexCount, segmentAngle, goalSquares, firstGoalSquareIndex, goalSquarePadding, squareSideLength, boardOuterPadding));
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
                    squareColor = new Vector3f(fade * TEAM_COLORS.get(teamID).x, fade * TEAM_COLORS.get(teamID).y, fade * TEAM_COLORS.get(teamID).z);
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
                Vector3f color = new Vector3f(fade * TEAM_COLORS.get(teamID).x, fade * TEAM_COLORS.get(teamID).y, fade * TEAM_COLORS.get(teamID).z);
                SquareGraphic squareGraphic = new SquareGraphic(goalPosition, color);
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
                Vector3f color = new Vector3f(fade * TEAM_COLORS.get(teamID).x, fade * TEAM_COLORS.get(teamID).y, fade * TEAM_COLORS.get(teamID).z);
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
        super.render(shader, material);
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

    private static class BoardMesh extends Mesh {

        private final Game game;
        private int vertexCount;
        private final float segmentAngle;

        private final Map<Integer, SquareGraphic> goalSquares;
        private final int firstGoalSquareIndex;

        private final float goalSquarePadding;
        private final float squareSideLength;
        private final float boardOuterPadding;

        public BoardMesh(Game game, int vertexCount, float segmentAngle, Map<Integer, SquareGraphic> goalSquares, int firstGoalSquareIndex, float goalSquarePadding, float squareSideLength, float boardOuterPadding) {
            this.game = game;
            this.vertexCount = vertexCount;
            this.segmentAngle = segmentAngle;

            this.goalSquares = goalSquares;
            this.firstGoalSquareIndex = firstGoalSquareIndex;

            this.goalSquarePadding = goalSquarePadding;
            this.squareSideLength = squareSideLength;
            this.boardOuterPadding = boardOuterPadding;

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
            v0.setColor(new Vector3f(0.2f, 0.2f, 0.2f));
            vertices[0] = v0;

            int index = 1;
            for (int i = 0; i < game.getNumberOfTeams(); i++) {

                Vector3f tempPosition = goalSquares.get(firstGoalSquareIndex + i * game.getTeam(i).getPieces().size()).getPosition();
                float boardRadius = boardOuterPadding * (tempPosition.length() + 2.5f * (squareSideLength + goalSquarePadding));

                Vector3f startPoint = new Vector3f((float) (boardRadius * (Math.cos(currentAngle))), 0, (float) (boardRadius * (Math.sin(currentAngle))));

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
        public int[] setupIndexData() {

            int[] indices = new int[vertexCount * 3];

            int index = 0;
            for (byte i = 1; i < vertexCount; i++) {
                indices[index++] = 0;
                indices[index++] = i + 1;
                indices[index++] = i;
            }
            indices[index++] = 0;
            indices[index++] = 1;
            indices[index++] = vertexCount;

            return indices;
        }
    }
}
