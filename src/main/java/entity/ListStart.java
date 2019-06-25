package entity;

public class ListStart {
    private final static String type = "list_start";
    private boolean ordered;

    public static String getType() {
        return type;
    }

    public boolean isOrdered() {
        return ordered;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }
}
