
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
    
    ResizeBorder(DrawObject drawObject, Color color) {
        this.drawObject = drawObject;
        borderColor = color;
        rectWidth = 5;
        rectHeight = 15;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        /*畫角落及邊框*/
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            if (drawObject.status == Status.Selected || drawObject.status == Status.Resize) {
                if (drawObject.status == Status.Resize) {
                    g2d.setColor(Color.BLUE);
                } else {
                    g2d.setColor(borderColor);
                }
                //西北
                g2d.fillRect(x, y, rectWidth, rectHeight);
                g2d.fillRect(x, y, rectHeight, rectWidth);
                //東北
                g2d.fillRect(width - rectWidth, y, rectWidth, rectHeight);
                g2d.fillRect(width - rectHeight, y, rectHeight, rectWidth);
                //西南
                g2d.fillRect(x, height - rectHeight, rectWidth, rectHeight);
                g2d.fillRect(x, height - rectWidth, rectHeight, rectWidth);
                //東南
                g2d.fillRect(width - rectWidth, height - rectHeight, rectWidth, rectHeight);
                g2d.fillRect(width - rectHeight, height - rectWidth, rectHeight, rectWidth);
                //東
                g2d.fillRect(width - rectWidth, (height - rectHeight / 2) / 2, rectWidth, rectHeight / 2);
                //南
                g2d.fillRect((width - rectHeight / 2 + x) / 2, height - rectWidth, rectHeight / 2, rectWidth);
                //西
                g2d.fillRect(x / 2, (height - rectHeight / 2) / 2, rectWidth, rectHeight / 2);
                //北
                g2d.fillRect((width - rectHeight / 2 + x) / 2, y, rectHeight / 2, rectWidth);
                //邊框
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2d.drawRect(x, y, width, height);
                /*儲存每個點*/
                Point[] corner = {new Point(0, 0), new Point(width, 0), new Point(0, height), new Point(width, height),
                    new Point(width, height / 2), new Point(width / 2, height), new Point(0, height / 2), new Point(width / 2, 0)};
                this.corner = corner;
            }
        }
    }

    /*傳回哪個角落*/
    int inCorner(MouseEvent e) {
        Point pt = e.getPoint();
        Component c = (Component) e.getSource();
        /*四角落*/
        for (int i = 0; i < 4; i++) {
            if (Math.pow(pt.x - corner[i].x, 2) + Math.pow(pt.y - corner[i].y, 2) < rectHeight * rectHeight) {
                return i;
            }
        }
        /*東南西北*/
        for (int i = 4; i < corner.length; i++) {
            if (Math.pow(pt.x - corner[i].x, 2) + Math.pow(pt.y - corner[i].y, 2) < rectHeight/2 * rectHeight/2) {
                return i;
            }
        }
        return -1;
    }
    
    void reShape(DrawObject drawObject, int a, int b, int x, int y) {
        Point loc = new Point(0, 0);
        
        loc.x += a + (drawObject.lineWidth / 2);
        loc.y += b + (drawObject.lineWidth / 2);
        
        if (a != 0 && b != 0) {
            loc.x = (drawObject.lineWidth / 2);
            loc.y = (drawObject.lineWidth / 2);
        }
        if (a == 0) {
            loc.y = (drawObject.lineWidth / 2);
        }
        if (b == 0) {
            loc.x = (drawObject.lineWidth / 2);
        }
        
        int width = drawObject.width += x;
        int height = drawObject.height += y;
        
        switch (drawObject.type) {
            case Rectangle:
                drawObject.shape = new Rectangle2D.Double(loc.x, loc.y, width, height);
                break;
            case Round_Rectangle:
                drawObject.shape = new RoundRectangle2D.Double(loc.x, loc.y, width, height, 30, 30);
                break;
            case Oval:
                drawObject.shape = new Ellipse2D.Double(loc.x, loc.y, width, height);
                break;
        }
    }
    
    /*設定移動游標*/
    @Override
    public void mouseMoved(MouseEvent e) {
        if (drawObject.status == Status.Selected) {
            switch (inCorner(e)) {
                case 0: //西北
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    break;
                case 1: //東北
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    break;
                case 2: //西南
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    break;
                case 3: //東南
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    break;
                case 4: //東
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    break;
                case 5: //南
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    break;
                case 6: //西
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    break;
                case 7: //北
                    drawObject.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
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
            int offset_x = newP.x - p.x;
            int offset_y = newP.y - p.y;
            
            switch (nowCorner) {
                case 0: //西北
                    drawObject.setBounds(drawObject.getX() + offset_x, drawObject.getY() + offset_y, drawObject.getWidth() - offset_x, drawObject.getHeight() - offset_y);
                    this.reShape(drawObject, offset_x, offset_y, -offset_x, -offset_y);
                    break;
                case 1: //東北
                    drawObject.setBounds(drawObject.getX(), drawObject.getY() + offset_y, drawObject.getWidth() + offset_x, drawObject.getHeight() - offset_y);
                    this.reShape(drawObject, 0, offset_y, offset_x, -offset_y);
                    break;
                case 2: //西南
                    drawObject.setBounds(drawObject.getX() + offset_x, drawObject.getY(), drawObject.getWidth() - offset_x, drawObject.getHeight() + offset_y);
                    this.reShape(drawObject, offset_x, 0, -offset_x, offset_y);
                    break;
                case 3: //東南
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth() + offset_x, drawObject.getHeight() + offset_y);
                    this.reShape(drawObject, 0, 0, offset_x, offset_y);
                    break;
                case 4: //東
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth() + offset_x, drawObject.getHeight());
                    this.reShape(drawObject, 0, 0, offset_x, 0);
                    break;
                case 5: //南
                    drawObject.setBounds(drawObject.getX(), drawObject.getY(), drawObject.getWidth(), drawObject.getHeight() + offset_y);
                    this.reShape(drawObject, 0, 0, 0, offset_y);
                    break;
                case 6: //西
                    drawObject.setBounds(drawObject.getX() + offset_x, drawObject.getY(), drawObject.getWidth() - offset_x, drawObject.getHeight());
                    this.reShape(drawObject, offset_x, 0, -offset_x, 0);
                    break;
                case 7: //北
                    drawObject.setBounds(drawObject.getX(), drawObject.getY() + offset_y, drawObject.getWidth(), drawObject.getHeight() - offset_y);
                    this.reShape(drawObject, 0, offset_y, 0, -offset_y);
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
