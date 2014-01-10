
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ToolBar extends JPanel implements ActionListener {
    /*MainWindow*/
    MainWindow parant;
    
    /*ToolBar*/
    JToolBar[] toolBar;
    String toolBarName[] = {"工具", "形狀", "大小", "復原　選擇顏色　截圖", "色彩1　色彩2", "調色盤"};
    
    /*工具按鈕*/
    JToggleButton[] pen_JTBtn;
    String penBtnName[][] = {{"Pen", "鉛筆，使用選取的線條寬度繪製任意形狀的線條"},
                             {"Eraser", "橡皮擦，清除圖片的的一部份，並以背景色彩取代"},
                             {"Fill", "填入色彩，在畫布上的某個區域按一下，以色彩1填滿"},
                             {"Class", "UML裡具有ClassDiagram的功能"}};
    String penImage[] = {"img/pencil.png", "img/eraser.png", "img/fill.png","img/Class.png"};
    
    /*形狀按鈕*/
    JToggleButton[] shape_JTBtn;
    String shapeBtnName[][] = {{"Line", "直線"}, {"Rectangle", "矩形"}, {"Round_Rectangle", "圓角矩形"}, {"Oval", "橢圓形"}, {"Triangle", "直角三角形"}, {"Star", "星形"}, {"Polygon", "正多邊形"}};
    String shapeImage[] = {"img/line.png", "img/rectangle.png", "img/round_rectangle.png", "img/oval.png", "img/triangle.png", "img/star.png", "img/polygon.png"};
    
    /*線條設定*/
    String[] lineWidth = {"▁▁","▃▃","▅▅","▇▇"};
    Integer[] lineWidth_px =  {2, 5, 8, 11};
    JComboBox lineWidthList;
    
    /*功能按鈕*/
    JButton[] jBtn;
    String btnName[][] = {{"復原", "復原(Ctrl+Z) 復原上次的動作"},
                          {"顏色", "編輯色彩，從調色盤選取色彩"},
                          {"擷取螢幕", "螢幕截圖"}};
    
    String btnImage[] = {"img/undo.png", "img/color.png", "img/screenshot.png"};

    /*調色盤按鈕*/
    JToggleButton[] colorJTBtn;
    JPanel setcolorPanel[], selectColorPanel[];
    JButton[] colorsBtn;
    String colorToolTip[] = {"色彩1(前景色彩)，按一下此處，然後從調色盤選取色彩，鉛筆圖形都會使用此色彩",
                             "色彩2(背景色彩)，按一下此處，然後從調色盤選取色彩，橡皮擦會使用此色彩"};
    Color colors[] = {new Color(0,0,0), new Color(255,255,255), new Color(136,0,21), new Color(237,28,36), new Color(255,127,39), new Color(255,242,0), new Color(34,177,76),
                      new Color(0,162,232), new Color(63,72,204), new Color(163,73,164), new Color(127,127,127), new Color(195,195,195), new Color(185,122,87), new Color(255,174,201),
                      new Color(255,201,14), new Color(239,228,176), new Color(181,230,29), new Color(153,217,234), new Color(112,146,190), new Color(200,191,231)};
    
    ToolBar(final MainWindow parant) {
        this.parant = parant;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        toolBar = new JToolBar[toolBarName.length];
        for (int i = 0; i < toolBarName.length; i++) {
            toolBar[i] = new JToolBar();
            toolBar[i].setBorder(null);
        }
        
        /*新增工具按鈕*/
        ButtonGroup buttonGroup = new ButtonGroup();
        pen_JTBtn = new JToggleButton[penBtnName.length];
        for (int i = 0; i < penBtnName.length; i++) {
            pen_JTBtn[i] = new JToggleButton();
            pen_JTBtn[i].setIcon(new ImageIcon(this.getClass().getResource(penImage[i])));
            pen_JTBtn[i].setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
            pen_JTBtn[i].setFocusable(false);
            pen_JTBtn[i].setToolTipText(penBtnName[i][1]);
            pen_JTBtn[0].setSelected(true);
            pen_JTBtn[i].addActionListener(this);
            buttonGroup.add(pen_JTBtn[i]);
            toolBar[0].add(pen_JTBtn[i]);
        }
        
        /*新增形狀按鈕*/
        shape_JTBtn = new JToggleButton[shapeBtnName.length];
        for (int i = 0; i < shapeBtnName.length; i++) {
            shape_JTBtn[i] = new JToggleButton();
            shape_JTBtn[i].setIcon(new ImageIcon(this.getClass().getResource(shapeImage[i])));
            shape_JTBtn[i].setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
            shape_JTBtn[i].setFocusable(false);
            shape_JTBtn[i].setToolTipText(shapeBtnName[i][1]);
            shape_JTBtn[i].addActionListener(this);
            buttonGroup.add(shape_JTBtn[i]);
            toolBar[1].add(shape_JTBtn[i]);
        }
        
        /*新增線條粗細設置*/
        lineWidthList = new JComboBox(lineWidth);
        lineWidthList.setToolTipText("大小(Ctrl++, Ctrl+-)");
        lineWidthList.setBorder(BorderFactory.createEmptyBorder(13, 10, 13, 10));
        lineWidthList.addActionListener(this);
        toolBar[2].add(lineWidthList);
        
        /*新增其他功能按鈕*/
        jBtn = new JButton[btnName.length];
        for (int i = 0; i < btnName.length; i++) {
            jBtn[i] = new JButton();
            jBtn[i].setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            jBtn[i].setToolTipText(btnName[i][1]);
            jBtn[i].setIcon(new ImageIcon(this.getClass().getResource(btnImage[i])));
            jBtn[i].setFocusable(false);
            jBtn[i].addActionListener(this);
            toolBar[3].add(jBtn[i]);
        }
        
        /*新增調色盤按鈕*/
        ButtonGroup color_ButtonGroup = new ButtonGroup();
        colorJTBtn = new JToggleButton[2];
        setcolorPanel = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            setcolorPanel[i] = new JPanel();
            setcolorPanel[i].setBackground(colors[i]);
            setcolorPanel[i].setBorder(BorderFactory.createEmptyBorder(7, 7, 15, 8));
            colorJTBtn[i] = new JToggleButton();
            colorJTBtn[i].setLayout(new GridLayout(2, 1));
            colorJTBtn[i].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            colorJTBtn[i].setToolTipText(colorToolTip[i]);
            colorJTBtn[i].setFocusable(false);
            colorJTBtn[0].setSelected(true);
            colorJTBtn[i].add(setcolorPanel[i]);
            colorJTBtn[i].add(new JLabel("色彩"+(i+1)));
            color_ButtonGroup.add(colorJTBtn[i]);
            toolBar[4].add(colorJTBtn[i]);
        }
        
        colorsBtn = new JButton[colors.length];
        selectColorPanel = new JPanel[colors.length];
        JToolBar colorbar = new JToolBar(JToolBar.VERTICAL);
        for (int i = 0; i < colors.length; i++) {
            colorbar.setBorder(null);
            colorbar.setFloatable(false);
            colorbar.setLayout(new GridLayout(2,10));
            selectColorPanel[i] = new JPanel();
            selectColorPanel[i].setBorder(BorderFactory.createEmptyBorder(10, 8, 12, 12));
            selectColorPanel[i].setBackground(colors[i]);
            colorsBtn[i] = new JButton();
            colorsBtn[i].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.WHITE));
            colorsBtn[i].setFocusable(false);
            colorsBtn[i].addActionListener(this);
            colorsBtn[i].add(selectColorPanel[i]);
            colorbar.add(colorsBtn[i]);
            
            colorsBtn[i].addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                for (int i = 0; i < colors.length; i++) {
                    if (ToolBar.this.parant.page.activeUMLO != null && e.getSource() == colorsBtn[i]) {
                        ToolBar.this.parant.page.activeUMLO.setBackground(colors[i]);
                    }
                }
            }

            public void mouseExited(MouseEvent e) {
                if (ToolBar.this.parant.page.activeUMLO != null) {
                    ToolBar.this.parant.page.activeUMLO.setBackground(setcolorPanel[0].getBackground());
                }
            }

        });
        }

        toolBar[4].add(colorbar);

        /*ToolBar 版面設置*/
        JToolBar[] storebar;
        storebar = new JToolBar[5];
        for (int i = 0; i < 4; i++) {
            storebar[i] = new JToolBar();
            storebar[i].setFloatable(false);
            storebar[i].setLayout(new BorderLayout());
            storebar[i].add(BorderLayout.CENTER, toolBar[i]);
            storebar[i].add(BorderLayout.SOUTH, new JLabel(toolBarName[i], JLabel.CENTER));
            this.add(storebar[i]);
        }
        storebar[4] = new JToolBar();
        storebar[4].setFloatable(false);
        storebar[4].add(toolBar[4]);
        this.add(storebar[4]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < penBtnName.length; i++) {
            if (e.getSource() == pen_JTBtn[i]) {
                parant.page.type = Status.valueOf(penBtnName[i][0]);
                parant.page.status = Status.Draw;
                System.out.println(parant.page.type);
                break;
            }
        }
        
        for (int i = 0; i < shapeBtnName.length; i++) {
            if (e.getSource() == shape_JTBtn[i]) {
                parant.page.type = Status.valueOf(shapeBtnName[i][0]);
                parant.page.status = Status.Draw;
                System.out.println(parant.page.type);
                if (i == 5) {
                    String side = JOptionPane.showInputDialog("請輸入要幾個角?");
                    parant.page.star_arm = side != null ? Integer.parseInt(side) : 5;
                }
                if (i == 6) {
                    String side = JOptionPane.showInputDialog("請輸入要幾個邊?");
                    parant.page.polygon_side = side != null ? Integer.parseInt(side) : 5;
                }
                break;
                
            }
        }
        
        for (int i = 0; i < colors.length; i++) {
            if (e.getSource() == colorsBtn[i]) {
                if (colorJTBtn[0].isSelected()) {
                    setcolorPanel[0].setBackground(colors[i]);
                    parant.page.penColor = colors[i];
                    if (parant.page.drawobject != null && parant.page.drawobject.status == Status.Selected) {
                        if(parant.page.drawobject.isOpaque()) {
                            parant.page.drawobject.setBackground(colors[i]);
                        }
                        parant.page.drawobject.color = colors[i];
                        parant.page.repaint();
                    }
                } else if (colorJTBtn[1].isSelected()){
                    setcolorPanel[1].setBackground(colors[i]);
                    parant.page.eraserColor = colors[i];
                }
                break;
            }
        }
        
        if (e.getSource() == lineWidthList) {
            parant.page.SetLineWidth(lineWidth_px[lineWidthList.getSelectedIndex()]);
        }
        
        if (e.getSource() == jBtn[0]) {
            parant.page.Undo();
        }
        
        if (e.getSource() == jBtn[1]) {
            parant.page.ChooseColor();
        }
        
        if (e.getSource() == jBtn[2]) {
            parant.page.ScreenShot();
        }
    }
}
