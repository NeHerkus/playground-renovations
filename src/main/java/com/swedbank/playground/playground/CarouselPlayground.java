package com.swedbank.playground.playground;

import com.swedbank.playground.kid.Kid;
import com.swedbank.playground.visit.Visit;
import com.swedbank.playground.visit.VisitRepository;

import java.util.Date;
import java.util.ListIterator;

public class CarouselPlayground extends Playground {

    public CarouselPlayground(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
        this.playgroundType = PlaygroundType.CAROUSEL;
    }

    @Override
    public int getMaxKidsPlaying() {
        return maxKidsPlaying;
    }

    public void setMaxKidsPlaying(int maxKidsPlaying) {
        this.maxKidsPlaying = maxKidsPlaying;
    }

    @Override
    public PlaygroundType getPlaygroundType() {
        return playgroundType;
    }

    @Override
    protected VisitRepository getVisitRepository() {
        return visitRepository;
    }

    @Override
    public boolean addKid(Kid kid) {
        if (kidsPlaying.size() < getMaxKidsPlaying()) {
            kidsPlaying.add(kid);
            saveVisit(kid, true);
            return true;
        }
        if (kid.isVIP()) {
            int amountOfKidsWaitingInLine = kidsWaiting.size();
            ListIterator<Kid> listIterator = kidsWaiting.listIterator(amountOfKidsWaitingInLine);
            while (listIterator.hasPrevious()) {
                Kid kidWaiting = listIterator.previous();
                int index = kidsWaiting.indexOf(kidWaiting);
                if (kidWaiting.isSkipped() || kidWaiting.isVIP()) {
                    markSkipped(index);
                    kidsWaiting.add(index + 1, kid);
                    return false;
                }
            }
            markSkipped(0);
            kidsWaiting.add(0, kid);
            return false;
        }
        kidsWaiting.add(kid);
        return false;
    }

    @Override
    public void removeKid(Kid kid) {
        if (kidsWaiting.remove(kid)) {
            return;
        }
        if (kidsPlaying.remove(kid)) {
            saveVisit(kid, false);

            if (kidsWaiting.size() > 0) {
                Kid kid2 = kidsWaiting.remove(0);
                kidsPlaying.add(kid2);
                saveVisit(kid, true);
            }
        } else {
            throw new KidNotFoundException("No kid " + kid + " in playground: " + this);
        }
    }

    @Override
    public int calculatePlaygroundUtilization() {
        return (int) Math.round(kidsPlaying.size() * 100.0 / getMaxKidsPlaying());
    }
}
