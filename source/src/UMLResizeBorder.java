
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;

public class UMLResizeBorder extends AbstractBorder implements MouseInputListener {

    Color borderColor;
    int rectWidth, rectHeight, nowCorner;
    UMLObject UMLObject;
    UMLMenu UMLMenu;
    Point[] corner;
    Point p = new Point(0, 0);
    
    UMLResizeBorder(UMLObject UMLObject, Color color) {
        this.UMLObject = UMLObject;
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
            if (UMLObject.status == Status.Selected || UMLObject.status == Status.Resize) {
                
                if (UMLObject.status == Status.Resize) {
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

    /*設定移動游標*/
    @Override
    public void mouseMoved(MouseEvent e) {
        if (UMLObject.status == Status.Selected) {
            switch (inCorner(e)) {
                case 0: //西北
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
                    break;
                case 1: //東北
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
                    break;
                case 2: //西南
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
                    break;
                case 3: //東南
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    break;
                case 4: //東
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    break;
                case 5: //南
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
                    break;
                case 6: //西
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    break;
                case 7: //北
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
                    break;
                case -1: //其他地方
                    UMLObject.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    break;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        /*設定狀態 = Resize*/
        if (inCorner(e) != -1) {
            nowCorner = inCorner(e);
            UMLObject.status = Status.Resize;
            p = SwingUtilities.convertPoint(UMLObject, e.getPoint(), UMLObject.getParent());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        /*Resize*/
        
        if (UMLObject.status == Status.Resize) {
            Point newP = SwingUtilities.convertPoint(UMLObject, e.getPoint(), UMLObject.getParent());
            int offset_x = newP.x - p.x;
            int offset_y = newP.y - p.y;
            
            switch (nowCorner) {
                case 0: //西北
                    UMLObject.setBounds(UMLObject.getX() + offset_x, UMLObject.getY() + offset_y, UMLObject.getWidth() - offset_x, UMLObject.getHeight() - offset_y);

                    break;
                case 1: //東北
                    UMLObject.setBounds(UMLObject.getX(), UMLObject.getY() + offset_y, UMLObject.getWidth() + offset_x, UMLObject.getHeight() - offset_y);

                    break;
                case 2: //西南
                    UMLObject.setBounds(UMLObject.getX() + offset_x, UMLObject.getY(), UMLObject.getWidth() - offset_x, UMLObject.getHeight() + offset_y);

                    break;
                case 3: //東南
                    UMLObject.setBounds(UMLObject.getX(), UMLObject.getY(), UMLObject.getWidth() + offset_x, UMLObject.getHeight() + offset_y);

                    break;
                case 4: //東
                    UMLObject.setBounds(UMLObject.getX(), UMLObject.getY(), UMLObject.getWidth() + offset_x, UMLObject.getHeight());

                    break;
                case 5: //南
                    UMLObject.setBounds(UMLObject.getX(), UMLObject.getY(), UMLObject.getWidth(), UMLObject.getHeight() + offset_y);

                    break;
                case 6: //西
                    UMLObject.setBounds(UMLObject.getX() + offset_x, UMLObject.getY(), UMLObject.getWidth() - offset_x, UMLObject.getHeight());

                    break;
                case 7: //北
                    UMLObject.setBounds(UMLObject.getX(), UMLObject.getY() + offset_y, UMLObject.getWidth(), UMLObject.getHeight() - offset_y);

                    break;
            }
            p = newP;
            Component tmep = (Component) e.getSource();
            ((UMLObject) tmep).updateUI();
            tmep.repaint();
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        /* Resize 完狀態 = Selected*/
        Component tmep = (Component) e.getSource();
        if (UMLObject.status == Status.Resize) {
            UMLObject.status = Status.Selected;
            UMLObject.repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
