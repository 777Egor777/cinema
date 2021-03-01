package ru.job4j.cinema.model;

import net.jcip.annotations.Immutable;

import java.util.Objects;

/**
 * Модель данных "место в кинотеатре".
 * Реализована в неизменяемой
 * (Immutable) форме, потому что
 * реальный онлайн-кинотеатр
 * должен будет работать в
 * многопоточной среде.
 *
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
@Immutable
public final class Place implements Comparable<Place> {
    private final int id;

    /**
     * Флаг, занято ли место.
     */
    private final boolean isFilled;

    /**
     * Номер ряда.
     */
    private final int row;

    /**
     * Номер места.
     */
    private final int col;

    public Place(int id, int row, int col, boolean isFilled) {
        this.id = id;
        this.isFilled = isFilled;
        this.row = row;
        this.col = col;
    }

    public int getId() {
        return id;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Place setFilled(boolean isFilled) {
        return new Place(id, row, col, isFilled);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        return id == place.id
                && isFilled == place.isFilled;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isFilled);
    }

    @Override
    public String toString() {
        return "Place{"
                + "id=" + id
                + ", isFilled=" + isFilled
                + '}';
    }

    @Override
    public int compareTo(Place o) {
        return Integer.compare(id, o.id);
    }
}
