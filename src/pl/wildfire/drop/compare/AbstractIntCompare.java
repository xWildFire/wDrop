package pl.wildfire.drop.compare;

public abstract class AbstractIntCompare implements Compare<Integer> {
    protected int value;

    public AbstractIntCompare(final int value) {
        this.value = value;
    }
}
