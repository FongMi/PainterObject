import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuBar extends JPanel implements ActionListener {

    MainWindow parant;
    JMenuBar jMenuBar;
    JMenu jMenu;
    JMenuItem jMenuItem[];
    String menuItem[] = {"清除畫面", "儲存檔案", "結束"};

    MenuBar(MainWindow parant) {
        this.parant = parant;
        this.setBackground(Color.WHITE);
        this.setLayout(new BorderLayout());
        jMenuBar = new JMenuBar();
        jMenu = new JMenu("檔案");
        jMenuItem = new JMenuItem[menuItem.length];
        for (int i = 0; i < menuItem.length; i++) {
            jMenuItem[i] = new JMenuItem(menuItem[i]);
            jMenuItem[i].addActionListener((ActionListener) this);
            jMenu.add(jMenuItem[i]);
        }
        jMenuBar.add(jMenu);
        this.add(jMenuBar, BorderLayout.NORTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jMenuItem[0]) {
            parant.page.NewPage();
        }
        if (e.getSource() == jMenuItem[1]) {
            parant.page.Save();
        }
        if (e.getSource() == jMenuItem[2]) {
            System.exit(0);
        }
    }
}
