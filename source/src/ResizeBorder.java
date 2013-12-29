
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
    int rectWidth, rectHeight, nowCorner;
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
            if (drawObject.status == Status.Resize) {
                g2d.setColor(Color.BLUE);
            } else {
                g2d.setColor(borderColor);
            }
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
            //東
            g2d.fillRect(width - rectWidth, (height - rectHeight) / 2, rectWidth, rectHeight);
            //南
            g2d.fillRect((width - rectHeight + x) / 2, height - rectWidth, rectHeight, rectWidth);
            //邊框
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
            g2d.drawRect(x, y, width, height);
        }
        /*儲存每個點*/
        Point[] corner = {new Point(0, 0), new Point(width, 0), new Point(0, height), new Point(width, height), new Point(width, height / 2), new Point(width / 2, height)};
        this.corner = corner;
    }

    /*傳回哪個角落*/
    int inCorner(MouseEvent e) {
        Point pt = e.getPoint();
        Component c = (Component) e.getSource();
        for (int i = 0; i < corner.length; i++) {
            if (Math.pow(pt.x - corner[i].x, 2) + Math.pow(pt.y - corner[i].y, 2) < 15 * 15) {
                return i;
            }
        }
        return -1;
    }
    
    void reShape(DrawObject drawObject, int x, int y) {
        if (x == 0) {
            p.x = 0;
        }
        if (y == 0) {
            p.y = 0;
        }
        switch (drawObject.type) {
            case Rectangle:
                drawObject.shape = new Rectangle2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (x - p.x), drawObject.height += (y - p.y));
                break;
            case Round_Rectangle:
                drawObject.shape = new RoundRectangle2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (x - p.x), drawObject.height += (y - p.y), 30, 30);
                break;
            case Oval:
                drawObject.shape = new Ellipse2D.Double(drawObject.lineWidth / 2, drawObject.lineWidth / 2, drawObject.width += (x - p.x), drawObject.height += (y - p.y));
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
                case 4: //東
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    break;
                case 5: //南
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
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
        if (inCorner(e) != -1) {
            nowCorner = inCorner(e);
            drawObject.status = Status.Resize;
            p = SwingUtilities.convertPoint(drawObject, e.getPoint(), drawObject.getParent());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*開始 Resize*/
        if (drawObject.status == Status.Resize) {
            Point newP = SwingUtilities.convertPoint(drawObject, e.getPoint(), drawObject.getParent());
            switch (nowCorner) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth() + (newP.x - p.x), drawObject.getHeight() + (newP.y - p.y));
                    this.reShape(drawObject, newP.x, newP.y);
                    break;
                case 4:
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth() + (newP.x - p.x), drawObject.getHeight());
                    this.reShape(drawObject, newP.x, 0);
                    break;
                case 5:
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth(), drawObject.getHeight() + (newP.y - p.y));
                    this.reShape(drawObject, 0, newP.y);
                    break;
            }
            p = newP;
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        /* Resize 完狀態 = Selected*/
        if (drawObject.status == Status.Resize) {
            drawObject.status = Status.Selected;
            drawObject.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    
}
