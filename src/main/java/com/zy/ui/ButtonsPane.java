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
    private final JButton backupButton = new JButton("备份");
    private final JButton tablesButton = new JButton("备份读取");
    private final JButton deleteButton = new JButton("备份删除");
    private final JButton saveButton = new JButton("存档");
    private final JButton linkButton = new JButton("文档");
    private final JButton importButton = new JButton("导入表头");

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

        backupButton.setActionCommand("backup");
        backupButton.addActionListener(actionListener);
        backupButton.setFont(Constants.font);

        tablesButton.setActionCommand("tables");
        tablesButton.addActionListener(actionListener);
        tablesButton.setFont(Constants.font);

        deleteButton.setActionCommand("deleteBack");
        deleteButton.addActionListener(actionListener);
        deleteButton.setFont(Constants.font);

        linkButton.setActionCommand("link");
        linkButton.addActionListener(actionListener);
        linkButton.setFont(Constants.font);

        saveButton.setActionCommand("save");
        saveButton.addActionListener(actionListener);
        saveButton.setFont(Constants.font);

        importButton.setActionCommand("import");
        importButton.addActionListener(actionListener);
        importButton.setFont(Constants.font);

        add(createButton);
        add(addButton);
        add(removeButton);
        add(backupButton);
        add(tablesButton);
        add(deleteButton);
        add(saveButton);
        add(linkButton);
        add(importButton);
    }
}
