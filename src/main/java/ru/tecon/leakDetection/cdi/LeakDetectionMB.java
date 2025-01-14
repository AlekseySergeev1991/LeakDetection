package ru.tecon.leakDetection.cdi;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;
import ru.tecon.leakDetection.ejb.CheckUserSB;
import ru.tecon.leakDetection.ejb.LeakDetectionSB;
import ru.tecon.leakDetection.model.*;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Контроллер для формы выявление утечек
 *
 * @author Aleksey Sergeev
 */
@Named("leakDetectionMB")
@ViewScoped
public class LeakDetectionMB implements Serializable {

    // Поля описывающие данные для дерева объектов
    private TreeNode<StructTree> root;
    private TreeNode<StructTree> rootFiltered;
    private TreeNode<StructTree> selectedStructNode;
    private List<StructTree> structTreeList = new ArrayList<>();
    private List<ObjProp> propList;
    private ObjProp selectedProp = new ObjProp();

    private String user;
    private String filterWord = "";
    private String address;
    private Date date = new Date();
    private String strDate;
    private String timePeriod = "m";
    private List<String> yearsList = new ArrayList<>();
    private List<Const> leftSide = new ArrayList<>();
    private List<Const> rightSide = new ArrayList<>();
    private List<CoolantTable> coolantTableList = new ArrayList<>();
    private List<GvsTable> gvsTableList = new ArrayList<>();
    private CoolantTable total;
    private String struct;
    private String alg = "coolant";
    private String redirect;
    private List<ArchiveReport> reportList;
    private boolean structTreeListIsEmpty;
    private boolean inIframe;
    private boolean disableGraphTotalRedirect = false;
    private boolean noObjSelected = true;


    private static final Logger logger = Logger.getLogger(LeakDetectionMB.class.getName());

    @EJB
    LeakDetectionSB leakDetectionSB;

    @EJB
    private CheckUserSB checkUserSB;

    @PostConstruct
    private void init() {
        Map<String, String> request = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String sessionId = request.get("sessionId");
        user = checkUserSB.getUser(sessionId);

        root = new DefaultTreeNode<>(new StructTree(), null);
        loadStructTree(user, selectedProp.getPropId(), filterWord);
        loadProperty();
        periodChangeMonth();
        yearsList = createYearList();
        loadConstData(545);
        reportList = leakDetectionSB.getArchiveTable();
    }

    private void loadStructTree(String user, int propId, String filterWord) {
        if (!structTreeList.isEmpty()) {
            structTreeList.clear();
            root.getChildren().clear();
        }
        structTreeList = leakDetectionSB.getTreeParam(user, propId, filterWord);
        if (structTreeList.isEmpty()) {
            structTreeListIsEmpty = true;
        } else {
            structTreeListIsEmpty = false;
        }
        Map<String, TreeNode<StructTree>> nodes = new HashMap<>();
        nodes.put(null, root);

        if (!structTreeList.isEmpty()) {
            for (StructTree structNode : structTreeList) {
                if (structNode.getParent().equals("S")) {
                    structNode.setParent(null);
                    TreeNode<StructTree> parent = nodes.get(structNode.getParent());
                    DefaultTreeNode<StructTree> treeNode = new DefaultTreeNode<>(structNode, parent);
                    treeNode.setExpanded(true);
                    nodes.put(structNode.getId(), treeNode);
                } else {
                    TreeNode<StructTree> parent = nodes.get(structNode.getParent());
                    DefaultTreeNode<StructTree> treeNode = new DefaultTreeNode<>(structNode, parent);
                    if (!Objects.equals(filterWord, "")) {
                        treeNode.setExpanded(true);
                    }
                    nodes.put(structNode.getId(), treeNode);
                }
            }
        }
    }

    /**
     * Метод для загрузки таблицы по утечке теплоносителя
     */
    private void loadCoolantTable (int id, String date, String timePeriod, String user) {
        coolantTableList = leakDetectionSB.getCoolantTable(id, date,timePeriod, user);
        total = coolantTableList.get(coolantTableList.size()-1);
        coolantTableList.remove(coolantTableList.size()-1);
    }

    /**
     * Метод для загрузки таблицы по утечке ГВС
     */
    private void loadGvsTable (int id, String date, String timePeriod, String user) {
        gvsTableList = leakDetectionSB.getGvsTable(id, date,timePeriod, user);
        total = new CoolantTable(gvsTableList.get(gvsTableList.size()-1).getFilial(), gvsTableList.get(gvsTableList.size()-1).getHeat_eff(),
                gvsTableList.get(gvsTableList.size()-1).getWater_eff(), gvsTableList.get(gvsTableList.size()-1).getSum_eff(),
                gvsTableList.get(gvsTableList.size()-1).getColor());
        gvsTableList.remove(gvsTableList.size()-1);
    }

    /**
     * Метод для загрузки свойств в дереве
     */
    private void loadProperty() {
        propList = leakDetectionSB.getProp();
    }

    /**
     * Метод для создания списка для интервала Год
     */
    private List<String> createYearList() {
        List<String> result = new ArrayList<>();
        ZoneId z = ZoneId.of( "Europe/Moscow");
        LocalDate ld = LocalDate.now(z);
        int qurDate = ld.getYear() - 20;
        for (int i = qurDate; i <= ld.getYear(); i++) {
            String strYearDate = String.valueOf(i);
            result.add(strYearDate);
        }
        return result;
    }

    /**
     * Метод для логирования выбранного свойства объекта
     */
    public void selectProp() {
        logger.log(Level.INFO, "selected property for filter - " + selectedProp.getPropName());
    }

    /**
     * Метод для фильтрации дерева объектов
     */
    public void filtering() {
        loadStructTree(user, selectedProp.getPropId(), filterWord);
        PrimeFaces.current().ajax().update("leakDetectionForm:treeTableId");
        PrimeFaces.current().ajax().update("leakDetectionForm");
    }

    /**
     * Метод для действий после выбора даты для интервала месяц
     */
    public void periodChangeMonth() {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        strDate = df.format(date);
        strDate = strDate + "-01 00:00";
        if (!noObjSelected) {
            if (alg.equals("coolant")) {
                loadCoolantTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            } else {
                loadGvsTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            }
            loadStruct(user, selectedStructNode.getData().getMyId());

        }
        PrimeFaces.current().ajax().update("leakDetectionForm:leftSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:rightSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:struct");
        PrimeFaces.current().ajax().update("leakDetectionForm:tableField");
        PrimeFaces.current().ajax().update("leakDetectionForm:coolantTotalTablel");
    }

    /**
     * Метод для действий после выбора даты для интервала год
     */
    public void periodChangeYear() {
        if (!noObjSelected) {
            if (alg.equals("coolant")) {
                loadCoolantTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            } else {
                loadGvsTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            }
            loadStruct(user, selectedStructNode.getData().getMyId());
        }


        PrimeFaces.current().ajax().update("leakDetectionForm:leftSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:rightSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:struct");
        PrimeFaces.current().ajax().update("leakDetectionForm:tableField");
        PrimeFaces.current().ajax().update("leakDetectionForm:coolantTotalTablel");
    }

    /**
     * Метод для изменения на формк после выбора интервала
     */
    public void intervalChange() {
        if (timePeriod.equals("y")) {
            ZoneId z = ZoneId.of( "Europe/Moscow");
            LocalDate ld = LocalDate.now(z);
            strDate = String.valueOf(ld.getYear());
        } else {
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            strDate = df.format(date);
        }
        if (!noObjSelected) {
            if (alg.equals("coolant")) {
                loadCoolantTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            } else {
                loadGvsTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            }
            loadStruct(user, selectedStructNode.getData().getMyId());
        }

        PrimeFaces.current().ajax().update("leakDetectionForm:leftSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:rightSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:struct");
        PrimeFaces.current().ajax().update("leakDetectionForm:tableField");
        PrimeFaces.current().ajax().update("leakDetectionForm:coolantTotalTablel");
        PrimeFaces.current().ajax().update("leakDetectionForm:period");
    }

    /**
     * Метод для выбора объекта в дереве и обновления таблиц в зависимости от этого выбора
     */
    public void selectNode() {
        leftSide.clear();
        rightSide.clear();
        noObjSelected = false;
        loadConstData(selectedStructNode.getData().getMyId());
        if (alg.equals("coolant")) {
            loadCoolantTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
        } else {
            loadGvsTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
        }
        disableGraphTotalRedirect = selectedStructNode.getData().getMyType().equals("S145") || selectedStructNode.getData().getMyType().equals("O1");
        loadStruct(user, selectedStructNode.getData().getMyId());

        PrimeFaces.current().ajax().update("leakDetectionForm:leftSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:rightSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:struct");
        PrimeFaces.current().ajax().update("leakDetectionForm:tableField");
        PrimeFaces.current().ajax().update("leakDetectionForm:coolantTotalTablel");
        PrimeFaces.current().ajax().update("leakDetectionForm:noObjSelected");
    }

    /**
     * Метод для загрузки данных для констант по алгоритму "Определение утечек теплоносителя"
     */
    public void loadConstData(int id) {
        List<Const> constList = leakDetectionSB.getConst(id);
        for (Const tempConst : constList) {
            if (tempConst.getId() == 11 || tempConst.getId() == 12) {
                leftSide.add(tempConst);
            } else {
                rightSide.add(tempConst);
            }
        }
    }

    /**
     * Метод для загрузки структуры
     */
    public void loadStruct (String user, int id) {
        struct = leakDetectionSB.getStruct(user, String.valueOf(id));
    }

    /**
     * Метод для обработки после смены алгоритма
     */
    public void algChange() {
        if (timePeriod.equals("m")) {
            DateFormat df = new SimpleDateFormat("yyyy-MM");
            strDate = df.format(date);
        }
        if (!noObjSelected) {
            if (alg.equals("coolant")) {
                loadCoolantTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            } else {
                loadGvsTable(selectedStructNode.getData().getMyId(), strDate, timePeriod, user);
            }
            loadStruct(user, selectedStructNode.getData().getMyId());

        }

        PrimeFaces.current().ajax().update("leakDetectionForm:leftSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:rightSide");
        PrimeFaces.current().ajax().update("leakDetectionForm:struct");
        PrimeFaces.current().ajax().update("leakDetectionForm:tableField");
        PrimeFaces.current().ajax().update("leakDetectionForm:coolantTotalTablel");
        PrimeFaces.current().ajax().update("leakDetectionForm:period");

    }

    public String createDateForRedirect (String strDate) {
        if (strDate.length() < 10) {
            strDate = strDate + "-01";
        }
        return strDate.substring(0, 10).trim().replace("-","");
    }

    /**
     * Метод для загрузки отчета при нажатии на кнопку отчет
     */
    public void createReport() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
        if (selectedStructNode == null) {
            selectedStructNode = new DefaultTreeNode<>(structTreeList.get(0), null);
        }
        if (timePeriod.equals("m")) {
            if (selectedStructNode.getData().getMyIcon().equals("fa fa-cubes cubesIcon")) {
                redirect = url + "loadReport?id=" + selectedStructNode.getData().getMyId() + "&timestamp=" + createDateForRedirect(strDate) + "000000"
                        + "&interval=" + timePeriod + "&user=" + user + "&alg=" + alg + "&repType=0" + "&amp;";
            } else {
                redirect = url + "loadReport?id=" + selectedStructNode.getData().getMyId() + "&timestamp=" + createDateForRedirect(strDate) + "000000"
                        + "&interval=" + timePeriod + "&user=" + user + "&alg=" + alg + "&repType=1" + "&amp;";
            }

        } else {
            if (selectedStructNode.getData().getMyIcon().equals("fa fa-cubes cubesIcon")) {
                redirect = url + "loadReport?id=" + selectedStructNode.getData().getMyId() + "&timestamp=" + strDate + "0101000000"
                        + "&interval=" + timePeriod + "&user=" + user + "&alg=" + alg + "&repType=0" + "&amp;";
            } else {
                redirect = url + "loadReport?id=" + selectedStructNode.getData().getMyId() + "&timestamp=" + strDate + "0101000000"
                        + "&interval=" + timePeriod + "&user=" + user + "&alg=" + alg + "&repType=1" +  "&amp;";
            }
        }

        if (inIframe) {
            PrimeFaces.current().executeScript("window.parent.postMessage({fileUrl: '" + redirect + "'}, '*');");
        } else {
            PrimeFaces.current().executeScript("window.open('" + redirect + "', '_blank').focus();");
        }
    }

    /**
     * Метод для проверки, находится ли страница во фрейме
     */
    public void changeInIframe() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        inIframe = Boolean.parseBoolean(params.get("inIframe"));
    }

    public TreeNode<StructTree> getRoot() {
        return root;
    }

    public void setRoot(TreeNode<StructTree> root) {
        this.root = root;
    }

    public TreeNode<StructTree> getSelectedStructNode() {
        return selectedStructNode;
    }

    public void setSelectedStructNode(TreeNode<StructTree> selectedStructNode) {
        this.selectedStructNode = selectedStructNode;
    }

    public List<StructTree> getStructTreeList() {
        return structTreeList;
    }

    public void setStructTreeList(List<StructTree> structTreeList) {
        this.structTreeList = structTreeList;
    }

    public TreeNode<StructTree> getRootFiltered() {
        return rootFiltered;
    }

    public void setRootFiltered(TreeNode<StructTree> rootFiltered) {
        this.rootFiltered = rootFiltered;
    }

    public List<ObjProp> getPropList() {
        return propList;
    }

    public void setPropList(List<ObjProp> propList) {
        this.propList = propList;
    }

    public ObjProp getSelectedProp() {
        return selectedProp;
    }

    public void setSelectedProp(ObjProp selectedProp) {
        this.selectedProp = selectedProp;
    }

    public String getFilterWord() {
        return filterWord;
    }

    public void setFilterWord(String filterWord) {
        this.filterWord = filterWord;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(String timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStrDate() {
        return strDate;
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
    }

    public List<String> getYearsList() {
        return yearsList;
    }

    public void setYearsList(List<String> yearsList) {
        this.yearsList = yearsList;
    }

    public List<Const> getLeftSide() {
        return leftSide;
    }

    public void setLeftSide(List<Const> leftSide) {
        this.leftSide = leftSide;
    }

    public List<Const> getRightSide() {
        return rightSide;
    }

    public void setRightSide(List<Const> rightSide) {
        this.rightSide = rightSide;
    }

    public List<CoolantTable> getCoolantTableList() {
        return coolantTableList;
    }

    public void setCoolantTableList(List<CoolantTable> coolantTableList) {
        this.coolantTableList = coolantTableList;
    }

    public CoolantTable getTotal() {
        return total;
    }

    public void setTotal(CoolantTable total) {
        this.total = total;
    }

    public String getStruct() {
        return struct;
    }

    public void setStruct(String struct) {
        this.struct = struct;
    }

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public List<GvsTable> getGvsTableList() {
        return gvsTableList;
    }

    public void setGvsTableList(List<GvsTable> gvsTableList) {
        this.gvsTableList = gvsTableList;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public List<ArchiveReport> getReportList() {
        return reportList;
    }

    public void setReportList(List<ArchiveReport> reportList) {
        this.reportList = reportList;
    }

    public boolean isStructTreeListIsEmpty() {
        return structTreeListIsEmpty;
    }

    public void setStructTreeListIsEmpty(boolean structTreeListIsEmpty) {
        this.structTreeListIsEmpty = structTreeListIsEmpty;
    }

    public boolean isDisableGraphTotalRedirect() {
        return disableGraphTotalRedirect;
    }

    public void setDisableGraphTotalRedirect(boolean disableGraphTotalRedirect) {
        this.disableGraphTotalRedirect = disableGraphTotalRedirect;
    }

    public boolean isNoObjSelected() {
        return noObjSelected;
    }

    public void setNoObjSelected(boolean noObjSelected) {
        this.noObjSelected = noObjSelected;
    }
}
