package pl.wildfire.drop.compare;

public class IntBetweenCompare implements Compare<Integer> {
    protected int min;
    protected int max;

    public IntBetweenCompare(final int min, final int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean isInRange(final Integer in) {
        final int i = in;
        return this.min < i && i < this.max;
    }

    @Override
    public CompareType getCompareType() {
        return CompareType.BETWEEN;
    }

    @Override
    public String getParse() {
        return this.min + "-" + this.max;
    }
}
