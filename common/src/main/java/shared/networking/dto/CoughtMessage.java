package shared.networking.dto;

public class CoughtMessage extends  BaseMessage {
    private int indexCheater;
    private int indexCought;
    private int scoreCheater;
    private int scoreCought;
    private boolean cheated;

    public CoughtMessage(){

    }
    public CoughtMessage(int indexCheater, int indexCought, int scoreCheater, int scoreCought, boolean cheated) {
        this.indexCheater = indexCheater;
        this.indexCought = indexCought;
        this.scoreCheater = scoreCheater;
        this.scoreCought = scoreCought;
        this.cheated = cheated;
    }

    public int getIndexCheater() {
        return indexCheater;
    }

    public void setIndexCheater(int indexCheater) {
        this.indexCheater = indexCheater;
    }

    public int getIndexCought() {
        return indexCought;
    }

    public void setIndexCought(int indexCought) {
        this.indexCought = indexCought;
    }

    public int getScoreCheater() {
        return scoreCheater;
    }

    public void setScoreCheater(int scoreCheater) {
        this.scoreCheater = scoreCheater;
    }

    public int getScoreCought() {
        return scoreCought;
    }

    public void setScoreCought(int scoreCought) {
        this.scoreCought = scoreCought;
    }

    public boolean isCheated() {
        return cheated;
    }

    public void setCheated(boolean cheated) {
        this.cheated = cheated;
    }
}