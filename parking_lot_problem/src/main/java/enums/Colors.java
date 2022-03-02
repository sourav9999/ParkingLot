package enums;

public enum Colors {
    RED("RED"),
    WHITE("WHITE"),
    BLACK("BLACK");

    private String color;

    Colors(String color){
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}
