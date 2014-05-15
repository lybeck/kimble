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

    private Integer pieceID;
    private Boolean isHome;
    private Boolean isOptional;
    private Integer startSquareID;
    private Integer destSquareID;

    public LogEntryMove(Integer teamID, Integer dieRoll) {
        super(teamID, dieRoll);
    }

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

    public void setPieceID(Integer pieceID) {
        this.pieceID = pieceID;
    }

    public Boolean isIsHome() {
        return isHome;
    }

    public void setIsHome(Boolean isHome) {
        this.isHome = isHome;
    }

    public Boolean isIsOptional() {
        return isOptional;
    }

    public void setIsOptional(Boolean isOptional) {
        this.isOptional = isOptional;
    }

    public Integer getStartSquareID() {
        return startSquareID;
    }

    public void setStartSquareID(Integer startSquareID) {
        this.startSquareID = startSquareID;
    }

    public Integer getDestSquareID() {
        return destSquareID;
    }

    public void setDestSquareID(Integer destSquareID) {
        this.destSquareID = destSquareID;
    }
}
