package al420445.airport;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name="PASSENGERS")
public class Passenger {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private int id;

    @Column(name = "NAME", unique = true)
    private String name;

    @ManyToOne
    @JoinColumn
    private Airport airport;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL)
    private List<Ticket> tickets = new ArrayList<>();

    public Passenger(String name) {
        this.name = name;
    }

    public Passenger() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Airport getAirport() {
        return airport;
    }

    public void setAirport(Airport airport) {
        this.airport = airport;
    }

    public List<Ticket> getTickets() {
        return Collections.unmodifiableList(tickets);
    }

    public void addTicket(Ticket ticket) {
        ticket.setPassenger(this);
        tickets.add(ticket);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", airport=" + airport +
                '}';
    }
}
