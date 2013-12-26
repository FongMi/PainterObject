
import java.awt.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class MainWindow extends JFrame {

    Dimension d;
    ToolBar toolBar;
    MenuBar menuBar;
    Page page;
    static JLabel statusBar = new JLabel("滑鼠座標");

    MainWindow(String title, Dimension d) {
        super(title);
        this.d = d;
        this.setSize(d);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - d.width) / 2, (screenSize.height - d.height) / 2);

        menuBar = new MenuBar(this);
        toolBar = new ToolBar(this);
        menuBar.add(toolBar);
        this.getContentPane().add(BorderLayout.NORTH, menuBar);
        
        page = new Page(this);
        this.getContentPane().add(BorderLayout.CENTER, page);

        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        this.getContentPane().add(BorderLayout.SOUTH, statusBar);

        this.setVisible(true);
        page.requestFocus();
    }
}
