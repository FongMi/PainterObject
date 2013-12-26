
import java.awt.Color;
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

    Point p1, p2;
    int left, top, width, height;
    int start, end;
    Color color;
    Stroke stroke;
    boolean isFill;
    Shape shape;
    Status status;

    Point p;
    
    DrawObject(Shape shape, Status status) {
        this.shape = shape;
        this.status = status;
        this.setOpaque(true);
        this.addMouseListener(new myMouseAdapter());
        this.addMouseMotionListener(new myMouseAdapter());
    }

    void format(Point p1, Point p2, Color color, Stroke stroke) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        this.stroke = stroke;
        this.setBounds(Math.min(p1.x,p2.x), Math.min(p1.y,p2.y), Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        shape = new Line2D.Double(0, 0, Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        repaint();
    }

    void format(int left, int top, int width, int height, Color color, Stroke stroke, boolean isFill) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
        this.color = color;
        this.stroke = stroke;
        this.isFill = isFill;
        this.setBounds(left, top, width, height);
        switch (status) {
            case Rectangle:
                shape = new Rectangle2D.Double(0, 0, width, height);
                break;
            case Round_Rectangle:
                shape = new RoundRectangle2D.Double(0, 0, width, height, 30, 30);
                break;
            case Oval:
                shape = new Ellipse2D.Double(0, 0, width, height);
                break;
        }
        repaint();
    }

    void point(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(color);
        g2d.setStroke(stroke);
        if(isFill)
            g2d.fill(shape);
        g2d.draw(shape);
    }
    
    class myMouseAdapter extends MouseAdapter {
        @Override
        public void mousePressed(MouseEvent e) {
            p = e.getPoint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            int x = DrawObject.this.getX() + e.getX() - p.x;
            int y = DrawObject.this.getY() + e.getY() - p.y;
            DrawObject.this.setLocation(x, y);
        }
    }
}
