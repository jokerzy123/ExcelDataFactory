package com.zy.ui;

import com.alibaba.fastjson.JSON;
import com.zy.Field;
import com.zy.constant.Constants;
import com.zy.model.Model;
import com.zy.model.ModelType;
import com.zy.util.JsonFormatUtil;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static javax.swing.JOptionPane.*;

/**
 * @Author Zhanying
 * @Description 字段面板 TODO 加一个删除操作
 * @Date 2020/11/9 20:57
 * @Version 1.0
 */
public class FieldPane extends JPanel {
    private final String[] models = ModelType.getModelNames();//{"--请选择--", "累加id", "随机整数", "随机时间", "随机列表权重", "分组随机列表权重", "明细公式(待开发)"};
    private Field field;

    private final JTextField filedName_Component = new JTextField();
    private final JComboBox<String> modelType_Component = new JComboBox<>(models);
    private final JTextField jsonPara_Field = new JTextField(10);
    private final JButton editButton = new JButton("编辑");
    private final JButton removeButton = new JButton("删除");

    private final JsonFormatUtil jsonFormatUtil = new JsonFormatUtil();

    public FieldPane(){
        this.field = new Field();
        init();
    }

    public FieldPane(Field field){
        this.field = field;
        filedName_Component.setText(field.getFieldName());
        modelType_Component.setSelectedItem(field.getModelType());
        //String s = field.getJsonPara();
        //System.out.println(s);
        jsonPara_Field.setText(field.getJsonPara());
        init();
    }

    private void populate() {
        filedName_Component.setText(field.getFieldName());
        modelType_Component.setSelectedItem(field.getModelType());
        jsonPara_Field.setText(field.getJsonPara());

    }

    private void init() {
        LayoutManager layout = new GridLayout(1, 4);
        setLayout(layout);
        //setPreferredSize(new Dimension(0, 30));
        //网格布局，4个格子
        add(filedName_Component);
        add(modelType_Component);


        add(jsonPara_Field);
        jsonPara_Field.setEnabled(false);



        JPanel jPanel = new JPanel();//操作区域
        jPanel.setLayout(new GridLayout(1, 2));

        jPanel.add(editButton);

        Component parent = this;
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //弹窗编辑参数
                //JOptionPane.showInputDialog(null, jScrollPane, "参数", JOptionPane.PLAIN_MESSAGE);
                //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
                JTextArea jsonPara_Component = new JTextArea();
                jsonPara_Component.addFocusListener(new FocusListener() {
                    @Override
                    public void focusGained(FocusEvent e) {
                        jsonFormat(jsonPara_Component);
                    }

                    @Override
                    public void focusLost(FocusEvent e) {
                        jsonFormat(jsonPara_Component);
                    }
                });
                //jsonPara_Component.add

                JScrollPane jScrollPane = new JScrollPane(jsonPara_Component);
                jScrollPane.setPreferredSize(new Dimension(250, 300));
                //jScrollPane.setSize(new Dimension(200,300));
                //jScrollPane.setBackground(new Color(255, 251, 240));
                //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
                jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                jsonPara_Component.setText(field.getJsonPara());
                jsonFormat(jsonPara_Component);
                String result = showInputDialog(null, jScrollPane, "编辑参数", JOptionPane.PLAIN_MESSAGE);
                System.out.println(result);
                if(result != null) {
                    //field.setJsonPara(JSON.parseObject(jsonPara_Component.getText()).toJSONString());
                    jsonPara_Field.setText(JSON.parseObject(jsonPara_Component.getText()).toJSONString());//这个地方如果转JSON失败直接就抛异常了，不影响field的值
                    updateField();
                    populate();
                }
            }
        });

        jPanel.add(removeButton);

        add(jPanel);


        //add(removeButton);
        //add(jsonPara_Component);
        // TODO 加一个监听
        modelType_Component.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    /*
                    if(jsonPara_Component.getText() == null || jsonPara_Component.getText().length() == 0) {

                    }

                     */
                    String model = (String) e.getItem();
                    ModelType modelType = ModelType.getModelType(model);
                    if(modelType != null) {
                        Model model1 = ModelType.getModel(modelType);
                        //jsonPara_Field.setText(new JsonFormatUtil().formatJson(model1.defaultPara()));
                        field.setModelType(model);
                        field.setJsonPara(JSON.parseObject(model1.defaultPara()).toJSONString());
                        jsonPara_Field.setText(field.getJsonPara());
                    }
                }
            }
        });
    }

    public static String showInputDialog(Component parentComponent, Object message, String title, int messageType) throws HeadlessException {
        JOptionPane pane = new JOptionPane(message, messageType,
                DEFAULT_OPTION, null,
                null, null);

        //pane.setSize(new Dimension(200, 300));

        pane.setWantsInput(false);
        pane.setSelectionValues(null);
        pane.setInitialSelectionValue(null);
        pane.setComponentOrientation(((parentComponent == null) ?
                getRootFrame() : parentComponent).getComponentOrientation());

        JDialog dialog = pane.createDialog(title);
        dialog.setIconImage(Constants.icon);

        pane.selectInitialValue();
        dialog.show();
        dialog.dispose();


        pane.setInputValue("value");//这里不管按取消还是确定，都会生效
        Object value = pane.getInputValue();

        if (value == UNINITIALIZED_VALUE) {
            return null;
        }
        return (String) value;
    }

    public Field getField() {
        updateField();
        return field;
    }

    private void updateField() {
        field.setFieldName(filedName_Component.getText());
        field.setModelType((String) modelType_Component.getSelectedItem());
        field.setJsonPara(JSON.parseObject(jsonPara_Field.getText()).toJSONString());
    }

    private void jsonFormat(JTextArea jsonPara_Component) {
        String json = JSON.parseObject(jsonPara_Component.getText()).toJSONString();
        if(json != null && json.length() > 0) {
            jsonPara_Component.setText(jsonFormatUtil.formatJson(json));
        }
    }

    public void removeField(List<FieldPane> fieldList,JPanel fieldPanel_,GridLayout gridLayout) {
        fieldList.remove(this);
        fieldPanel_.remove(this);
        gridLayout.setRows(gridLayout.getRows()-1);
        fieldPanel_.updateUI();
    }

    public void addRemoveListener(ActionListener actionListener) {
        removeButton.addActionListener(actionListener);
    }
}
