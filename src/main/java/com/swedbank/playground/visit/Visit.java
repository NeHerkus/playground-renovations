package com.swedbank.playground.visit;

import com.google.common.base.MoreObjects;
import com.swedbank.playground.kid.Kid;
import com.swedbank.playground.playground.Playground;

import java.util.Date;
import java.util.Objects;

public final class Visit {

    private Playground playground;
    private Kid kid;
    private Date time;
    private boolean isStart;  // true - start of visit; false - end of visit

    public Kid getKid() {
        return kid;
    }

    public void setKid(Kid kid) {
        this.kid = kid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart(boolean start) {
        isStart = start;
    }

    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("playground", playground)
                .add("kid", kid)
                .add("time", time)
                .add("isStart", isStart)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Visit visit = (Visit) obj;
        return isStart() == visit.isStart() &&
                Objects.equals(getPlayground(), visit.getPlayground()) &&
                Objects.equals(getKid(), visit.getKid()) &&
                Objects.equals(getTime(), visit.getTime());
    }

}
