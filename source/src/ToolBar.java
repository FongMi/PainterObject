
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ToolBar extends JPanel implements ActionListener {
    
    MainWindow parant;
    JToolBar[] toolBar;
    String toolBarName[] = {"畫筆", "形狀", "粗細", "調色盤", "其他"};
    
    /*畫筆按鈕*/
    JToggleButton[] pen_JTBtn;
    String penBtnName[][] = {{"Pen", "鉛筆，使用選取的線條寬度繪製任意形狀的線條"},
                             {"Eraser", "橡皮擦，清除圖片的的一部份，並以背景色彩取代"},
                             {"Move","移動選取的圖形"}};
    
    String penImage[] = {"img/pencil.png", "img/eraser.png", "img/move.png"};
    
    /*填滿按鈕*/
    JToggleButton fill_JTBtn;
    
    /*選取按鈕*/
    JToggleButton select_JTBtn;
    
    /*形狀按鈕*/
    JToggleButton[] shape_JTBtn;
    String shapeBtnName[][] = {{"Line", "直線"}, {"Rectangle", "矩形"}, {"Round_Rectangle", "圓角矩形"}, {"Oval", "橢圓形"}};
    String shapeImage[] = {"img/line.png", "img/rectangle.png", "img/round_rectangle.png", "img/oval.png"};
    
    /*線條設定*/
    String[] lineWidth = {"▁▁","▃▃","▅▅","▇▇"};
    Integer[] lineWidth_px =  {2, 5, 8, 11};
    JComboBox lineWidthList;
    
    /*功能按鈕*/
    JButton[] jBtn;
    String btnName[][] = {{"復原","復原(Ctrl+Z) 復原上次的動作"},
                          {"顏色","編輯色彩，從調色盤選取色彩"}};
    
    String btnImage[] = {"img/undo.png", "img/color.png"};

    /*調色盤按鈕*/
    static JToggleButton[] colorJTBtn;
    static JPanel setcolorPanel[],selectColorPanel[];
    static JButton[] colorsBtn;
    String colorToolTip[] = {"色彩1(前景色彩)，按一下此處，然後從調色盤選取色彩，鉛筆圖形都會使用此色彩",
                             "色彩2(背景色彩)，按一下此處，然後從調色盤選取色彩，橡皮擦會使用此色彩"};
    
    Color colors[] = {new Color(0,0,0), new Color(255,255,255), new Color(136,0,21), new Color(237,28,36), new Color(255,127,39), new Color(255,242,0), new Color(34,177,76),
                      new Color(0,162,232), new Color(63,72,204), new Color(163,73,164), new Color(127,127,127), new Color(195,195,195), new Color(185,122,87), new Color(255,174,201),
                      new Color(255,201,14), new Color(239,228,176), new Color(181,230,29), new Color(153,217,234), new Color(112,146,190), new Color(200,191,231)};
    
    ToolBar(MainWindow p) {
        parant = p;
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBackground(new Color(255, 250, 240));
        
        toolBar = new JToolBar[toolBarName.length];
        for (int i = 0; i < toolBarName.length; i++) {
            toolBar[i] = new JToolBar();
            toolBar[i].setFloatable(false);
        }

        /*新增畫筆按鈕*/
        ButtonGroup buttonGroup = new ButtonGroup();
        pen_JTBtn = new JToggleButton[penBtnName.length];
        for (int i = 0; i < penBtnName.length; i++) {
            pen_JTBtn[i] = new JToggleButton();
            pen_JTBtn[i].setIcon(new ImageIcon(this.getClass().getResource(penImage[i])));
            pen_JTBtn[i].setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            pen_JTBtn[i].setFocusable(false);
            pen_JTBtn[i].setToolTipText(penBtnName[i][1]);
            pen_JTBtn[0].setSelected(true);
            pen_JTBtn[i].addActionListener(this);
            buttonGroup.add(pen_JTBtn[i]);
            toolBar[0].add(pen_JTBtn[i]);
        }
        
        /*新增填滿按鈕*/
        fill_JTBtn = new JToggleButton();
        fill_JTBtn.setIcon(new ImageIcon(this.getClass().getResource("img/fill.png")));
        fill_JTBtn.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        fill_JTBtn.setFocusable(false);
        fill_JTBtn.addActionListener(this);
        toolBar[0].add(fill_JTBtn);
        
        /*新增形狀按鈕*/
        shape_JTBtn = new JToggleButton[shapeBtnName.length];
        for (int i = 0; i < shapeBtnName.length; i++) {
            shape_JTBtn[i] = new JToggleButton();
            shape_JTBtn[i].setIcon(new ImageIcon(this.getClass().getResource(shapeImage[i])));
            shape_JTBtn[i].setBorder(BorderFactory.createEmptyBorder(15,10,15,10));
            shape_JTBtn[i].setFocusable(false);
            shape_JTBtn[i].setToolTipText(shapeBtnName[i][1]);
            shape_JTBtn[i].addActionListener(this);
            buttonGroup.add(shape_JTBtn[i]);
            toolBar[1].add(shape_JTBtn[i]);
        }
        
        /*新增線條粗細設置*/
        lineWidthList = new JComboBox(lineWidth);
        lineWidthList.setToolTipText("大小(Ctrl++, Ctrl+-)");
        lineWidthList.setBorder(BorderFactory.createEmptyBorder(14,9,14,9));
        lineWidthList.addActionListener(this);
        toolBar[2].add(lineWidthList);
        
        /*新增調色盤按鈕*/
        ButtonGroup color_ButtonGroup = new ButtonGroup();
        colorJTBtn = new JToggleButton[2];
        setcolorPanel = new JPanel[2];
        for (int i = 0; i < 2; i++) {
            setcolorPanel[i] = new JPanel();
            setcolorPanel[i].setBorder(BorderFactory.createEmptyBorder(6,0,16,0));
            setcolorPanel[i].setBackground(colors[i]);
            colorJTBtn[i] = new JToggleButton();
            colorJTBtn[i].setLayout(new GridLayout(2,1));
            colorJTBtn[i].setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
            colorJTBtn[i].setToolTipText(colorToolTip[i]);
            colorJTBtn[i].setFocusable(false);
            colorJTBtn[0].setSelected(true);
            colorJTBtn[i].add(setcolorPanel[i]);
            colorJTBtn[i].add(new JLabel("色彩"+(i+1)));
            color_ButtonGroup.add(colorJTBtn[i]);
            toolBar[3].add(colorJTBtn[i]);
        }
        
        colorsBtn = new JButton[colors.length];
        selectColorPanel = new JPanel[colors.length];
        JToolBar colorbar = new JToolBar(JToolBar.VERTICAL);
        for (int i = 0; i < colors.length; i++) {
            colorbar.setFloatable(false);
            colorbar.setLayout(new GridLayout(2,10));
            selectColorPanel[i] = new JPanel();
            selectColorPanel[i].setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
            selectColorPanel[i].setBackground(colors[i]);
            colorsBtn[i] = new JButton();
            colorsBtn[i].setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
            colorsBtn[i].setFocusable(false);
            colorsBtn[i].addActionListener(this);
            colorsBtn[i].add(selectColorPanel[i]);
            colorbar.add(colorsBtn[i]);
        }
        toolBar[3].add(colorbar);
        
        /*新增其他功能按鈕*/
        jBtn = new JButton[btnName.length];
        for (int i = 0; i < btnName.length; i++) {
            jBtn[i] = new JButton();
            jBtn[i].setToolTipText(btnName[i][1]);
            jBtn[i].setIcon(new ImageIcon(this.getClass().getResource(btnImage[i])));
            jBtn[i].setFocusable(false);
            jBtn[i].setPreferredSize(new Dimension(32,32));
            jBtn[i].addActionListener(this);
            toolBar[4].add(jBtn[i]);
        }

        /*ToolBar 版面設置*/
        JToolBar[] storebar;
        storebar = new JToolBar[3];
        for (int i = 0; i < 3; i++) {
            storebar[i] = new JToolBar();
            storebar[i].setFloatable(false);
            storebar[i].setLayout(new BorderLayout());
            storebar[i].add(BorderLayout.CENTER, toolBar[i]);
            storebar[i].add(BorderLayout.SOUTH, new JLabel(toolBarName[i], JLabel.CENTER));
            this.add(storebar[i]);
        }
        this.add(toolBar[3]);
        this.add(toolBar[4]);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < penBtnName.length; i++) {
            if (e.getSource() == pen_JTBtn[i]) {
                parant.page.status = Status.valueOf(penBtnName[i][0]);
            }
        }
        
        if (e.getSource() == fill_JTBtn) {
            AbstractButton abstractButton = (AbstractButton) e.getSource();
            boolean selected = abstractButton.getModel().isSelected();
            parant.page.isFill = selected;
        }

        for (int i = 0; i < shapeBtnName.length; i++) {
            if (e.getSource() == shape_JTBtn[i]) {
                parant.page.status = Status.valueOf(shapeBtnName[i][0]);
            }
        }
        for (int i = 0; i < colors.length; i++) {
            if (e.getSource() == colorsBtn[i]) {
                if (colorJTBtn[0].isSelected()) {
                    setcolorPanel[0].setBackground(colors[i]);
                } else {
                    setcolorPanel[1].setBackground(colors[i]);
                }
            }
        }

        if (e.getSource() == lineWidthList) {
            parant.page.SetStroke(lineWidth_px[lineWidthList.getSelectedIndex()]);
        }
        
        if (e.getSource() == jBtn[0]) {
            parant.page.Undo();
        }
        
        if (e.getSource() == jBtn[1]) {
            parant.page.ChooseColor();
        }
    }
}
