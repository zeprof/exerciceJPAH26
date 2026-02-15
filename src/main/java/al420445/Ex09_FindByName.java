package al420445;

import al420445.airport.Passenger;
import al420445.dao.AirportDaoImpl;
import al420445.dao.JpaUtil;
import al420445.service.AirportService;

import java.util.List;

/**
 * Ex09: Demonstrates DAO search with LIKE query.
 */
public class Ex09_FindByName {
    public static void main(String[] args) {
        final AirportService airportService = new AirportService(new AirportDaoImpl());
        List<Passenger> results = airportService.findPassengersByName("Loki");
        System.out.println("Found: " + results);
        JpaUtil.close();
    }
}
