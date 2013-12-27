
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
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

class DrawObject extends JPanel {
    
    /*起點、終點、圖形起點、移動起點*/
    static Point p1, p2, loc, lp;
    /*圖形寬高*/
    int width, height;
    /*線條起點、終點*/
    int start, end;
    /*顏色*/
    Color color;
    /*畫筆*/
    Stroke stroke;
    /*填滿*/
    boolean isFill;
    /*圖形*/
    Shape shape;
    /*畫筆型態、狀態*/
    Status type, status;
    /*Page*/
    Page page;
    /*滑鼠監聽事件*/
    MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
    /*選擇框*/
    ResizeBorder rborder = new ResizeBorder(Color.RED, 10, 25);
    
    DrawObject(Page page, Shape shape, Status type) {
        this.page = page;
        this.shape = shape;
        this.type = type;
        this.setOpaque(false); /*變成透明*/
        this.addMouseListener(myMouseAdapter);
        this.addMouseMotionListener(myMouseAdapter);
        status = Status.Selected;
        this.setBorder(rborder);
    }

    void point(int start, int end) {
        this.start = start;
        this.end = end;
    }

    /*設定線條起點、終點、顏色、粗細*/
    void format(Point p1, Point p2, Color color, Stroke stroke) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        this.stroke = stroke;
        /*TODO*/
        shape = new Line2D.Double(p1, p2);
        repaint();
    }

    /*設定圖形起點、寬高、顏色、粗細、填滿*/
    void format(Point loc, int width, int height, Color color, int lineWidth, Stroke stroke, boolean isFill) {
        this.loc = loc;
        this.width = width;
        this.height = height;
        this.color = color;
        this.stroke = stroke;
        this.isFill = isFill;
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
            if (page.drawobject.status == Status.Selected) {
                page.drawobject.status = Status.Idle;
                page.drawobject.setBorder(null);
            }
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
                /*設定圖形位置*/
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
