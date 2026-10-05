package minermetrics.java;

import io.prometheus.metrics.core.metrics.Gauge;

public final class BitaxeGauges {

    public BitaxeGauges() {}

    public static final Gauge boardVersion = Gauge.builder()
            .name("bitaxeboardVersion")
            .help("board version of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge frequency = Gauge.builder()
            .name("bitaxefrequency")
            .help("Frequency setting of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge coreVoltage = Gauge.builder()
            .name("bitaxecoreVoltage")
            .help("Voltage setting of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge coreVoltageActual = Gauge.builder()
            .name("bitaxecoreVoltageActual")
            .help("Actual core voltage of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge tempTarget = Gauge.builder()
            .name("bitaxetempTarget")
            .help("Temprature setting of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge fanSpeed = Gauge.builder()
            .name("bitaxefanSpeed")
            .help("fan speed setting of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge fanRpm = Gauge.builder()
            .name("bitaxeFanRpm")
            .help("Fan speed in RPM")
            .labelNames("instance")
            .register();

    public static final Gauge hashRate = Gauge.builder()
            .name("bitaxeHashRate")
            .help("Current hashrate")
            .labelNames("instance")
            .register();

    public static final Gauge temp = Gauge.builder()
            .name("bitaxetemp")
            .help("Temprature of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge power = Gauge.builder()
            .name("bitaxepower")
            .help("power consumption of the bitaxe")
            .labelNames("instance")
            .register();

    public static final Gauge uptimeSeconds = Gauge.builder()
            .name("bitaxeuptimeSeconds")
            .help("Uptime in seconds")
            .labelNames("instance")
            .register();

    public static final Gauge joulesPerTerahash = Gauge.builder()
            .name("bitaxejoulesPerTerahash")
            .help("Joules per terahash power / terahashes")
            .labelNames("instance")
            .register();

}
