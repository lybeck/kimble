package kimble.graphic.pickingray;

import kimble.graphic.Screen;
import kimble.graphic.camera.Camera3D;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author Christoffer
 */
public class PickingRayUtil {

    private final Vector3f upVector = new Vector3f(0, 1, 0);
    private Vector3f cameraPosition;
    private Vector3f cameraLookDirection;

    private Vector3f screenHorizontally;
    private Vector3f screenVertically;

    public void update(Camera3D camera) {
        this.cameraPosition = camera.getPosition();

        // TODO: add method for getting camera lookAt
        Vector3f lookAt = new Vector3f(0, 0, 0);

        cameraLookDirection = new Vector3f();
        Vector3f.sub(lookAt, camera.getPosition(), cameraLookDirection);
        cameraLookDirection.normalise();

        screenHorizontally = new Vector3f();
        Vector3f.cross(cameraLookDirection, upVector, screenHorizontally);
        screenHorizontally.normalise();

        screenVertically = new Vector3f();
        Vector3f.cross(screenHorizontally, cameraLookDirection, screenVertically);
        screenVertically.normalise();

        double radians = Math.toRadians(camera.getFov());
        float halfHeight = (float) (Math.tan(radians / 2) * camera.getzNear());
        float halfAspectRatio = halfHeight * Screen.getAspectRatio();

        screenVertically.scale(halfHeight);
        screenHorizontally.scale(halfAspectRatio);
    }

    public void pick(int mouseX, int mouseY, PickingRay ray) {

        float screenX = mouseX;
        float screenY = mouseY;

        ray.setClickedWorldPosition(new Vector3f(cameraPosition));
        Vector3f.add(ray.getClickedWorldPosition(), cameraLookDirection, ray.getClickedWorldPosition());

        screenX -= Screen.getWidth() / 2f;
        screenY -= Screen.getHeight() / 2f;

        screenX /= Screen.getWidth() / 2f;
        screenY /= Screen.getHeight() / 2f;

        ray.getClickedWorldPosition().x += screenHorizontally.x * screenX + screenVertically.x * screenY;
        ray.getClickedWorldPosition().y += screenHorizontally.y * screenX + screenVertically.y * screenY;
        ray.getClickedWorldPosition().z += screenHorizontally.z * screenX + screenVertically.z * screenY;

        Vector3f rayDirection = new Vector3f();
        Vector3f.sub(ray.getClickedWorldPosition(), cameraPosition, rayDirection);
        ray.setDirection(rayDirection);
    }
}
