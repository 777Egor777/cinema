package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;

import java.util.List;
import java.util.Set;

/**
 * Интерфейс для хранилища
 * состояния кинозала.
 *
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public interface HallStore {
    int getNumOfRows();
    int getNumOfCols();
    List<Place> getPlaces();
    Set<Integer> getFilledIds();
    Place getById(int id);
    void fillPlaces(List<Integer> placesId);
}
