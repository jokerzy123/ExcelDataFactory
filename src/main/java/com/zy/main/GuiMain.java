package com.zy.main;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.Field;
import com.zy.conf.Conf;
import com.zy.conf.ConfManager;
import com.zy.constant.Constants;
import com.zy.csv.GenerateCsv;
import com.zy.datafunction.DataFunction;
import com.zy.datafunction.DateFunction;
import com.zy.excel.GenerateExcel;
import com.zy.excel.RowObject;
import com.zy.ui.ButtonsPane;
import com.zy.ui.FieldPane;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;

public class GuiMain {

    private JLabel statusLabel;
    private JPanel fieldPanel_;
    private GridLayout gridLayout;
    //行元素统一放到一个列表里
    private final List<FieldPane> fieldPaneList = new ArrayList<>();
    private JTextField jTextField_Num;//行数设置
    private JTextField jTextField_Unique;

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
        Image img = new ImageIcon("./icon/favicon.png").getImage();
        mainFrame.setIconImage(img);
        mainFrame.setSize(800,(int) (800*0.618));
        mainFrame.setLocationByPlatform(true);//由平台显示一个合适的位置
        //设置布局
        //mainFrame.setLayout(new GridLayout(4, 1));
        mainFrame.setLayout(new BorderLayout());
        //headerLabel = new JLabel("",JLabel.CENTER );//居中
        statusLabel = new JLabel("",JLabel.CENTER);
        statusLabel.setSize(350,100);
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }
        });
        JPanel controlPanel = new JPanel();//JPanel 是一个最简单的容器。它提供了任何其他组件可以被放置的空间，包括其他面板。
        controlPanel.setLayout(new GridLayout(3,1));
        JPanel paraPanel = new JPanel();
        paraPanel.setLayout(new FlowLayout());//存放参数

        JPanel btnPanel = new ButtonsPane(new ActionListener() {
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
                        statusLabel.setText("添加字段成功！");
                        break;
                    }
                    case "remove": {
                        removeAllFields();
                        statusLabel.setText("清空字段成功！");
                    }
                }
            }
        });
        controlPanel.add(btnPanel);
        controlPanel.add(paraPanel);

        JPanel fieldPanel = new JPanel();
        gridLayout = new GridLayout(1,1);
        fieldPanel.setLayout(new BorderLayout(0,0));
        JScrollPane scrollPane = new JScrollPane();
        fieldPanel.add(scrollPane, BorderLayout.CENTER);
        fieldPanel_ = new JPanel();
        fieldPanel_.setLayout(gridLayout);
        scrollPane.setViewportView(fieldPanel_);
        mainFrame.add(controlPanel, BorderLayout.NORTH);
        mainFrame.add(fieldPanel, BorderLayout.CENTER);
        mainFrame.add(statusLabel, BorderLayout.SOUTH);
        //网格容器里面放置控件
        jTextField_Num = new JTextField(10);//行数设置
        jTextField_Num.addFocusListener(new JTextFieldHintListener(jTextField_Num, "行数设置"));
        jTextField_Unique = new JTextField(20);//主键关联设置
        jTextField_Unique.addFocusListener(new JTextFieldHintListener(jTextField_Unique, "联合关联约束"));
        paraPanel.add(jTextField_Num);
        controlPanel.add(jTextField_Unique);
        //测试字段区域
        fieldPanel_.add(new JPanel(new GridLayout(1,4)) {{
            add(new JLabel("字段名称", JLabel.CENTER));
            add(new JLabel("数据模型", JLabel.CENTER));
            add(new JLabel("参数", JLabel.CENTER));
            add(new JLabel("操作", JLabel.CENTER));
        }});
        mainFrame.setVisible(true);
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
                    //优先级最高的
                    if (field1.getModelType().equals("分组随机列表权重")) {
                        String pGroup = JSON.parseObject(field1.getJsonPara()).getString("pGroup");
                        if(field2.getFieldName().equals(pGroup)) {
                            return 1;
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
                    //根据不同数据模型，生成数据
                    switch (model){
                        case "累加id":
                        {
                            map.put(fieldName, String.valueOf(jsonObject.getLong("begin")+i));
                            break;
                        }
                        case "随机整数":
                        {
                            map.put(fieldName, String.valueOf(DataFunction.random(jsonObject.getLong("begin"),jsonObject.getLong("end"))));
                            break;
                        }
                        case "随机时间":
                        {
                            map.put(fieldName, new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(DateFunction.randomDate(jsonObject.getString("beginDate"),jsonObject.getString("endDate"))));
                            break;
                        }
                        case "随机列表权重":
                        {
                            List<String> dataList = new ArrayList<>();
                            List<Integer> weightList = new ArrayList<>();
                            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                                dataList.add(entry.getKey());
                                weightList.add((Integer) entry.getValue());
                                //System.out.println("key值="+entry.getKey());
                                //System.out.println("对应key值的value="+entry.getValue());
                            }
                            map.put(fieldName, DataFunction.weightChoice(dataList,weightList));
                            break;
                        }
                        case "分组随机列表权重": {
                            if(jsonObject.getJSONObject("value").containsKey(map.get(jsonObject.getString("pGroup")))) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("value").getJSONObject(map.get(jsonObject.getString("pGroup")));
                                List<String> dataList = new ArrayList<>();
                                List<Integer> weightList = new ArrayList<>();
                                for (Map.Entry<String, Object> entry : jsonObject1.entrySet()) {
                                    dataList.add(entry.getKey());
                                    weightList.add((Integer) entry.getValue());
                                    //System.out.println("key值="+entry.getKey());
                                    //System.out.println("对应key值的value="+entry.getValue());
                                }
                                map.put(fieldName, DataFunction.weightChoice(dataList,weightList));
                            } else {
                                map.put(fieldName, "");
                            }
                            break;
                        }
                        default:
                            break;
                    }
                }
                //添加完属性后，传给RowObject对象
                RowObject rowObject = new RowObject(map);
                //这里进行约束处理
                //System.out.println(rowObject.getPropertMap().get("A"));
                String unique = jTextField_Unique.getText();//获取约束条件
                if(unique != null && unique.length()!=0 && !unique.equals("联合关联约束")){
                    JSONObject uniqueJsonObject = JSONObject.parseObject(unique);
                    String primaryField = uniqueJsonObject.getString("primaryField");
                    String[] relatedFields = uniqueJsonObject.getString("relatedFields").split(",");
                    //调用reset方法
                    rowObject.uniqueReset(rowObjectList,primaryField,relatedFields);//重置关联字段
                }
                rowObjectList.add(rowObject);
            }
            String fileName = JOptionPane.showInputDialog(null, "请输入文件名（无需后缀）：\n", "文件名", JOptionPane.PLAIN_MESSAGE);
            System.out.println(fileName);
            //两种不同的方式生成xls或者csv文件，以支撑更大数据量
            if(rowNum>65535){
                //GenerateCsv.createCsv(jTextField_FileName.getText()+".csv", fieldNameList, rowObjectList);
                GenerateCsv.createCsv(fileName+".csv", fieldNameList, rowObjectList);
                statusLabel.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date())+" 生成表格成功！"+fileName+".csv");
            }else{
                //xls格式，HSSFWorkbook
                //GenerateExcel.createExcel(jTextField_FileName.getText()+".xls", fieldNameList, rowObjectList);
                GenerateExcel.createExcel(fileName+".xls", fieldNameList, rowObjectList);
                statusLabel.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(new Date())+"生成表格成功！"+fileName+".xls");
            }
            //保存配置
            saveConf();
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.toString()+e.getCause()+"\n", "错误提示",JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * 保存配置文件
     * @throws JsonProcessingException
     */
    private void saveConf() throws JsonProcessingException {
        //获取jsonPara
        ObjectMapper mapper = new ObjectMapper();
        List<Field> fields = new ArrayList<>();
        for(FieldPane fieldPane : fieldPaneList) {
            fields.add(fieldPane.getField());
        }
        Conf conf = new Conf(mapper.writeValueAsString(fields));
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
