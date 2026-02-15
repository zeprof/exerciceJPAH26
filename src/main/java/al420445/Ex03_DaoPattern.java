package al420445;

import al420445.dao.AirportDao;
import al420445.dao.AirportDaoImpl;
import al420445.service.AirportService;

/**
 * Ex03: Demonstrates the DAO + Service layer pattern.
 * Separates persistence logic from business logic.
 */
public class Ex03_DaoPattern {
    public static void main(String[] args) {
        AirportDao dao = new AirportDaoImpl();
        System.out.println(new AirportService(dao).getAirports());
    }
}
