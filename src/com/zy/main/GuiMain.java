package com.zy.main;

import com.alibaba.fastjson.JSONObject;
import com.zy.csv.GenerateCsv;
import com.zy.datafunction.DataFunction;
import com.zy.datafunction.DateFunction;
import com.zy.excel.GenerateExcel;
import com.zy.excel.RowObject;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class GuiMain {
    //界面化，先实现最简单的swing界面
    //基础布局
    private JFrame mainFrame;
    //private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JPanel btnPanel;
    private JPanel paraPanel;
    private JPanel fieldPanel;
    private JPanel fieldPanel_;
    private Font font;
    private GridLayout gridLayout;
    private JScrollPane scrollPane;
    //行元素统一放到一个列表里
    private List<Map<String,JComponent>> fieldList;
    private JTextField jTextField_Num;//行数设置
    //private JTextField jTextField_FileName;//文件名
    private JTextField jTextField_Unique;
    private String[] modelList;

    //构造器
    public GuiMain(){
        prepareGUI();
        font = new Font("楷体",Font.PLAIN, 15);
        InitGlobalFont(font);  //统一设置字体
        fieldList = new ArrayList<>();
        modelList = new String[]{"--请选择--", "累加id", "随机整数", "随机时间", "随机列表权重", "明细公式(待开发)"};
    }

    public static void main(String[] args){
        GuiMain guiMain = new GuiMain();
        guiMain.showEventDemo();
    }

    private void prepareGUI(){
        //初始化准备一些布局构建
        mainFrame = new JFrame("excel模拟数据工厂");//设置标题
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
        controlPanel = new JPanel();//JPanel 是一个最简单的容器。它提供了任何其他组件可以被放置的空间，包括其他面板。
        //controlPanel.setLayout(new FlowLayout());//FlowLayout 是默认的布局。它用定向流动来布局组件。
        //controlPanel加一个网格布局吧
        controlPanel.setLayout(new GridLayout(3,1));
        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        paraPanel =new JPanel();
        paraPanel.setLayout(new FlowLayout());//存放参数

        controlPanel.add(btnPanel);
        controlPanel.add(paraPanel);
        //paraPanel.setSize(200,400);
        fieldPanel = new JPanel();
        gridLayout = new GridLayout(1,4);
        fieldPanel.setLayout(new BorderLayout(0,0));
        scrollPane = new JScrollPane();
        fieldPanel.add(scrollPane, BorderLayout.CENTER);
        fieldPanel_ = new JPanel();
        fieldPanel_.setLayout(gridLayout);
        scrollPane.setViewportView(fieldPanel_);
        //mainFrame.add(headerLabel, BorderLayout.NORTH);
        mainFrame.add(controlPanel, BorderLayout.NORTH);
        //mainFrame.add(paraPanel, BorderLayout.WEST);
        mainFrame.add(fieldPanel, BorderLayout.CENTER);
        mainFrame.add(statusLabel, BorderLayout.SOUTH);
        mainFrame.setVisible(true);
    }

    private void showEventDemo(){
        //展示方法
        //headerLabel.setText("字段设置");
        //headerLabel.setFont(font);
        JButton okButton = new JButton("生成");
        //okButton.setFont(font);
        JButton submitButton = new JButton("添加");
        //submitButton.setFont(font);
        JButton cancelButton = new JButton("删除");
        //cancelButton.setFont(font);
        okButton.setActionCommand("OK");
        submitButton.setActionCommand("Submit");
        cancelButton.setActionCommand("Cancel");
        okButton.addActionListener(new ButtonClickListener());
        submitButton.addActionListener(new ButtonClickListener());
        cancelButton.addActionListener(new ButtonClickListener());
        //网格容器里面放置控件
        jTextField_Num = new JTextField(10);//行数设置
        jTextField_Num.addFocusListener(new JTextFieldHintListener(jTextField_Num,"行数设置"));
        //controlPanel.add(jTextField_Num);
        //jTextField_Unique = new JTextArea(3, 10);
        jTextField_Unique = new JTextField(20);//主键关联设置
        jTextField_Unique.addFocusListener(new JTextFieldHintListener(jTextField_Unique,"联合关联约束"));
        //controlPanel.add(jTextField_Unique);
        //jTextField_FileName = new JTextField(10);//excel文件名称
        //jTextField_FileName.addFocusListener(new JTextFieldHintListener(jTextField_FileName,"文件名称"));
        //controlPanel.add(jTextField_FileName);

        btnPanel.add(okButton);
        btnPanel.add(submitButton);
        btnPanel.add(cancelButton);


        //参数区域
        //paraPanel.add(new JLabel("行数",JLabel.CENTER));
        paraPanel.add(jTextField_Num);
        //paraPanel.add(new JLabel("文件名称",JLabel.CENTER));
        //paraPanel.add(jTextField_FileName);弃用
        //paraPanel.add(new JLabel("联合关联约束",JLabel.CENTER));
        controlPanel.add(jTextField_Unique);

        //测试字段区域
        fieldPanel_.add(new JLabel("字段名称", JLabel.CENTER));
        //fieldPanel_.add(new JLabel("字段类型", JLabel.CENTER));
        fieldPanel_.add(new JLabel("数据模型", JLabel.CENTER));
        fieldPanel_.add(new JLabel("参数", JLabel.CENTER));
        fieldPanel_.add(new JLabel("约束(开发中)", JLabel.CENTER));

        /*
        for(int i=0;i<1;i++){
            addField();
        }

         */
        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        //按钮点击的监听事件都放在一个接口实现类里面，这里是内部类
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if( command.equals( "OK" ))  {
                createExcel();
                //statusLabel.setText("生成excel成功！");
            }
            else if( command.equals( "Submit" ) )  {
                addField();
                statusLabel.setText("添加字段成功！");
            }
            else  {
                statusLabel.setText("Cancel Button clicked.待开发...");
            }
        }
    }

    private class JTextAreaHintListener implements FocusListener {
        //提示文字功能
        private String hintText;
        private JTextArea textField;
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

    private class JTextFieldHintListener implements FocusListener {
        //提示文字功能
        private String hintText;
        private JTextField textField;
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


    private void addField(){
        gridLayout.setRows(gridLayout.getRows()+1);//网格添加一行
        /*
        JComboBox cmb=new JComboBox();    //创建JComboBox
        //cmb.addItem("--请选择--");    //向下拉列表中添加一项

        cmb.addItem("文本");//默认文本
        cmb.addItem("时间");
        cmb.addItem("数值");
        cmb.getSelectedItem();

         */
        JComboBox cmb1=new JComboBox();    //创建JComboBox
        //cmb.addItem("--请选择--");    //向下拉列表中添加一项
        /*
        cmb1.addItem("随机数");//默认文本
        cmb1.addItem("随机时间");
        cmb1.addItem("随机列表权重");
        cmb1.addItem("明细公式");

         */
        for(String model:modelList){
            cmb1.addItem(model);
        }
        JTextField jTextField = new JTextField();
        fieldPanel_.add(jTextField);
        //fieldPanel_.add(cmb);
        fieldPanel_.add(cmb1);

        JTextField jTextField_ = new JTextField();
        fieldPanel_.add(jTextField_);
        JTextField jTextField_1 = new JTextField();
        fieldPanel_.add(jTextField_1);
        fieldPanel_.revalidate();
        Map<String, JComponent> map = new HashMap<>();
        map.put("字段名称", jTextField);
        //map.put("字段类型", cmb);
        map.put("数据模型", cmb1);
        map.put("参数",jTextField_);
        map.put("约束",jTextField_1);
        fieldList.add(map);
    }

    private void createExcel(){
        try{
            int rowNum = Integer.parseInt(jTextField_Num.getText());//设置表的行数
            //获取字段名称，字段名称不能重复
            List<String> fieldNameList = new ArrayList<>();
            //String[] list = {"边防检查站", "姓名", "身份证号", "性别", "国籍", "交通工具", "出入境", "日期", "是否携带违规品", "违规品类型"};
            Map<String,String> fieldMap = new HashMap<>();
            //遍历网格布局的各个组件
            for(Map<String, JComponent> map : fieldList){
                String fieldName = ((JTextField) map.get("字段名称")).getText();
                fieldNameList.add(fieldName);
                fieldMap.put(fieldName+".name",fieldName);
                //fieldMap.put(fieldName+".type",(String) ((JComboBox) map.get("字段类型")).getSelectedItem());
                fieldMap.put(fieldName+".model",(String) ((JComboBox) map.get("数据模型")).getSelectedItem());
                fieldMap.put(fieldName+".para",((JTextField) map.get("参数")).getText());
                fieldMap.put(fieldName+".condition",((JTextField) map.get("约束")).getText());
            }
            System.out.println(fieldNameList);
            List<RowObject> rowObjectList = new ArrayList<>();//行元素列表
            for(int i=0; i<rowNum; i++){
                Map<String,String> map = new HashMap<>();
                //写入属性
                for(String fieldName : fieldNameList){
                    String type = fieldMap.get(fieldName+".type");
                    String model = fieldMap.get(fieldName+".model");
                    String para = fieldMap.get(fieldName+".para");//这里传入一个json参数字符串，进行解析
                    String condition = fieldMap.get(fieldName+".condition");//约束
                    JSONObject jsonObject = JSONObject.parseObject(para);//转换为json对象
                    //fastjson解析方法
                /*
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    System.out.println("key值="+entry.getKey());
                    System.out.println("对应key值的value="+entry.getValue());
                }

                 */
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
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, e.toString()+e.getCause()+"\n", "错误提示",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
     */
    //还没怎么看懂这个代码的运行机制
    private static void InitGlobalFont(Font font) {
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
