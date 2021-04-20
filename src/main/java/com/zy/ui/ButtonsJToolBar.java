package com.zy.ui;

import com.zy.constant.Constants;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Created by Zhanying on 2021/4/20.
 */
public class ButtonsJToolBar extends JToolBar {
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
    public ButtonsJToolBar(ActionListener actionListener) {
        super(JToolBar.VERTICAL);
        this.actionListener = actionListener;
        init();
    }

    public void init() {
//        LayoutManager layout = new FlowLayout();
//        setLayout(layout);

        //setLayout(new FlowLayout(FlowLayout.LEADING));

        TitledBorder tb = BorderFactory.createTitledBorder("功能");
        tb.setTitleJustification(TitledBorder.RIGHT);
        setBorder(tb);
        //setBorder(new LineBorder(Color.LIGHT_GRAY,5));
        setBackground(Color.decode("#CCCCCC"));

        // 创建一个垂直箱容器，放置上面两个水平箱（Box组合嵌套）
        //Box vBox = Box.createVerticalBox();
        //vBox.setBackground(Color.ORANGE);

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

        //add(vBox);
    }
}
