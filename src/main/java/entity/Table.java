package entity;

public class Table {
    private final static String type = "table";
    private String[] header;
    private TableAlign align;
    private String[][] cells;

    public static String getType() {
        return type;
    }

    public String[] getHeader() {
        return header;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public TableAlign getAlign() {
        return align;
    }

    public void setAlign(TableAlign align) {
        this.align = align;
    }

    public String[][] getCells() {
        return cells;
    }

    public void setCells(String[][] cells) {
        this.cells = cells;
    }
}
