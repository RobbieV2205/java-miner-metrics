package minermetrics.java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Bitaxe {

//    int boardVersion = 0;
//    double hashRate = 0.0;

    public int getBoardVersion() {
        return boardVersion;
    }
    private int boardVersion;

    public double getHashRate() {return hashRate;}
    private double hashRate;

    public int getCoreVoltage() {return coreVoltage;}
    private int coreVoltage;
}

