package ci.ahmadfauzirahman.hrm.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ci.ahmadfauzirahman.hrm.Model.LogAbsensiModel;

public class LogAbsensiResponse {
    @SerializedName("con")
    @Expose
    private Boolean con;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("results")
    @Expose
    private List<LogAbsensiModel> results = null;

    public Boolean getCon() {
        return con;
    }

    public void setCon(Boolean con) {
        this.con = con;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<LogAbsensiModel> getResults() {
        return results;
    }

    public void setResults(List<LogAbsensiModel> results) {
        this.results = results;
    }
}
