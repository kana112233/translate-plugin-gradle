package entity;

public enum TableAlign {
    CENTER("center"),
    LEFT("left"),
    RIGHT("right"),
    NULL("");
    private String value;
    TableAlign(String value){
        this.value = value;
    }
}
