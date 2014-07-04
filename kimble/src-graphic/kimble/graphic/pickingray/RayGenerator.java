package kimble.graphic.pickingray;

import kimble.graphic.Screen;
import kimble.graphic.camera.Camera3D;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 *
 * @author Christoffer
 */
public class RayGenerator {

    public static Ray create(int mouseX, int mouseY, Camera3D camera) {

        float ndcX = ((float) mouseX / Screen.getWidth() - 0.5f) * 2.0f;
        float ndcY = ((float) mouseY / Screen.getHeight() - 0.5f) * 2.0f;

        Vector4f rayStartNDC = new Vector4f();
        rayStartNDC.x = ndcX;
        rayStartNDC.y = ndcY;
        rayStartNDC.z = -1.0f;
        rayStartNDC.w = 1.0f;

        Vector4f rayEndNDC = new Vector4f();
        rayEndNDC.x = ndcX;
        rayEndNDC.y = ndcY;
        rayEndNDC.z = 0.0f;
        rayEndNDC.w = 1.0f;

        //        // from NDC space to Camera space
        //        Matrix4f inverseProjectionMatrix = new Matrix4f();
        //        Matrix4f.invert(camera.getProjectionMatrix(), inverseProjectionMatrix);
        //
        //        // from Camera space to World space
        //        Matrix4f inverseViewMatrix = new Matrix4f();
        //        Matrix4f.invert(camera.getViewMatrix(), inverseViewMatrix);
        //
        //        Vector4f rayStartCamera = assignAndNormalizeWithW(inverseProjectionMatrix, rayStartNDC);
        //        Vector4f rayStartWorld = assignAndNormalizeWithW(inverseViewMatrix, rayStartCamera);
        //        Vector4f rayEndCamera = assignAndNormalizeWithW(inverseProjectionMatrix, rayEndNDC);
        //        Vector4f rayEndWorld = assignAndNormalizeWithW(inverseViewMatrix, rayEndCamera);
        //
        // faster way of doing the calculation above.
        Matrix4f inverseViewProjectionMatrix = new Matrix4f();
        Matrix4f.invert(Matrix4f.mul(camera.getProjectionMatrix(), camera.getViewMatrix(), null), inverseViewProjectionMatrix);

        Vector4f rayStartWorld = assignAndNormalizeWithW(inverseViewProjectionMatrix, rayStartNDC);
        Vector4f rayEndWorld = assignAndNormalizeWithW(inverseViewProjectionMatrix, rayEndNDC);

        Vector4f tempDirectionWorld = new Vector4f();
        Vector4f.sub(rayEndWorld, rayStartWorld, tempDirectionWorld);
        Vector3f rayDirectionWorld = new Vector3f(tempDirectionWorld);
        rayDirectionWorld.normalise();

        return new Ray(new Vector3f(rayStartWorld), rayDirectionWorld);
    }

    private static Vector4f assignAndNormalizeWithW(Matrix4f matrix, Vector4f vector) {
        Vector4f result = new Vector4f();
        Matrix4f.transform(matrix, vector, result);
        result.x /= result.w;
        result.y /= result.w;
        result.z /= result.w;
        result.w /= result.w;
        return result;
    }
}
