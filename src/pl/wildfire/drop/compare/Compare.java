package pl.wildfire.drop.compare;

public interface Compare<T> {
    boolean isInRange(T p0);

    CompareType getCompareType();

    String getParse();
}
