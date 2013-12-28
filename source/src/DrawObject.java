
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

class DrawObject extends JPanel {
    /*起點、終點、圖形起點、移動起點*/
    static Point p1, p2, loc, lp;
    /*圖形寬高, 線條粗細*/
    int width, height, lineWidth;
    /*線條起點、終點*/
    int start, end;
    /*筆刷*/
    Stroke stroke;
    /*顏色*/
    Color color;
    /*填滿*/
    boolean isFill;
    /*圖形*/
    Shape shape;
    /*畫筆型態、狀態*/
    Status type, status;
    /*Page*/
    Page page;
    /*外框*/
    ResizeBorder rborder;
        
    /*設定形狀、類型、粗細、顏色*/
    DrawObject(Page page, Shape shape, Status type, int lineWidth, Color color) {
        this.page = page;
        this.shape = shape;
        this.type = type;
        this.lineWidth = lineWidth;
        this.color = color;
        stroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    }

    void point(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /*設定線條起點、終點*/
    void format(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    /*設定圖形起點、寬高、填滿、外框*/
    void format(Point loc, int width, int height, boolean isFill) {
        this.loc = loc;
        this.width = width;
        this.height = height;
        this.isFill = isFill;
        this.status = Status.Selected;
        /*新增滑鼠事件、設定外框*/
        rborder = new ResizeBorder(this, Color.RED, 10, 30);
        this.setBorder(rborder);
        this.addMouseListener(new MyMouseAdapter());
        this.addMouseListener(rborder);
        this.addMouseMotionListener(new MyMouseAdapter());
        this.addMouseMotionListener(rborder);
        /*變成透明*/
        this.setOpaque(false);
        /*設定大小*/
        this.setSize(new Dimension(width + lineWidth, height + lineWidth));
        /*設定位置*/
        this.setLocation(loc.x - lineWidth / 2, loc.y - lineWidth / 2);
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
        g2d.setStroke(stroke);
        if (isFill) {
            g2d.fill(shape);
        }
        g2d.draw(shape);
    }

    /*滑鼠監聽事件*/
    class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            lp = e.getPoint();
            /*如果目前物件是選擇狀態，就變成 Idle*/
            if (page.drawobject.status == Status.Selected) {
                page.drawobject.status = Status.Idle;
                page.drawobject.setBorder(null);
            }
            /*如果目前物件是閒置狀態，就變成 Selected*/
            if (DrawObject.this.status == Status.Idle) {
                DrawObject.this.status = Status.Selected;
                DrawObject.this.setBorder(rborder);
                page.drawobject = DrawObject.this;
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            if (status == Status.Selected) {
                /*計算移動中的 X Y*/
                int x = DrawObject.this.getX() + e.getX() - lp.x;
                int y = DrawObject.this.getY() + e.getY() - lp.y;
                /*重新設定圖形位置*/
                DrawObject.this.setLocation(x, y);
            }
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            DrawObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent e) {
            DrawObject.this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
