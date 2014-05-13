/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.utils;

/**
 *
 * @author Christoffer
 */
public class MathHelper {
    
    /**
     * Returns a value between 'a' and 'b' with increment step 'dt'
     * 
     * @param a
     * @param b
     * @param dt
     * @return 
     */
    public static float lerp(float a, float b, float dt){
        return a + dt * (b - a);
    }
    
}
