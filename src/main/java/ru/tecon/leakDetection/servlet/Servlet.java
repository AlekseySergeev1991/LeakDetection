package ru.tecon.leakDetection.servlet;

import ru.tecon.leakDetection.ejb.CheckUserSB;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Сервлет обертка для запуска формы Выявление утечек
 *
 * @author Aleksey Sergeev
 */

@WebServlet("/leakDetection")
public class Servlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(Servlet.class.getName());

    @EJB
    private CheckUserSB bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String[]> parameterMap = req.getParameterMap();
        if (parameterMap.containsKey("sessionId")) {
            if (bean.checkSession(req.getParameter("sessionId"))) {
                req.getRequestDispatcher("/leakDetection.xhtml").forward(req, resp);
            } else {
                // Авторизуйтесь в системе
                logger.log(Level.WARNING, "authorization error");
                req.getRequestDispatcher("/error.html").forward(req, resp);
            }
        } else {
            // Не хватает параметров
            logger.log(Level.WARNING, "missing parameters");
            req.getRequestDispatcher("/error.html").forward(req, resp);
        }
    }
}