/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package kimble.connection.messages;

/**
 *
 * @author Christoffer
 */
public class PingMessage extends SendMessage {

    @Override
    protected String getType() {
        return "ping";
    }
    
}
