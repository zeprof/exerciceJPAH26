package al420445.dao.base;

/**
 * Factory for creating AirportDao instances.
 * This allows easy swapping between different implementation strategies:
 * - Template Method pattern (AirportDaoImpl)
 * - Functional Interface approach (AirportDaoFunctional)
 *
 * Usage:
 * <pre>
 * // Use Template Method pattern (default)
 * AirportDao dao = AirportDaoFactory.create();
 *
 * // Or explicitly choose Template Method
 * AirportDao dao = AirportDaoFactory.createTemplateMethod();
 *
 * // Or choose Functional Interface approach
 * AirportDao dao = AirportDaoFactory.createFunctional();
 * </pre>
 */
public class AirportDaoFactory {

    /**
     * Implementation strategy enum
     */
    public enum Strategy {
        TEMPLATE_METHOD,
        FUNCTIONAL_INTERFACE
    }

    // Default strategy - can be changed
    private static Strategy defaultStrategy = Strategy.TEMPLATE_METHOD;

    /**
     * Set the default strategy for creating DAOs
     */
    public static void setDefaultStrategy(Strategy strategy) {
        defaultStrategy = strategy;
    }

    /**
     * Get the current default strategy
     */
    public static Strategy getDefaultStrategy() {
        return defaultStrategy;
    }

    /**
     * Create an AirportDao using the default strategy
     */
    public static AirportDao create() {
        return create(defaultStrategy);
    }

    /**
     * Create an AirportDao using the specified strategy
     */
    public static AirportDao create(Strategy strategy) {
        return switch (strategy) {
            case TEMPLATE_METHOD -> new AirportDaoImpl();
            case FUNCTIONAL_INTERFACE -> new AirportDaoFunctional();
        };
    }

    /**
     * Create an AirportDao using Template Method pattern
     */
    public static AirportDao createTemplateMethod() {
        return new AirportDaoImpl();
    }

    /**
     * Create an AirportDao using Functional Interface approach
     */
    public static AirportDao createFunctional() {
        return new AirportDaoFunctional();
    }
}

