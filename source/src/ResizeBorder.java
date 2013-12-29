
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class ResizeBorder extends AbstractBorder implements MouseInputListener {
    Color borderColor;
    int rectWidth, rectHeight;
    DrawObject drawObject;
    Point[] corner;
    Point p = new Point(0, 0);
    
    public ResizeBorder(DrawObject drawObject, Color color, int w, int h) {
        this.drawObject = drawObject;
        borderColor = color;
        rectWidth = w;
        rectHeight = h;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        /*畫角落及邊框*/
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(borderColor);
            //左上角
            g2d.fillRect(x, y, rectWidth, rectHeight);
            g2d.fillRect(x, y, rectHeight, rectWidth);
            //右上角
            g2d.fillRect(width - rectWidth, y, rectWidth, rectHeight);
            g2d.fillRect(width - rectHeight, y, rectHeight, rectWidth);
            //左下角
            g2d.fillRect(x, height - rectHeight, rectWidth, rectHeight);
            g2d.fillRect(x, height - rectWidth, rectHeight, rectWidth);
            //右下角
            g2d.fillRect(width - rectWidth, height - rectHeight, rectWidth, rectHeight);
            g2d.fillRect(width - rectHeight, height - rectWidth, rectHeight, rectWidth);
            //邊框
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g2d.drawRect(x, y, width, height);
        }
        /*儲存角落四個點*/
        Point[] corner = {new Point(0, 0), new Point(width, 0), new Point(0, height), new Point(width, height)};
        this.corner = corner;
    }

    /*傳回哪個角落*/
    int inCorner(MouseEvent e) {
        Point pt = e.getPoint();
        Component c = (Component) e.getSource();
        for (int i = 0; i < corner.length; i++) {
            if (Math.pow(pt.x - corner[i].x, 2) + Math.pow(pt.y - corner[i].y, 2) < 30 * 30) {
                return i;
            }
        }
        return -1;
    }
    
    void reShape(DrawObject drawObject, Point newP) {
        switch (drawObject.type) {
            case Rectangle:
                drawObject.shape = new Rectangle2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (newP.x - p.x), drawObject.height += (newP.y - p.y));
                break;
            case Round_Rectangle:
                drawObject.shape = new RoundRectangle2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (newP.x - p.x), drawObject.height += (newP.y - p.y), 30, 30);
                break;
            case Oval:
                drawObject.shape = new Ellipse2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (newP.x - p.x), drawObject.height += (newP.y - p.y));
                break;
        }
    }
    
    /*設定移動游標*/
    @Override
    public void mouseMoved(MouseEvent e) {
        if (drawObject.status == Status.Selected) {
            switch (inCorner(e)) {
                case 0: //左上角
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    break;
                case 1: //右上角
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    break;
                case 2: //左下角
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    break;
                case 3: //右下角
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    break;
                case -1: //其他地方
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*設定狀態 = Resize*/
        switch (inCorner(e)) {
            case 0:
            case 1:
            case 2:
            case 3:
                drawObject.status = Status.Resize;
                p = SwingUtilities.convertPoint(drawObject, e.getPoint(),drawObject.getParent());
                break;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*開始 Resize*/
        if (drawObject.status == Status.Resize) {
            switch (inCorner(e)) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    Point newP = SwingUtilities.convertPoint(drawObject, e.getPoint(), drawObject.getParent());
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth() + (newP.x - p.x), drawObject.getHeight() + (newP.y - p.y));
                    this.reShape(drawObject, newP);
                    p = newP;
                    break;
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        /* Resize 完狀態 = Selected*/
        if (drawObject.status == Status.Resize) {
            drawObject.status = Status.Selected;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
}
