package ru.job4j.cinema.store;

import java.util.*;

/**
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public final class MemUserStore implements UserStore {
    Map<String, List<Integer>> phoneToPlaces = new HashMap<>();

    private MemUserStore() {
    }

    private final static class Holder {
        private final static UserStore INSTANCE = new MemUserStore();
    }

    public static UserStore instOf() {
        return Holder.INSTANCE;
    }

    public static String parsePhone(String phone) {
        StringJoiner joiner = new StringJoiner("");
        for (int i = 0; i < phone.length(); ++i) {
            char c = phone.charAt(i);
            if (c >= '0' && c <= '9') {
                joiner.add("" + c);
            }
        }
        String result = joiner.toString();
        if (result.charAt(0) == '7') {
            result = "8" + result.substring(1);
        }
        return result;
    }

    @Override
    public void add(String phone, String fio, List<Integer> placeIds) {
        phone = parsePhone(phone);
        phoneToPlaces.putIfAbsent(phone, new ArrayList<>());
        phoneToPlaces.get(phone).addAll(placeIds);
    }
}
