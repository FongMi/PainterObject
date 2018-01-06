
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

class DrawObject extends JPanel {
    /*圖形起點、移動起點*/
    Point loc, lp;
    /*圖形寬高, 線條粗細*/
    int width, height, lineWidth;
    /*線條起點、終點*/
    int start, end;
    /*筆刷*/
    Stroke stroke;
    /*顏色*/
    Color color;
    /*圖形*/
    Shape shape;
    /*是否填滿*/
    boolean isFill;
    /*畫筆型態、狀態*/
    Status type, status;
    /*Page*/
    Page page;
    /*外框*/
    ResizeBorder rborder;

    /*UML*/
    UMLMenu uMLMenu;
    JTextField className;
    JTable attributeTable;
    JTable methodTable;
    UMLTableModel attributeModel;
    UMLTableModel methodModel;
    JButton[] Menu_JTBtn;
    String Menu_JTBtnName[] = {"img/add1.png", "img/add2.png", "img/del.png", "img/up.png", "img/down.png"};
    
    
    /*設定形狀、類型、粗細、顏色*/
    DrawObject(Page page, Shape shape, Status type, Stroke stroke, Color color) {
        this.page = page;
        this.shape = shape;
        this.type = type;
        this.stroke = stroke; 
        this.color = color;
        this.lineWidth = page.lineWidth;
    }

    DrawObject(Page page, Status type) {
        this.page = page;
        this.type = type;
        this.rborder = new ResizeBorder(this, Color.RED) ;
        this.status = Status.Selected;
        this.setBorder(rborder);
        this.setBackground(Color.BLACK);
        this.addMouseListener(new MyMouseAdapter());
        this.addMouseMotionListener(new MyMouseAdapter());
        this.addMouseListener(rborder);
        this.addMouseMotionListener(rborder);

        className = new JTextField("ClassName");
        className.setHorizontalAlignment(JTextField.CENTER);
        
        attributeTable =new JTable (attributeModel = new UMLTableModel ());
        attributeTable.addMouseListener(new TableListener());
        attributeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        methodTable =new JTable (methodModel = new UMLTableModel ());
        methodTable .addMouseListener(new TableListener());
        methodTable .setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        uMLMenu = new UMLMenu (this,attributeTable,methodTable);
        
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
        GBC.gridy = 3;
        GBC.weightx = 0.1;
        GBC.weighty = 0.1;
        this.add(uMLMenu, GBC); 
    }
    
    /*設定線條區段*/
    void setSection(int start, int end) {
        this.start = start;
        this.end = end;
    }
    
    void UMLformat(Point loc, int width, int height) {
        this.loc = loc;
        this.setLocation(loc);
        this.setSize(new Dimension(width, height));
    }

    void format(Point loc, int width, int height) {
        /*設定圖形起點、寬高*/
        this.loc = loc;
        this.width = width;
        this.height = height;
        /*預設選擇狀態*/
        this.status = Status.Selected;
        /*預設非填滿*/
        this.isFill = false;
        /*設定大小*/
        this.setSize(new Dimension(width + lineWidth, height + lineWidth));
        /*設定位置*/
        this.setLocation(loc.x - lineWidth / 2, loc.y - lineWidth / 2);
        /*設定外框*/
        this.rborder = new ResizeBorder(this, Color.RED);
        this.setBorder(rborder);
        /*新增滑鼠事件*/
        this.addMouseListener(new MyMouseAdapter());
        this.addMouseListener(rborder);
        this.addMouseMotionListener(new MyMouseAdapter());
        this.addMouseMotionListener(rborder);
        /*變成透明*/
        this.setOpaque(false);
        /*建立圖形*/
        switch (type) {
            case Rectangle:
                shape = new Rectangle2D.Double(lineWidth / 2, lineWidth / 2, width, height);
                break;
            case Round_Rectangle:
                shape = new RoundRectangle2D.Double(lineWidth / 2, lineWidth / 2, width, height, 30, 30);
                break;
            case Oval:
                shape = new Ellipse2D.Double(lineWidth / 2, lineWidth / 2, width, height);
                break;
        }
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        if (stroke != null) {
            g2d.setStroke(stroke);
        }
        if (isFill) {
            g2d.fill(shape);
        }
        if (shape != null) {
            g2d.draw(shape);
        }
    }

    /*滑鼠監聽事件*/
    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            lp = e.getPoint();
            /*如果目前物件是選擇狀態，就變成 Idle*/
            if (page.drawobject.status == Status.Selected) {
                page.drawobject.status = Status.Idle;
                if (page.drawobject.uMLMenu != null) {
                    page.drawobject.uMLMenu.setVisible(false);
                }
            }
            /*如果物件是閒置狀態，就變成 Selected*/
            if (DrawObject.this.status == Status.Idle) {
                DrawObject.this.status = Status.Selected;
                page.drawobject = DrawObject.this;
                if (page.drawobject.uMLMenu != null) {
                    page.drawobject.uMLMenu.setVisible(true);
                }
            }
            /*如果畫筆型態是填滿*/
            if (page.type == Status.Fill) {
                page.drawobject.color = page.penColor;
                page.drawobject.isFill = true;
            }
            page.repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (status == Status.Selected) {
                /*計算移動中的 X Y*/
                int offset_x = DrawObject.this.getX() + e.getX() - lp.x;
                int offset_y = DrawObject.this.getY() + e.getY() - lp.y;
                /*重新設定圖形位置*/
                DrawObject.this.setLocation(offset_x, offset_y);
                page.repaint();
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            if (DrawObject.this.status != Status.Resize) {
                DrawObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (DrawObject.this.status != Status.Resize) {
                DrawObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        }
    }

    class TableListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() == attributeTable) {
                methodTable.clearSelection();
            }
            if (e.getSource() == methodTable) {
                attributeTable.clearSelection();
            }
        }
    }
}
