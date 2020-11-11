package com.zy.ui;

import com.alibaba.fastjson.JSON;
import com.zy.Field;
import com.zy.util.JsonFormatUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

/**
 * @Author Zhanying
 * @Description 字段面板 TODO 加一个删除操作
 * @Date 2020/11/9 20:57
 * @Version 1.0
 */
public class FieldPane extends JPanel {
    private final String[] models = {"--请选择--", "累加id", "随机整数", "随机时间", "随机列表权重", "明细公式(待开发)"};
    private Field field;

    private final JTextField filedName_Component = new JTextField();
    private final JComboBox<String> modelType_Component = new JComboBox<>(models);
    private final JTextArea jsonPara_Component = new JTextArea();
    private final JButton removeButton = new JButton("删除");

    private final JsonFormatUtil jsonFormatUtil = new JsonFormatUtil();

    public FieldPane(){
        this.field = new Field();
        init();
    }

    public FieldPane(Field field){
        this.field = field;
        init();
        filedName_Component.setText(field.getFieldName());
        modelType_Component.setSelectedItem(field.getModelType());
        jsonPara_Component.setText(field.getJsonPara());
        jsonFormat();
    }

    private void init() {
        LayoutManager layout = new GridLayout(1, 3);
        setLayout(layout);
        //网格布局，4个格子
        add(filedName_Component);
        add(modelType_Component);
        jsonPara_Component.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }

            @Override
            public void focusLost(FocusEvent e) {
                jsonFormat();
            }
        });

        //设置矩形大小.参数依次为(矩形左上角横坐标x,矩形左上角纵坐标y，矩形长度，矩形宽度)
        JScrollPane jScrollPane = new JScrollPane(jsonPara_Component);
        //jScrollPane.setBackground(new Color(255, 251, 240));
        //默认的设置是超过文本框才会显示滚动条，以下设置让滚动条一直显示
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        add(jScrollPane);
        add(removeButton);
        //add(jsonPara_Component);
    }

    public Field getField() {
        updateField();
        return field;
    }

    private void updateField() {
        field.setFieldName(filedName_Component.getText());
        field.setModelType((String) modelType_Component.getSelectedItem());
        field.setJsonPara(JSON.parseObject(jsonPara_Component.getText()).toJSONString());
    }

    private void jsonFormat() {
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
