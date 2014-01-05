
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class MenuBar extends JPanel implements ActionListener {

    MainWindow parant;
    JMenuBar jMenuBar;
    JMenu jMenu;
    JMenuItem jMenuItem[];
    String menuItem[] = {"開新檔案", "開啟舊檔", "儲存檔案", "灰階化", "結束"};
    String menuItem_img[] = {"img/new.png", "img/open.png", "img/save.png", "img/gray.png", "img/exit.png"};
    
    MenuBar(MainWindow parant) {
        this.parant = parant;
        this.setLayout(new BorderLayout());
        jMenuBar = new JMenuBar();
        jMenuBar.setOpaque(false);
        jMenu = new JMenu("檔案");
        jMenuItem = new JMenuItem[menuItem.length];
        for (int i = 0; i < menuItem.length; i++) {
            jMenuItem[i] = new JMenuItem(menuItem[i]);
            jMenuItem[i].addActionListener((ActionListener) this);
            jMenuItem[i].setIcon(new ImageIcon(this.getClass().getResource(menuItem_img[i])));
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
            parant.page.Open();
        }
        if (e.getSource() == jMenuItem[2]) {
            parant.page.Save();
        }
        if (e.getSource() == jMenuItem[3]) {
            parant.page.Convert();
        }
        if (e.getSource() == jMenuItem[4]) {
            System.exit(0);
        }
    }
}
