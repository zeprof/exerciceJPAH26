# Template Method Pattern for Transaction Management

## Overview

The DAO layer uses the **Template Method Pattern** to manage JPA transactions. This approach eliminates the need for functional interfaces (Function<T,R>) while providing a clean way to handle transaction lifecycle.

## How It Works

### 1. TransactionalDao Base Class

The `TransactionalDao<T>` abstract class provides:
- A **template method** `executeInTransaction()` that manages the transaction lifecycle
- An **abstract method** `executeInTransaction(EntityManager em)` that subclasses must implement

The template method handles:
- Creating the EntityManager
- Beginning the transaction
- Committing on success
- Rolling back on exceptions
- Closing the EntityManager

### 2. Using the Pattern

To execute code within a transaction, create an anonymous inner class that extends `TransactionalDao<T>`:

```java
public void addPassenger(String name, int airportId) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            // Your transactional logic here
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

### 3. Return Values

The generic type `<T>` allows you to return values from transactional operations:

```java
public Airport createAirport(String name, String code) {
    return new TransactionalDao<Airport>() {
        @Override
        protected Airport executeInTransaction(EntityManager em) {
            Airport airport = new Airport(name, code);
            em.persist(airport);
            return airport;
        }
    }.executeInTransaction();
}
```

## Benefits

1. **No functional interfaces required** - Students don't need to understand lambdas or Function<T,R>
2. **Clear structure** - The template method pattern is straightforward to understand
3. **Automatic resource management** - Transaction lifecycle is handled consistently
4. **Type-safe** - Generic return types ensure type safety

## When to Use

- **Use transactions** for operations that modify data (INSERT, UPDATE, DELETE)
- **Don't use transactions** for simple read-only queries (SELECT)

Example:
```java
// Read-only: no transaction needed
public List<Airport> getAirports() {
    EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
    try {
        return em.createQuery("select a from Airport a", Airport.class).getResultList();
    } finally {
        em.close();
    }
}

// Write operation: use TransactionalDao
public void deleteAirport(Long airportId) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            Airport airport = em.find(Airport.class, airportId);
            if (airport != null) {
                em.remove(airport);
            }
            return null;
        }
    }.executeInTransaction();
}
```

## Teaching Notes

This pattern demonstrates:
1. **Template Method Pattern** - A behavioral design pattern where a base class defines the algorithm structure
2. **Anonymous Inner Classes** - Creating a class without a formal declaration
3. **Abstract Methods** - Methods that must be implemented by subclasses
4. **Transaction Management** - Proper handling of database transactions
5. **Separation of Concerns** - DAO layer handles persistence, Service layer handles business logic

