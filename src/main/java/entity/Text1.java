package entity;

public class Text1 {
    private final static String type = "text";
    private String text;

    public static String getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
