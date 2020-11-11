package com.zy.ui;

import com.zy.constant.Constants;
import com.zy.main.GuiMain;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @Author Zhanying
 * @Description TODO
 * @Date 2020/11/10 10:31
 * @Version 1.0
 */
public class ButtonsPane extends JPanel {
    //按钮组件
    private final JButton createButton = new JButton("生成");
    private final JButton addButton = new JButton("添加");
    private final JButton removeButton = new JButton("清空");

    private final ActionListener actionListener;

    //布局
    public ButtonsPane(ActionListener actionListener) {
        this.actionListener = actionListener;
        init();
    }

    public void init() {
        LayoutManager layout = new FlowLayout();
        setLayout(layout);
        createButton.setActionCommand("create");
        createButton.addActionListener(actionListener);
        createButton.setFont(Constants.font);
        addButton.setActionCommand("add");
        addButton.addActionListener(actionListener);
        addButton.setFont(Constants.font);
        removeButton.setActionCommand("remove");
        removeButton.addActionListener(actionListener);
        removeButton.setFont(Constants.font);
        add(createButton);
        add(addButton);
        add(removeButton);
    }
}
