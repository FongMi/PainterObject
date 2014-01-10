import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class UMLTableModel extends AbstractTableModel {
    Vector content = null;
    String[] title_name = { "TYPE", "NAME" };
    
    UMLTableModel() {
        content = new Vector();
    }
    //新增
    public void addRow(Object obj0, Object obj1) {
        Vector v = new Vector(2);
        v.add(0, obj0);
        v.add(1, obj1);
        content.add(v);
    }
    
    //插入
    public void insertRow(int row, Object obj0, Object obj1) {
        Vector v = new Vector(2);
        v.add(0, obj0);
        v.add(1, obj1);
        content.add(row, v);
    }
    
    //移除
    public void removeRow(int row) {
        if (row < 0)
            return;
        content.remove(row);
    }
    
    //編輯
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0)
            return false;
        return true;
    }
    
    public int getRowCount() {
        return content.size();
    }

    public int getColumnCount() {
        return title_name.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Vector) content.get(rowIndex)).get(columnIndex);
    }

    public void setValueAt(Object value, int row, int col) {
        ((Vector) content.get(row)).remove(col);
        ((Vector) content.get(row)).add(col, value);
        this.fireTableCellUpdated(row, col);
    }

}


