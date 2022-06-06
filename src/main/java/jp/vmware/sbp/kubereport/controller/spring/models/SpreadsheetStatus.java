package jp.vmware.sbp.kubereport.controller.spring.models;

public class SpreadsheetStatus {

    private String FriendlyDescription;
    private ExecStatus Aggregated;
    private ExecStatus Formatted;

    public String getFriendlyDescription() {
        return FriendlyDescription;
    }

    public void setFriendlyDescription(String friendlyDescription) {
        FriendlyDescription = friendlyDescription;
    }

    public ExecStatus getAggregated() {
        return Aggregated;
    }

    public void setAggregated(ExecStatus aggregated) {
        Aggregated = aggregated;
    }

    public ExecStatus getFormatted() {
        return Formatted;
    }

    public void setFormatted(ExecStatus formatted) {
        Formatted = formatted;
    }
}
