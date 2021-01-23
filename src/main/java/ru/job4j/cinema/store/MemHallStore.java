package ru.job4j.cinema.store;

import ru.job4j.cinema.model.Place;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public final class MemHallStore implements HallStore {
    private final int numOfRows = 3;
    private final int numOfCols = 3;
    private final Place[] hall = new Place[numOfRows * numOfCols];

    private MemHallStore() {
        int i = 0;
        for (int row = 1; row <= numOfRows; ++row) {
            for (int col = 1; col <= numOfCols; ++col) {
                hall[i] = new Place(i, row, col, false);
                i++;
            }
        }
    }

    private final static class Holder {
        private final static HallStore INSTANCE = new MemHallStore();
    }

    public static HallStore instOf() {
        return Holder.INSTANCE;
    }

    @Override
    public int getNumOfRows() {
        return numOfRows;
    }

    @Override
    public int getNumOfCols() {
        return numOfCols;
    }

    @Override
    public Place[] getPlaces() {
        return hall;
    }

    @Override
    public Set<Integer> getFilledIds() {
        Set<Integer> result = new HashSet<>();
        for (Place place : hall) {
            if (place.isFilled()) {
                result.add(place.getId());
            }
        }
        return result;
    }

    @Override
    public Place getById(int id) {
        return hall[id];
    }

    @Override
    public void fillPlaces(List<Integer> placesId) {
        for (int placeId : placesId) {
            hall[placeId] = hall[placeId].setFilled(true);
        }
    }
}
