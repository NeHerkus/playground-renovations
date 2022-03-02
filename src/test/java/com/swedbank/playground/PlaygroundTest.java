package com.swedbank.playground;

import com.google.common.collect.ImmutableList;
import com.swedbank.playground.kid.Kid;
import com.swedbank.playground.playground.DoubleSwingsPlayground;
import com.swedbank.playground.playground.KidNotFoundException;
import com.swedbank.playground.playground.SlidePlayground;
import com.swedbank.playground.visit.Visit;
import com.swedbank.playground.visit.VisitRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PlaygroundTest {

    @Mock
    private VisitRepository visitRepository;

    @Test
    public void addKid() {
        SlidePlayground playground = new SlidePlayground(visitRepository);
        playground.setMaxKidsPlaying(1);
        Kid kid = new Kid("John Smith", 5);
        kid.setVIP(true);

        boolean playing = playground.addKid(kid);
        assertTrue(playing);
        assertEquals(ImmutableList.of(kid), playground.kidsPlaying());

        final Visit visit = new Visit();
        visit.setPlayground(playground);
        visit.setKid(kid);
        visit.setStart(true);

        verify(visitRepository).save(argThat(new ArgumentMatcher<Visit>() {
            @Override
            public boolean matches(Object argument) {
                Visit v = (Visit) argument;
                return visit.getPlayground().equals(v.getPlayground())
                        && visit.getKid().equals(v.getKid())
                        && visit.isStart() == v.isStart();
            }
        }));

        Kid k1 = new Kid("k1", 6);
        playing = playground.addKid(k1);
        assertFalse(playing);
        assertEquals(ImmutableList.of(k1), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));

        Kid k2 = new Kid("k2", 6);
        playing = playground.addKid(k2);
        assertFalse(playing);
        assertEquals(ImmutableList.of(k1, k2), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));

        Kid k3 = new Kid("k3", 6);
        playing = playground.addKid(k3);
        assertFalse(playing);
        assertEquals(ImmutableList.of(k1, k2, k3), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));

        Kid v1 = new Kid("v1", 6);
        v1.setVIP(true);
        playing = playground.addKid(v1);
        assertFalse(playing);
        assertEquals(ImmutableList.of(v1, k1, k2, k3), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));

        Kid k4 = new Kid("k4", 6);
        playground.addKid(k4);

        Kid v2 = new Kid("v2", 6);
        v2.setVIP(true);
        playing = playground.addKid(v2);
        assertFalse(playing);
        assertEquals(ImmutableList.of(v1, k1, k2, k3, v2, k4), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));

        Kid v3 = new Kid("v3", 6);
        v3.setVIP(true);
        playing = playground.addKid(v3);
        assertFalse(playing);
        assertEquals(ImmutableList.of(v1, k1, k2, k3, v2, k4, v3), playground.kidsWaiting());

        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    public void removeKid_kid_is_removed() {
        SlidePlayground playground = new SlidePlayground(visitRepository);
        playground.setMaxKidsPlaying(1);
        Kid kid = new Kid("John Smith", 5);

        boolean playing = playground.addKid(kid);
        assertTrue(playing);
        assertEquals(ImmutableList.of(kid), playground.kidsPlaying());

        playground.removeKid(kid);
        assertEquals(ImmutableList.of(), playground.kidsPlaying());

        final Visit visit = new Visit();
        visit.setPlayground(playground);
        visit.setKid(kid);
        visit.setStart(false);

        verify(visitRepository).save(argThat(new ArgumentMatcher<Visit>() {
            @Override
            public boolean matches(Object argument) {
                Visit v = (Visit) argument;
                return visit.getPlayground().equals(v.getPlayground())
                        && visit.getKid().equals(v.getKid())
                        && visit.isStart() == v.isStart();
            }
        }));

        playground.addKid(kid);

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playground.addKid(kid2);

        playground.removeKid(kid2);
        assertEquals(ImmutableList.of(), playground.kidsWaiting());

        playground.addKid(kid2);
        playground.removeKid(kid);

        assertEquals(ImmutableList.of(kid2), playground.kidsPlaying());
    }

    @Test(expected = KidNotFoundException.class)
    public void removeKid_kid_not_in_playground() {
        SlidePlayground playground = new SlidePlayground(visitRepository);
        playground.setMaxKidsPlaying(1);
        Kid kid = new Kid("John Smith", 5);

        boolean playing = playground.addKid(kid);
        assertTrue(playing);
        assertEquals(ImmutableList.of(kid), playground.kidsPlaying());

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playground.removeKid(kid2);
    }

    @Test
    public void calculatePlaygroundUtilization_slidePlayground() {
        SlidePlayground playground = new SlidePlayground(visitRepository);
        playground.setMaxKidsPlaying(3);
        Kid kid = new Kid("John Smith", 5);
        playground.addKid(kid);

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playground.addKid(kid2);
        assertEquals(67, playground.calculatePlaygroundUtilization());
    }

    @Test
    public void calculatePlaygroundUtilization_doubleSwingPlayground() {
        DoubleSwingsPlayground playground = new DoubleSwingsPlayground(visitRepository);
        Kid kid = new Kid("John Smith", 5);
        playground.addKid(kid);

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playground.addKid(kid2);
        assertEquals(100, playground.calculatePlaygroundUtilization());
    }

    @Test
    public void addKid_doubleSwingPlayground() {
        DoubleSwingsPlayground playground = new DoubleSwingsPlayground(visitRepository);
        Kid kid = new Kid("John Smith", 5);
        boolean playing = playground.addKid(kid);
        assertFalse(playing);
        assertEquals(ImmutableList.of(kid), playground.kidsWaiting());
        assertEquals(ImmutableList.of(), playground.kidsPlaying());

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playing = playground.addKid(kid2);
        assertTrue(playing);
        assertEquals(ImmutableList.of(), playground.kidsWaiting());
        assertEquals(ImmutableList.of(kid2, kid), playground.kidsPlaying());
    }

    @Test
    public void removeKid_doubleSwingPlayground() {
        DoubleSwingsPlayground playground = new DoubleSwingsPlayground(visitRepository);
        Kid kid = new Kid("John Smith", 5);
        boolean playing = playground.addKid(kid);
        assertFalse(playing);
        assertEquals(ImmutableList.of(kid), playground.kidsWaiting());
        assertEquals(ImmutableList.of(), playground.kidsPlaying());

        Kid kid2 = new Kid("Johnus Smithus", 6);
        playing = playground.addKid(kid2);
        assertTrue(playing);
        assertEquals(ImmutableList.of(), playground.kidsWaiting());

        playground.removeKid(kid2);
        assertEquals(ImmutableList.of(kid), playground.kidsWaiting());

        Kid kid3 = new Kid("Johnus Smithus", 6);
        playing = playground.addKid(kid3);
        assertTrue(playing);
        assertEquals(ImmutableList.of(), playground.kidsWaiting());
        assertEquals(ImmutableList.of(kid3, kid), playground.kidsPlaying());
    }

}
