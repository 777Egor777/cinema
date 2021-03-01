package ru.job4j.cinema.servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.job4j.cinema.model.Place;
import ru.job4j.cinema.store.HallStore;
import ru.job4j.cinema.store.MemHallStore;
import ru.job4j.cinema.store.PsqlHallStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Сервлет - контроллер для страницы
 * выбора мест.
 *
 * @author Egor Geraskin(yegeraskin13@gmail.com)
 * @version 1.0
 * @since 22.01.2021
 */
public class HallServlet extends HttpServlet {

    /**
     * Сюда поступают запросы
     * c AJAX'а и актуальная
     * информация о зале
     * заполняется в динамическом
     * режиме.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Place> places = PsqlHallStore.instOf().getPlaces();
        JSONObject obj = new JSONObject();
        obj.put("rowsNum", PsqlHallStore.instOf().getNumOfRows());
        obj.put("colsNum", PsqlHallStore.instOf().getNumOfCols());
        JSONArray arr = new JSONArray();
        for (Place place : places) {
            JSONObject placeObj = new JSONObject();
            placeObj.put("id", "" + place.getId());
            placeObj.put("filled", "" + place.isFilled());
            arr.add(placeObj);
        }
        obj.put("places", arr);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.print(obj.toJSONString());
        writer.flush();
    }

    /**
     * Сюда поступает запрос с местами, выбранными пользователем.
     * Данные валидируются и перенаправляются на страницу
     * бронирования.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HallStore store = PsqlHallStore.instOf();
        List<Place> places = store.getPlaces();
        List<Integer> reservedPlaces = new ArrayList<>();
        Set<Integer> alreadyFilled = store.getFilledIds();
        for (Place place : places) {
            if (req.getParameter("place" + place.getId()) != null
            && !alreadyFilled.contains(place.getId())) {
                reservedPlaces.add(place.getId());
            }
        }
        req.setAttribute("reserved", reservedPlaces);
        RequestDispatcher disp = req.getRequestDispatcher("/payment.jsp");
        disp.forward(req, resp);
    }
}
