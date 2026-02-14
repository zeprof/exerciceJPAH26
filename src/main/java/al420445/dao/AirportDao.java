package al420445.dao;

import al420445.airport.Airport;
import al420445.airport.Passenger;

import java.util.List;

public interface AirportDao {
    List<Airport> getAirports();
    void addPassenger(String name, int airportId);

    List<Passenger> findPassengersByName(String passengersByName);
}
