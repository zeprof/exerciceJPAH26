# Side-by-Side Code Comparison

This document shows identical functionality implemented with both approaches.

---

## Example 1: Simple Void Operation

### Template Method Pattern
```java
public void addPassenger(String name, int airportId) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            Airport airport = em.find(Airport.class, (long) airportId);
            if (airport == null) return null;

            Passenger newPassenger = new Passenger(name);
            airport.addPassenger(newPassenger);
            em.persist(newPassenger);
            return null;
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public void addPassenger(String name, int airportId) {
    TransactionExecutor.executeInTransaction(em -> {
        Airport airport = em.find(Airport.class, (long) airportId);
        if (airport == null) return null;

        Passenger newPassenger = new Passenger(name);
        airport.addPassenger(newPassenger);
        em.persist(newPassenger);
        return null;
    });
}
```

**Key Difference:**
- Template: `new TransactionalDao<Void>() { @Override protected Void executeInTransaction(EntityManager em) { ... } }.executeInTransaction();`
- Functional: `TransactionExecutor.executeInTransaction(em -> { ... });`

---

## Example 2: Returning an Entity

### Template Method Pattern
```java
public Airport createAirport(String name) {
    return new TransactionalDao<Airport>() {
        @Override
        protected Airport executeInTransaction(EntityManager em) {
            Airport airport = new Airport(name);
            em.persist(airport);
            return airport;
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public Airport createAirport(String name) {
    return TransactionExecutor.executeInTransaction(em -> {
        Airport airport = new Airport(name);
        em.persist(airport);
        return airport;
    });
}
```

**Key Difference:**
- Template: More verbose, explicitly declares return type in class definition
- Functional: More concise, return type inferred from lambda

---

## Example 3: Updating an Entity

### Template Method Pattern
```java
public void updateAirportName(Long airportId, String newName) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            Airport airport = em.find(Airport.class, airportId);
            if (airport != null) {
                airport.setName(newName);
            }
            return null;
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public void updateAirportName(Long airportId, String newName) {
    TransactionExecutor.executeInTransaction(em -> {
        Airport airport = em.find(Airport.class, airportId);
        if (airport != null) {
            airport.setName(newName);
        }
        return null;
    });
}
```

---

## Example 4: Returning a Primitive

### Template Method Pattern
```java
public Long countPassengers(Long airportId) {
    return new TransactionalDao<Long>() {
        @Override
        protected Long executeInTransaction(EntityManager em) {
            return em.createQuery(
                "select count(p) from Passenger p where p.airport.id = :airportId", 
                Long.class)
                .setParameter("airportId", airportId)
                .getSingleResult();
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public Long countPassengers(Long airportId) {
    return TransactionExecutor.executeInTransaction(em -> {
        return em.createQuery(
            "select count(p) from Passenger p where p.airport.id = :airportId", 
            Long.class)
            .setParameter("airportId", airportId)
            .getSingleResult();
    });
}
```

**Note:** Can be simplified further in functional style:
```java
public Long countPassengers(Long airportId) {
    return TransactionExecutor.executeInTransaction(em -> 
        em.createQuery(
            "select count(p) from Passenger p where p.airport.id = :airportId", 
            Long.class)
            .setParameter("airportId", airportId)
            .getSingleResult()
    );
}
```

---

## Example 5: Returning a Boolean

### Template Method Pattern
```java
public boolean airportExists(String name) {
    return new TransactionalDao<Boolean>() {
        @Override
        protected Boolean executeInTransaction(EntityManager em) {
            Long count = em.createQuery(
                "select count(a) from Airport a where a.name = :name", 
                Long.class)
                .setParameter("name", name)
                .getSingleResult();
            return count > 0;
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public boolean airportExists(String name) {
    return TransactionExecutor.executeInTransaction(em -> {
        Long count = em.createQuery(
            "select count(a) from Airport a where a.name = :name", 
            Long.class)
            .setParameter("name", name)
            .getSingleResult();
        return count > 0;
    });
}
```

---

## Example 6: Complex Multi-Step Operation

### Template Method Pattern
```java
public void transferPassenger(Long passengerId, Long newAirportId) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            Passenger passenger = em.find(Passenger.class, passengerId);
            Airport newAirport = em.find(Airport.class, newAirportId);
            
            if (passenger != null && newAirport != null) {
                newAirport.addPassenger(passenger);
            }
            return null;
        }
    }.executeInTransaction();
}
```

### Functional Interface
```java
public void transferPassenger(Long passengerId, Long newAirportId) {
    TransactionExecutor.executeInTransaction(em -> {
        Passenger passenger = em.find(Passenger.class, passengerId);
        Airport newAirport = em.find(Airport.class, newAirportId);
        
        if (passenger != null && newAirport != null) {
            newAirport.addPassenger(passenger);
        }
        return null;
    });
}
```

---

## Line Count Comparison

For a typical DAO method with transaction:

| Approach | Lines | Characters |
|----------|-------|------------|
| **Without abstraction** | ~12 lines | ~400 chars |
| **Template Method** | 9 lines | ~280 chars |
| **Functional Interface** | 7 lines | ~220 chars |

The functional interface saves about **2 lines per method** on average.

---

## What Students Need to Understand

### Template Method Approach
✅ Abstract classes
✅ Method overriding
✅ Anonymous inner classes
✅ Generic types
✅ Template Method design pattern

### Functional Interface Approach
✅ All of the above, PLUS:
✅ Functional interfaces
✅ Lambda expressions
✅ Type inference
✅ Method references (optional)

---

## Teaching Recommendation

1. **Start with Template Method** - More explicit, easier to understand
2. **Show the pattern** - Explain how it eliminates boilerplate
3. **Introduce lambdas** - Show simpler syntax for same thing
4. **Compare side-by-side** - Use this document
5. **Let students choose** - Via factory pattern

The factory pattern allows you to teach both approaches and let students see they're functionally equivalent!

