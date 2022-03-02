package com.swedbank.playground.playground;

import com.google.common.collect.ImmutableList;
import com.swedbank.playground.kid.Kid;
import com.swedbank.playground.visit.Visit;
import com.swedbank.playground.visit.VisitRepository;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public abstract class Playground {

    protected PlaygroundType playgroundType;
    protected int maxKidsPlaying;
    protected List<Kid> kidsPlaying = new LinkedList<>();
    protected List<Kid> kidsWaiting = new LinkedList<>();
    protected VisitRepository visitRepository;

    public abstract int getMaxKidsPlaying();

    public abstract PlaygroundType getPlaygroundType();

    protected abstract VisitRepository getVisitRepository();

    public abstract boolean addKid(Kid kid);

    public abstract void removeKid(Kid kid);

    public abstract int calculatePlaygroundUtilization();

    public List<Kid> kidsPlaying() {
        ImmutableList.Builder<Kid> builder = ImmutableList.builder();
        for (Kid kid : kidsPlaying) {
            builder.add(kid);
        }
        return builder.build();
    }

    public List<Kid> kidsWaiting() {
        ImmutableList.Builder<Kid> builder = ImmutableList.builder();
        for (Kid kid : kidsWaiting) {
            builder.add(kid);
        }
        return builder.build();
    }

    protected void markSkipped(int index) {
        int toMark = 3;
        ListIterator<Kid> listIterator = kidsWaiting.listIterator(index);
        while (listIterator.hasNext() && toMark > 0) {
            listIterator.next().setSkipped(true);
            toMark--;
        }
    }

    protected void saveVisit(Kid kid, boolean isStart) {
        Visit visit = new Visit();
        visit.setPlayground(this);
        visit.setKid(kid);
        visit.setStart(isStart);
        visit.setTime(new Date());
        getVisitRepository().save(visit);
    }
}
