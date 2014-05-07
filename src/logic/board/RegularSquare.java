package logic.board;

class RegularSquare extends Square {

    @Override
    public boolean isRegularSquare() {
        return true;
    }

    @Override
    public boolean isGoalSquare() {
        return false;
    }

}
