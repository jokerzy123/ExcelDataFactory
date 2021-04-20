package com.zy.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.Field;
import com.zy.conf.Conf;
import com.zy.conf.ConfManager;
import com.zy.conf.Table;
import com.zy.constant.Constants;
import com.zy.csv.GenerateCsv;
import com.zy.excel.GenerateExcel;
import com.zy.excel.RowObject;
import com.zy.model.Model;
import com.zy.model.ModelType;
import com.zy.ui.ButtonsJToolBar;
import com.zy.ui.FieldPane;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.function.BiConsumer;

public class GuiMain {

    private JLabel statusLabel;
    private JPanel fieldPanel_;
    private GridLayout gridLayout;
    //行元素统一放到一个列表里
    private final List<FieldPane> fieldPaneList = new ArrayList<>();
    private JTextField jTextField_Num;//行数设置
    private JTextField jTextField_Unique;

    private Map<String, Object> tableMap = new HashMap<>();//备份的表配置

    //构造器
    public GuiMain(){
        prepareGUI();
        //读取配置文件，写入配置
        try {
            readConfWhenInit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        InitGlobalFont(Constants.font);  //统一设置字体
        GuiMain guiMain = new GuiMain();
    }

    private void prepareGUI(){
        //初始化准备一些布局构建
        //界面化，先实现最简单的swing界面
        //基础布局
        JFrame mainFrame = new JFrame("excel模拟数据工厂");//设置标题
        //设置图标
        Image img = Constants.icon;
        mainFrame.setIconImage(img);
        mainFrame.setSize(800,(int) (800*0.618));
        mainFrame.setLocationByPlatform(true);//由平台显示一个合适的位置
        //mainFrame.setBackground(Color.decode("#CCFF99"));


        // 创建 内容面板，使用 边界布局
        JPanel mainPanel = new JPanel(new BorderLayout());//整体区域

        //设置布局
        //mainFrame.setLayout(new GridLayout(4, 1));
        mainFrame.setContentPane(mainPanel);
        //headerLabel = new JLabel("",JLabel.CENTER );//居中

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });

        //工具栏测试
        // 创建 一个工具栏实例
        JToolBar toolBar = new ButtonsJToolBar(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String command = e.getActionCommand();
                switch (command) {
                    case "create": {
                        createExcel();
                        break;
                    }
                    case "add": {
                        addField();
                        printLog("添加字段成功！");
                        break;
                    }
                    case "remove": {
                        removeAllFields();
                        printLog("清空字段成功！");
                        break;
                    }
                    case "backup": {
                        backup();
                        break;
                    }
                    case "tables": {
                        tables();
                        break;
                    }
                    case "deleteBack": {
                        deleteBack();
                        break;
                    }
                    case "save": {
                        try {
                            saveConf();
                            printLog("存档成功！");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            printLog("存档失败！请补充好字段");
                        }
                        break;
                    }
                    case "link": {
                        try {
                            browse2("http://note.youdao.com/noteshare?id=da24263769490356b42262543126373e&sub=F788D88B2FEB46F1AD5A6D0072737591");
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                        break;
                    }
                    case "import": {
                        importFields();
                        break;
                    }
                }
            }
        });
        mainPanel.add(toolBar, BorderLayout.EAST);

        JPanel workPanel = new JPanel(new BorderLayout());//工作区域
        TitledBorder tb = BorderFactory.createTitledBorder("工作区");
        tb.setTitleJustification(TitledBorder.RIGHT);
        workPanel.setBorder(tb);
        workPanel.setBackground(Color.decode("#FFCC99"));
        //workPanel.setBorder(new LineBorder(Color.LIGHT_GRAY,5));

        mainPanel.add(workPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);

        JPanel controlPanel = new JPanel();//JPanel 是一个最简单的容器。它提供了任何其他组件可以被放置的空间，包括其他面板。
        controlPanel.setLayout(new GridLayout(2,1));
        //controlPanel.setBorder(new LineBorder(new Color(0,0,0,0),5));

        JPanel paraPanel = new JPanel();
        paraPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));//存放参数

        controlPanel.add(paraPanel);

        JPanel fieldPanel = new JPanel();
        gridLayout = new GridLayout(1,1);
        fieldPanel.setLayout(new BorderLayout(0,0));
        fieldPanel.setBorder(new LineBorder(new Color(0,0,0,0),5));
        JScrollPane scrollPane = new JScrollPane();
        fieldPanel.add(scrollPane, BorderLayout.CENTER);
        fieldPanel_ = new JPanel();
        fieldPanel_.setLayout(gridLayout);
        scrollPane.setViewportView(fieldPanel_);

        workPanel.add(controlPanel, BorderLayout.NORTH);
        workPanel.add(fieldPanel, BorderLayout.CENTER);
        workPanel.add(new JPanel(){{
            TitledBorder tb1 = BorderFactory.createTitledBorder("日志");
            tb1.setTitleJustification(TitledBorder.RIGHT);
            setBorder(tb1);
            add(statusLabel);
        }}, BorderLayout.SOUTH);

        //网格容器里面放置控件
        jTextField_Num = new JTextField(10);//行数设置
        jTextField_Num.addFocusListener(new JTextFieldHintListener(jTextField_Num, ""));
        jTextField_Unique = new JTextField(50);//主键关联设置
        jTextField_Unique.addFocusListener(new UniqueJTextFieldHintListener(jTextField_Unique, ""));

        paraPanel.add(new JLabel("行数设置"));
        paraPanel.add(jTextField_Num);


        controlPanel.add(new JPanel() {{
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
            add(new JLabel("关联约束"));
            add(jTextField_Unique);
        }});
        //测试字段区域
        fieldPanel_.add(new JPanel(new GridLayout(1,4)) {{
            add(new JLabel("字段名称", JLabel.CENTER));
            add(new JLabel("数据模型", JLabel.CENTER));
            add(new JLabel("参数", JLabel.CENTER));
            add(new JLabel("操作", JLabel.CENTER));
        }});
        //fieldPanel_.setPreferredSize(new Dimension(0, 30));


        //toolBar.add(btnPanel);

        mainFrame.setVisible(true);
    }

    /**
     * @title 使用默认浏览器打开
     * @param url 要打开的网址
     */
    private static void browse2(String url) throws Exception {
        Desktop desktop = Desktop.getDesktop();
        if (Desktop.isDesktopSupported() && desktop.isSupported(Desktop.Action.BROWSE)) {
            URI uri = new URI(url);
            desktop.browse(uri);
        }
    }

    private static class JTextAreaHintListener implements FocusListener {
        //提示文字功能
        private final String hintText;
        private final JTextArea textField;
        public JTextAreaHintListener(JTextArea jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }

    }

    private static class JTextFieldHintListener implements FocusListener {
        //提示文字功能
        private final String hintText;
        private final JTextField textField;
        public JTextFieldHintListener(JTextField jTextField,String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if(temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if(temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }
    }

    private static class UniqueJTextFieldHintListener implements FocusListener {
        //提示文字功能
        private final String hintText;
        private final JTextField textField;

        public UniqueJTextFieldHintListener(JTextField jTextField, String hintText) {
            this.textField = jTextField;
            this.hintText = hintText;
            jTextField.setText(hintText);  //默认直接显示
            jTextField.setForeground(Color.GRAY);
        }

        @Override
        public void focusGained(FocusEvent e) {
            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if (temp.equals(hintText)) {
                textField.setText("{primaryField:\"\",relatedFields:[]}");
                textField.setForeground(Color.BLACK);
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if (temp.equals("") || temp.equals("{primaryField:\"\",relatedFields:[]}")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }
    }

    /**
     * 添加字段
     */
    private void addField() {
        gridLayout.setRows(gridLayout.getRows()+1);//网格添加一行
        final FieldPane fieldPane = new FieldPane();
        fieldPane.addRemoveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldPane.removeField(fieldPaneList, fieldPanel_, gridLayout);
            }
        });
        fieldPanel_.add(fieldPane);
        fieldPanel_.revalidate();
        fieldPaneList.add(fieldPane);
    }

    /**
     * 添加字段
     */
    private void addField(Field field) {
        gridLayout.setRows(gridLayout.getRows()+1);//网格添加一行
        final FieldPane fieldPane = new FieldPane(field);
        fieldPane.addRemoveListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldPane.removeField(fieldPaneList, fieldPanel_, gridLayout);
            }
        });
        fieldPanel_.add(fieldPane);
        fieldPanel_.revalidate();
        fieldPaneList.add(fieldPane);
    }

    /**
     * 删除字段
     */
    private void removeAllFields() {
        gridLayout.setRows(1);
        for(FieldPane fieldPane : fieldPaneList) {
            fieldPanel_.remove(fieldPane);
            //fieldPaneList.remove(fieldPane);
        }
        fieldPaneList.clear();
    }

    /**
     * 生成表格文件 TODO 优化重构下
     */
    private void createExcel(){
        try{
            //关联约束处理
            boolean uniqueFlag = false;
            Map<String, RowObject> uniqueMap = new HashMap<>();
            String primaryField = null;
            String[] relatedFields = null;
            String unique = jTextField_Unique.getText();//获取约束条件
            if(unique != null && unique.length()!=0 && !unique.equals("联合关联约束")){
                JSONObject uniqueJsonObject = JSONObject.parseObject(unique);
                primaryField = uniqueJsonObject.getString("primaryField");
                relatedFields = uniqueJsonObject.getString("relatedFields").split(",");
                if(primaryField.length() > 0 && relatedFields.length > 0) {
                    uniqueFlag = true;
                }
                //调用reset方法
                //rowObject.uniqueReset(rowObjectList,primaryField,relatedFields);//重置关联字段
            }


            int rowNum = Integer.parseInt(jTextField_Num.getText());//设置表的行数
            //获取字段名称，字段名称不能重复
            List<String> fieldNameList = new ArrayList<>();
            //String[] list = {"边防检查站", "姓名", "身份证号", "性别", "国籍", "交通工具", "出入境", "日期", "是否携带违规品", "违规品类型"};

            //优先级排序
            fieldPaneList.sort(new Comparator<FieldPane>() {
                @Override
                public int compare(FieldPane o1, FieldPane o2) {
                    Field field1 = o1.getField();
                    Field field2 = o2.getField();
                    ModelType modelType = ModelType.getModelType(field1.getModelType());
                    if(modelType != null) {
                        switch (modelType) {
                            case GroupRandomListWeight: {
                                String pGroup = JSON.parseObject(field1.getJsonPara()).getString("pGroup");
                                if(field2.getFieldName().equals(pGroup)) {
                                    return 1;
                                }
                            }
                            case GroupRandomInt: {
                                String pGroup = JSON.parseObject(field1.getJsonPara()).getString("pGroup");
                                if(field2.getFieldName().equals(pGroup)) {
                                    return 1;
                                }
                            }
                        }
                    }
                    return 0;
                }
            });

            //获取字段列表
            for(FieldPane fieldPane : fieldPaneList){
                Field field = fieldPane.getField();
                String fieldName = field.getFieldName();
                fieldNameList.add(fieldName);
            }
            System.out.println(fieldNameList);
            List<RowObject> rowObjectList = new ArrayList<>();//行元素列表
            for(int i=0; i<rowNum; i++){
                Map<String,String> map = new HashMap<>();
                //写入属性
                for(FieldPane fieldPane : fieldPaneList){
                    Field field = fieldPane.getField();
                    String fieldName = field.getFieldName();
                    String model = field.getModelType();
                    String para = field.getJsonPara();
                    JSONObject jsonObject = JSONObject.parseObject(para);//转换为json对象
                    Model model1 = ModelType.getModel(ModelType.getModelType(model));
                    if(model1 != null) {
                        model1.Compute(fieldName, jsonObject, map, i);
                    }
                }
                //添加完属性后，传给RowObject对象
                RowObject rowObject = new RowObject(map);
                //这里进行约束处理
                //System.out.println(rowObject.getPropertMap().get("A"));
                if(uniqueFlag) {
                    rowObject.uniqueReset(uniqueMap, primaryField, relatedFields);
                }
                rowObjectList.add(rowObject);
            }
            String fileName = JOptionPane.showInputDialog(null, "请输入文件名（无需后缀）：\n", "文件名", JOptionPane.PLAIN_MESSAGE);
            System.out.println(fileName);
            if(fileName != null && fileName.length() > 0) {
                //两种不同的方式生成xls或者csv文件，以支撑更大数据量
                if(rowNum>1048575){
                    //GenerateCsv.createCsv(jTextField_FileName.getText()+".csv", fieldNameList, rowObjectList);
                    GenerateCsv.createCsv(fileName+".csv", fieldNameList, rowObjectList);
                    printLog("生成表格成功！"+fileName+".csv");
                }else{
                    //xls格式，HSSFWorkbook
                    //GenerateExcel.createExcel(jTextField_FileName.getText()+".xls", fieldNameList, rowObjectList);
                    GenerateExcel.createExcel(fileName+".xlsx", fieldNameList, rowObjectList);
                    printLog("生成表格成功！"+fileName+".xlsx");
                }
                //保存配置
                saveConf();
            }
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.toString()+e.getCause()+"\n", "错误提示",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 备份
     */
    private void backup() {
        String name = (String) JOptionPane.showInputDialog(null, "请输入标签名：\n", "备份", JOptionPane.PLAIN_MESSAGE);
        if(name != null && name.length() > 0) {
            Conf conf = getCurrentConf();
            String jsonPara = conf.getJsonPara();
            String uniquePara = conf.getUniquePara();

            Table table = new Table();
            table.setJsonPara(jsonPara);
            table.setUniquePara(uniquePara);

            tableMap.put(name, table);
            printLog("添加备份成功！");
            try {
                saveConf();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 管理备份，读取删除等
     */
    private void tables() {
        List<Object> list = new ArrayList<>();
        list.add("--请选择--");
        list.addAll(tableMap.keySet());
        Object[] objects = list.toArray(new Object[0]);

        if(objects.length > 0) {
            String name = (String) JOptionPane.showInputDialog(null, "请选择标签名：\n", "备份管理", JOptionPane.PLAIN_MESSAGE, null, objects, objects[0]);
            if(name != null && name.length() > 0 && tableMap.containsKey(name)) {
                Table table = (Table) tableMap.get(name);
                try {
                    refresh(table);
                    printLog("读取备份成功！");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 刷新当前表
     */
    private void refresh(Table table) throws IOException {
        String jsonPara = table.getJsonPara();
        String uniquePara = table.getUniquePara();
        //先要清空所有
        removeAllFields();
        if(jsonPara != null && jsonPara.length() > 0) {
            JSONArray jsonArray = JSONArray.parseArray(jsonPara);
            for(Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Field field = new ObjectMapper().readValue(jsonObject.toJSONString(), Field.class);
                addField(field);
            }
        }
        if(uniquePara != null && uniquePara.length() > 0) {
            jTextField_Unique.setText(uniquePara);
            jTextField_Unique.setForeground(Color.BLACK);
        } else {
            //jTextField_Unique.setText("联合关联约束");
            jTextField_Unique.setForeground(Color.GRAY);
        }
    }

    private void deleteBack() {
        List<Object> list = new ArrayList<>();
        list.add("--请选择--");
        list.addAll(tableMap.keySet());
        Object[] objects = list.toArray(new Object[0]);

        if(objects.length > 0) {
            String name = (String) JOptionPane.showInputDialog(null, "请选择待删除的标签名：\n", "备份删除", JOptionPane.PLAIN_MESSAGE, null, objects, objects[0]);
            if(name != null && name.length() > 0 && tableMap.containsKey(name)) {
                tableMap.remove(name);
                printLog("删除备份成功！");
            }
        }
        try {
            saveConf();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    private Conf getCurrentConf(){
        ObjectMapper mapper = new ObjectMapper();
        List<Field> fields = new ArrayList<>();
        for(FieldPane fieldPane : fieldPaneList) {
            fields.add(fieldPane.getField());
        }

        String tables = new JSONObject(tableMap).toJSONString();
        String jsonPara = null;
        try {
            jsonPara = mapper.writeValueAsString(fields);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Conf conf = new Conf();
        conf.setTables(tables);
        conf.setJsonPara(jsonPara);

        String uniquePara = jTextField_Unique.getText();
        if(uniquePara != null && uniquePara.length() > 0 && !uniquePara.equals("联合关联约束")) {
            conf.setUniquePara(jTextField_Unique.getText());
        }
        return conf;
    }


    /**
     * 保存配置文件
     * @throws JsonProcessingException
     */
    private void saveConf() throws JsonProcessingException {
        Conf conf = getCurrentConf();
        ConfManager.getInstance().setConf(conf);
    }

    private void readConfWhenInit() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Conf conf = ConfManager.getInstance().getConf();
        String jsonPara = conf.getJsonPara();
        if(jsonPara != null && jsonPara.length() > 0) {
            JSONArray jsonArray = JSONArray.parseArray(jsonPara);
            for(Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;
                Field field = mapper.readValue(jsonObject.toJSONString(), Field.class);
                addField(field);
            }
        }

        String uniquePara = conf.getUniquePara();
        if(uniquePara !=null && uniquePara.length() > 0) {
            jTextField_Unique.setText(uniquePara);
            jTextField_Unique.setForeground(Color.BLACK);
        }

        //tables读取
        String tables = conf.getTables();
        if(tables != null && tables.length() > 0) {
            JSONObject jsonObject = JSON.parseObject(tables);
            jsonObject.forEach(new BiConsumer<String, Object>() {
                @Override
                public void accept(String s, Object o) {
                    String table = o.toString();
                    try {
                        Table table1 = mapper.readValue(table, Table.class);
                        tableMap.put(s, table1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    /**
     * 导入表头
     *
     */
    private void importFields() {
        JFileChooser fileChooser = new JFileChooser();
        //过滤Excel文件，只寻找以xls结尾的Excel文件，如果想过滤word文档也可以写上doc
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "xls", "xlsx");

        fileChooser.setFileFilter(filter);

        //fd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(null);
        //弹出一个文件选择提示框
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            //当用户选择文件后获取文件路径
            File chooseFile = fileChooser.getSelectedFile();

            //根据文件路径初始化Excel工作簿
            Workbook workBook = null;
            try {
                workBook = new XSSFWorkbook(chooseFile);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(workBook != null && workBook.getNumberOfSheets() > 0 ) {
                //获取该工作表中的第一个工作表
                Sheet sheet = workBook.getSheetAt(0);
                if(sheet.getPhysicalNumberOfRows() > 0) {
                    Row row = sheet.getRow(0);
                    Iterator<Cell> iterator = row.cellIterator();
                    while (iterator.hasNext()) {
                        Cell cell = iterator.next();
                        String fieldName = cell.getStringCellValue();
                        addField(new Field(fieldName, null, null));
                    }
                }
            }
        }
        printLog("导入表头成功");
    }

    /**
     * 日志区打印日志
     * @param s
     */
    private void printLog(String s) {
        statusLabel.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date()) + " " + s);
    }


    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    //还没怎么看懂这个代码的运行机制
    public static void InitGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys();
             keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}
