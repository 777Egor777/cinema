package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Before;
import org.junit.Test;
import ru.job4j.cinema.model.Place;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class PsqlHallStoreTest {
    private BasicDataSource pool = new BasicDataSource();

    private HallStore store;

    @Before
    public void setUp() {
        pool.setDriverClassName("org.hsqldb.jdbcDriver");
        pool.setUrl("jdbc:hsqldb:mem:tests;sql.syntax_pgs=true");
        pool.setUsername("sa");
        pool.setPassword("");
        pool.setMaxTotal(2);

        store = PsqlHallStore.instanceForTest(pool);
    }

    @Test
    public void whenGetPlacesThenSizeNine() {
        assertThat(store.getNumOfCols(), is(3));
        assertThat(store.getNumOfRows(), is(3));
        assertThat(store.getPlaces().size(), is(9));
    }

    @Test
    public void whenStartThenAllPlacesIsEmpty() {
        for (Place place : store.getPlaces()) {
            assertFalse(place.isFilled());
        }
    }

    @Test
    public void whenFillPlaces() {
        int id1 = 3;
        int id2 = 7;
        store.fillPlaces(List.of(id1, id2));
        assertTrue(store.getById(id1).isFilled());
        assertTrue(store.getById(id2).isFilled());
    }

}