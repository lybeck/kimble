package logic.board;

class GoalSquare extends Square {

    @Override
    public boolean isRegularSquare() {
        return false;
    }

    @Override
    public boolean isGoalSquare() {
        return true;
    }

}
