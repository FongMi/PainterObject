
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame {
    MenuBar menuBar;
    JTabbedPane tabbed;
    ToolBar toolBar;
    ObjectToolBar objectToolBar;
    Page page;
    JLabel statusBar;
    
    MainWindow(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*預設開啟最大化*/
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        /*設定最小畫面Size*/
        this.setMinimumSize(new Dimension(1152, 768));
        /*設定位置*/
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - 1152) / 2, (screenSize.height - 768) / 2);
        
        /*ToolBar*/
        toolBar = new ToolBar(this);
        /*ObjectToolBar*/
        objectToolBar = new ObjectToolBar(this);
        
        /*設定 Tabbed 透明*/
        UIManager.put("TabbedPane.contentOpaque", false);
        /*Tabbed*/
        tabbed = new JTabbedPane();
        tabbed.add("小畫家", toolBar);
        tabbed.add("物件", objectToolBar);
        
        /*MenuBar*/
        menuBar = new MenuBar(this);
        menuBar.add(tabbed);
        this.getContentPane().add(BorderLayout.NORTH, menuBar);
        
        /*Page*/
        page = new Page(this);
        JScrollPane scrollpane = new JScrollPane(page);
        scrollpane.setPreferredSize(new Dimension(1152, 768));
        this.getContentPane().add(BorderLayout.CENTER, scrollpane);
        
        /*statusBar*/
        statusBar = new JLabel("滑鼠座標");
        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(BorderLayout.SOUTH, statusBar);
        
        /*MainWindow*/
        this.setVisible(true);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/painter.png")));
        page.requestFocus();
    }
}
