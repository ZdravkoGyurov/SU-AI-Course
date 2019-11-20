package minimax.tictactoe;

public class EvaluatedBoard {

    private Board state;
    private int evaluation;

    public EvaluatedBoard(final Board state, final int evaluation) {
        this.state = state;
        this.evaluation = evaluation;
    }

    public Board getState() {
        return state;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setState(final Board state) {
        this.state = state;
    }

    public void setEvaluation(final int evaluation) {
        this.evaluation = evaluation;
    }

}
