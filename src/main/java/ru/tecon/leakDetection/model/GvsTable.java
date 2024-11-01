package ru.tecon.leakDetection.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class GvsTable implements Serializable {
    private String filial;
    private String pred;
    private String chp;
    private String algoritm;
    private String date;
    private String t7;
    private String thv;
    private String ghvnagvsf;
    private String g7f;
    private String g13f;
    private String qf;
    private String dgf;
    private String dgavg;
    private String qavg;
    private String dg;
    private String dq;
    private String heat_eff;
    private String water_eff;
    private String sum_eff;
    private String color;

    public GvsTable(String filial, String pred, String chp, String algoritm, String date, String t7, String thv, String ghvnagvsf, String g7f, String g13f, String qf, String dgf, String dgavg, String qavg, String dg, String dq, String heat_eff, String water_eff, String sum_eff, String color) {
        this.filial = filial;
        this.pred = pred;
        this.chp = chp;
        this.algoritm = algoritm;
        this.date = date;
        this.t7 = t7;
        this.thv = thv;
        this.ghvnagvsf = ghvnagvsf;
        this.g7f = g7f;
        this.g13f = g13f;
        this.qf = qf;
        this.dgf = dgf;
        this.dgavg = dgavg;
        this.qavg = qavg;
        this.dg = dg;
        this.dq = dq;
        this.heat_eff = heat_eff;
        this.water_eff = water_eff;
        this.sum_eff = sum_eff;
        this.color = color;
    }

    public String getFilial() {
        return filial;
    }

    public void setFilial(String filial) {
        this.filial = filial;
    }

    public String getPred() {
        return pred;
    }

    public void setPred(String pred) {
        this.pred = pred;
    }

    public String getChp() {
        return chp;
    }

    public void setChp(String chp) {
        this.chp = chp;
    }

    public String getAlgoritm() {
        return algoritm;
    }

    public void setAlgoritm(String algoritm) {
        this.algoritm = algoritm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getT7() {
        return t7;
    }

    public void setT7(String t7) {
        this.t7 = t7;
    }

    public String getThv() {
        return thv;
    }

    public void setThv(String thv) {
        this.thv = thv;
    }

    public String getGhvnagvsf() {
        return ghvnagvsf;
    }

    public void setGhvnagvsf(String ghvnagvsf) {
        this.ghvnagvsf = ghvnagvsf;
    }

    public String getG7f() {
        return g7f;
    }

    public void setG7f(String g7f) {
        this.g7f = g7f;
    }

    public String getG13f() {
        return g13f;
    }

    public void setG13f(String g13f) {
        this.g13f = g13f;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }

    public String getDgf() {
        return dgf;
    }

    public void setDgf(String dgf) {
        this.dgf = dgf;
    }

    public String getDgavg() {
        return dgavg;
    }

    public void setDgavg(String dgavg) {
        this.dgavg = dgavg;
    }

    public String getQavg() {
        return qavg;
    }

    public void setQavg(String qavg) {
        this.qavg = qavg;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getDq() {
        return dq;
    }

    public void setDq(String dq) {
        this.dq = dq;
    }

    public String getHeat_eff() {
        return heat_eff;
    }

    public void setHeat_eff(String heat_eff) {
        this.heat_eff = heat_eff;
    }

    public String getWater_eff() {
        return water_eff;
    }

    public void setWater_eff(String water_eff) {
        this.water_eff = water_eff;
    }

    public String getSum_eff() {
        return sum_eff;
    }

    public void setSum_eff(String sum_eff) {
        this.sum_eff = sum_eff;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", GvsTable.class.getSimpleName() + "[", "]")
                .add("filial='" + filial + "'")
                .add("pred='" + pred + "'")
                .add("chp='" + chp + "'")
                .add("algoritm='" + algoritm + "'")
                .add("date='" + date + "'")
                .add("t7='" + t7 + "'")
                .add("thv='" + thv + "'")
                .add("ghvnagvsf='" + ghvnagvsf + "'")
                .add("g7f='" + g7f + "'")
                .add("g13f='" + g13f + "'")
                .add("qf='" + qf + "'")
                .add("dgf='" + dgf + "'")
                .add("dgavg='" + dgavg + "'")
                .add("qavg='" + qavg + "'")
                .add("dg='" + dg + "'")
                .add("dq='" + dq + "'")
                .add("heat_eff='" + heat_eff + "'")
                .add("water_eff='" + water_eff + "'")
                .add("sum_eff='" + sum_eff + "'")
                .add("color='" + color + "'")
                .toString();
    }
}
