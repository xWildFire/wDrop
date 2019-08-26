package pl.wildfire.drop.compare;

public class IntGreaterThanCompare extends AbstractIntCompare {
    public IntGreaterThanCompare(final int value) {
        super(value);
    }

    @Override
    public boolean isInRange(final Integer i) {
        return i > this.value;
    }

    @Override
    public CompareType getCompareType() {
        return CompareType.GREATER_THAN;
    }

    @Override
    public String getParse() {
        return ">" + this.value;
    }
}
