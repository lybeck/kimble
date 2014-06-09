package kimble.graphic.pickingray;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class PickingRay {

    private Vector3f clickedWorldPosition;
    private Vector3f direction;

    public PickingRay() {
        clickedWorldPosition = new Vector3f();
        direction = new Vector3f();
    }

//    public void intersectsWithXyPlane(float[] worldPos) {
//        float s = -clickedWorldPosition.z / direction.z;
//        worldPos[0] = clickedWorldPosition.x + direction.x * s;
//        worldPos[1] = clickedWorldPosition.y + direction.y * s;
//        worldPos[2] = 0;
//    }
    public Vector3f getIntersectionPoint() {
        Vector3f point = new Vector3f();

        float s = -clickedWorldPosition.z / direction.z;
        point.x = clickedWorldPosition.x + direction.x * s;
        point.y = clickedWorldPosition.y + direction.y * s;
        point.z = 0; //clickedWorldPosition.z + direction.z * s;

        return point;
    }

    public void setClickedWorldPosition(Vector3f clickedWorldPosition) {
        this.clickedWorldPosition = clickedWorldPosition;
    }

    public Vector3f getClickedWorldPosition() {
        return clickedWorldPosition;
    }

    public void setDirection(Vector3f direction) {
        this.direction = direction;
    }

    public Vector3f getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return clickedWorldPosition + " -> " + direction;
    }
}
