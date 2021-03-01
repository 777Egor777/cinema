package ru.job4j.cinema.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Хранилище покупателей,
 * использующее БД PostgreSQL.
 *
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 23.01.2021
 */
public class PsqlUserStore implements UserStore {
    private static final Logger LOG = LoggerFactory.getLogger(PsqlHallStore.class.getName());
    private final static int MIN_IDLE_COUNT = 5;
    private final static int MAX_IDLE_COUNT = 10;
    private final static int MAX_OPEN_PS_COUNT = 100;
    private final static String TABLE_NAME = "visitor";
    private final BasicDataSource pool = new BasicDataSource();

    private PsqlUserStore() {
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
    }

    private final static class Holder {
        private final static PsqlUserStore INSTANCE = new PsqlUserStore();
    }

    public static PsqlUserStore instOf() {
        return Holder.INSTANCE;
    }

    @Override
    public void add(String phone, String fio, List<Integer> placeIds) {
        phone = MemUserStore.parsePhone(phone);
        List<Integer> currentTickets =  getTicketsByPhone(phone);
        currentTickets.addAll(placeIds);
        setTicketsByPhone(phone, currentTickets);
    }

    private List<Integer> getTicketsByPhone(String phone) {
        List<Integer> result = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select tickets from visitor where phone=?;")) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Array array = rs.getArray(1);
                    System.out.println(array.getBaseTypeName());
                    Integer[] ar = (Integer[]) array.getArray();
                    result = Arrays.asList(ar);
                }
            }
        } catch (Exception ex) {
            LOG.error("Exception when extractong tickets by phone", ex);
        }
        return result;
    }

    private void setTicketsByPhone(String phone, List<Integer> tickets) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("update visitor set tickets=? where phone=?;")) {
            ps.setArray(1, cn.createArrayOf("int4", tickets.toArray(new Integer[0])));
            ps.setString(2, phone);
            ps.execute();
        } catch (Exception ex) {
            LOG.error("Exception when inserting tickets by phone", ex);
        }
    }
}
