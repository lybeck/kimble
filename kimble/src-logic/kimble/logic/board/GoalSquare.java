package kimble.logic.board;

class GoalSquare extends Square {

    public GoalSquare(int id) {
        super(id);
    }

    @Override
    public boolean isRegularSquare() {
        return false;
    }

    @Override
    public boolean isGoalSquare() {
        return true;
    }

}
