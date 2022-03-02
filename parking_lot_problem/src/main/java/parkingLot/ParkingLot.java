package parkingLot;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import vehicle.Vehicle;
import vehicle.commercial.Bus;
import vehicle.personal.Bike;
import vehicle.personal.Car;

import java.util.*;

public class ParkingLot {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParkingLot.class);

    public static int parkingLotSize;
    public List<Integer> availableParkingLots;
    public Map<Integer, Vehicle> slotToVehicleMap;
    public Map<String, Integer> registrationToSlotMap;
    public Map<String, ArrayList<String>> colorToRegistrationListMap;

    // initialize the parking lot
    public ParkingLot(int lotSize) {
        if (lotSize > 0) {
            parkingLotSize = lotSize;

            availableParkingLots = new ArrayList<Integer>();
            allocateLotNumber();

            slotToVehicleMap = new HashMap<Integer, Vehicle>();
            registrationToSlotMap = new HashMap<String, Integer>();
            colorToRegistrationListMap = new HashMap<String, ArrayList<String>>();
        } else
            throw new RuntimeException("Please enter valid lot size- " + lotSize);
    }

    /**
     * Park the vehicle and map the slot and update the parking lot status
     *
     * @param registrationNumber
     * @param color
     * @param vehicleParam
     */
    public void park(String registrationNumber, String color, String... vehicleParam) {
        int slotAllocation;
        Vehicle parkingVehicle;
        String vehicle = vehicleParam.length > 0 ? vehicleParam[0] : "car";

        if (getAvailableParkingLots() == null) {
            throw new RuntimeException("Parking lot is Full !!");
        }

        // Allocate nearest slot
        Collections.sort(availableParkingLots);
        slotAllocation = availableParkingLots.get(0);

        parkingVehicle = initializeVehicleInfo(registrationNumber, color, vehicle.toLowerCase());

        slotToVehicleMap.put(slotAllocation, parkingVehicle);
        registrationToSlotMap.put(registrationNumber, slotAllocation);

        if (!colorToRegistrationListMap.containsKey(color)) {
            ArrayList<String> registrationNumbers = new ArrayList<String>();
            registrationNumbers.add(registrationNumber);
            colorToRegistrationListMap.put(color, registrationNumbers);
        } else {
            ArrayList<String> registrationNumbers = colorToRegistrationListMap.get(color);
            registrationNumbers.add(registrationNumber);
            colorToRegistrationListMap.put(color, registrationNumbers);
        }

        availableParkingLots.remove(0);
        LOGGER.trace("Allocated slot number: " + slotAllocation);
    }

    /**
     * Vacant the parking at desired slot number
     *
     * @param slotNumber
     */
    public void exit(int slotNumber) {
        if (slotToVehicleMap.size() > 0) {
            Vehicle vehicleInfo = slotToVehicleMap.get(slotNumber);

            if (vehicleInfo != null) {
                registrationToSlotMap.remove(vehicleInfo.getRegistrationNumber());
                slotToVehicleMap.remove(slotNumber);

                ArrayList<String> registrationNumbers = colorToRegistrationListMap.get(vehicleInfo.getColor());

                if (registrationNumbers.contains(vehicleInfo.getRegistrationNumber())) {
                    registrationNumbers.remove(vehicleInfo.getRegistrationNumber());
                }

                availableParkingLots.add(slotNumber);
                LOGGER.trace("Parked Vehicle have left the slot:- " + slotNumber);
            } else {
                throw new RuntimeException("Slot is vacant. No Vehicle is parked at slot:- " + slotNumber);
            }
        } else
            throw new RuntimeException("Parking lot is empty. No Vehicle is parked at this instance");
    }

    /**
     * Get slot number of an parked vehicle from registration number
     *
     * @param registrationNumber
     * @return
     */
    public int getSlotNumberFromRegistrationNumber(String registrationNumber) {
        if (registrationToSlotMap.isEmpty()) {
            throw new RuntimeException("Slots are empty, No vehicle is parked with registration Number " + registrationNumber);
        } else if (!registrationToSlotMap.containsKey(registrationNumber)) {
            throw new RuntimeException("No vehicle is parked with the registration Number " + registrationNumber);
        } else
            return registrationToSlotMap.get(registrationNumber);
    }

    /**
     * Get List of Registration Number of an color
     *
     * @param color
     * @return
     */
    public List<String> getRegistrationNumbersFromColor(String color) {
        if (colorToRegistrationListMap.isEmpty()) {
            throw new RuntimeException("Slots are empty, No vehicle is parked with color " + color);
        } else if (!colorToRegistrationListMap.containsKey(color)) {
            throw new RuntimeException("No Vehicle is parked with the color:- " + color);
        } else
            return colorToRegistrationListMap.get(color);
    }

    // Allocate parking lot number
    private void allocateLotNumber() {
        int i = 1;
        while (i <= parkingLotSize) {
            availableParkingLots.add(i);
            LOGGER.trace("Allocated lot number " + i);
            i++;
        }
    }

    /**
     * Get Slots of all can of an defined color
     *
     * @param color
     * @return
     */
    public List getSlotNumbersFromColor(String color) {
        List<String> regNosOfAnColor = getRegistrationNumbersFromColor(color);
        List<Integer> slots = new ArrayList<Integer>();

        for (String regNo : regNosOfAnColor) {
            slots.add(registrationToSlotMap.get(regNo));
        }

        return slots;
    }

    private Vehicle initializeVehicleInfo(String registrationNumber, String color, String vehicle) {
        Vehicle instance = null;

        switch (vehicle) {
            case "car":
                instance = new Car(registrationNumber, color);
                break;
            case "bike":
                instance = new Bike(registrationNumber, color);
                break;
            case "bus":
                instance = new Bus(registrationNumber, color);
                break;
        }

        if (instance != null) {
            return instance;
        } else
            throw new RuntimeException("In appropiate vehicle passed " + vehicle);
    }

    public List<Integer> getAvailableParkingLots() {
        if (!availableParkingLots.isEmpty()) {
            return availableParkingLots;
        } else
            return null;
    }

    public Map<Integer, Vehicle> getParkedSlotVehicles() {
        if (!slotToVehicleMap.isEmpty()) {
            return slotToVehicleMap;
        } else
            return null;
    }

    public void status(){
        if(getParkedSlotVehicles() != null){
            for(Map.Entry parkedVehicle : slotToVehicleMap.entrySet()){
                System.out.println("At slot " + parkedVehicle.getKey() + " vehicle with regNo " + ((Vehicle)parkedVehicle.getValue()).getRegistrationNumber() + " is parked.");
            }
        }
    }
}
