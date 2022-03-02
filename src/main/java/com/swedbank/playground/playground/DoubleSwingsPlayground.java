package com.swedbank.playground.playground;

import com.swedbank.playground.kid.Kid;
import com.swedbank.playground.visit.VisitRepository;
import com.swedbank.playground.visit.Visit;

import java.util.Date;
import java.util.ListIterator;

public class DoubleSwingsPlayground extends Playground {

    public DoubleSwingsPlayground(VisitRepository visitRepository) {
        this.visitRepository = visitRepository;
        this.playgroundType = PlaygroundType.DOUBLE_SWINGS;
        this.maxKidsPlaying = 2;
    }

    @Override
    public int getMaxKidsPlaying() {
        return maxKidsPlaying;
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
        if (kidsPlaying.size() != maxKidsPlaying && kidsWaiting.size() > 0) {
            kidsPlaying.add(kid);
            Visit visit = new Visit();
            visit.setPlayground(this);
            visit.setKid(kid);
            visit.setStart(true);
            visit.setTime(new Date());
            visitRepository.save(visit);

            Kid kid2 = kidsWaiting.remove(0);
            kidsPlaying.add(kid2);
            visit = new Visit();
            visit.setPlayground(this);
            visit.setKid(kid2);
            visit.setStart(true);
            visit.setTime(new Date());
            visitRepository.save(visit);
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
            Visit visit = new Visit();
            visit.setPlayground(this);
            visit.setKid(kid);
            visit.setStart(false);
            visit.setTime(new Date());
            visitRepository.save(visit);

            if (kidsWaiting.size() > 0) {
                Kid kid2 = kidsWaiting.remove(0);
                kidsPlaying.add(kid2);
                visit = new Visit();
                visit.setPlayground(this);
                visit.setKid(kid2);
                visit.setStart(true);
                visit.setTime(new Date());
                visitRepository.save(visit);
            } else {
                kidsWaiting.add(0, kidsPlaying.get(0));
                visit = new Visit();
                visit.setPlayground(this);
                visit.setKid(kid);
                visit.setStart(false);
                visit.setTime(new Date());
                visitRepository.save(visit);
                kidsPlaying.clear();
            }
        } else {
            throw new KidNotFoundException("No kid " + kid + " in playground: " + this);
        }
    }

    @Override
    public int calculatePlaygroundUtilization() {
        return maxKidsPlaying == kidsPlaying.size() ? 100 : 0;
    }
}
