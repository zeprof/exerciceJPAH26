package al420445.service;

import al420445.airport.Airport;
import al420445.airport.Passenger;
import al420445.dao.AirportDao;

import java.util.List;

public class AirportService {

    private final AirportDao airportDao;

    public AirportService(AirportDao airportDao) {
        this.airportDao = airportDao;
    }

    public List<Airport> getAirports() {
        return airportDao.getAirports();
    }

    // Note: @Transactional only works with a container (Spring/CDI).
    // Here, the DAO manages its own transactions.
    public void addPassenger(String name, int airportId) {
        airportDao.addPassenger(name, airportId);
    }

    public List<Passenger> findPassengersByName(String name) {
        return airportDao.findPassengersByName(name);
    }
}
