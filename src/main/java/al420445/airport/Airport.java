package al420445.airport;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "AIRPORTS")
public class Airport {

     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "ID")
     private Long id;

     @Column(name = "NAME")
     private String name;

     @OneToMany(mappedBy = "airport")
     private List<Passenger> passengers = new ArrayList<>();

     public Airport(String name) {
          this.name = name;
     }

     protected Airport() {
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

     public List<Passenger> getPassengers() {
          return Collections.unmodifiableList(passengers);
     }

     public void addPassenger(Passenger passenger) {
          passengers.add(passenger);
          passenger.setAirport(this);
     }

     @Override
     public boolean equals(Object o) {
          if (this == o) return true;
          if (o == null || getClass() != o.getClass()) return false;
          Airport airport = (Airport) o;
          return id != null && Objects.equals(id, airport.id);
     }

     @Override
     public int hashCode() {
          return getClass().hashCode();
     }

     @Override
     public String toString() {
          return "Airport{" +
                  "id=" + id +
                  ", name='" + name + '\'' +
                  '}';
     }
}
