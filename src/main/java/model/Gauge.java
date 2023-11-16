package model;

public enum Gauge {

    OO("OO", "1/76th scale"),
    TT("TT", "1/120th scale"),
    N("N", "1/148th scale");

    private final String name;
    private final String scale;

    /**
     * Constructs a new Gauge with the specified name and scale.
     *
     * @param name  The name of the gauge.
     * @param scale The scale of the gauge.
     */
    Gauge(String name, String scale) {
        this.name = name;
        this.scale = scale;
    }

    /**
     * Retrieves the name of the gauge.
     *
     * @return The name of the gauge.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the scale associated with the gauge.
     *
     * @return The scale of the gauge.
     */
    public String getScale() {
        return scale;
    }

    /**
     * Returns the Gauge corresponding to the given name.
     *
     * @param gaugeName The name of the gauge to find.
     * @return The Gauge corresponding to the given name.
     * @throws IllegalArgumentException If the gauge name does not match any Gauge.
     */
    public static Gauge fromName(String gaugeName) {
        for (Gauge gauge : Gauge.values()) {
            if (gauge.getName().equalsIgnoreCase(gaugeName)) {
                return gauge;
            }
        }
        throw new IllegalArgumentException("Invalid gauge name: " + gaugeName);
    }
}

