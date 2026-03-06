import java.util.*;

class ParkingSpot {
    String licensePlate;
    long entryTime;
    String status;

    ParkingSpot() {
        status = "EMPTY";
    }
}

public class ParkingLot {

    private ParkingSpot[] table;
    private int size = 500;
    private int occupied = 0;
    private int totalProbes = 0;

    public ParkingLot() {
        table = new ParkingSpot[size];
        for (int i = 0; i < size; i++) {
            table[i] = new ParkingSpot();
        }
    }

    private int hash(String plate) {
        return Math.abs(plate.hashCode()) % size;
    }

    public void parkVehicle(String plate) {

        int index = hash(plate);
        int probes = 0;

        while (!table[index].status.equals("EMPTY")) {
            index = (index + 1) % size;
            probes++;
        }

        table[index].licensePlate = plate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].status = "OCCUPIED";

        occupied++;
        totalProbes += probes;

        System.out.println("Assigned spot #" + index + " (" + probes + " probes)");
    }

    public void exitVehicle(String plate) {

        int index = hash(plate);

        while (!table[index].status.equals("EMPTY")) {

            if (table[index].status.equals("OCCUPIED") &&
                    plate.equals(table[index].licensePlate)) {

                long duration = System.currentTimeMillis() - table[index].entryTime;

                double hours = duration / (1000.0 * 60 * 60);
                double fee = hours * 5;

                table[index].status = "EMPTY";
                table[index].licensePlate = null;

                occupied--;

                System.out.println("Spot #" + index + " freed");
                System.out.println("Duration: " + String.format("%.2f", hours) + " hours");
                System.out.println("Fee: $" + String.format("%.2f", fee));

                return;
            }

            index = (index + 1) % size;
        }

        System.out.println("Vehicle not found");
    }

    public void getStatistics() {

        double occupancy = (occupied * 100.0) / size;
        double avgProbes = occupied == 0 ? 0 : (double) totalProbes / occupied;

        System.out.println("Occupancy: " + occupancy + "%");
        System.out.println("Avg Probes: " + avgProbes);
    }

    public static void main(String[] args) {

        ParkingLot lot = new ParkingLot();

        lot.parkVehicle("ABC-1234");
        lot.parkVehicle("ABC-1235");
        lot.parkVehicle("XYZ-9999");

        lot.exitVehicle("ABC-1234");

        lot.getStatistics();
    }
}