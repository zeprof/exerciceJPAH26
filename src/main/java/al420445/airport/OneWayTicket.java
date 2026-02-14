package al420445.airport;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("O")
public class OneWayTicket extends Ticket {
    private LocalDate latestDepartureDate;

    public OneWayTicket(LocalDate latestDepartureDate) {
        this.latestDepartureDate = latestDepartureDate;
    }

    public OneWayTicket() {
    }

    public LocalDate getLatestDepartureDate() {
        return latestDepartureDate;
    }

    public void setLatestDepartureDate(LocalDate latestDepartureDate) {
        this.latestDepartureDate = latestDepartureDate;
    }

}
