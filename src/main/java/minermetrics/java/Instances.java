package minermetrics.java;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Instances {
    public int getBoardVersion() {
        return boardVersion;
    }

    public void setBoardVersion(int boardVersion) {
        this.boardVersion = boardVersion;
    }

    private int boardVersion;

    @Override
    public String toString() {
        return "Instances{boardVersion=" + boardVersion + "}";
    }


}

