
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.TableColumnModel;

class UMLObject extends JPanel {

    UMLOListener UMLOListener = new UMLOListener();
    TableListener TableListener = new TableListener();
    UMLMenu UMLMenu;
    Page Page;
    UMLResizeBorder rborder;
    JTextField className;
    JTable attributeTable ;
    JTable methodTable ;
    UMLTableModel attributeModel;
    UMLTableModel methodModel;

    int width, height;

    Status status;
    Dimension Size;
    Point Location;
    JButton[] Menu_JTBtn;
    String Menu_JTBtnName[] = {"img/add1.png", "img/add2.png", "img/del.png", "img/up.png", "img/down.png"};



    UMLObject(Page Page) {

         
        status = Status.Idle;
        rborder = new UMLResizeBorder (this, Color.RED) ;
        
        
        this.setBorder(rborder);
        this.Page = Page;
        this.setBackground(Color.BLACK);
        this.addMouseListener(UMLOListener);
        this.addMouseMotionListener(UMLOListener);
        this.addMouseListener(rborder);
        this.addMouseMotionListener(rborder);

        className = new JTextField("ClassName");
        className.setHorizontalAlignment(JTextField.CENTER);
        
        attributeTable =new JTable (attributeModel = new UMLTableModel ());
        attributeTable.addMouseListener(TableListener);
        attributeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        methodTable =new JTable (methodModel = new UMLTableModel ());
        methodTable .addMouseListener(TableListener);
        methodTable .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        UMLMenu = new UMLMenu (this,attributeTable,methodTable);
        
        GridBagLayout GB = new GridBagLayout();
        GridBagConstraints GBC = new GridBagConstraints();
        this.setLayout(GB);
        GBC.insets = new Insets(10,10,2,10);
        GBC.fill = GridBagConstraints.BOTH;
        GBC.gridx = 0;
        GBC.gridy = 0;
        GBC.weightx = 2;
        GBC.weighty = 2;
        this.add(className, GBC);
        
        
        TableColumnModel tcm = attributeTable.getColumnModel();
        tcm.getColumn(0).setMaxWidth(10);
        GBC.insets = new Insets(2,10,1,10);
        GBC.gridx = 0;
        GBC.gridy = 1;
        GBC.weightx = 20;
        GBC.weighty = 20;
        this.add(attributeTable , GBC);
        
        TableColumnModel tcm2 = methodTable.getColumnModel();
        tcm2.getColumn(0).setMaxWidth(10);
        GBC.insets = new Insets(0,10,10,10);
        GBC.gridy = 2;
        GBC.weightx = 20;
        GBC.weighty = 20;
        this.add(methodTable , GBC);
        
        
        GBC.insets = new Insets(2,10,10,10);
        //GBC.fill = GridBagConstraints.BOTH;
        //GBC.anchor = GridBagConstraints.NORTHEAST;
        GBC.gridy = 3;
        GBC.weightx = 0.1;
        GBC.weighty = 0.1;
        this.add(UMLMenu, GBC); 
        
    }

    void format(Point Location, int width, int height) {
        this.Location = Location;
        this.setLocation(Location);
        this.setSize(this.Size = new Dimension(width, height));
    }


    class UMLOListener extends MouseAdapter {
        Point lp;

        public void mousePressed(MouseEvent e) {
            
            lp = e.getPoint();
            
            if (Page.activeUMLO.status == Status.Selected) {
                Page.activeUMLO.status = Status.Idle;
                Page.activeUMLO.UMLMenu.setVisible(false);
            }
            
            if (UMLObject.this.status == Status.Idle) {
                UMLObject.this.status = Status.Selected;
                Page.activeUMLO = UMLObject.this;
                Page.activeUMLO.UMLMenu.setVisible(true);
            }
            Page.repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (UMLObject.this.status == Status.Selected) {
                int x = UMLObject.this.getX() + e.getX() - lp.x;
                int y = UMLObject.this.getY() + e.getY() - lp.y;
                UMLObject.this.setLocation(x, y);
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }
        
        
        @Override
        public void mouseEntered(MouseEvent e) {
            if(UMLObject.this.status == Status.Selected) 
                UMLObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            UMLObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
        

         }
         
         class TableListener extends MouseAdapter {
             
         public void mouseClicked(MouseEvent e) {
             if(e.getSource()== attributeTable)
                 methodTable.clearSelection();
             if(e.getSource()== methodTable)
                 attributeTable.clearSelection();
         }
        
    }
}
