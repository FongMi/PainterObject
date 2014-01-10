
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class UMLMenu extends JToolBar implements ActionListener {

    DrawObject drawobject;
    JTable attributeTable;
    JTable methodTable;
    JButton[] Menu_JTBtn;
    String Menu_JTBtnName[] = {"img/add1.png", "img/add2.png", "img/del.png", "img/up.png", "img/down.png"};

    UMLMenu(DrawObject drawobject, JTable attributeTable, JTable methodTable) {
        this.drawobject = drawobject;
        this.attributeTable = attributeTable;
        this.methodTable = methodTable;
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setFloatable(false);
        this.setOpaque(false);
        Menu_JTBtn = new JButton[Menu_JTBtnName.length];

        for (int i = 0; i < Menu_JTBtnName.length; i++) {
            Menu_JTBtn[i] = new JButton();
            Menu_JTBtn[i].setIcon(new ImageIcon(this.getClass().getResource(Menu_JTBtnName[i])));
            Menu_JTBtn[i].setBorder(BorderFactory.createEmptyBorder());
            Menu_JTBtn[i].setFocusable(false);
            Menu_JTBtn[i].addActionListener(this);
            Menu_JTBtn[i].setOpaque(false);
            this.add(Menu_JTBtn[i]);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == Menu_JTBtn[0]) {
            attributeTable.clearSelection();
            drawobject.attributeModel.addRow("+", "AttributeName");
            methodTable.clearSelection();
            updateUI();
        }
        if (e.getSource() == Menu_JTBtn[1]) {
            methodTable.clearSelection();
            drawobject.methodModel.addRow("+", "MethodName()");
            attributeTable.clearSelection();
            updateUI();
        }
        if (e.getSource() == Menu_JTBtn[2]) {
            int row = methodTable.getSelectedRow();
            if (methodTable.getSelectedRow() > -1)//{
            {
                drawobject.methodModel.removeRow(methodTable.getSelectedRow());
            }
            //methodTable.setRowSelectionInterval(row - 1,row - 1);}
            if (attributeTable.getSelectedRow() > -1) {
                drawobject.attributeModel.removeRow(attributeTable.getSelectedRow());
            }
            updateUI();
        }
        if (e.getSource() == Menu_JTBtn[3]) {
            if (attributeTable.getSelectedRow() > 0) {
                int row = attributeTable.getSelectedRow();
                Object obj0, obj1;
                obj0 = drawobject.attributeModel.getValueAt(row, 0);
                obj1 = drawobject.attributeModel.getValueAt(row, 1);
                drawobject.attributeModel.removeRow(row);
                drawobject.attributeModel.insertRow(row - 1, obj0, obj1);
                attributeTable.setRowSelectionInterval(row - 1, row - 1);
            }
            if (methodTable.getSelectedRow() > 0) {
                int row = methodTable.getSelectedRow();
                Object obj0, obj1;
                obj0 = drawobject.methodModel.getValueAt(row, 0);
                obj1 = drawobject.methodModel.getValueAt(row, 1);
                drawobject.methodModel.removeRow(row);
                drawobject.methodModel.insertRow(row - 1, obj0, obj1);
                methodTable.setRowSelectionInterval(row - 1, row - 1);
            }
            updateUI();
        }
        if (e.getSource() == Menu_JTBtn[4]) {
            if (attributeTable.getSelectedRow() != -1
                    && attributeTable.getSelectedRow() < attributeTable.getRowCount() - 1) {
                int rowIdx = attributeTable.getSelectedRow();
                Object obj0, obj1;
                obj0 = drawobject.attributeModel.getValueAt(rowIdx, 0);
                obj1 = drawobject.attributeModel.getValueAt(rowIdx, 1);
                drawobject.attributeModel.removeRow(rowIdx);
                drawobject.attributeModel.insertRow(rowIdx + 1, obj0, obj1);
                attributeTable.setRowSelectionInterval(rowIdx + 1, rowIdx + 1);
            }
            if (methodTable.getSelectedRow() != -1
                    && methodTable.getSelectedRow() < methodTable.getRowCount() - 1) {
                System.out.println(methodTable.getSelectedRow());
                int rowIdx = methodTable.getSelectedRow();
                Object obj0, obj1;
                obj0 = drawobject.methodModel.getValueAt(rowIdx, 0);
                obj1 = drawobject.methodModel.getValueAt(rowIdx, 1);
                drawobject.methodModel.removeRow(rowIdx);
                drawobject.methodModel.insertRow(rowIdx + 1, obj0, obj1);
                methodTable.setRowSelectionInterval(rowIdx + 1, rowIdx + 1);
            }
            updateUI();
        }
    }
}
