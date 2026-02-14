package al420445.airport;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
@DiscriminatorValue("R")
public class ReturnTicket extends Ticket {
    private LocalDate latestReturnDate;

    public ReturnTicket(LocalDate latestReturnDate) {
        this.latestReturnDate = latestReturnDate;
    }
    public ReturnTicket() {
    }


    public LocalDate getLatestReturnDate() {
        return latestReturnDate;
    }

    public void setLatestReturnDate(LocalDate latestReturnDate) {
        this.latestReturnDate = latestReturnDate;
    }

}
