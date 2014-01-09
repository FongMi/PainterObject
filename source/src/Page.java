
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Page extends JPanel {
    /*MainWindow*/
    MainWindow parant;
    /*起始點、結束點、圖形起始點*/
    private Point p1, p2, loc;
    /*圖形寬高、圖形計量、線條起點*/
    private int width, height, shape_counter, Start;
    /*畫筆型式*/
    private Stroke penStroke;
    /*線條粗細*/
    public int lineWidth;
    /*畫筆顏色、橡皮擦顏色*/
    public Color penColor, eraserColor;
    /*星星角數量, 多邊形數量*/
    public int star_arm = 0, polygon_side = 0;
    /*圖形暫存*/
    private Shape shape = null;
    /*DrawObject 暫存*/
    DrawObject drawobject;
    /*Shift 事件*/
    private boolean ShiftDown = false;
    /*畫筆型態、狀態*/
    Status type, status;
    /*儲存線條及圖形*/
    private final HashMap<Integer, DrawObject> shapeList = new HashMap<>();
    /*儲存線條起點終點*/
    private final ArrayList<DrawObject> freeList = new ArrayList();
    /*圖片暫存*/
    BufferedImage image;
    
    Page(MainWindow parant) {
        this.parant = parant;
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.addMouseListener(new myMouseAdapter());
        this.addMouseMotionListener(new myMouseAdapter());
        this.addKeyListener(new myKeyAdapter());
        lineWidth = 2; //粗細預設=2
        shape_counter = 0; //計算圖形數量
        type = Status.Pen; //畫筆型態預設=Pen
        status = Status.Draw; //狀態預設=Draw
        penColor = Color.BLACK; //畫筆顏色
        eraserColor = Color.WHITE; //橡皮擦顏色
        penStroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        /*畫圖片*/
        if (image != null) {
            g2d.drawImage(image, 0, 0, this);
        }
        /*畫出線條及橡皮擦*/
        for (Entry<Integer, DrawObject> entry : shapeList.entrySet()) {
            DrawObject line = entry.getValue();
            switch (line.type) {
                case Pen:
                case Eraser:
                case Line:
                case Triangle:
                case Star:
                case Polygon:
                    g2d.setStroke(line.stroke);
                    g2d.setColor(line.color);
                    g2d.draw(line.shape);
                    break;
            }
        }
        
        /*畫出拖曳軌跡*/
        if (shape != null) {
            if (type == Status.Eraser) {
                g2d.setColor(eraserColor);
            } else {
                g2d.setColor(penColor);
            }
            g2d.setStroke(penStroke);
            g2d.draw(shape);
            shape = null;
        }
    }

    public void Undo() {
        if(shape_counter > 0) {
            switch(shapeList.get(shape_counter).type) {
                case Pen:
                case Eraser:
                    int f_size = freeList.size() - 1;
                    if (freeList.size() > 0 && shape_counter == freeList.get(f_size).end) {
                        int start = freeList.get(f_size).start;
                        int end = freeList.get(f_size).end;
                        for (; end > start; end--) {
                            shapeList.remove(end);
                            shape_counter--;
                        }
                        freeList.remove(f_size);
                    }
                case Line:
                case Triangle:
                case Star:
                case Polygon:
                    shapeList.remove(shape_counter);
                    shape_counter--;
                    break;
                case Rectangle:
                case Round_Rectangle:
                case Oval:
                    Page.this.remove(shapeList.get(shape_counter));
                    shapeList.remove(shape_counter);
                    shape_counter--;
                    break;
            }
        } else if (image != null) {
            image = null;
        }
        repaint();
    }
    
    /*選擇顏色*/
    public void ChooseColor() {
        Color c = JColorChooser.showDialog(this, "選擇顏色", getBackground());
        if (c != null) {
            if (parant.toolBar.colorJTBtn[0].isSelected()) {
                parant.toolBar.setcolorPanel[0].setBackground(c);
            } else if (parant.toolBar.colorJTBtn[1].isSelected()) {
                parant.toolBar.setcolorPanel[1].setBackground(c);
            }
        }
    }

    /*設定線條粗細*/
    public void SetLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
        penStroke = new BasicStroke(this.lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        this.requestFocus();
    }
    
    /*星星圖形*/
    public Shape createStar(int arms, Point center, double rOuter, double rInner) {
        double angle = Math.PI / arms;
        GeneralPath path = new GeneralPath();
        for (int i = 0; i < 2 * arms; i++) {
            double r = (i & 1) == 0 ? rOuter : rInner;
            Point2D.Double p = new Point2D.Double(center.x + Math.cos(i * angle) * r, center.y + Math.sin(i * angle) * r);
            if (i == 0) {
                path.moveTo(p.getX(), p.getY());
            } else {
                path.lineTo(p.getX(), p.getY());
            }
        }
        path.closePath();
        return path;
    }

    /*三角形*/
    public Shape createTriangle(Point p1, Point p2, Point p3) {
        Polygon poly = new Polygon();
        poly.addPoint(p1.x, p1.y);
        poly.addPoint(p2.x, p2.y);
        poly.addPoint(p3.x, p3.y);
        return poly;
    }
    
    /*多邊形*/
    public Shape createPolygon(int arms, Point p1, int width) {
        Polygon poly = new Polygon();
        for (int i = 0; i < arms; i++) {
            poly.addPoint((int) (p1.x + width * Math.cos(i * 2 * Math.PI / arms)), (int) (p1.y + width * Math.sin(i * 2 * Math.PI / arms)));
        }
        return poly;
    }

    /*鍵盤監聽事件*/
    class myKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            /*Ctrl + Z 復原*/
            if (e.getKeyCode() == KeyEvent.VK_Z && e.isControlDown()) {
                Undo();
            }
            /*Ctrl + + 變粗*/
            if (e.getKeyCode() == KeyEvent.VK_ADD && e.isControlDown() && lineWidth < 30) {
                SetLineWidth(lineWidth + 1);
            }
            /*Ctrl + - 變細*/
            if (e.getKeyCode() == KeyEvent.VK_SUBTRACT && e.isControlDown() && lineWidth > 2) {
                SetLineWidth(lineWidth - 1);
            }
            /*按下Shift*/
            if (e.isShiftDown()) {
                ShiftDown = true;
            }
            /*按下Delete 刪除選取物件*/
            if (e.getKeyCode()== KeyEvent.VK_DELETE) {
                if (drawobject != null && drawobject.status == Status.Selected) {
                    Page.this.remove(drawobject);
                    repaint();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            /*放開Shift*/
            if (!e.isShiftDown()) {
                ShiftDown = false;
            }
        }
    }

    /*滑鼠監聽事件*/
    class myMouseAdapter extends MouseAdapter {       
        @Override
        public void mousePressed(MouseEvent e) {
            /*取得起點*/
            p1 = e.getPoint();
            
            /*在Page上點擊將 drawobject 狀態變成 Idle*/
            if (drawobject != null && drawobject.status == Status.Selected) {
                drawobject.status = Status.Idle;
                drawobject.repaint();
            }
            
            switch (type) {
                case Pen:
                case Eraser:
                    /*設定線條起點*/
                    Start = shape_counter + 1;
                    break;
                case Fill:
                    /*設定背景色彩*/
                    Page.this.setBackground(penColor);
                    break;
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            /*取得拖曳中的點*/
            p2 = e.getPoint();
            
            /*計算圖形長寬*/
            width = Math.abs(p2.x - p1.x);
            height = Math.abs(p2.y - p1.y);
            
            /*計算圖形起點*/
            loc = new Point(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y));
            
            /*按下Shift 寬=高*/
            if (ShiftDown) {
                width = height;
            }
            
            /*狀態 = Drawing*/
            if (status == Status.Draw) {
                status = Status.Drawing;
            }

            switch (type) {
                case Pen:
                    /*畫出線條*/
                    shape = new Line2D.Double(p1, p2);
                    /*建立物件，設定粗細、顏色*/
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    /*加到HashMap*/
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    /*更新起點*/
                    p1 = p2;
                    break;
                case Eraser:
                    shape = new Line2D.Double(p1, p2);
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, eraserColor);
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    p1 = p2;
                    break;
                case Line:
                    shape = new Line2D.Double(p1, p2);
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    break;
                case Triangle:
                    shape = createTriangle(p1, new Point(p1.x, p2.y), p2);
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    break;
                case Star:
                    shape = createStar(star_arm, p1, width, width / 3);
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    break;
                case Polygon:
                    shape = createPolygon(polygon_side, p1, width);
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    break;
                case Rectangle:
                    shape = new Rectangle2D.Double(loc.x, loc.y, width, height);
                    break;
                case Round_Rectangle:
                    shape = new RoundRectangle2D.Double(loc.x, loc.y, width, height, 30, 30);
                    break;
                case Oval:
                    shape = new Ellipse2D.Double(loc.x, loc.y, width, height);
                    break;
            }
            repaint();
            parant.statusBar.setText("滑鼠座標: (" + e.getX() + "," + e.getY() + ")");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (status != Status.Drawing) {
                return;
            }
            switch (type) {
                case Pen:
                case Eraser:
                    /*設定線條區段*/
                    drawobject.setSection(Start, shape_counter);
                    /*新增線條區段*/
                    freeList.add(drawobject);
                    break;
                case Line:
                case Triangle:
                case Star:
                case Polygon:
                    /*加到HashMap*/
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    break;
                case Rectangle:
                case Round_Rectangle:
                case Oval:
                    /*建立物件，設定粗細、顏色*/
                    drawobject = new DrawObject(Page.this, shape, type, penStroke, penColor);
                    /*設定起點、寬高、填滿*/
                    drawobject.format(loc, width, height);
                    /*加到HashMap*/
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    /*加到 Page 畫面, 0表示永遠在最上層*/
                    Page.this.add(drawobject, 0);
                    break;
            }
            repaint();
            /*狀態 = Draw*/
            status = Status.Draw;
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            parant.statusBar.setText("滑鼠座標: (" + e.getX() + "," + e.getY() + ")");
        }
    }

    /*開新檔案*/
    public void NewPage() {
        /*清空 shapeList*/
        shapeList.clear();
        /*清空 freeList*/
        freeList.clear();
        /*清空畫面*/
        this.removeAll();
        image = null;
        shape_counter = 0;
        repaint();
    }

    /*開啟舊檔*/
    public void Open() {
        JFileChooser Open_JC = new JFileChooser();
        Open_JC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        Open_JC.setDialogTitle("開啟檔案");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "png");
        Open_JC.setFileFilter(filter);
        int result = Open_JC.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = Open_JC.getSelectedFile();
            try {
                NewPage();
                image = ImageIO.read(new File(file.getAbsolutePath()));
                this.setPreferredSize(new Dimension(this.getWidth(), image.getHeight()));
                repaint();
            } catch (IOException e) {
            }
        }
    }

    /*儲存檔案*/
    public void Save() {
        JFileChooser Save_JC = new JFileChooser();
        Save_JC.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY);
        Save_JC.setDialogTitle("儲存檔案");
        int result = Save_JC.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = Save_JC.getSelectedFile().getAbsolutePath();
            BufferedImage image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            this.paint(g);
            if (path != null) {
                try {
                    File file = new File(path, "未命名" + getDateTime() + ".png");
                    ImageIO.write(image, "png", file);
                } catch (IOException ex) {
                }
            }
        }
    }

    /*取得現在時間*/
    public String getDateTime() {
        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date date = new Date();
        String strDate = sdFormat.format(date);
        return strDate;
    }

    /*灰階化*/
    public void Convert() {
        if (image == null) {
            return;
        }
        width = image.getWidth();
        height = image.getHeight();
        int gray;
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);
        for (int y = 0; y < height; y++) {
            int index = y * width;
            for (int x = 0; x < width; x++) {
                int rgb = pixels[index];
                int r = (rgb & 0xff0000) >> 16;
                int g = (rgb & 0x00ff00) >> 8;
                int b = rgb & 0x0000ff;
                gray = (r + g + b) / 3;
                pixels[index++] = (0xff000000 | (gray << 16) | (gray << 8) | gray);
            }
        }
        image.setRGB(0, 0, width, height, pixels, 0, width);
        repaint();
    }
    
    /*擷取螢幕*/
    public void ScreenShot() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        Rectangle screenRect = new Rectangle(screenSize);
        try {
            parant.setVisible(false);
            Thread.sleep(300);
            Robot robot = new Robot();
            BufferedImage screen_shot = robot.createScreenCapture(screenRect);
            parant.setVisible(true);
            ImageIO.write(screen_shot, "png", new File("ScreenShot-" + getDateTime() + ".png"));
        } catch (Exception ex) {
            Logger.getLogger(Page.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
