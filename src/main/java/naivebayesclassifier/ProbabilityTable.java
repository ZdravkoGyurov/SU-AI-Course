package naivebayesclassifier;

public class ProbabilityTable {

    private int trueWhenRepublicans;
    private int trueWhenDemocrats;
    private int falseWhenRepublicans;
    private int falseWhenDemocrats;
    private int totalRepublicans;
    private int totalDemocrats;

    public ProbabilityTable() {
        trueWhenRepublicans = 0;
        trueWhenDemocrats = 0;
        falseWhenRepublicans = 0;
        falseWhenDemocrats = 0;
        totalRepublicans = 0;
        totalDemocrats = 0;
    }

    public void updateTable(final int republicansWhenTrue, final int democratsWhenTrue, final int republicansWhenFalse,
            final int democratsWhenFalse, final int totalRepublicans, final int totalDemocrats) {
        trueWhenRepublicans = republicansWhenTrue;
        trueWhenDemocrats = democratsWhenTrue;
        falseWhenRepublicans = republicansWhenFalse;
        falseWhenDemocrats = democratsWhenFalse;
        this.totalRepublicans = totalRepublicans;
        this.totalDemocrats = totalDemocrats;
    }

    public double getWhenTrue() {
        return (double) (trueWhenRepublicans + trueWhenDemocrats) / (totalRepublicans + totalDemocrats);
    }

    public double getWhenFalse() {
        return (double) (falseWhenRepublicans + falseWhenDemocrats) / (totalRepublicans + totalDemocrats);
    }

    public int getTrueWhenRepublicans() {
        return trueWhenRepublicans;
    }

    public int getTrueWhenDemocrats() {
        return trueWhenDemocrats;
    }

    public int getFalseWhenRepublicans() {
        return falseWhenRepublicans;
    }

    public int getFalseWhenDemocrats() {
        return falseWhenDemocrats;
    }

    public int getTotalRepublicans() {
        return totalRepublicans;
    }

    public int getTotalDemocrats() {
        return totalDemocrats;
    }

    public double probabilityTrueWhenRepublicans() {
        return (double) trueWhenRepublicans / (double) totalRepublicans;
    }

    public double probabilityTrueWhenDemocrats() {
        return (double) trueWhenDemocrats / (double) totalDemocrats;
    }

    public double probabilityFalseWhenRepublicans() {
        return (double) falseWhenRepublicans / (double) totalRepublicans;
    }

    public double probabilityFalseWhenDemocrats() {
        return (double) falseWhenDemocrats / (double) totalDemocrats;
    }

    public void setTrueWhenRepublicans(final int trueWhenRepublicans) {
        this.trueWhenRepublicans = trueWhenRepublicans;
    }

    public void setTrueWhenDemocrats(final int trueWhenDemocrats) {
        this.trueWhenDemocrats = trueWhenDemocrats;
    }

    public void setFalseWhenRepublicans(final int falseWhenRepublicans) {
        this.falseWhenRepublicans = falseWhenRepublicans;
    }

    public void setFalseWhenDemocrats(final int falseWhenDemocrats) {
        this.falseWhenDemocrats = falseWhenDemocrats;
    }

    public void setTotalRepublicans(final int totalRepublicans) {
        this.totalRepublicans = totalRepublicans;
    }

    public void setTotalDemocrats(final int totalDemocrats) {
        this.totalDemocrats = totalDemocrats;
    }

}
