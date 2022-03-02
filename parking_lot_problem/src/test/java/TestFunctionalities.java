import enums.Colors;
import enums.Vehicles;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import parkingLot.ParkingLot;
import vehicle.Vehicle;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class TestFunctionalities {
    // Test Data
    private final VehicleData[] vehicleDatas = {
            new VehicleData("qwerty", Colors.RED.getColor(), Vehicles.CAR.getVehicle()),
            new VehicleData("popping", Colors.WHITE.getColor(), Vehicles.BIKE.getVehicle()),
            new VehicleData("flicker", Colors.BLACK.getColor(), Vehicles.BUS.getVehicle())
    };

    ParkingLot parkingLot;
    int lotGenerator;

    @BeforeTest(alwaysRun = true)
    void setup() {
//        lotGenerator = new Random().nextInt(10);
//        parkingLot = new ParkingLot(lotGenerator);
//        System.out.println("Parking lot size allocated:- " + lotGenerator);
    }


    @Test(enabled = false)
    void createInvalidLot() {
        try {
            parkingLot = new ParkingLot(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void createLot() {
        parkingLot = new ParkingLot(17);
        // Assert that the lot is assigned
        assertThat("Unexpected Parking lot size", parkingLot.getAvailableParkingLots().size(), is(17));
    }

    @Test
    void parkVehicles() {
        parkingLot = new ParkingLot(8);

        for (VehicleData vehicleData : vehicleDatas) {
            parkingLot.park(vehicleData.regNo, vehicleData.color, vehicleData.vehicle);
        }

        // Display the vehicle alloted to the parking slot
        for (Map.Entry allotedVehicle : parkingLot.slotToVehicleMap.entrySet()) {
            Vehicle parkedVehicle = ((Vehicle) allotedVehicle.getValue());

            System.out.println("Vehicle Registration Number:- " + parkedVehicle.getRegistrationNumber() + " with color : " + parkedVehicle.getColor() + " is parked at slot:- " + allotedVehicle.getKey());
        }

        assertThat("Unexpected Available parking lot size", parkingLot.getAvailableParkingLots().size(), is(8 - vehicleDatas.length));
    }

    @Test
    void exitParking() {
        parkingLot = new ParkingLot(7);

        // Try to exit when no vehicle is parked, parking lot is empty
        try {
            parkingLot.exit(3);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        // Park vehicle
        for (VehicleData vehicleData : vehicleDatas) {
            parkingLot.park(vehicleData.regNo, vehicleData.color, vehicleData.vehicle);
        }

        // Try to exit on slot where no vehicle is parked, parking lot is filled in different slots
        if (lotGenerator >= 6) {
            try {
                parkingLot.exit(lotGenerator - 2);
            } catch (RuntimeException ex) {
                System.out.println(ex.getMessage());
            }
        }

        // Try to exit on slot where vehicle is parked
        int availableSlots = parkingLot.getAvailableParkingLots().size();
        int parkedSlots = parkingLot.getParkedSlotVehicles().size();

        System.out.println("Available slots number are: " + availableSlots);
        System.out.println("Parked slots number are: " + parkedSlots);
        // Exit from slot 3(Vehicle parked at slot:1 - 2 - 3
        parkingLot.exit(3);

        assertThat("Unexpected Available slots size", parkingLot.getAvailableParkingLots().size(), is(availableSlots + 1));
        assertThat("Unexpected Parked slots size", parkingLot.getParkedSlotVehicles().size(), is(parkedSlots - 1));
    }

    /**
     * Services
     */
    @Test
    void getSlotOfParkedVehicleRegNo() {
        parkingLot = new ParkingLot(12);

        // No vehicle is parked and request initiated to get Slot
        try {
            parkingLot.getSlotNumberFromRegistrationNumber("qwerty");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        // Park vehicle
        for (VehicleData vehicleData : vehicleDatas) {
            parkingLot.park(vehicleData.regNo, vehicleData.color, vehicleData.vehicle);
        }

        // Request initiated to fetch wrong registration Number
        try {
            parkingLot.getSlotNumberFromRegistrationNumber("12345");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        int slotFetched = parkingLot.getSlotNumberFromRegistrationNumber(vehicleDatas[2].regNo);

        System.out.println("Slot Fetched is : " + slotFetched + " for vehicle " + vehicleDatas[2].regNo);
        // Assert Slot Number
        assertThat("Unexpected slot number fetched " + slotFetched + " for vehicle " + vehicleDatas[2].regNo, vehicleDatas[2].regNo, is(parkingLot.slotToVehicleMap.get(slotFetched).getRegistrationNumber()));
    }

    @Test
    void getRegistrationNumbersFromColor() {
        parkingLot = new ParkingLot(11);

        // No vehicle is parked and request is initiated to get registration Numbers
        try {
            parkingLot.getRegistrationNumbersFromColor("qwerty");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        // Park vehicle
        for (VehicleData vehicleData : vehicleDatas) {
            parkingLot.park(vehicleData.regNo, vehicleData.color, vehicleData.vehicle);
        }

        // Request initiated to fetch wrong color
        try {
            parkingLot.getRegistrationNumbersFromColor("Parrot Green");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        List<String> fetchedRegNoFromColor = parkingLot.getRegistrationNumbersFromColor(Colors.RED.getColor());
        System.out.println("Fetched regNo's from Color :- " + fetchedRegNoFromColor);
    }

    @Test
    void getSlotsFromColor() {
        parkingLot = new ParkingLot(14);

        // No vehicle is parked and request is initiated to get registration Numbers
        try {
            parkingLot.getSlotNumbersFromColor(Colors.BLACK.getColor());
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        // Park vehicle
        for (VehicleData vehicleData : vehicleDatas) {
            parkingLot.park(vehicleData.regNo, vehicleData.color, vehicleData.vehicle);
        }

        // Request initiated to fetch wrong color
        try {
            parkingLot.getSlotNumbersFromColor("Parrot Green");
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }

        List<Integer> fetchedSlotNumbers = parkingLot.getSlotNumbersFromColor(Colors.BLACK.getColor());

        System.out.println("Fetched slot's from Color :- " + fetchedSlotNumbers);
    }

    private class VehicleData {
        String regNo;
        String color;
        String vehicle;

        VehicleData(String regNum, String Color, String Vehicle) {
            this.regNo = regNum;
            this.color = Color;
            this.vehicle = Vehicle;
        }
    }
}
