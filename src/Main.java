import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.io.IOException;

class TabbedFrame extends JFrame {
    private JTabbedPane tabbedPane;
    private JPanel taskPanel1, taskPanel2, taskPanel3;

    public TabbedFrame(String painting) {
        tabbedPane = new JTabbedPane();
        taskPanel1 = new ListPanel();
        tabbedPane.addTab("Task1", taskPanel1);
        tabbedPane.addTab("Task2", taskPanel2);
        tabbedPane.addTab("Task3", taskPanel3);
        this.setLayout(new BorderLayout());
        this.add(tabbedPane, BorderLayout.CENTER);

    }

    public static void main(String[] args) {
        TabbedFrame tabbedFrame = new TabbedFrame("Application");
        tabbedFrame.setBounds(100, 100, 500, 500);
        tabbedFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        tabbedFrame.setVisible(true);
    }
}