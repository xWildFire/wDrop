package pl.wildfire.drop.compare;

public class IntEqualsCompare extends AbstractIntCompare {
    public IntEqualsCompare(final int value) {
        super(value);
    }

    @Override
    public boolean isInRange(final Integer i) {
        return i == this.value;
    }

    @Override
    public CompareType getCompareType() {
        return CompareType.EQUALS;
    }

    @Override
    public String getParse() {
        return "=" + this.value;
    }
}
