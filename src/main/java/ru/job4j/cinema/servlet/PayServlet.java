package ru.job4j.cinema.servlet;

import ru.job4j.cinema.store.MemHallStore;
import ru.job4j.cinema.store.MemUserStore;
import ru.job4j.cinema.store.PsqlHallStore;
import ru.job4j.cinema.store.PsqlUserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public class PayServlet extends HttpServlet {
    private List<Integer> parseReservedIdFromStr(String resStr) {
        String[] parts = resStr.split("x");
        List<Integer> result = new ArrayList<>();
        for (String part : parts) {
            result.add(Integer.parseInt(part.trim()));
        }
        return result;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String reservedString = req.getParameter("reserved");
        List<Integer> reservedIds = parseReservedIdFromStr(reservedString);
        String fio = req.getParameter("username");
        String phone = req.getParameter("phone");
        PsqlUserStore.instOf().add(phone, fio, reservedIds);
        PsqlHallStore.instOf().fillPlaces(reservedIds);
        resp.sendRedirect(req.getContextPath() + "/index.jsp");
    }
}
