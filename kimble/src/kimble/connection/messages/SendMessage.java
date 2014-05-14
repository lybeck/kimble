/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.messages;

import com.google.gson.Gson;

/**
 *
 * @author Christoffer
 */
public abstract class SendMessage {

    public String toJson() {
        return new Gson().toJson(new MessageWrapper(this));
    }

    protected Object getData() {
        return this;
    }

    protected abstract String getType();

}
