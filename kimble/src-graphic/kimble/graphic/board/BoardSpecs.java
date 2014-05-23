/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kimble.graphic.board;

/**
 *
 * @author Christoffer
 */
public class BoardSpecs {

    public float squareSideLength;
    public float squarePadding;
    public float goalSquarePadding;
    public float boardOuterPadding;
    public float dieCupRadius;
    public float specialSquareColorFadeFactor;

    public BoardSpecs(float sideLength) {
        this(0.8f, 0.1f, 0.03f, 1.15f, 1.0f, 0.6f);
    }

    public BoardSpecs(float squareSideLength, float squarePadding, float goalSquarePadding, float boardOuterPadding, float dieCupRadius, float specialSquareColorFadeFactor) {
        this.squareSideLength = squareSideLength;
        this.squarePadding = squarePadding;
        this.goalSquarePadding = goalSquarePadding;
        this.boardOuterPadding = boardOuterPadding;
        this.dieCupRadius = dieCupRadius;
        this.specialSquareColorFadeFactor = specialSquareColorFadeFactor;
    }

}
