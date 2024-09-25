public class TicketService {

    private SpotAssignmentStrategy spotAssignmentStrategy;
    private GateRepsitory gateRepsitory;
    private VehicleRepsitory vehicleRepsitory;
    private TicketRepository ticketRepository;
    private ParkingLotRepository parkingLotRepository;

    TicketService(GateRepository gateRepository, VehicleRepsitory vehicleRepsitory,
                  SpotAssignmentStrategy spotAssignmentStrategy,
                  TicketRepository ticketRepository,
                  ParkingLotRepository parkingLotRepository) {
        this.gateRepsitory = gateRepository;
        this.vehicleRepsitory = vehicleRepsitory;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.ticketRepository = ticketRepository;
        this.parkingLotRepository = parkingLotRepository;
    }


    public Ticket generateTicket(String vehicleNumber,
                                 VehicleType vehicleType,
                                 Long gateId) {

        // get Gate for that id from db
        // if Gate does not exist in db we have invalid request we can raise an exception
        Optional<Gate> gateOptional = this.gateRepsitory.findById(gateId);

        if (gateOptional.isEmpty()) {
            throw new Exception("Invalid gate exception.");
        }

        Gate gate = getOptinal.get();

        // get the current operator details, we can get that from Gate
        Operator operator = gate.getOperator();

        // create vehicle object- check if already exists in db else create Vehicle
        Optional<Vehicle> vehicleOptional = this.vehicleRepsitory.findVehicleByVehicleNumber(vehicleNumber);
        Vehicle vehicle = new Vehicle();
        if (vehicleOptional.isEmpty()) {
            vehicle.setVehicleNumber(vehicleNumber);
            vehicle.setVehicleType(vehicleType);
            vehicle = this.vehicleRepsitory.save(vehicle);
        } else {
            vehicle = vehicleOptional.get();
        }

        ParkingLot parkingLot = this.parkingLotRepository.getParkingLotOfGate(gate);
        // get nearest available slot
        Optional<ParkingSpot> parkingSpotOptional = this.spotAssignmentStrategy.findSpot(
                vehicleType, parkingLot, gate
        );

        if (parkingSpotOptional.isEmpty()) {
            throw new Exception("No parking spot available");
        }
        ParkingSpot parkingSpot = parkingSpotOptional.get();
        // create ticket object and return
        Ticket ticket = new Ticket();
        ticket.setParkingSpot(parkingSpot);
        ticket.setEntryTime(new Date());
        ticket.setVehicle(vehicle);
        ticket.setGate(gate);
        ticket.setOperator(operator);

        return this.ticketRepository.save(ticket);
    }
}
