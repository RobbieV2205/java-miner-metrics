package minermetrics.java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Bitaxe {

    public int boardVersion = 0;
    public int frequency = 0;
    public int coreVoltage = 0;
    public int temptarget = 0;
    public int fanspeed = 0;
    public int fanrpm = 0;
    public double hashRate = 0.0;
    public double temp = 0.0;
    public double power = 0.0;
    //public double joulesPerTeraHash = power / hashRate;
    public int uptimeSeconds = 0;
}

