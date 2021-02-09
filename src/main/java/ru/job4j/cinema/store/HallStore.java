package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;

import java.util.List;
import java.util.Set;

public interface HallStore {
    int getNumOfRows();
    int getNumOfCols();
    List<Place> getPlaces();
    Set<Integer> getFilledIds();
    Place getById(int id);
    void fillPlaces(List<Integer> placesId);
}
