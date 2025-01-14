package ru.tecon.leakDetection.servlet;

import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.util.IOUtils;
import ru.tecon.leakDetection.ejb.LeakDetectionSB;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@WebServlet("/loadArchiveReport")
public class LeakReportArchiveServlet extends HttpServlet {


    @EJB
    private LeakDetectionSB bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));

        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                URLEncoder.encode(bean.getArchiveName(id), "UTF-8") + " " +
                URLEncoder.encode("архив", "UTF-8") +
                URLEncoder.encode(".xlsx", "UTF-8") + "\"");
        resp.setCharacterEncoding("UTF-8");

        try (OutputStream output = resp.getOutputStream()) {
            IOUtils.setByteArrayMaxOverride(1000000000);
            byte[] bytes = bean.getByteReport(id);
            InputStream input = new ByteArrayInputStream(bytes);
            IOUtils.copy(input, output);
            input.close();
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
