package jp.vmware.sbp.kubereport.controller.spring.models;

public class ExecStatus {

    private String Success;
    private String Error;
    private String StartedAt;
    private String UpdateAt;

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String success) {
        Success = success;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getStartedAt() {
        return StartedAt;
    }

    public void setStartedAt(String startedAt) {
        StartedAt = startedAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }
}
