/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graphic;

import logic.board.Square;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex3f;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class SquareGraphic {

    private Square square;
    private float squareWidth;
    private Vector3f position;

    public SquareGraphic(Square square, float squareWidth, Vector3f position) {
        this.square = square;
        this.squareWidth = squareWidth;
        this.position = position;
    }

    public void render() {
        glBegin(GL_TRIANGLES);
        {
            glColor3f(1, 0, 0);
            glVertex3f(position.x - squareWidth / 2, position.y, position.z - squareWidth / 2);
            glVertex3f(position.x + squareWidth / 2, position.y, position.z - squareWidth / 2);
            glVertex3f(position.x + squareWidth / 2, position.y, position.z + squareWidth / 2);

            glVertex3f(position.x - squareWidth / 2, position.y, position.z - squareWidth / 2);
            glVertex3f(position.x + squareWidth / 2, position.y, position.z + squareWidth / 2);
            glVertex3f(position.x - squareWidth / 2, position.y, position.z + squareWidth / 2);
        }
        glEnd();
    }

    public Square getSquare() {
        return square;
    }

}
