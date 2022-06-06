package jp.vmware.sbp.kubereport.controller.spring.models;

public class SpreadsheetSpec {
    private String[] TargetNamespaces;
    private String[] ScrapeResources;

    private boolean Summary;
    private boolean VSphere;

    private String KubeAggregatorURL;
    private String KubeFormatterURL;

    public String[] getTargetNamespaces() {
        return TargetNamespaces;
    }

    public void setTargetNamespaces(String[] targetNamespaces) {
        TargetNamespaces = targetNamespaces;
    }

    public String[] getScrapeResources() {
        return ScrapeResources;
    }

    public void setScrapeResources(String[] scrapeResources) {
        ScrapeResources = scrapeResources;
    }

    public boolean isSummary() {
        return Summary;
    }

    public void setSummary(boolean summary) {
        Summary = summary;
    }

    public boolean isVSphere() {
        return VSphere;
    }

    public void setVSphere(boolean VSphere) {
        this.VSphere = VSphere;
    }

    public String getKubeAggregatorURL() {
        return KubeAggregatorURL;
    }

    public void setKubeAggregatorURL(String kubeAggregatorURL) {
        KubeAggregatorURL = kubeAggregatorURL;
    }

    public String getKubeFormatterURL() {
        return KubeFormatterURL;
    }

    public void setKubeFormatterURL(String kubeFormatterURL) {
        KubeFormatterURL = kubeFormatterURL;
    }
}
