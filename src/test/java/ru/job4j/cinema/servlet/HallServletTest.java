package ru.job4j.cinema.servlet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import ru.job4j.cinema.store.HallStore;
import ru.job4j.cinema.store.MemHallStore;
import ru.job4j.cinema.store.PsqlHallStore;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PsqlHallStore.class)
public class HallServletTest {
    @Test
    public void whenFillSomePlaces() {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        HttpServletResponse resp = Mockito.mock(HttpServletResponse.class);
        PowerMockito.mockStatic(PsqlHallStore.class);
        HallStore store = MemHallStore.instOf();
        Mockito.when(PsqlHallStore.instOf()).thenReturn(store);
        Mockito.when(req.getParameter("place1")).thenReturn("1");
        RequestDispatcher disp = Mockito.mock(RequestDispatcher.class);
        Mockito.when(req.getRequestDispatcher(eq("/payment.jsp"))).thenReturn(disp);
        try {
            new HallServlet().doPost(req, resp);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
        List<Integer> reserved = List.of(1);
        Mockito.verify(req).setAttribute(eq("reserved"), eq(reserved));
        try {
            Mockito.verify(disp).forward(eq(req), eq(resp));
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}