# Transaction Management: Template Method vs Functional Interface

## Overview

This project demonstrates **two different approaches** for managing database transactions in the DAO layer:

1. **Template Method Pattern** - Uses anonymous inner classes (no functional interfaces required)
2. **Functional Interface** - Uses lambda expressions and `Function<T,R>` (Java 8+)

Both approaches solve the same problem: eliminating transaction boilerplate code.

## Files Organization

### Core Transaction Management

| File | Approach | Description |
|------|----------|-------------|
| `TransactionalDao.java` | Template Method | Abstract base class with template method pattern |
| `TransactionExecutor.java` | Functional Interface | Static utility with `Function<EntityManager, T>` |

### DAO Implementations

| File | Approach | Uses |
|------|----------|------|
| `AirportDaoImpl.java` | Template Method | Anonymous inner classes extending `TransactionalDao` |
| `AirportDaoFunctional.java` | Functional Interface | Lambda expressions with `TransactionExecutor` |

### Factory & Service

| File | Purpose |
|------|---------|
| `AirportDaoFactory.java` | Allows switching between implementations |
| `AirportService.java` | Updated to use factory for easy swapping |

## Comparison

### Template Method Pattern (AirportDaoImpl)

**Pros:**
- ✅ No functional interfaces needed
- ✅ Familiar OOP concepts (abstract classes, inheritance)
- ✅ Easier for beginners
- ✅ Clear structure with named methods

**Code:**
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

### Functional Interface Approach (AirportDaoFunctional)

**Pros:**
- ✅ More concise syntax
- ✅ Modern Java style
- ✅ Functional programming concepts
- ✅ Less boilerplate

**Code:**
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

## How to Switch Between Implementations

### Option 1: Using the Factory (Recommended)

```java
// In main() or setup code:

// Use Template Method pattern (default)
AirportDaoFactory.setDefaultStrategy(Strategy.TEMPLATE_METHOD);
AirportService service = new AirportService();

// Or use Functional Interface approach
AirportDaoFactory.setDefaultStrategy(Strategy.FUNCTIONAL_INTERFACE);
AirportService service = new AirportService();
```

### Option 2: Explicit Constructor

```java
// Choose implementation when creating the service
AirportService service = new AirportService(Strategy.TEMPLATE_METHOD);
// or
AirportService service = new AirportService(Strategy.FUNCTIONAL_INTERFACE);
```

### Option 3: Direct DAO Creation

```java
// Create specific implementation directly
AirportDao dao = AirportDaoFactory.createTemplateMethod();
AirportService service = new AirportService(dao);

// Or
AirportDao dao = AirportDaoFactory.createFunctional();
AirportService service = new AirportService(dao);
```

## Teaching Strategy

### Week 1-2: Template Method Pattern
- Start with `TransactionalDao` and `AirportDaoImpl`
- Students learn:
  - Abstract classes
  - Template method pattern
  - Anonymous inner classes
  - Transaction management

### Week 3-4: Introduce Functional Interfaces
- Introduce `TransactionExecutor` and `AirportDaoFunctional`
- Students learn:
  - Functional interfaces (`Function<T,R>`)
  - Lambda expressions
  - Comparison with previous approach
  - When to use each approach

### Comparison Exercise
- Use `AirportDaoFactory` to demonstrate both approaches side-by-side
- Students see that both solve the same problem differently
- Discuss trade-offs and appropriate use cases

## Running Examples

```bash
# Compile the project
mvn clean compile

# Run with Template Method (default)
mvn exec:java -Dexec.mainClass="al420445.Ex03_DaoPattern"

# The factory allows you to switch implementations without changing application code!
```

## Key Takeaways

1. **Same Interface, Different Implementation** - Both implement `AirportDao`
2. **Strategy Pattern** - Factory allows runtime selection
3. **Design Patterns** - Template Method vs Functional Programming
4. **Flexibility** - Easy to add more strategies in the future
5. **Teaching Tool** - Perfect for comparing different approaches

## Files Summary

- `TransactionalDao.java` - Template method base class
- `TransactionExecutor.java` - Functional interface utility
- `AirportDaoImpl.java` - Template method implementation
- `AirportDaoFunctional.java` - Functional interface implementation
- `AirportDaoFactory.java` - Factory for creating DAOs
- `AirportService.java` - Service layer (updated to use factory)
- `AirportDaoExamples.java` - Extended examples using template method

