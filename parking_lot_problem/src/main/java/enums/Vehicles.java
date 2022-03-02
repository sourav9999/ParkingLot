package enums;

public enum Vehicles {
    CAR("CAR"),
    BIKE("BIKE"),
    BUS("BUS");

    private String vehicle;

    Vehicles(String vehicle){
        this.vehicle = vehicle;
    }

    public String getVehicle() {
        return vehicle;
    }
}
