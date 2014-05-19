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
public class SkipEntry extends AbstractEntry {

    public final boolean optional;

    public SkipEntry(int teamID, int dieRoll, boolean optional) {
        super(teamID, dieRoll);
        this.optional = optional;
    }
}
