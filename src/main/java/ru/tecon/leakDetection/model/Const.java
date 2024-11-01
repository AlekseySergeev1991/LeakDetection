package ru.tecon.leakDetection.model;

import java.io.Serializable;
import java.util.StringJoiner;

public class Const implements Serializable {
    private int id;
    private String name;
    private String value;
    private String customColor;

    public Const(int id, String name, String value, String customColor) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.customColor = customColor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCustomColor() {
        return customColor;
    }

    public void setCustomColor(String customColor) {
        this.customColor = customColor;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Const.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("name='" + name + "'")
                .add("value='" + value + "'")
                .add("customColor='" + customColor + "'")
                .toString();
    }
}
