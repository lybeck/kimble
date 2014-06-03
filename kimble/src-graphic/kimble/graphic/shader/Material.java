package kimble.graphic.shader;

/**
 *
 * @author Christoffer
 */
public interface Material {


    public void fetchUniformLocations(int shaderProgramID);
    
    public void uploadUniforms();

}
