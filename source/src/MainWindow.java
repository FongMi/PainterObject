
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame {

    Dimension d;
    ToolBar toolBar;
    ObjectToolBar objectToolBar;
    MenuBar menuBar;
    Page page;
    JLabel statusBar = new JLabel("滑鼠座標");
    Tabbed tabbed;

    MainWindow(String title, Dimension d) {
        super(title);
        this.d = d;
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - d.width) / 2, (screenSize.height - d.height) / 2);

        menuBar = new MenuBar(this);
        toolBar = new ToolBar(this);
        objectToolBar = new ObjectToolBar(this);

        tabbed = new Tabbed(JTabbedPane.TOP);
        tabbed.add("小畫家", toolBar);
        tabbed.add("物件", objectToolBar);

        menuBar.add(tabbed);
        this.getContentPane().add(BorderLayout.NORTH, menuBar);

        page = new Page(this);
        this.getContentPane().add(BorderLayout.CENTER, page);

        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(BorderLayout.SOUTH, statusBar);

        this.setVisible(true);
        page.requestFocus();
    }
}
