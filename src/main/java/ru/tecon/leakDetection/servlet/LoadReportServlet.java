package ru.tecon.leakDetection.servlet;

import ru.tecon.leakDetection.ejb.LeakDetectionSB;
import ru.tecon.leakDetection.report.LeakReport;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/loadReport")
public class LoadReportServlet extends HttpServlet {

    @EJB
    private LeakDetectionSB bean;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = Integer.parseInt(req.getParameter("id"));
        String timestamp = req.getParameter("timestamp");
        String interval = req.getParameter("interval");
        String user = req.getParameter("user");
        String alg = req.getParameter("alg");
        int repType = Integer.parseInt(req.getParameter("repType"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime timestampLDT = LocalDateTime.parse(timestamp, formatter);

        resp.setContentType("application/vnd.ms-excel; charset=UTF-8");

        if (repType == 0) {
            if (alg.equals("coolant")) {
                String struct = bean.getStructRep(id);
                struct = structCorrection(resp, struct);
                resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                        URLEncoder.encode("Определение", "UTF-8") + " " +
                        URLEncoder.encode("утечки", "UTF-8") + " " +
                        URLEncoder.encode("теплоносителя", "UTF-8") + " " +
                        URLEncoder.encode(struct, "UTF-8") +
                        URLEncoder.encode(".xlsx", "UTF-8") + "\"");
                resp.setCharacterEncoding("UTF-8");
            } else {
                String struct = bean.getStructRep(id);
                struct = structCorrection(resp, struct);
                resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                        URLEncoder.encode("Определение", "UTF-8") + " " +
                        URLEncoder.encode("утечки", "UTF-8") + " " +
                        URLEncoder.encode("ГВС", "UTF-8") + " " +
                        URLEncoder.encode(struct, "UTF-8") +
                        URLEncoder.encode(".xlsx", "UTF-8") + "\"");
                resp.setCharacterEncoding("UTF-8");
            }
        } else {
            if (alg.equals("coolant")) {
                String struct = bean.getObjName(id);
                struct = struct.replace('/', '_');
                struct = struct.replaceAll(" ", "");
                struct = struct.replaceFirst("\"", "_");
                struct = struct.replaceAll("\"", "");

                resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                        URLEncoder.encode("Определение", "UTF-8") + " " +
                        URLEncoder.encode("утечки", "UTF-8") + " " +
                        URLEncoder.encode("теплоносителя", "UTF-8") + " " +
                        URLEncoder.encode(struct, "UTF-8") +
                        URLEncoder.encode(".xlsx", "UTF-8") + "\"");
                resp.setCharacterEncoding("UTF-8");
            } else {
                String struct = bean.getObjName(id);
                struct = struct.replace('/', '_');
                struct = struct.replaceAll(" ", "");
                struct = struct.replaceFirst("\"", "_");
                struct = struct.replaceAll("\"", "");
                resp.setHeader("Content-Disposition", "attachment; filename=\"" +
                        URLEncoder.encode("Определение", "UTF-8") + " " +
                        URLEncoder.encode("утечки", "UTF-8") + " " +
                        URLEncoder.encode("ГВС", "UTF-8") + " " +
                        URLEncoder.encode(struct, "UTF-8") +
                        URLEncoder.encode(".xlsx", "UTF-8") + "\"");
                resp.setCharacterEncoding("UTF-8");
            }
        }

        try (OutputStream output = resp.getOutputStream()) {
            LeakReport.createReport(id, timestampLDT, interval, user, alg, repType, bean).write(output);
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String structCorrection(HttpServletResponse resp, String struct) throws UnsupportedEncodingException {
        struct = struct.replace('/', '_');
        struct = struct.replaceAll(" ", "");
        struct = struct.replaceFirst("\"", "_");
        struct = struct.replaceAll("\"", "");
        return struct;

    }

}
