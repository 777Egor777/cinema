package ru.job4j.cinema.store;

import java.util.List;

public interface UserStore {
    void add(String phone, String fio, List<Integer> placeIds);
}
