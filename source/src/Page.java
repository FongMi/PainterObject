
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;

public class Page extends JPanel {
    /*起始點、結束點、圖形起始點*/
    private Point p1, p2, loc;
    /*圖形寬高、圖形計量、線條起點*/
    private int width, height, shape_counter, Start;
    /*線條粗細*/
    public int lineWidth;
    /*畫筆顏色、橡皮擦顏色*/
    private Color PenColor, EraserColor;
    /*畫筆型式*/
    private Stroke PenStroke;
    /*圖形暫存*/
    private Shape shape = null;
    /*DrawObject 暫存*/
    DrawObject drawobject;
    /*Shift 事件*/
    private boolean isShiftDown = false;
    /*是否要填滿*/
    public boolean isFill = false;
    /*畫筆型態、狀態*/
    Status type, status;
    /*儲存線條及圖形*/
    private final HashMap<Integer, DrawObject> shapeList = new HashMap<>();
    /*儲存線條起點終點*/
    private final ArrayList<DrawObject> freeList = new ArrayList();
   
    Page(MainWindow parant) {
        this.setBackground(Color.WHITE);
        this.setLayout(null);
        this.addMouseListener(new myMouseAdapter());
        this.addMouseMotionListener(new myMouseAdapter());
        this.addKeyListener(new myKeyAdapter());
        lineWidth = 2; //粗細預設=2
        shape_counter = 0; //計算圖形數量
        type = Status.Pen; //畫筆型態預設=Pen
        status = Status.Draw; //狀態預設=Draw
        PenStroke = new BasicStroke(lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        /*畫出線條及橡皮擦*/
        for (Entry<Integer, DrawObject> entry : shapeList.entrySet()) {
            DrawObject line = entry.getValue();
            switch (line.type) {
                case Pen:
                case Eraser:
                case Line:
                    g2d.setStroke(line.stroke);
                    g2d.setColor(line.color);
                    g2d.draw(line.shape);
                    break;
            }
        }
        /*畫出拖曳軌跡*/
        if (shape != null) {
            if (type == Status.Eraser) {
                g2d.setColor(EraserColor);
            } else {
                g2d.setColor(PenColor);
            }
            if (isFill) {
                g2d.fill(shape);
            }
            g2d.setStroke(PenStroke);
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
        }
        repaint();
    }
    
    /*選擇顏色*/
    public void ChooseColor() {
        Color c = JColorChooser.showDialog(this, "選擇顏色", getBackground());
        if (c != null) {
            if (ToolBar.colorJTBtn[0].isSelected()) {
                ToolBar.setcolorPanel[0].setBackground(c);
            } else if (ToolBar.colorJTBtn[1].isSelected()) {
                ToolBar.setcolorPanel[1].setBackground(c);
            }
        }
    }

    /*設定筆刷粗細*/
    public void SetStroke(int lineWidth) {
        this.lineWidth = lineWidth;
        PenStroke = new BasicStroke(this.lineWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
        this.requestFocus();
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
                SetStroke(lineWidth + 1);
            }
            /*Ctrl + - 變細*/
            if (e.getKeyCode() == KeyEvent.VK_SUBTRACT && e.isControlDown() && lineWidth > 2) {
                SetStroke(lineWidth - 1);
            }
            /*按下Shift*/
            if (e.isShiftDown()) {
                isShiftDown = true;
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
                isShiftDown = false;
            }
        }
    }

    /*滑鼠監聽事件*/
    class myMouseAdapter extends MouseAdapter {       
        @Override
        public void mousePressed(MouseEvent e) {
            /*取得起點*/
            p1 = e.getPoint();
            
            /*取得顏色*/
            PenColor = ToolBar.setcolorPanel[0].getBackground();
            EraserColor = ToolBar.setcolorPanel[1].getBackground();
            
            /*在Page上點擊將 drawobject 狀態變成 Idle*/
            if (drawobject != null && drawobject.status == Status.Selected) {
                drawobject.status = Status.Idle;
                drawobject.repaint();
            }
            
            /*設定線條起點*/
            switch (type) {
                case Pen:
                case Eraser:
                    Start = shape_counter + 1;
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
            if (isShiftDown) {
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
                    drawobject = new DrawObject(Page.this, shape, type, PenStroke, PenColor);
                    /*設定起點、終點*/
                    drawobject.format(p1, p2);
                    /*加到HashMap*/
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    /*更新起點*/
                    p1 = p2;
                    break;
                case Eraser:
                    shape = new Line2D.Double(p1, p2);
                    drawobject = new DrawObject(Page.this, shape, type, PenStroke, EraserColor);
                    drawobject.format(p1, p2);
                    shape_counter++;
                    shapeList.put(shape_counter, drawobject);
                    p1 = p2;
                    break;
                case Line:
                    shape = new Line2D.Double(p1, p2);
                    drawobject = new DrawObject(Page.this, shape, type, PenStroke, PenColor);
                    drawobject.format(p1, p2);
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
            MainWindow.statusBar.setText("滑鼠座標: (" + e.getX() + "," + e.getY() + ")");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (status == Status.Drawing) {
                switch (type) {
                    case Pen:
                    case Eraser:
                        /*設定線條區段*/
                        drawobject.setSection(Start, shape_counter);
                        /*新增線條區段*/
                        freeList.add(drawobject);
                        break;
                    case Line:
                        /*加到HashMap*/
                        shape_counter++;
                        shapeList.put(shape_counter, drawobject);
                        break;
                    case Rectangle:
                    case Round_Rectangle:
                    case Oval:
                        /*建立物件，設定粗細、顏色*/
                        drawobject = new DrawObject(Page.this, shape, type, PenStroke, PenColor);
                        /*設定起點、寬高、填滿*/
                        drawobject.format(loc, width, height, isFill);
                        /*加到HashMap*/
                        shape_counter++;
                        shapeList.put(shape_counter, drawobject);
                        /*加到 Page 畫面*/
                        Page.this.add(drawobject);
                        break;
                }
                repaint();
                /*狀態 = Draw*/
                status = Status.Draw;
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            MainWindow.statusBar.setText("滑鼠座標: (" + e.getX() + "," + e.getY() + ")");
        }
    }

    /*清除畫面*/
    public void NewPage() {
        /*清空 shapeList*/
        shapeList.clear();
        /*清空 freeList*/
        freeList.removeAll(freeList);
        /*清空畫面*/
        this.removeAll();
        shape_counter = 0;
        repaint();
    }

    /*開啟檔案*/
    /*public void Open() {
        JFileChooser Open_JC = new JFileChooser();
        Open_JC.setFileSelectionMode(JFileChooser.FILES_ONLY);
        Open_JC.setDialogTitle("開啟檔案");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Images", "jpg", "gif", "png");
        Open_JC.setFileFilter(filter);
        int result = Open_JC.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = Open_JC.getSelectedFile();
            try {
                image = ImageIO.read(new File(file.getAbsolutePath()));
                repaint();
            } catch (IOException e) {
            }
        }
    }*/

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
                    File file = new File(path, "未命名.png");
                    ImageIO.write(image, "png", file);
                } catch (IOException ex) { }
            }
        }
    }
}
