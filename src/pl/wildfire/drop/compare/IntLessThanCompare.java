package pl.wildfire.drop.compare;

public class IntLessThanCompare extends AbstractIntCompare {
    public IntLessThanCompare(final int value) {
        super(value);
    }

    @Override
    public boolean isInRange(final Integer i) {
        return i < this.value;
    }

    @Override
    public CompareType getCompareType() {
        return CompareType.LESS_THAN;
    }

    @Override
    public String getParse() {
        return "<" + this.value;
    }
}
