package al420445.service;

import al420445.airport.Airport;
import al420445.airport.Passenger;
import al420445.dao.AirportDao;
import al420445.dao.AirportDaoFactory;

import java.util.List;

public class AirportService {

    private final AirportDao airportDao;

    /**
     * Constructor accepting a specific DAO implementation
     */
    public AirportService(AirportDao airportDao) {
        this.airportDao = airportDao;
    }

    /**
     * Default constructor using the factory's default strategy
     */
    public AirportService() {
        this.airportDao = AirportDaoFactory.create();
    }

    /**
     * Constructor allowing choice of implementation strategy
     */
    public AirportService(AirportDaoFactory.Strategy strategy) {
        this.airportDao = AirportDaoFactory.create(strategy);
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
