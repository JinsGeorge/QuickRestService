package quickrestservice;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import quickrestservice.server.QuickRestService;
import java.awt.Toolkit;

public class QuickRestServiceApplication  extends JFrame{

	private static final long serialVersionUID = -8452811432568257448L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					QuickRestServiceApplication frame = new QuickRestServiceApplication();
					frame.setVisible(true);
					QuickRestService.run(args);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public QuickRestServiceApplication() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(QuickRestServiceApplication.class.getResource("/com/sun/java/swing/plaf/windows/icons/Computer.gif")));
		setForeground(SystemColor.menu);
		setBackground(SystemColor.activeCaptionBorder);
		setTitle("Quick Rest Service");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 326, 106);
		contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane txtpnSdsfd = new JTextPane();
		txtpnSdsfd.setForeground(new Color(47, 79, 79));
		txtpnSdsfd.setBackground(SystemColor.menu);
		txtpnSdsfd.setFont(new Font("Consolas", Font.PLAIN, 12));
		txtpnSdsfd.setEditable(false);
		txtpnSdsfd.setText("Quick Rest Service application is running.\r\n\r\nClosing this window exists the program.");
		contentPane.add(txtpnSdsfd, BorderLayout.CENTER);
	}

}
