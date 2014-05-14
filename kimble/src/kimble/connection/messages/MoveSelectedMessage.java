/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.messages;

import kimble.connection.clientside.MoveInfo;

/**
 *
 * @author Christoffer
 */
public class MoveSelectedMessage extends SendMessage {

    int selectedMove;

    public MoveSelectedMessage(MoveInfo moveInfo) {
        selectedMove = moveInfo.moveId;
    }

    @Override
    protected String getType() {
        return "selectedMove";
    }

}
