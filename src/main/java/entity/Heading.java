package entity;

public class Heading {
    private final static String type = "heading";
    private int depth;
    private String text;

    public static String getType() {
        return type;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
