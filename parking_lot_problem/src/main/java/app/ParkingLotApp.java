package app;

import parkingLot.ParkingLot;

import java.io.*;

public class ParkingLotApp {
    static BufferedReader bufferedReader;
    static String FilePath = "src/main/java/input/inputFile.txt";

    static ParkingLot parkingLot;

    public static void main(String[] args) {
        parseFile(FilePath);
    }

    private static void parseFile(String filePath) {
        File file = new File(filePath);
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String input;
            try {
                while ((input = bufferedReader.readLine()) != null) {
                    executeEachInstruction(input.trim());
                }
            } catch (IOException ex) {
                System.out.println("Error in reading the input file.");
                ex.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found in the path specified.");
            e.printStackTrace();
        }
    }

    private static void executeEachInstruction(String text) {
        String[] textData = text.split(" ");
        int textDataLength = textData.length;

        if (textDataLength < 1 || textDataLength > 3) {
            System.out.println("Invalid Input");
        }

        switch (textData[0]) {
            case "create_parking_lot":
                parkingLot = new ParkingLot(Integer.parseInt(textData[1]));
                break;
            case "park":
                parkingLot.park(textData[1], textData[2]);
                break;
            case "leave":
                parkingLot.exit(Integer.parseInt(textData[1]));
                break;
            case "status":
                parkingLot.status();
                break;
            case "registration_numbers_for_cars_with_colour":
                parkingLot.getRegistrationNumbersFromColor(textData[1]);
                break;
            case "slot_numbers_for_cars_with_colour":
                parkingLot.getSlotNumbersFromColor(textData[1]);
                break;
            case "slot_number_for_registration_number":
                parkingLot.getSlotNumberFromRegistrationNumber(textData[1]);
                break;
            default:
                System.out.println("Invalid input !!");
        }
    }
}
