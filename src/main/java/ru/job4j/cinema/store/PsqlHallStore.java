package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.cinema.model.Place;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 23.01.2021
 */
public class PsqlHallStore implements HallStore {
    private final int numOfRows = 3;
    private final int numOfCols = 3;
    private static final Logger LOG = LoggerFactory.getLogger(PsqlHallStore.class.getName());
    private final static int MIN_IDLE_COUNT = 5;
    private final static int MAX_IDLE_COUNT = 10;
    private final static int MAX_OPEN_PS_COUNT = 100;
    private final static String TABLE_NAME = "hall";
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlHallStore() {
        Properties cfg = new Properties();
        try (FileReader reader = new FileReader("db.properties")) {
            cfg.load(reader);
        } catch (IOException e) {
            LOG.error("Exception when loading db.properties cfg", e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (ClassNotFoundException e) {
            LOG.error("Exception when registering JDBC driver", e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(MIN_IDLE_COUNT);
        pool.setMaxIdle(MAX_IDLE_COUNT);
        pool.setMaxOpenPreparedStatements(MAX_OPEN_PS_COUNT);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LOG.error("Exception when Thread.Sleep in PsqlHallStore constructor", e);
        }
        createTable();
        init();
    }

    private void createTable() {
        String query = "create table if not exists hall(id serial primary key, row int, col int, filled boolean);";
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)) {
            ps.execute();
        } catch (Exception ex) {
            LOG.error("Exception when creating table hall", ex);
        }
    }

    private void init() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from " + TABLE_NAME);
             ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) {
                for (int i = 1; i <= numOfRows; ++i) {
                    for (int j = 1; j <= numOfCols; ++j) {
                        try (PreparedStatement prep = cn.prepareStatement(
                                "insert into hall(row, col, filled) values(?, ?, ?);"
                        )) {
                            prep.setInt(1, i);
                            prep.setInt(2, j);
                            prep.setBoolean(3, false);
                            prep.executeUpdate();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception when init db", ex);
        }
    }

    private final static class Holder {
        private final static HallStore INSTANCE = new PsqlHallStore();
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
    public List<Place> getPlaces() {
        List<Place> places = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from " + TABLE_NAME);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                places.add(new Place(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getInt(3),
                        rs.getBoolean(4)
                ));
            }
        } catch (Exception ex) {
            LOG.error("Exception when extracting all places from db", ex);
        }
        places.sort(Place::compareTo);
        return places;
    }

    @Override
    public Set<Integer> getFilledIds() {
        Set<Integer> result = new HashSet<>();
        for (Place place : getPlaces()) {
            if (place.isFilled()) {
                result.add(place.getId());
            }
        }
        return result;
    }

    @Override
    public Place getById(int id) {
        Place place = null;
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from hall where id=?;")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    place = new Place(
                            rs.getInt(1),
                            rs.getInt(2),
                            rs.getInt(3),
                            rs.getBoolean(4)
                    );
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception when extracting by id", ex);
        }
        return place;
    }

    @Override
    public void fillPlaces(List<Integer> placesId) {
        StringJoiner joiner = new StringJoiner(" or ", "update hall set filled=true where ", ";");
        for (int id : placesId) {
            joiner.add("id=" + id);
        }
        String query = joiner.toString();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(query)) {
            ps.execute();
        } catch (Exception ex) {
            LOG.error("Exception when filling places to hall bd", ex);
        }
    }
}
