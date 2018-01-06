
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.*;
import javax.swing.SwingUtilities;
import javax.swing.border.*;
import javax.swing.event.MouseInputListener;

public class ResizeBorder extends AbstractBorder implements MouseInputListener {

    Color borderColor;
    int rectWidth, rectHeight, nowCorner;
    DrawObject drawobject;
    UMLMenu UMLMenu;
    Point[] corner;
    Point p = new Point(0, 0);

    ResizeBorder(DrawObject drawobject, Color color) {
        this.drawobject = drawobject;
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
            if (drawobject.status == Status.Selected || drawobject.status == Status.Resize) {
                if (drawobject.status == Status.Resize) {
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
            if (Math.pow(pt.x - corner[i].x, 2) + Math.pow(pt.y - corner[i].y, 2) < rectHeight / 2 * rectHeight / 2) {
                return i;
            }
        }
        return -1;
    }

    void reShape(DrawObject drawobject, int a, int b, int x, int y) {
        Point loc = new Point(0, 0);

        loc.x += a + (drawobject.lineWidth / 2);
        loc.y += b + (drawobject.lineWidth / 2);

        if (a != 0 && b != 0) {
            loc.x = (drawobject.lineWidth / 2);
            loc.y = (drawobject.lineWidth / 2);
        }
        if (a == 0) {
            loc.y = (drawobject.lineWidth / 2);
        }
        if (b == 0) {
            loc.x = (drawobject.lineWidth / 2);
        }

        int width = drawobject.width += x;
        int height = drawobject.height += y;

        switch (drawobject.type) {
            case Rectangle:
                drawobject.shape = new Rectangle2D.Double(loc.x, loc.y, width, height);
                break;
            case Round_Rectangle:
                drawobject.shape = new RoundRectangle2D.Double(loc.x, loc.y, width, height, 30, 30);
                break;
            case Oval:
                drawobject.shape = new Ellipse2D.Double(loc.x, loc.y, width, height);
                break;
        }
    }

    /*設定移動游標*/
    @Override
    public void mouseMoved(MouseEvent e) {
        if (drawobject.status == Status.Selected) {
            switch (inCorner(e)) {
                case 0: //西北
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    break;
                case 1: //東北
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    break;
                case 2: //西南
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    break;
                case 3: //東南
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    break;
                case 4: //東
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    break;
                case 5: //南
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    break;
                case 6: //西
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    break;
                case 7: //北
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    break;
                case -1: //其他地方
                    drawobject.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*設定狀態 = Resize*/
        if (inCorner(e) != -1) {
            nowCorner = inCorner(e);
            drawobject.status = Status.Resize;
            p = SwingUtilities.convertPoint(drawobject, e.getPoint(), drawobject.getParent());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*開始 Resize*/
        if (drawobject.status == Status.Resize) {
            Point newP = SwingUtilities.convertPoint(drawobject, e.getPoint(), drawobject.getParent());
            int offset_x = newP.x - p.x;
            int offset_y = newP.y - p.y;
            switch (nowCorner) {
                case 0: //西北
                    drawobject.setBounds(drawobject.getX() + offset_x, drawobject.getY() + offset_y, drawobject.getWidth() - offset_x, drawobject.getHeight() - offset_y);
                    this.reShape(drawobject, offset_x, offset_y, -offset_x, -offset_y);
                    break;
                case 1: //東北
                    drawobject.setBounds(drawobject.getX(), drawobject.getY() + offset_y, drawobject.getWidth() + offset_x, drawobject.getHeight() - offset_y);
                    this.reShape(drawobject, 0, offset_y, offset_x, -offset_y);
                    break;
                case 2: //西南
                    drawobject.setBounds(drawobject.getX() + offset_x, drawobject.getY(), drawobject.getWidth() - offset_x, drawobject.getHeight() + offset_y);
                    this.reShape(drawobject, offset_x, 0, -offset_x, offset_y);
                    break;
                case 3: //東南
                    drawobject.setBounds(drawobject.getX(), drawobject.getY(), drawobject.getWidth() + offset_x, drawobject.getHeight() + offset_y);
                    this.reShape(drawobject, 0, 0, offset_x, offset_y);
                    break;
                case 4: //東
                    drawobject.setBounds(drawobject.getX(), drawobject.getY(), drawobject.getWidth() + offset_x, drawobject.getHeight());
                    this.reShape(drawobject, 0, 0, offset_x, 0);
                    break;
                case 5: //南
                    drawobject.setBounds(drawobject.getX(), drawobject.getY(), drawobject.getWidth(), drawobject.getHeight() + offset_y);
                    this.reShape(drawobject, 0, 0, 0, offset_y);
                    break;
                case 6: //西
                    drawobject.setBounds(drawobject.getX() + offset_x, drawobject.getY(), drawobject.getWidth() - offset_x, drawobject.getHeight());
                    this.reShape(drawobject, offset_x, 0, -offset_x, 0);
                    break;
                case 7: //北
                    drawobject.setBounds(drawobject.getX(), drawobject.getY() + offset_y, drawobject.getWidth(), drawobject.getHeight() - offset_y);
                    this.reShape(drawobject, 0, offset_y, 0, -offset_y);
                    break;
            }
            p = newP;
            Component tmep = (Component) e.getSource();
            ((DrawObject) tmep).updateUI();
            tmep.repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /* Resize 完狀態 = Selected*/
        if (drawobject.status == Status.Resize) {
            drawobject.status = Status.Selected;
            drawobject.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
