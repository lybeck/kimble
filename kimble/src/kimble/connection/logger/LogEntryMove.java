/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.connection.logger;

/**
 *
 * @author Christoffer
 */
public class LogEntryMove extends LogEntry {

    private final Integer pieceID;
    private final Boolean isHome;
    private final Boolean isOptional;
    private final Integer startSquareID;
    private final Integer destSquareID;

    public LogEntryMove(Integer teamID, Integer dieRoll, Integer pieceID, Boolean isHome, Boolean isOptional, Integer startSquareID, Integer destSquareID) {
        super(teamID, dieRoll);

        this.pieceID = pieceID;
        this.isHome = isHome;
        this.isOptional = isOptional;
        this.startSquareID = startSquareID;
        this.destSquareID = destSquareID;
    }

    public Integer getPieceID() {
        return pieceID;
    }

    public Boolean isIsHome() {
        return isHome;
    }

    public Boolean isIsOptional() {
        return isOptional;
    }

    public Integer getStartSquareID() {
        return startSquareID;
    }

    public Integer getDestSquareID() {
        return destSquareID;
    }

}
