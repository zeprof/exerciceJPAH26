# Transaction Management Cheat Sheet

## 🎯 Which Approach Should I Use?

| If You Want... | Use This |
|----------------|----------|
| Beginner-friendly code | Template Method (`TransactionalDao`) |
| Modern, concise syntax | Functional Interface (`TransactionExecutor`) |
| To teach design patterns | Template Method |
| To teach functional programming | Functional Interface |
| To compare both | Factory with Strategy |

---

## 📝 Code Snippets

### Create Operation (Returns Entity)

**Template Method:**
```java
return new TransactionalDao<Airport>() {
    @Override
    protected Airport executeInTransaction(EntityManager em) {
        Airport airport = new Airport(name);
        em.persist(airport);
        return airport;
    }
}.executeInTransaction();
```

**Functional:**
```java
return TransactionExecutor.executeInTransaction(em -> {
    Airport airport = new Airport(name);
    em.persist(airport);
    return airport;
});
```

---

### Update Operation (Returns Void)

**Template Method:**
```java
new TransactionalDao<Void>() {
    @Override
    protected Void executeInTransaction(EntityManager em) {
        Airport airport = em.find(Airport.class, id);
        if (airport != null) airport.setName(newName);
        return null;
    }
}.executeInTransaction();
```

**Functional:**
```java
TransactionExecutor.executeInTransaction(em -> {
    Airport airport = em.find(Airport.class, id);
    if (airport != null) airport.setName(newName);
    return null;
});
```

---

### Query Operation (Returns Count)

**Template Method:**
```java
return new TransactionalDao<Long>() {
    @Override
    protected Long executeInTransaction(EntityManager em) {
        return em.createQuery("SELECT COUNT(p) FROM Passenger p", Long.class)
                 .getSingleResult();
    }
}.executeInTransaction();
```

**Functional:**
```java
return TransactionExecutor.executeInTransaction(em ->
    em.createQuery("SELECT COUNT(p) FROM Passenger p", Long.class)
      .getSingleResult()
);
```

---

## 🏭 Factory Usage

### Default Strategy
```java
AirportService service = new AirportService();  // Uses default
```

### Specific Strategy
```java
// Template Method
AirportService service = new AirportService(Strategy.TEMPLATE_METHOD);

// Functional Interface
AirportService service = new AirportService(Strategy.FUNCTIONAL_INTERFACE);
```

### Change Default
```java
AirportDaoFactory.setDefaultStrategy(Strategy.TEMPLATE_METHOD);
// or
AirportDaoFactory.setDefaultStrategy(Strategy.FUNCTIONAL_INTERFACE);
```

### Direct DAO Creation
```java
AirportDao dao = AirportDaoFactory.createTemplateMethod();
// or
AirportDao dao = AirportDaoFactory.createFunctional();
```

---

## 📦 Import Statements

### Template Method
```java
import al420445.dao.TransactionalDao;
import jakarta.persistence.EntityManager;
```

### Functional Interface
```java
import al420445.dao.TransactionExecutor;
```

### Factory
```java
import al420445.dao.AirportDaoFactory;
import al420445.dao.AirportDaoFactory.Strategy;
```

---

## 🎓 Teaching Sequence

1. **Show problem:** Transaction boilerplate everywhere
2. **Introduce Template Method:** Explain abstract classes, pattern
3. **Practice:** Students write DAOs with Template Method
4. **Introduce lambdas:** Show simpler syntax
5. **Introduce Functional:** Same logic, different syntax
6. **Compare:** Run Ex10_CompareTransactionApproaches
7. **Factory pattern:** Let students switch between them

---

## 🚀 Run Examples

```bash
# Compile
mvn clean compile

# Compare both approaches
mvn exec:java -Dexec.mainClass="al420445.Ex10_CompareTransactionApproaches"

# Original DAO example (uses Template Method by default)
mvn exec:java -Dexec.mainClass="al420445.Ex03_DaoPattern"
```

---

## 📂 File Locations

```
src/main/java/al420445/dao/
├── TransactionalDao.java              ← Template Method base class
├── TransactionExecutor.java           ← Functional Interface utility
├── AirportDaoImpl.java                ← Template Method implementation
├── AirportDaoFunctional.java          ← Functional implementation
├── AirportDaoFactory.java             ← Factory for switching
├── AirportDaoExamples.java            ← Template Method examples
└── FunctionalInterfaceExamples.java   ← Functional examples
```

---

## 💡 Quick Tips

- **Use Void for operations that don't return anything** (create, update, delete)
- **Use entity types when returning objects** (Airport, Passenger, etc.)
- **Use primitives/wrappers for counts/flags** (Long, Boolean, etc.)
- **Read-only queries don't need transactions** (just use EntityManager directly)
- **Both approaches handle rollback automatically** on exceptions

---

## 🔑 Key Differences

| Aspect | Template Method | Functional |
|--------|----------------|------------|
| Class definition | `new TransactionalDao<T>() { ... }` | `TransactionExecutor.executeInTransaction(...)` |
| Method header | `@Override protected T executeInTransaction(EntityManager em)` | `em -> { ... }` |
| Return statement | Inside method body | Inside lambda body |
| Lines of code | ~9 lines | ~7 lines |
| Student level | Beginner | Intermediate+ |

---

## ✅ Benefits of Both

- ✅ No more copy-paste transaction code
- ✅ Automatic begin/commit/rollback
- ✅ Automatic EntityManager cleanup
- ✅ Type-safe with generics
- ✅ Consistent error handling
- ✅ Less error-prone
- ✅ Easier to maintain

---

**Remember:** Both approaches do the exact same thing - they just use different Java features! Choose based on your teaching goals and student experience level.

