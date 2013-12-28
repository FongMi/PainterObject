
import javax.swing.border.*;
import java.awt.*;

public class ResizeBorder extends AbstractBorder {
    private final Color borderColor;
    private final int rectWidth, rectHeight;
    private final boolean selected = true;
    
    public ResizeBorder(Color color, int w, int h) {
        borderColor = color;
        rectWidth = w;
        rectHeight = h;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        super.paintBorder(c, g, x, y, width, height);
        if (g instanceof Graphics2D) {
            Graphics2D g2d = (Graphics2D) g;
            if (selected /*|| usedLarge*/) {
                g2d.setColor(borderColor);
                // left-up corner.
                g2d.fillRect(x, y, rectWidth, rectHeight);
                g2d.fillRect(x, y, rectHeight, rectWidth);
                // right-up corner.
                g2d.fillRect(width - rectWidth, y, rectWidth, rectHeight);
                g2d.fillRect(width - rectHeight, y, rectHeight, rectWidth);
                // left-bottom corner.
                g2d.fillRect(x, height - rectHeight, rectWidth, rectHeight);
                g2d.fillRect(x, height - rectWidth, rectHeight, rectWidth);
                // right-bottom corner.
                g2d.fillRect(width - rectWidth, height - rectHeight, rectWidth, rectHeight);
                g2d.fillRect(width - rectHeight, height - rectWidth, rectHeight, rectWidth);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
                g2d.drawRect(x, y, width, height);
            }
        }
    }
}
