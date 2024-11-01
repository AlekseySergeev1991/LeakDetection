package ru.tecon.leakDetection.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class ArchiveReport implements Serializable {

    private int id;
    private String repType;
    private String repDate;
    private String redirect;

    public ArchiveReport(int id, String repType, String repDate) {
        this.id = id;
        this.repType = repType;
        this.repDate = repDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRepType() {
        return repType;
    }

    public void setRepType(String repType) {
        this.repType = repType;
    }

    public String getRepDate() {
        return repDate;
    }

    public void setRepDate(String repDate) {
        this.repDate = repDate;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ArchiveReport.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("repType='" + repType + "'")
                .add("repDate='" + repDate + "'")
                .add("redirect='" + redirect + "'")
                .toString();
    }
}
