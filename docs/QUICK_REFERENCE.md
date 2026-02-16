# Transaction Management - Quick Reference Guide

## Two Approaches Implemented

This project provides **two equivalent approaches** for managing database transactions:

| Approach | Class | Best For |
|----------|-------|----------|
| **Template Method** | `TransactionalDao` | Beginners, clear OOP structure |
| **Functional Interface** | `TransactionExecutor` | Advanced students, concise code |

---

## Quick Start

### Using Template Method (Recommended for Beginners)

```java
public void addPassenger(String name, int airportId) {
    new TransactionalDao<Void>() {
        @Override
        protected Void executeInTransaction(EntityManager em) {
            Airport airport = em.find(Airport.class, (long) airportId);
            if (airport == null) return null;
            
            Passenger passenger = new Passenger(name);
            airport.addPassenger(passenger);
            em.persist(passenger);
            return null;
        }
    }.executeInTransaction();
}
```

### Using Functional Interface (Java 8+)

```java
public void addPassenger(String name, int airportId) {
    TransactionExecutor.executeInTransaction(em -> {
        Airport airport = em.find(Airport.class, (long) airportId);
        if (airport == null) return null;
        
        Passenger passenger = new Passenger(name);
        airport.addPassenger(passenger);
        em.persist(passenger);
        return null;
    });
}
```

---

## Switching Between Implementations

### Method 1: Factory with Strategy (Recommended)

```java
// Set default strategy
AirportDaoFactory.setDefaultStrategy(Strategy.TEMPLATE_METHOD);
AirportService service = new AirportService();
```

### Method 2: Service Constructor

```java
// Template Method
AirportService service = new AirportService(Strategy.TEMPLATE_METHOD);

// Functional Interface
AirportService service = new AirportService(Strategy.FUNCTIONAL_INTERFACE);
```

### Method 3: Direct DAO Creation

```java
AirportDao dao = AirportDaoFactory.createTemplateMethod();
// or
AirportDao dao = AirportDaoFactory.createFunctional();

AirportService service = new AirportService(dao);
```

---

## File Organization

### Core Classes

```
dao/
├── TransactionalDao.java          # Template Method base class
├── TransactionExecutor.java       # Functional Interface utility
├── AirportDao.java                # Interface
├── AirportDaoImpl.java            # Template Method implementation
├── AirportDaoFunctional.java      # Functional Interface implementation
└── AirportDaoFactory.java         # Factory for switching
```

### Example Classes

```
dao/
├── AirportDaoExamples.java        # Template Method examples
└── FunctionalInterfaceExamples.java # Functional Interface examples
```

### Documentation

```
dao/
├── README_TEMPLATE_METHOD.md      # Template Method guide
├── COMPARISON_GUIDE.md            # Detailed comparison
└── QUICK_REFERENCE.md             # This file
```

### Demonstration

```
al420445/
└── Ex10_CompareTransactionApproaches.java  # Demo program
```

---

## Running Examples

```bash
# Compile
mvn clean compile

# Run comparison demo
mvn exec:java -Dexec.mainClass="al420445.Ex10_CompareTransactionApproaches"

# Run original DAO pattern example
mvn exec:java -Dexec.mainClass="al420445.Ex03_DaoPattern"
```

---

## Key Differences

| Feature | Template Method | Functional Interface |
|---------|----------------|---------------------|
| **Syntax** | Anonymous inner class | Lambda expression |
| **Lines of code** | More verbose | More concise |
| **Concepts needed** | Abstract classes, inheritance | Functional interfaces, lambdas |
| **Java version** | Any | Java 8+ |
| **Student level** | Beginner-friendly | Intermediate+ |
| **Readability** | Very explicit | Compact |

---

## Common Return Types

### Void Operations
```java
// Template Method
new TransactionalDao<Void>() {
    protected Void executeInTransaction(EntityManager em) {
        // ... code ...
        return null;
    }
}.executeInTransaction();

// Functional
TransactionExecutor.executeInTransaction(em -> {
    // ... code ...
    return null;
});
```

### Returning Entities
```java
// Template Method
return new TransactionalDao<Airport>() {
    protected Airport executeInTransaction(EntityManager em) {
        // ... code ...
        return airport;
    }
}.executeInTransaction();

// Functional
return TransactionExecutor.executeInTransaction(em -> {
    // ... code ...
    return airport;
});
```

### Returning Primitives
```java
// Template Method
return new TransactionalDao<Long>() {
    protected Long executeInTransaction(EntityManager em) {
        // ... code ...
        return count;
    }
}.executeInTransaction();

// Functional
return TransactionExecutor.executeInTransaction(em -> {
    // ... code ...
    return count;
});
```

---

## Teaching Progression

### Week 1-2: Template Method
- Start with `TransactionalDao`
- Use `AirportDaoImpl`
- Focus on OOP concepts

### Week 3-4: Functional Interfaces
- Introduce `TransactionExecutor`
- Show `AirportDaoFunctional`
- Compare approaches

### Week 5: Design Patterns
- Discuss Factory pattern
- Use `AirportDaoFactory`
- Run `Ex10_CompareTransactionApproaches`

---

## Benefits of Both Approaches

✅ **Eliminates transaction boilerplate**
✅ **Consistent error handling**
✅ **Automatic resource management**
✅ **Type-safe with generics**
✅ **Same interface, different implementation**
✅ **Easy to swap via factory**

---

## Summary

Both approaches solve the exact same problem - they just use different Java features to do it. The Template Method pattern is more explicit and easier for beginners, while the Functional Interface approach is more concise and demonstrates modern Java practices.

Choose based on your students' experience level and what concepts you want to emphasize!

