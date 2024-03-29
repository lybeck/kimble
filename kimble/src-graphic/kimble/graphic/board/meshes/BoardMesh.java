package kimble.graphic.board.meshes;

import java.util.List;
import java.util.Map;
import kimble.graphic.board.BoardSpecs;
import kimble.graphic.board.SquareGraphic;
import kimble.graphic.model.Mesh;
import kimble.graphic.model.VertexData;
import kimble.logic.Team;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class BoardMesh extends Mesh {

    private final List<Team> teams;
    private int vertexCount;
    private final float segmentAngle;

    private final Map<Integer, SquareGraphic> goalSquares;
    private final int firstGoalSquareIndex;

    private final float goalSquarePadding;
    private final float squareSideLength;
    private final float boardOuterPadding;

    public BoardMesh(List<Team> teams, int vertexCount, float segmentAngle, Map<Integer, SquareGraphic> goalSquares, int firstGoalSquareIndex, BoardSpecs specs) {
        this.teams = teams;
        this.vertexCount = vertexCount;
        this.segmentAngle = segmentAngle;

        this.goalSquares = goalSquares;
        this.firstGoalSquareIndex = firstGoalSquareIndex;

        this.goalSquarePadding = specs.goalSquarePadding;
        this.squareSideLength = specs.squareSideLength;
        this.boardOuterPadding = specs.boardOuterPadding;

        setup();
    }

    @Override
    public VertexData[] setupVertexData() {

        this.vertexCount = teams.size() * 2;

        VertexData[] vertices = new VertexData[vertexCount + 1];
        float segmentLength = (float) (2 * Math.PI / teams.size());

        float currentAngle = 0; //-0.5f * segmentAngle;

        VertexData v0 = new VertexData();
        v0.setPosition(new Vector3f(0, 0, 0));
        v0.setColor(new Vector3f(0.75f, 0.75f, 0.75f));
        vertices[0] = v0;

        int index = 1;
        for (int i = 0; i < teams.size(); i++) {

            Vector3f tempPosition = goalSquares.get(firstGoalSquareIndex + i * teams.get(i).getPieces().size()).getPosition();
            float boardRadius = boardOuterPadding * (tempPosition.length() + 2.5f * (squareSideLength
                    + goalSquarePadding));

            Vector3f startPoint = new Vector3f((float) (boardRadius * (Math.cos(currentAngle))), 0, (float) (boardRadius
                    * (Math.sin(currentAngle))));

            Vector3f right = new Vector3f();
            Vector3f.cross(tempPosition, new Vector3f(0, 1, 0), right);
            right.normalise().scale((squareSideLength + goalSquarePadding) * teams.get(0).getPieces().size() / 1.5f);

            Vector3f leftPoint = new Vector3f();
            Vector3f rightPoint = new Vector3f();
            Vector3f.sub(startPoint, right, leftPoint);
            Vector3f.add(startPoint, right, rightPoint);

            VertexData leftVertex = new VertexData();
            leftVertex.setPosition(leftPoint);
            leftVertex.setColor(new Vector3f(0.2f, 0.2f, 0.2f));
            VertexData rightVertex = new VertexData();
            rightVertex.setPosition(rightPoint);
            rightVertex.setColor(new Vector3f(0.2f, 0.2f, 0.2f));

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
