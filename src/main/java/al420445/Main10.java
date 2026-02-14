package al420445;

import al420445.dao.AirportDaoImpl;
import al420445.service.AirportService;

public class Main10 {
    public static void main(String[] args) {
        final AirportService airportService = new AirportService(new AirportDaoImpl());
        airportService.findPassengersByName("Loki");
    }
}
