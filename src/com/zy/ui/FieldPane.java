package com.zy.ui;

import com.zy.Field;

import javax.swing.*;
import java.awt.*;

/**
 * @Author Zhanying
 * @Description 字段面板 TODO
 * @Date 2020/11/9 20:57
 * @Version 1.0
 */
public class FieldPane extends JPanel {
    private static final GridLayout gridLayout = new GridLayout(1,3);

    private Field field = new Field();
    private JTextField filedName_Component = new JTextField();
    private JComboBox modelType_Component = new JComboBox();
    private JTextField jsonPara_Component = new JTextField();

    public FieldPane(){
        super(gridLayout);
        init();
    }

    private void init() {
        //网格布局，4个格子
        this.add(filedName_Component);
        this.add(modelType_Component);
        this.add(jsonPara_Component);

    }

    public Field getField() {
        updateField();
        return field;
    }

    public void updateField() {
        field.setFiledName(filedName_Component.getText());
        field.setModelType((String) modelType_Component.getSelectedItem());
        field.setJsonPara(jsonPara_Component.getText());
    }
}
