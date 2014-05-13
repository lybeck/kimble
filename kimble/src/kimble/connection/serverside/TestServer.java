/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.connection.serverside;

/**
 *
 * @author Christoffer
 */
public class TestServer {
    
    public static void main(String[] args){
        new KimbleServer(5391, 4).setVisible(true);
    }
    
}
