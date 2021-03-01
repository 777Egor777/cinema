package ru.job4j.cinema.store;

import java.util.List;

/**
 * Интерфейс для хранилища
 * покупателей.
 *
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public interface UserStore {
    void add(String phone, String fio, List<Integer> placeIds);
}
