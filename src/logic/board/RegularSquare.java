package logic.board;

class RegularSquare extends Square {

    public RegularSquare(int id) {
        super(id);
    }

    @Override
    public boolean isRegularSquare() {
        return true;
    }

    @Override
    public boolean isGoalSquare() {
        return false;
    }

}
