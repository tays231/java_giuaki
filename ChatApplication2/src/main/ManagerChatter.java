package main;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

public class ManagerChatter extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtServerPort;
    private JTabbedPane tabbedPane; // Đặt tabbedPane ở mức lớp
    private ServerSocket srvSocket = null;
    private BufferedReader bf = null;
    private Thread t;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ManagerChatter frame = new ManagerChatter();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public ManagerChatter() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 624, 439);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout(0, 0));

        JPanel panel = new JPanel();
        contentPane.add(panel, BorderLayout.NORTH);
        panel.setLayout(new GridLayout(1, 2, 0, 0));

        JLabel ManagerPort = new JLabel("Port");
        ManagerPort.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(ManagerPort);

        txtServerPort = new JTextField();
        txtServerPort.setText("12340");
        panel.add(txtServerPort);
        txtServerPort.setColumns(10);

        tabbedPane = new JTabbedPane(JTabbedPane.TOP); // Khởi tạo tabbedPane ở đây
        contentPane.add(tabbedPane, BorderLayout.CENTER);

        this.setSize(600, 300);
        int serverPort = Integer.parseInt(txtServerPort.getText());
        try {
            srvSocket = new ServerSocket(serverPort);
        } catch (Exception e) {
            e.printStackTrace();
        }
        t = new Thread(this);
        t.start();
    }

    public void run() {
        while (true) {
            try {
                Socket aStaffSocket = srvSocket.accept();
                if (aStaffSocket != null) {
                    bf = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream()));
                    String S = bf.readLine();
                    int pos = S.indexOf(":");
                    String staffName = S.substring(pos + 1);
                    ChatPanel p = new ChatPanel(aStaffSocket, "Manager", staffName);
                    tabbedPane.add(staffName, p);
                    p.updateUI();
                }
                // Thread.sleep(null); // Dòng này không đúng, bạn cần chỉ định một thời gian để sleep
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
