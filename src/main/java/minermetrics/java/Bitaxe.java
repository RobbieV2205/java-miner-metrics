package minermetrics.java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Bitaxe {

    public int id = 0;
    public String instanceIpv4 = "";
    public int boardVersion = 0;
    public int frequency = 0;
    public int coreVoltage = 0;
    public int coreVoltageActual = 0;
    public int tempTarget = 0;
    public int fanSpeed = 0;
    public int fanrpm = 0;
    public double hashRate = 0.0;
    public double temp = 0.0;
    public double power = 0.0;
    public double active_power_w = 0;
    public int uptimeSeconds = 0;
    public double joulesPerTerahash = 0.0;
    public double getJoulesPerTerahash() {
        double terahash = hashRate / 1000.0; // GH/s -> TH/s
        return power / terahash;             // Watt / (TH/s) = J/TH
    }

}

