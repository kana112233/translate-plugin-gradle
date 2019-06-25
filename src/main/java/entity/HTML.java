package entity;

public class HTML {
    private final static String type = "html";
    private boolean pre;
    private String text;

    public static String getType() {
        return type;
    }

    public boolean isPre() {
        return pre;
    }

    public void setPre(boolean pre) {
        this.pre = pre;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
