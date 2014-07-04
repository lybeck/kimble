package kimble.graphic.pickingray;

import kimble.graphic.Model;
import org.lwjgl.util.vector.Vector3f;

public class Ray {

    private final Vector3f startPosition;
    private final Vector3f direction;

    private float intersectionDistance;

    private float tMin;
    private float tMax;

    public Ray(Vector3f startPosition, Vector3f direction) {
        this.startPosition = startPosition;
        this.direction = direction;
    }

    public Vector3f getIntersectPointAtHeight(float y) {
        Vector3f result = new Vector3f();
        Vector3f.add(startPosition, direction, result);
        while (result.y > y) {
            Vector3f.add(result, direction, result);
        }
        result.y = y;
        return result;
    }

    public boolean intersects(Model model) {

        tMin = 0.0f;
        tMax = 100000.0f;

        Vector3f obbPositionWorld = new Vector3f(model.getModelMatrix().m30, model.getModelMatrix().m31, model.getModelMatrix().m32);
        Vector3f delta = new Vector3f();
        Vector3f.sub(obbPositionWorld, startPosition, delta);

        if (checkAxisX(model, delta)) {
            return false;
        }
        if (checkAxisY(model, delta)) {
            return false;
        }
        if (checkAxisZ(model, delta)) {
            return false;
        }

        intersectionDistance = tMin;
        return true;
    }

    private boolean checkAxisX(Model model, Vector3f delta) {
        Vector3f xAxis = new Vector3f(model.getModelMatrix().m00, model.getModelMatrix().m01, model.getModelMatrix().m02);
        return checkAxis(model.getAabbMin().x, model.getAabbMax().x, delta, xAxis);
    }

    private boolean checkAxisY(Model model, Vector3f delta) {
        Vector3f yAxis = new Vector3f(model.getModelMatrix().m10, model.getModelMatrix().m11, model.getModelMatrix().m12);
        return checkAxis(model.getAabbMin().y, model.getAabbMax().y, delta, yAxis);
    }

    private boolean checkAxisZ(Model model, Vector3f delta) {
        Vector3f zAxis = new Vector3f(model.getModelMatrix().m20, model.getModelMatrix().m21, model.getModelMatrix().m22);
        return checkAxis(model.getAabbMin().z, model.getAabbMax().z, delta, zAxis);
    }

    private boolean checkAxis(float aabbMin, float aabbMax, Vector3f delta, Vector3f axis) {
        float e = Vector3f.dot(axis, delta);
        float f = Vector3f.dot(direction, axis);
        if (Math.abs(f) > 0.001f) {
            float t1 = (e + aabbMin) / f;
            float t2 = (e + aabbMax) / f;
            if (t1 > t2) {
                float w = t1;
                t1 = t2;
                t2 = w;
            }
            if (t2 < tMax) {
                tMax = t2;
            }
            if (t1 > tMin) {
                tMin = t1;
            }
            if (tMax < tMin) {
                return true;
            }
        } else {
            if (-e + aabbMin > 0.0f || -e + aabbMax < 0.0f) {
                return true;
            }
        }
        return false;
    }

    public float getIntersectionDistance() {
        return intersectionDistance;
    }

    public Vector3f getStartPosition() {
        return startPosition;
    }

    public Vector3f getDirection() {
        return direction;
    }

}
