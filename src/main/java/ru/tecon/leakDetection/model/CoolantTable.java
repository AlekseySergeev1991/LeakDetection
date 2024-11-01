package ru.tecon.leakDetection.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class CoolantTable implements Serializable {
    private String filial;
    private String pred;
    private String chp;
    private String algoritm;
    private String date;
    private String t3;
    private String thv;
    private String t4;
    private String g1;
    private String g2;
    private String g3;
    private String g4;
    private String gp;
    private String gsr;
    private String dg;
    private String coolant_eff;
    private String heat_eff;
    private String sum_eff;
    private String color;

    public CoolantTable(String filial, String pred, String chp, String algoritm, String date, String t3, String thv, String t4, String g1, String g2, String g3, String g4, String gp, String gsr, String dg, String coolant_eff, String heat_eff, String sum_eff, String color) {
        this.filial = filial;
        this.pred = pred;
        this.chp = chp;
        this.algoritm = algoritm;
        this.date = date;
        this.t3 = t3;
        this.thv = thv;
        this.t4 = t4;
        this.g1 = g1;
        this.g2 = g2;
        this.g3 = g3;
        this.g4 = g4;
        this.gp = gp;
        this.gsr = gsr;
        this.dg = dg;
        this.coolant_eff = coolant_eff;
        this.heat_eff = heat_eff;
        this.sum_eff = sum_eff;
        this.color = color;
    }

    public CoolantTable(String filial, String coolant_eff, String heat_eff, String sum_eff, String color) {
        this.filial = filial;
        this.coolant_eff = coolant_eff;
        this.heat_eff = heat_eff;
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

    public String getT3() {
        return t3;
    }

    public void setT3(String t3) {
        this.t3 = t3;
    }

    public String getThv() {
        return thv;
    }

    public void setThv(String thv) {
        this.thv = thv;
    }

    public String getT4() {
        return t4;
    }

    public void setT4(String t4) {
        this.t4 = t4;
    }

    public String getG1() {
        return g1;
    }

    public void setG1(String g1) {
        this.g1 = g1;
    }

    public String getG2() {
        return g2;
    }

    public void setG2(String g2) {
        this.g2 = g2;
    }

    public String getG3() {
        return g3;
    }

    public void setG3(String g3) {
        this.g3 = g3;
    }

    public String getG4() {
        return g4;
    }

    public void setG4(String g4) {
        this.g4 = g4;
    }

    public String getGp() {
        return gp;
    }

    public void setGp(String gp) {
        this.gp = gp;
    }

    public String getGsr() {
        return gsr;
    }

    public void setGsr(String gsr) {
        this.gsr = gsr;
    }

    public String getDg() {
        return dg;
    }

    public void setDg(String dg) {
        this.dg = dg;
    }

    public String getCoolant_eff() {
        return coolant_eff;
    }

    public void setCoolant_eff(String coolant_eff) {
        this.coolant_eff = coolant_eff;
    }

    public String getHeat_eff() {
        return heat_eff;
    }

    public void setHeat_eff(String heat_eff) {
        this.heat_eff = heat_eff;
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
        return new StringJoiner(", ", CoolantTable.class.getSimpleName() + "[", "]")
                .add("filial='" + filial + "'")
                .add("pred='" + pred + "'")
                .add("chp='" + chp + "'")
                .add("algoritm='" + algoritm + "'")
                .add("date='" + date + "'")
                .add("t3='" + t3 + "'")
                .add("thv='" + thv + "'")
                .add("t4='" + t4 + "'")
                .add("g1='" + g1 + "'")
                .add("g2='" + g2 + "'")
                .add("g3='" + g3 + "'")
                .add("g4='" + g4 + "'")
                .add("gp='" + gp + "'")
                .add("gsr='" + gsr + "'")
                .add("dg='" + dg + "'")
                .add("coolant_eff='" + coolant_eff + "'")
                .add("heat_eff='" + heat_eff + "'")
                .add("sum_eff='" + sum_eff + "'")
                .add("color='" + color + "'")
                .toString();
    }
}
