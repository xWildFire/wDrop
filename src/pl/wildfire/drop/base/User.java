package pl.wildfire.drop.base;

public class User {
    private final String name;
    private int lvl;
    private long points;
    private boolean cobble;

    public User(final String name, final int lvl, final long points) {
        this.cobble = true;
        this.name = name;
        this.lvl = lvl;
        this.points = points;
    }

    public long getToLvl() {
        if (this.lvl == 0) {
            return 50L;
        }
        return this.lvl * 2 * 50;
    }

    public boolean isCobble(final Drop d) {
        return !this.cobble;
    }

    public void changeCobble() {
        this.cobble = !this.cobble;
    }

    public String getName() {
        return this.name;
    }

    public int getLvl() {
        return this.lvl;
    }

    public void setLvl(final int lvl) {
        this.lvl = lvl;
    }

    public long getPoints() {
        return this.points;
    }

    public void setPoints(final long points) {
        this.points = points;
    }

    public boolean isCobble() {
        return this.cobble;
    }

    public void setCobble(final boolean cobble) {
        this.cobble = cobble;
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        final User other = (User) o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getLvl() == other.getLvl() && this.getPoints() == other.getPoints() && this.isCobble() == other.isCobble();
            }
        } else if (this$name.equals(other$name)) {
            return this.getLvl() == other.getLvl() && this.getPoints() == other.getPoints() && this.isCobble() == other.isCobble();
        }
        return false;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof User;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 0 : $name.hashCode());
        result = result * 59 + this.getLvl();
        final long $points = this.getPoints();
        result = result * 59 + (int) ($points >>> 32 ^ $points);
        result = result * 59 + (this.isCobble() ? 79 : 97);
        return result;
    }

    @Override
    public String toString() {
        return "User(name=" + this.getName() + ", lvl=" + this.getLvl() + ", points=" + this.getPoints() + ", cobble=" + this.isCobble() + ")";
    }
}
