package com.swedbank.playground.kid;

import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Kid {
    private String name;
    private int age;
    private boolean isVIP;
    private boolean isSkipped;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public boolean isSkipped() {
        return isSkipped;
    }

    public void setSkipped(boolean skipped) {
        this.isSkipped = skipped;
    }

    public Kid(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Kid kid = (Kid) obj;
        return getAge() == kid.getAge() &&
                Objects.equals(getName(), kid.getName()) &&
                isVIP() == kid.isVIP();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getAge(), isVIP());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("age", age)
                .add("isVIP", isVIP)
                .toString();
    }
}
