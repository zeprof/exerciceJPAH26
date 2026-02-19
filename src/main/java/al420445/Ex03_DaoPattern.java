package al420445;

import al420445.dao.base.AirportDao;
import al420445.dao.base.AirportDaoImpl;
import al420445.service.AirportService;

import java.sql.SQLException;

/**
 * Ex03: Demonstrates the DAO + Service layer pattern.
 * Separates persistence logic from business logic.
 */
public class Ex03_DaoPattern {
    public static void main(String[] args) throws SQLException, InterruptedException {
        TcpServer.createTcpServer();
        Ex01_PersistEntities.insertDataInDb();

        AirportDao dao = new AirportDaoImpl();
        System.out.println("Airports: " + new AirportService(dao).getAirports());

        Thread.currentThread().join();
    }
}
