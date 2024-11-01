package ru.tecon.leakDetection.ejb;

import ru.tecon.leakDetection.model.*;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Stateless bean для получения данных из база для формы выявление утечек
 *
 * @author Aleksey Sergeev
 */
@Stateless
@LocalBean
public class LeakDetectionSB {
    private static final String SELECT_STRUCT_TREE = "select * from eff_calc.filter_view(?,?,?)";
    private static final String SELECT_OBJ_TYPE_PROP = "select * from dsp_0032t.get_obj_type_props(1)";
    private static final String GET_CONST = "select * from eff_calc_002.const_view_coolant(?)";
    private static final String GET_COOLANT_TABLE_DATA = "select * from eff_calc_002.table_leak_detection_coolant(?, ?, ?, ?)";
    private static final String GET_GVS_TABLE_DATA = "select * from eff_calc_002.table_leak_detection_gvs(?, ?, ?, ?)";
    private static final String GET_STRUCT = "select * from eff_calc.text_struct(?, ?)";
    private static final String GET_ARCHIVE_TABLE = "select * from eff_calc.report_view('leak')";
    private static final String GET_ARCHIVE_REPORT = "select * from eff_calc.decode_xlsx(?)";
    private static final String GET_STRUCT_REP = "select * from eff_calc.text_struct_no_user(?)";
    private static final String GET_OBJ_NAME = "select * from eff_calc.get_name_from_id(?)";
    private static final String GET_ARCHIVE_NAME = "select * from eff_calc.get_inf_from_excel(?)";


    private Logger logger;

    @Resource(name = "jdbc/DataSource")
    private DataSource dsRw;

    @Resource(name = "jdbc/DataSourceR")
    private DataSource dsR;

    /**
     * @return дерево для формы
     */
    public List<StructTree> getTreeParam(String user, int filterId, String filter) {
        List<StructTree> result = new ArrayList<>();
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(SELECT_STRUCT_TREE)) {
            stm.setString(1, user);
            stm.setInt(2, filterId);
            stm.setString(3, filter);
            ResultSet res = stm.executeQuery();

            while (res.next()) {

                StructTree structTree = new StructTree(res.getString("id"),
                        res.getString("name"),
                        res.getString("parent"),
                        res.getInt("my_id"),
                        res.getString("my_type"),
                        res.getString("my_icon"));

                if (structTree.getMyIcon().equals("L")) {
                    structTree.setMyIcon("fa fa-link fa-rotate-90 linkIcon");
                } else {
                    structTree.setMyIcon("fa fa-cubes cubesIcon");
                }

                result.add(structTree);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return список свойств объета
     */
    public List<ObjProp> getProp() {
        List<ObjProp> result = new ArrayList<>();
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(SELECT_OBJ_TYPE_PROP)) {
            ResultSet res = stm.executeQuery();
            while (res.next()) {

                ObjProp objProp = new ObjProp(res.getInt("obj_prop_id"),
                        res.getString("obj_prop_name"));

                result.add(objProp);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return список констант
     */
    public List<Const> getConst(int id) {
        List<Const> result = new ArrayList<>();
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_CONST)) {
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            while (res.next()) {

                Const curConst = new Const(res.getInt("const_id"), res.getString("name_const"),
                        res.getString("value"), res.getString("private"));

                result.add(curConst);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return списикок значений для таблицы по утечке теплоносителя
     */
    public List<CoolantTable> getCoolantTable(int id, String strDate, String interval, String user) {
        List<CoolantTable> result = new ArrayList<>();
        if (strDate.length() < 16) {
            if (interval.equals("m")) {
                strDate = strDate + "-01 00:00";
            } else {
                strDate = strDate + "-01-01 00:00";
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(strDate, formatter);
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_COOLANT_TABLE_DATA)) {
            stm.setInt(1, id);
            stm.setTimestamp(2, Timestamp.valueOf(date));
            stm.setString(3, interval);
            stm.setString(4, user);
            ResultSet res = stm.executeQuery();
            while (res.next()) {

                CoolantTable coolantTable = new CoolantTable(res.getString("name_filial"),
                        res.getString("name_pred"), res.getString("name_chp"),
                        res.getString("name_algoritm"), res.getString("date_"),
                        res.getString("t3"), res.getString("thv"),res.getString("t4"),
                        res.getString("g1"), res.getString("g2"), res.getString("g3"),
                        res.getString("g4"), res.getString("gp"), res.getString("gsr"),
                        res.getString("dg"), res.getString("coolant_eff"), res.getString("heat_eff"),
                        res.getString("sum_eff"), res.getString("color"));

                result.add(coolantTable);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return списикок значений для таблице по утечке гвс
     */
    public List<GvsTable> getGvsTable(int id, String strDate, String interval, String user) {
        List<GvsTable> result = new ArrayList<>();
        if (strDate.length() < 16) {
            if (interval.equals("m")) {
                strDate = strDate + "-01 00:00";
            } else {
                strDate = strDate + "-01-01 00:00";
            }
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime date = LocalDateTime.parse(strDate, formatter);
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_GVS_TABLE_DATA)) {
            stm.setInt(1, id);
            stm.setTimestamp(2, Timestamp.valueOf(date));
            stm.setString(3, interval);
            stm.setString(4, user);
            ResultSet res = stm.executeQuery();
            while (res.next()) {
                GvsTable gvsTable = new GvsTable(res.getString("name_filial"),
                        res.getString("name_pred"), res.getString("name_chp"),
                        res.getString("name_algoritm"), res.getString("date_"),
                        res.getString("t7"), res.getString("thv"), res.getString("ghvnagvsf"),
                        res.getString("g7f"), res.getString("g13f"), res.getString("qf"),
                        res.getString("dgf"), res.getString("dgavg"), res.getString("qavg"),
                        res.getString("dg"), res.getString("dq"), res.getString("heat_eff"),
                        res.getString("water_eff"), res.getString("sum_eff"), res.getString("color"));

                result.add(gvsTable);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return адрес объекта
     */
    public String getStruct(String user, String id) {
        String result = "";
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_STRUCT)) {
            stm.setString(1, user);
            stm.setString(2, id);
            ResultSet res = stm.executeQuery();
            if (res.next()) {
                result = res.getString("text_struct");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return таблица с перечнем всех архивных отчетов
     */
    public List<ArchiveReport> getArchiveTable() {
        List<ArchiveReport> result = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM.yyyy");
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_ARCHIVE_TABLE)) {
            ResultSet res = stm.executeQuery();
            while (res.next()) {
                ArchiveReport archiveReport = new ArchiveReport(res.getInt("id"), res.getString("rep_type"),
                        res.getDate("report_time").toLocalDate().format(dtf));
                archiveReport.setRedirect("loadArchiveReport?id=" + archiveReport.getId() + "&amp;");
                result.add(archiveReport);
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    public byte[] getByteReport(int id) {
        byte[] result = new byte[0];
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_ARCHIVE_REPORT)) {
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            if (res.next()) {
                result = res.getBytes("decode_xlsx");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return структуру объекта
     */
    public String getStructRep(int id) {
        String result = "";
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_STRUCT_REP)) {
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            if (res.next()) {
                result = res.getString("text_struct_no_user");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return имя объекта
     */
    public String getObjName(int id) {
        String result = "";
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_OBJ_NAME)) {
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            if (res.next()) {
                result = res.getString("name");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }

    /**
     * @return имя архива
     */
    public String getArchiveName(int id) {
        String result = "";
        try (Connection connect = dsR.getConnection();
             PreparedStatement stm = connect.prepareStatement(GET_ARCHIVE_NAME)) {
            stm.setInt(1, id);
            ResultSet res = stm.executeQuery();
            if (res.next()) {
                result = res.getString("get_inf_from_excel");
                result = result.replaceAll("\"", "");
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "SQLException", e);
        }
        return result;
    }
}
