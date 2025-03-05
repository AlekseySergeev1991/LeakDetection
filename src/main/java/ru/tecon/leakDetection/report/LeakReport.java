package ru.tecon.leakDetection.report;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFColor;
import ru.tecon.leakDetection.ejb.LeakDetectionSB;
import ru.tecon.leakDetection.model.CoolantTable;
import ru.tecon.leakDetection.model.GvsTable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class LeakReport {

    public static SXSSFWorkbook createReport(int id, LocalDateTime timestampLDT, String interval, String user, String alg, int repType, LeakDetectionSB bean) throws IOException, DecoderException {
        SXSSFWorkbook wb;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timestamp = timestampLDT.format(formatter);
        if (alg.equals("gvs")) {

            wb = createGvsReport(id, timestamp, interval, user, repType, bean);
        } else {
            wb = createCoolantReport(id, timestamp, interval, user, repType, bean);
        }


        return wb;
    }

    public static SXSSFWorkbook createGvsReport(int id, String timestamp, String interval, String user, int repType, LeakDetectionSB bean) throws DecoderException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sh = wb.createSheet("Отчет");

        CellStyle headerStyle = setHeaderStyle(wb);
        CellStyle headerStyleNoBold = setHeaderStyleNoBold(wb);
        CellStyle nowStyle = setCellNow (wb);
        CellStyle tableHeaderStyle = setTableHeaderStyle(wb);
        CellStyle cellYStyle = setCellYStyle(wb);
        CellStyle cellRStyle = setCellRStyle(wb);
        CellStyle cellGStyle = setCellGStyle(wb);
        CellStyle cellGStyleItog = setCellGStyleItog (wb);

        SXSSFRow row_1 = sh.createRow(0);
        row_1.setHeight((short) 435);
        SXSSFCell cell_1_1 = row_1.createCell(0);
        cell_1_1.setCellValue("ПАО \"МОЭК\": АС \"ТЕКОН - Диспетчеризация\"");
        CellRangeAddress title = new CellRangeAddress(0, 0, 0, 7);
        sh.addMergedRegion(title);
        cell_1_1.setCellStyle(headerStyle);

        SXSSFRow row_2 = sh.createRow(1);
        row_2.setHeight((short) 435);
        SXSSFCell cell_2_1 = row_2.createCell(0);
        cell_2_1.setCellValue("Определение утечки ГВС");
        CellRangeAddress formName = new CellRangeAddress(1, 1, 0, 7);
        sh.addMergedRegion(formName);
        cell_2_1.setCellStyle(headerStyle);

        if (interval.equals("y")) {
            SXSSFRow row_3 = sh.createRow(2);
            row_3.setHeight((short) 435);
            SXSSFCell cell_3_1 = row_3.createCell(0);
            cell_3_1.setCellValue("Период: " + timestamp.substring(0, 4) + " год");
            CellRangeAddress objName = new CellRangeAddress(2, 2, 0, 7);
            sh.addMergedRegion(objName);
            cell_3_1.setCellStyle(headerStyleNoBold);
        } else {
            SXSSFRow row_3 = sh.createRow(2);
            row_3.setHeight((short) 435);
            SXSSFCell cell_3_1 = row_3.createCell(0);
            cell_3_1.setCellValue("Период: " + timestamp.substring(5, 7) + "/" + timestamp.substring(0, 4));
            CellRangeAddress objName = new CellRangeAddress(2, 2, 0, 7);
            sh.addMergedRegion(objName);
            cell_3_1.setCellStyle(headerStyleNoBold);
        }

        SXSSFRow row_4 = sh.createRow(3);
        row_4.setHeight((short) 435);
        SXSSFCell cell_4_1 = row_4.createCell(0);
        sh.setColumnWidth(0, 15 * 256);
        sh.setColumnWidth(1, 16 * 256);
        sh.setColumnWidth(2, 16 * 256);
        sh.setColumnWidth(3, 17 * 256);
        sh.setColumnWidth(4, 10 * 256);
        sh.setColumnWidth(5, 10 * 256);
        sh.setColumnWidth(6, 10 * 256);
        sh.setColumnWidth(7, 13 * 256);
        sh.setColumnWidth(8, 13 * 256);
        sh.setColumnWidth(9, 13 * 256);
        sh.setColumnWidth(10, 13 * 256);
        sh.setColumnWidth(11, 13 * 256);
        sh.setColumnWidth(12, 13 * 256);
        sh.setColumnWidth(13, 13 * 256);
        sh.setColumnWidth(14, 13 * 256);
        sh.setColumnWidth(15, 13 * 256);
        sh.setColumnWidth(16, 22 * 256);
        sh.setColumnWidth(17, 22 * 256);
        sh.setColumnWidth(18, 22 * 256);

        if (repType == 0) {
            cell_4_1.setCellValue("по " + bean.getStructRep(id));
        } else {
            cell_4_1.setCellValue(bean.getStructRep(id));
        }
        CellRangeAddress repName = new CellRangeAddress(3, 3, 0, 7);
        sh.addMergedRegion(repName);
        cell_4_1.setCellStyle(headerStyleNoBold);

        createNow(sh, nowStyle);

        // готовим шапку таблицы
        SXSSFRow row_7 = sh.createRow(6);
        SXSSFCell cell_7_1 = row_7.createCell(0);
        cell_7_1.setCellStyle(tableHeaderStyle);
        cell_7_1.setCellValue("Филиал");

        SXSSFCell cell_7_2 = row_7.createCell(1);
        cell_7_2.setCellStyle(tableHeaderStyle);
        cell_7_2.setCellValue("Предприятие");

        SXSSFCell cell_7_3 = row_7.createCell(2);
        cell_7_3.setCellStyle(tableHeaderStyle);
        cell_7_3.setCellValue("ЦТП");

        SXSSFCell cell_7_4 = row_7.createCell(3);
        cell_7_4.setCellStyle(tableHeaderStyle);
        cell_7_4.setCellValue("Тип алгоритма");

        SXSSFCell cell_7_5 = row_7.createCell(4);
        cell_7_5.setCellStyle(tableHeaderStyle);
        cell_7_5.setCellValue("Дата");

        SXSSFCell cell_7_6 = row_7.createCell(5);
        cell_7_6.setCellStyle(tableHeaderStyle);
        cell_7_6.setCellValue("Тхв, ℃");

        SXSSFCell cell_7_7 = row_7.createCell(6);
        cell_7_7.setCellStyle(tableHeaderStyle);
        cell_7_7.setCellValue("Т7, ℃");

        SXSSFCell cell_7_8 = row_7.createCell(7);
        cell_7_8.setCellStyle(tableHeaderStyle);
        cell_7_8.setCellValue("Vхвнагвcф, м.куб/сут.");

        SXSSFCell cell_7_9 = row_7.createCell(8);
        cell_7_9.setCellStyle(tableHeaderStyle);
        cell_7_9.setCellValue("V7ф, м.куб/сут.");

        SXSSFCell cell_7_10 = row_7.createCell(9);
        cell_7_10.setCellStyle(tableHeaderStyle);
        cell_7_10.setCellValue("V13ф, м.куб/сут.");

        SXSSFCell cell_7_11 = row_7.createCell(10);
        cell_7_11.setCellStyle(tableHeaderStyle);
        cell_7_11.setCellValue("Qф, Гкал/сут.");

        SXSSFCell cell_7_12 = row_7.createCell(11);
        cell_7_12.setCellStyle(tableHeaderStyle);
        cell_7_12.setCellValue("ΔVф, м.куб/сут.");

        SXSSFCell cell_7_13 = row_7.createCell(12);
        cell_7_13.setCellStyle(tableHeaderStyle);
        cell_7_13.setCellValue("ΔVср, м.куб/сут.");

        SXSSFCell cell_7_14 = row_7.createCell(13);
        cell_7_14.setCellStyle(tableHeaderStyle);
        cell_7_14.setCellValue("Qср, Гкал/сут.");

        SXSSFCell cell_7_15 = row_7.createCell(14);
        cell_7_15.setCellStyle(tableHeaderStyle);
        cell_7_15.setCellValue("ΔV, м.куб/сут.");

        SXSSFCell cell_7_16 = row_7.createCell(15);
        cell_7_16.setCellStyle(tableHeaderStyle);
        cell_7_16.setCellValue("ΔQ, Гкал/сут.");

        SXSSFCell cell_7_17 = row_7.createCell(16);
        cell_7_17.setCellStyle(tableHeaderStyle);
        cell_7_17.setCellValue("Эффект по теплу, тыс.руб.");

        SXSSFCell cell_7_18 = row_7.createCell(17);
        cell_7_18.setCellStyle(tableHeaderStyle);
        cell_7_18.setCellValue("Эффект по воде, тыс.руб.");

        SXSSFCell cell_7_19 = row_7.createCell(18);
        cell_7_19.setCellStyle(tableHeaderStyle);
        cell_7_19.setCellValue("Суммарный эффект, тыс.руб.");

        //с шапкой таблицы покончено, начинаем заполнять тело отчета
        List<GvsTable> objects = bean.getGvsTable(id, timestamp, interval, user);

        int begRow = 7;
        GvsTable itog = objects.get(objects.size()-1);
        objects.remove(objects.size()-1);

        if (!objects.isEmpty()) {
            for (GvsTable object : objects) {
                SXSSFRow row = sh.createRow(begRow);

                CellStyle cellStyle;
                if (object.getColor().equals("g")) {
                    cellStyle = cellGStyle;
                } else if (object.getColor().equals("y")){
                    cellStyle = cellYStyle;
                } else {
                    cellStyle = cellRStyle;
                }


                SXSSFCell cell_1 = row.createCell(0);
                cell_1.setCellValue(object.getFilial());
                cell_1.setCellStyle(cellStyle);

                SXSSFCell cell_2 = row.createCell(1);
                cell_2.setCellValue(object.getPred());
                cell_2.setCellStyle(cellStyle);

                SXSSFCell cell_3 = row.createCell(2);
                cell_3.setCellValue(object.getChp());
                cell_3.setCellStyle(cellStyle);

                SXSSFCell cell_4 = row.createCell(3);
                cell_4.setCellValue(object.getAlgoritm());
                cell_4.setCellStyle(cellStyle);

                SXSSFCell cell_5 = row.createCell(4);
                cell_5.setCellValue(object.getDate());
                cell_5.setCellStyle(cellStyle);

                SXSSFCell cell_7 = row.createCell(5);
                cell_7.setCellValue(object.getThv());
                cell_7.setCellStyle(cellStyle);

                SXSSFCell cell_6 = row.createCell(6);
                cell_6.setCellValue(object.getT7());
                cell_6.setCellStyle(cellStyle);

                SXSSFCell cell_8 = row.createCell(7);
                cell_8.setCellValue(object.getGhvnagvsf());
                cell_8.setCellStyle(cellStyle);

                SXSSFCell cell_9 = row.createCell(8);
                cell_9.setCellValue(object.getG7f());
                cell_9.setCellStyle(cellStyle);

                SXSSFCell cell_10 = row.createCell(9);
                cell_10.setCellValue(object.getG13f());
                cell_10.setCellStyle(cellStyle);

                SXSSFCell cell_11 = row.createCell(10);
                cell_11.setCellValue(object.getQf());
                cell_11.setCellStyle(cellStyle);

                SXSSFCell cell_12 = row.createCell(11);
                cell_12.setCellValue(object.getDgf());
                cell_12.setCellStyle(cellStyle);

                SXSSFCell cell_13 = row.createCell(12);
                cell_13.setCellValue(object.getDgavg());
                cell_13.setCellStyle(cellStyle);

                SXSSFCell cell_14 = row.createCell(13);
                cell_14.setCellValue(object.getQavg());
                cell_14.setCellStyle(cellStyle);

                SXSSFCell cell_15 = row.createCell(14);
                cell_15.setCellValue(object.getDg());
                cell_15.setCellStyle(cellStyle);

                SXSSFCell cell_16 = row.createCell(15);
                cell_16.setCellValue(object.getDq());
                cell_16.setCellStyle(cellStyle);

                SXSSFCell cell_17 = row.createCell(16);
                cell_17.setCellValue(object.getHeat_eff());
                cell_17.setCellStyle(cellStyle);

                SXSSFCell cell_18 = row.createCell(17);
                cell_18.setCellValue(object.getWater_eff());
                cell_18.setCellStyle(cellStyle);

                SXSSFCell cell_19 = row.createCell(18);
                cell_19.setCellValue(object.getSum_eff());
                cell_19.setCellStyle(cellStyle);

                begRow ++;
            }
            SXSSFRow row = sh.createRow(begRow);
            SXSSFCell cell_1 = row.createCell(0);
            cell_1.setCellValue(itog.getFilial());
            cell_1.setCellStyle(cellGStyleItog);

            SXSSFCell cell_16 = row.createCell(16);
            cell_16.setCellValue(itog.getHeat_eff());
            cell_16.setCellStyle(cellGStyle);

            SXSSFCell cell_17 = row.createCell(17);
            cell_17.setCellValue(itog.getWater_eff());
            cell_17.setCellStyle(cellGStyle);

            SXSSFCell cell_18 = row.createCell(18);
            cell_18.setCellValue(itog.getSum_eff());
            cell_18.setCellStyle(cellGStyle);

            CellRangeAddress itogMerge = new CellRangeAddress(begRow, begRow, 0, 15);
            sh.addMergedRegion(itogMerge);
            RegionUtil.setBorderBottom(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderTop(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderLeft(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderRight(BorderStyle.THIN, itogMerge, sh);
        } else {
            SXSSFRow row = sh.createRow(begRow);
            SXSSFCell cell_1 = row.createCell(0);
            cell_1.setCellValue("Данные отсутствуют");
            cell_1.setCellStyle(nowStyle);
            CellRangeAddress itogMerge = new CellRangeAddress(begRow, begRow, 0, 3);
            sh.addMergedRegion(itogMerge);
        }

        return wb;
    }

    public static SXSSFWorkbook createCoolantReport(int id, String timestamp, String interval, String user, int repType, LeakDetectionSB bean) throws DecoderException {
        SXSSFWorkbook wb = new SXSSFWorkbook();
        SXSSFSheet sh = wb.createSheet("Отчет");

        CellStyle headerStyle = setHeaderStyle(wb);
        CellStyle headerStyleNoBold = setHeaderStyleNoBold(wb);
        CellStyle nowStyle = setCellNow (wb);
        CellStyle tableHeaderStyle = setTableHeaderStyle(wb);
        CellStyle cellYStyle = setCellYStyle(wb);
        CellStyle cellRStyle = setCellRStyle(wb);
        CellStyle cellGStyle = setCellGStyle(wb);
        CellStyle cellGStyleItog = setCellGStyleItog (wb);


        SXSSFRow row_1 = sh.createRow(0);
        row_1.setHeight((short) 435);
        SXSSFCell cell_1_1 = row_1.createCell(0);
        cell_1_1.setCellValue("ПАО \"МОЭК\": АС \"ТЕКОН - Диспетчеризация\"");
        CellRangeAddress title = new CellRangeAddress(0, 0, 0, 7);
        sh.addMergedRegion(title);
        cell_1_1.setCellStyle(headerStyle);

        SXSSFRow row_2 = sh.createRow(1);
        row_2.setHeight((short) 435);
        SXSSFCell cell_2_1 = row_2.createCell(0);
        cell_2_1.setCellValue("Определение утечки теплоносителя");
        CellRangeAddress formName = new CellRangeAddress(1, 1, 0, 7);
        sh.addMergedRegion(formName);
        cell_2_1.setCellStyle(headerStyle);

        if (interval.equals("y")) {
            SXSSFRow row_3 = sh.createRow(2);
            row_3.setHeight((short) 435);
            SXSSFCell cell_3_1 = row_3.createCell(0);
            cell_3_1.setCellValue("Период: " + timestamp.substring(0, 4) + " год");
            CellRangeAddress objName = new CellRangeAddress(2, 2, 0, 7);
            sh.addMergedRegion(objName);
            cell_3_1.setCellStyle(headerStyleNoBold);
        } else {
            SXSSFRow row_3 = sh.createRow(2);
            row_3.setHeight((short) 435);
            SXSSFCell cell_3_1 = row_3.createCell(0);
            cell_3_1.setCellValue("Период: " + timestamp.substring(5, 7) + "/" + timestamp.substring(0, 4));
            CellRangeAddress objName = new CellRangeAddress(2, 2, 0, 7);
            sh.addMergedRegion(objName);
            cell_3_1.setCellStyle(headerStyleNoBold);
        }

        SXSSFRow row_4 = sh.createRow(3);
        row_4.setHeight((short) 435);
        SXSSFCell cell_4_1 = row_4.createCell(0);
        sh.setColumnWidth(0, 15 * 256);
        sh.setColumnWidth(1, 16 * 256);
        sh.setColumnWidth(2, 16 * 256);
        sh.setColumnWidth(3, 17 * 256);
        sh.setColumnWidth(4, 10 * 256);
        sh.setColumnWidth(5, 10 * 256);
        sh.setColumnWidth(6, 10 * 256);
        sh.setColumnWidth(7, 10 * 256);
        sh.setColumnWidth(8, 13 * 256);
        sh.setColumnWidth(9, 13 * 256);
        sh.setColumnWidth(10, 13 * 256);
        sh.setColumnWidth(11, 13 * 256);
        sh.setColumnWidth(12, 13 * 256);
        sh.setColumnWidth(13, 13 * 256);
        sh.setColumnWidth(14, 13 * 256);
        sh.setColumnWidth(15, 22 * 256);
        sh.setColumnWidth(16, 22 * 256);
        sh.setColumnWidth(17, 22 * 256);

        if (repType == 0) {
            cell_4_1.setCellValue("по " + bean.getStructRep(id));
        } else {
            cell_4_1.setCellValue(bean.getStructRep(id));
        }
        CellRangeAddress repName = new CellRangeAddress(3, 3, 0, 7);
        sh.addMergedRegion(repName);
        cell_4_1.setCellStyle(headerStyleNoBold);

        createNow(sh, nowStyle);

        // готовим шапку таблицы
        SXSSFRow row_7 = sh.createRow(6);
        SXSSFCell cell_7_1 = row_7.createCell(0);
        cell_7_1.setCellStyle(tableHeaderStyle);
        cell_7_1.setCellValue("Филиал");

        SXSSFCell cell_7_2 = row_7.createCell(1);
        cell_7_2.setCellStyle(tableHeaderStyle);
        cell_7_2.setCellValue("Предприятие");

        SXSSFCell cell_7_3 = row_7.createCell(2);
        cell_7_3.setCellStyle(tableHeaderStyle);
        cell_7_3.setCellValue("ЦТП");

        SXSSFCell cell_7_4 = row_7.createCell(3);
        cell_7_4.setCellStyle(tableHeaderStyle);
        cell_7_4.setCellValue("Тип алгоритма");

        SXSSFCell cell_7_5 = row_7.createCell(4);
        cell_7_5.setCellStyle(tableHeaderStyle);
        cell_7_5.setCellValue("Дата");

        SXSSFCell cell_7_6 = row_7.createCell(5);
        cell_7_6.setCellStyle(tableHeaderStyle);
        cell_7_6.setCellValue("Тхв, ℃");

        SXSSFCell cell_7_7 = row_7.createCell(6);
        cell_7_7.setCellStyle(tableHeaderStyle);
        cell_7_7.setCellValue("Т3, ℃");

        SXSSFCell cell_7_8 = row_7.createCell(7);
        cell_7_8.setCellStyle(tableHeaderStyle);
        cell_7_8.setCellValue("Т4, ℃");

        SXSSFCell cell_7_9 = row_7.createCell(8);
        cell_7_9.setCellStyle(tableHeaderStyle);
        cell_7_9.setCellValue("V1, м.куб/сут.");

        SXSSFCell cell_7_10 = row_7.createCell(9);
        cell_7_10.setCellStyle(tableHeaderStyle);
        cell_7_10.setCellValue("V2, м.куб/сут.");

        SXSSFCell cell_7_11 = row_7.createCell(10);
        cell_7_11.setCellStyle(tableHeaderStyle);
        cell_7_11.setCellValue("V3, м.куб/сут.");

        SXSSFCell cell_7_12 = row_7.createCell(11);
        cell_7_12.setCellStyle(tableHeaderStyle);
        cell_7_12.setCellValue("V4, м.куб/сут.");

        SXSSFCell cell_7_13 = row_7.createCell(12);
        cell_7_13.setCellStyle(tableHeaderStyle);
        cell_7_13.setCellValue("Vп, м.куб/сут.");

        SXSSFCell cell_7_14 = row_7.createCell(13);
        cell_7_14.setCellStyle(tableHeaderStyle);
        cell_7_14.setCellValue("Vср, м.куб/сут.");

        SXSSFCell cell_7_15 = row_7.createCell(14);
        cell_7_15.setCellStyle(tableHeaderStyle);
        cell_7_15.setCellValue("ΔV, м.куб/сут.");

        SXSSFCell cell_7_16 = row_7.createCell(15);
        cell_7_16.setCellStyle(tableHeaderStyle);
        cell_7_16.setCellValue("Эффект по теплоносителю, тыс.руб.");

        SXSSFCell cell_7_17 = row_7.createCell(16);
        cell_7_17.setCellStyle(tableHeaderStyle);
        cell_7_17.setCellValue("Эффект по теплу, тыс.руб.");

        SXSSFCell cell_7_18 = row_7.createCell(17);
        cell_7_18.setCellStyle(tableHeaderStyle);
        cell_7_18.setCellValue("Суммарный эффект, тыс.руб.");

        //с шапкой таблицы покончено, начинаем заполнять тело отчета
        List<CoolantTable> objects = bean.getCoolantTable(id, timestamp, interval, user);
        CoolantTable itog = objects.get(objects.size()-1);
        objects.remove(objects.size()-1);

        int begRow = 7;

        if (!objects.isEmpty()) {
            for (CoolantTable object : objects) {
                SXSSFRow row = sh.createRow(begRow);
                CellStyle cellStyle;
                if (object.getColor().equals("g")) {
                    cellStyle = cellGStyle;
                } else if (object.getColor().equals("y")){
                    cellStyle = cellYStyle;
                } else {
                    cellStyle = cellRStyle;
                }

                SXSSFCell cell_1 = row.createCell(0);
                cell_1.setCellValue(object.getFilial());
                cell_1.setCellStyle(cellStyle);

                SXSSFCell cell_2 = row.createCell(1);
                cell_2.setCellValue(object.getPred());
                cell_2.setCellStyle(cellStyle);

                SXSSFCell cell_3 = row.createCell(2);
                cell_3.setCellValue(object.getChp());
                cell_3.setCellStyle(cellStyle);

                SXSSFCell cell_4 = row.createCell(3);
                cell_4.setCellValue(object.getAlgoritm());
                cell_4.setCellStyle(cellStyle);

                SXSSFCell cell_5 = row.createCell(4);
                cell_5.setCellValue(object.getDate());
                cell_5.setCellStyle(cellStyle);

                SXSSFCell cell_6 = row.createCell(5);
                cell_6.setCellValue(object.getThv());
                cell_6.setCellStyle(cellStyle);

                SXSSFCell cell_7 = row.createCell(6);
                cell_7.setCellValue(object.getT3());
                cell_7.setCellStyle(cellStyle);

                SXSSFCell cell_8 = row.createCell(7);
                cell_8.setCellValue(object.getT4());
                cell_8.setCellStyle(cellStyle);

                SXSSFCell cell_9 = row.createCell(8);
                cell_9.setCellValue(object.getG1());
                cell_9.setCellStyle(cellStyle);

                SXSSFCell cell_10 = row.createCell(9);
                cell_10.setCellValue(object.getG2());
                cell_10.setCellStyle(cellStyle);

                SXSSFCell cell_11 = row.createCell(10);
                cell_11.setCellValue(object.getG3());
                cell_11.setCellStyle(cellStyle);

                SXSSFCell cell_12 = row.createCell(11);
                cell_12.setCellValue(object.getG4());
                cell_12.setCellStyle(cellStyle);

                SXSSFCell cell_13 = row.createCell(12);
                cell_13.setCellValue(object.getGp());
                cell_13.setCellStyle(cellStyle);

                SXSSFCell cell_14 = row.createCell(13);
                cell_14.setCellValue(object.getGsr());
                cell_14.setCellStyle(cellStyle);

                SXSSFCell cell_15 = row.createCell(14);
                cell_15.setCellValue(object.getDg());
                cell_15.setCellStyle(cellStyle);

                SXSSFCell cell_16 = row.createCell(15);
                cell_16.setCellValue(object.getCoolant_eff());
                cell_16.setCellStyle(cellStyle);

                SXSSFCell cell_17 = row.createCell(16);
                cell_17.setCellValue(object.getHeat_eff());
                cell_17.setCellStyle(cellStyle);

                SXSSFCell cell_18 = row.createCell(17);
                cell_18.setCellValue(object.getSum_eff());
                cell_18.setCellStyle(cellStyle);

                begRow ++;
            }

            SXSSFRow row = sh.createRow(begRow);
            SXSSFCell cell_1 = row.createCell(0);
            cell_1.setCellValue(itog.getFilial());
            cell_1.setCellStyle(cellGStyleItog);

            SXSSFCell cell_16 = row.createCell(15);
            cell_16.setCellValue(itog.getCoolant_eff());
            cell_16.setCellStyle(cellGStyle);

            SXSSFCell cell_17 = row.createCell(16);
            cell_17.setCellValue(itog.getHeat_eff());
            cell_17.setCellStyle(cellGStyle);

            SXSSFCell cell_18 = row.createCell(17);
            cell_18.setCellValue(itog.getSum_eff());
            cell_18.setCellStyle(cellGStyle);

            CellRangeAddress itogMerge = new CellRangeAddress(begRow, begRow, 0, 14);
            sh.addMergedRegion(itogMerge);
            RegionUtil.setBorderBottom(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderTop(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderLeft(BorderStyle.THIN, itogMerge, sh);
            RegionUtil.setBorderRight(BorderStyle.THIN, itogMerge, sh);
        } else {
            SXSSFRow row = sh.createRow(begRow);
            SXSSFCell cell_1 = row.createCell(0);
            cell_1.setCellValue("Данные отсутствуют");
            cell_1.setCellStyle(nowStyle);
            CellRangeAddress itogMerge = new CellRangeAddress(begRow, begRow, 0, 3);
            sh.addMergedRegion(itogMerge);
        }
        return wb;
    }

    private static void createNow(SXSSFSheet sh, CellStyle nowStyle) {
        String now = new SimpleDateFormat("dd.MM.yyyy HH:mm").format(new Date());
        SXSSFRow row_5 = sh.createRow(4);
        row_5.setHeight((short) 435);
        SXSSFCell cell_5_1 = row_5.createCell(0);
        cell_5_1.setCellStyle(nowStyle);
        cell_5_1.setCellValue("Отчет сформирован  " + now);
        CellRangeAddress nowDone = new CellRangeAddress(4, 4, 0, 7);
        sh.addMergedRegion(nowDone);
    }

    ///////////////////////////////////////////  Определение стилей тут

    //  Стиль заголовка жирный
    private static CellStyle setHeaderStyle(SXSSFWorkbook p_wb) {

        CellStyle style = p_wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);

        Font headerFont = p_wb.createFont();
        headerFont.setBold(true);
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeightInPoints((short) 16);

        style.setFont(headerFont);

        return style;
    }

    //  Стиль заголовка не жирный
    private static CellStyle setHeaderStyleNoBold(SXSSFWorkbook p_wb) {

        CellStyle style = p_wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setWrapText(false);

        Font headerFont = p_wb.createFont();
        headerFont.setBold(false);
        headerFont.setFontName("Times New Roman");
        headerFont.setFontHeightInPoints((short) 16);

        style.setFont(headerFont);

        return style;
    }

    //стиль для даты создания отчета
    private static CellStyle setCellNow(SXSSFWorkbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);


        Font nowFont = wb.createFont();
        nowFont.setBold(false);
        nowFont.setFontName("Times New Roman");
        nowFont.setFontHeightInPoints((short) 12);

        style.setFont(nowFont);

        return style;
    }

    //  Стиль шапки таблицы
    private static CellStyle setTableHeaderStyle(SXSSFWorkbook p_wb) {
        CellStyle style = p_wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        style.setBorderTop(BorderStyle.THICK);
        style.setBorderLeft(BorderStyle.THICK);
        style.setBorderRight(BorderStyle.THICK);
        style.setBorderBottom(BorderStyle.THICK);

        Font tableHeaderFont = p_wb.createFont();

        tableHeaderFont.setBold(true);
        tableHeaderFont.setFontName("Times New Roman");
        tableHeaderFont.setFontHeightInPoints((short) 12);

        style.setFont(tableHeaderFont);

        return style;
    }

    private static CellStyle setCellRStyle(SXSSFWorkbook p_wb) throws DecoderException {
        CellStyle style = setCellTemplate(p_wb);
        String rgbS = "ffc7ce";
        byte [] rgbB = Hex.decodeHex(rgbS);
        XSSFColor color = new XSSFColor(rgbB, null);
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(false);
        return style;
    }

    private static CellStyle setCellGStyle(SXSSFWorkbook p_wb) throws DecoderException {
        CellStyle style = setCellTemplate(p_wb);
        String rgbS = "BDF8AE";
        byte [] rgbB = Hex.decodeHex(rgbS);
        XSSFColor color = new XSSFColor(rgbB, null);
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
        return style;
    }

    private static CellStyle setCellYStyle(SXSSFWorkbook p_wb) throws DecoderException {
        CellStyle style = setCellTemplate(p_wb);
        String rgbS = "FFFDB8";
        byte [] rgbB = Hex.decodeHex(rgbS);
        XSSFColor color = new XSSFColor(rgbB, null);
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
        return style;
    }

    private static CellStyle setCellTemplate(SXSSFWorkbook p_wb) {
        CellStyle style = p_wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        Font cellNoBoldFont = p_wb.createFont();

        cellNoBoldFont.setBold(false);
        cellNoBoldFont.setFontName("Times New Roman");
        cellNoBoldFont.setFontHeightInPoints((short) 11);

        style.setFont(cellNoBoldFont);

        return style;
    }

    private static CellStyle setCellGStyleItog(SXSSFWorkbook p_wb) throws DecoderException {
        CellStyle style = p_wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);

        Font cellNoBoldFont = p_wb.createFont();

        cellNoBoldFont.setBold(false);
        cellNoBoldFont.setFontName("Times New Roman");
        cellNoBoldFont.setFontHeightInPoints((short) 11);

        style.setFont(cellNoBoldFont);
        String rgbS = "BDF8AE";
        byte [] rgbB = Hex.decodeHex(rgbS);
        XSSFColor color = new XSSFColor(rgbB, null);
        style.setFillForegroundColor(color);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
        return style;
    }
}
