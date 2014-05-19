/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger.entries;

/**
 *
 * @author Christoffer
 */
public abstract class AbstractEntry {

    public final int teamID;
    public final int dieRoll;

    public AbstractEntry(int teamID, int dieRoll) {
        this.teamID = teamID;
        this.dieRoll = dieRoll;
    }
}
