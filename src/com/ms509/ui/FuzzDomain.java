package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import com.ms509.ui.menu.Menu;
import com.ms509.util.InitConfig;

public class FuzzDomain {
	private static JFrame main;

	public static JFrame getMain() {
		return main;
	}

	public FuzzDomain() {
		setLookAndFeel();
		new InitConfig();
		System.setProperty ("jsse.enableSNIExtension", "false");	// 防止javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name异常
		Toolkit t = Toolkit.getDefaultToolkit();
		Dimension d = t.getScreenSize();
		main = new JFrame("FuzzDomain 1.0 beta");
		main.setIconImage(new ImageIcon(getClass().getResource(
				"/com/ms509/images/main.png")).getImage());
		main.setSize(910, 480);
		main.setLocation((d.width - main.getWidth()) / 2,
				(d.height - main.getHeight()) / 2);
		// main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// 窗口默认的关闭操作
		main.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);	// 屏蔽窗口默认的关闭操作
		main.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int act = JOptionPane.showConfirmDialog(main, "确认要退出该程序吗?",
						"提示", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (act == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
			}
		});
		main.getContentPane().setLayout(new BorderLayout(0, 0));
		main.add(new FuzzPanel());
		Menu menu = new Menu(); // 菜单
		main.setJMenuBar(menu);
		main.setVisible(true);
	}

	private void setLookAndFeel() {
		try {
			JFrame.setDefaultLookAndFeelDecorated(true);
			JDialog.setDefaultLookAndFeelDecorated(true);
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			try {
				UIManager
						.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
			} catch (Exception e1) {
			}
		}
	}

	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new FuzzDomain();
			}
		});
	}
}