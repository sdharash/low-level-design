public class TicketController {
    private TicketService ticketService;

    TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    public GenerateTicketResponseDto generateTicket(GenerateTicketRequestDto generateTicketRequestDto) {
        String vehicleNumber = generateTicketRequestDto.getVehicleNumber();
        VehicleType vehicleType = generateTicketRequestDto.getVehicleType();
        Long gateId = generateTicketRequestDto.getGateId;

        Ticket ticket = ticketService.generateTicket(vehicleNumber,
                vehicleType, gateId);

        GenerateTicketResponseDto response = new GenerateTicketResponseDto();
        response.setTicketId(ticket.getId());
        response.setperatorName(ticket.getOperator().getName());
        response.setSpotNumber(ticket.getParkingSpot().getSpotNumber());

        return response;

    }
}
