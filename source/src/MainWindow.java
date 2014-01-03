
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame {
    MenuBar menuBar;
    JTabbedPane tabbed;
    ToolBar toolBar;
    ObjectToolBar objectToolBar;
    Page page;
    JLabel statusBar = new JLabel("滑鼠座標");
    

    MainWindow(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /*預設開啟最大化*/
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        /*設定最小畫面Size*/
        this.setMinimumSize(new Dimension(1024, 768));
        /*設定位置*/
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - 1024) / 2, (screenSize.height - 768) / 2);
        /*設定 tabbed 透明*/
        UIManager.put("TabbedPane.contentOpaque", false);
        tabbed = new JTabbedPane();
        toolBar = new ToolBar(this);
        objectToolBar = new ObjectToolBar(this);
        tabbed.add("小畫家", toolBar);
        tabbed.add("物件", objectToolBar);
        
        menuBar = new MenuBar(this);
        menuBar.add(tabbed);
        this.getContentPane().add(BorderLayout.NORTH, menuBar);

        page = new Page(this);
        this.getContentPane().add(BorderLayout.CENTER, page);

        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(BorderLayout.SOUTH, statusBar);

        this.setVisible(true);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("img/painter.png")));
        page.requestFocus();
    }
}
