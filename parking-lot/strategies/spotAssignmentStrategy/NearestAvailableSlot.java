public class NearestAvailableSlot implements SpotAssignmentStrategy {

    public Optional<ParkingSpot> findSpot(VehicleType vehicleType, ParkingLot parkingLot, Gate entryGate) {

        for (ParkingFloor floor: parkingLot.getParkingFloor()) {
            for (ParkingSpot parkingSpot: floor.getSpots()) {
                if (parkingSpot.getStatus() == "AVAILABLE" &&
                        parkingSpot.getSupportedVehicleType().contains(vehicleType)) {
                    return parkingSpot;
                }
            }
        }
        return Optional.empty();

    }
}
