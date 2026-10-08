package minermetrics.java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class EnergySocket {
    public double active_power_w = 0;

}
