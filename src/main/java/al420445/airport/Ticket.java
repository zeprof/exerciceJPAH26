package al420445.airport;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "discriminator")
public abstract class Ticket {

    @Id
    @GeneratedValue
    private int id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "PASSENGER_ID")
    private Passenger passenger;

    public Ticket(int id, String number) {
        this.id = id;
        this.number = number;
    }

    public Ticket() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Passenger getPassenger() {
        return passenger;
    }

    public void setPassenger(Passenger passenger) {
        this.passenger = passenger;
    }
}
