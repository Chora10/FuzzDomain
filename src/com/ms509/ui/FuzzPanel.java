package com.ms509.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import com.ms509.model.ListDefaultTableModel;
import com.ms509.ui.FuzzDomain;
import com.ms509.ui.CTextField;
import com.ms509.ui.worker.FuzzSwingWorker;
import com.ms509.util.Common;
import com.ms509.util.Configuration;
import com.ms509.util.ConstantClass;

public class FuzzPanel extends JPanel {
	private JCheckBox c;
	private JCheckBox c2;
	private JCheckBox cr;
	private JCheckBox cr2;
	private JTextField root;
	private JTextField rule;
	private JTextField rule2;
	private JTextField min;
	private JTextField max;
	private JTextField min2;
	private JTextField max2;
	private JTextField thread;
	private JTextField layer;
	private JTextField name;
	private JTextField name2;
	private JLabel lstatus;
	private JLabel status;
	private JTable list;
	private JButton stop;
	private JButton start;
	private JButton b_start;
	private JButton add;
	private JButton b_stop;
	private JButton pause;
	private boolean isStop;
	private int row;
	private int b_count;
	private int time;
	private DefaultTableModel dtm;
	private Set<String> b_domains = new TreeSet<String>(); // 遍历发现的域名
	private Set<String> domains = new TreeSet<String>(); // 总发现的域名
	private Configuration config = new Configuration();
	private JPopupMenu spop;
	private JPopupMenu pop;
	private int col;
	private JScrollPane main;
	private int act;
	private String data;
	private int[] rows;
	private Clipboard cb;

	public JButton getStart() {
		return start;
	}

	public JButton getAdd() {
		return add;
	}

	public JTextField getRoot() {
		return root;
	}

	public JLabel getStatus() {
		return status;
	}

	public Set<String> getB_domains() {
		return b_domains;
	}

	public Set<String> getDomains() {
		return domains;
	}

	public DefaultTableModel getDtm() {
		return dtm;
	}

	public FuzzPanel() {
		cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		this.setLayout(new BorderLayout());
		JPanel top = new JPanel();
		JPanel top1 = new JPanel();
		JPanel top2 = new JPanel();
		JPanel top3 = new JPanel();
		top.setLayout(new GridLayout(3, 1));
		top1.setLayout(new FlowLayout(FlowLayout.LEFT));
		top2.setLayout(new FlowLayout(FlowLayout.LEFT));
		top3.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel label = new JLabel("域名:");
		root = new CTextField("");
		root.setPreferredSize(new Dimension(220, 30));
		JLabel label1 = new JLabel("层次:");
		layer = new CTextField("10");
		layer.setPreferredSize(new Dimension(30, 30));
		JLabel label2 = new JLabel("线程:");
		thread = new CTextField("500");
		thread.setPreferredSize(new Dimension(45, 30));
		FuzzAction startaction = new FuzzAction("开始");
		startaction.putValue(Action.ACTION_COMMAND_KEY, "开始");
		start = new JButton(startaction);
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "diystart");
		this.getActionMap().put("diystart", startaction);
		b_start = new JButton(new FuzzAction("遍历"));
		pause = new JButton(new FuzzAction("暂停"));
		stop = new JButton(new FuzzAction("停止"));
		b_stop = new JButton(new FuzzAction("停止遍历"));
		add = new JButton(new FuzzAction("添加"));
		final JButton imp = new JButton(new FuzzAction("导入"));
		final JButton clear = new JButton(new FuzzAction("清空"));
		stop.setEnabled(false);
		b_stop.setEnabled(false);
		add.setEnabled(false);
		pause.setEnabled(false);
		top1.add(label);
		top1.add(root);
		top1.add(label1);
		top1.add(layer);
		top1.add(label2);
		top1.add(thread);
		top1.add(start);
		top1.add(b_start);
		top1.add(pause);
		top1.add(stop);
		top1.add(b_stop);
		// top1.add(add);
		top1.add(imp);
		top1.add(clear);
		c = new JCheckBox("字典");
		name = new CTextField("");
		name.setEnabled(false);
		name.setPreferredSize(new Dimension(205, 30));
		final JButton brower = new JButton("浏览");
		JLabel margin = new JLabel("");
		margin.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 101));
		c2 = new JCheckBox("字典2");
		name2 = new CTextField("");
		name2.setEnabled(false);
		name2.setPreferredSize(new Dimension(205, 30));
		final JButton brower2 = new JButton("浏览");
		brower.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setDialogTitle("导入字典");
				chooser.setApproveButtonText("导入字典");
				int act = chooser.showOpenDialog(getRootPane());
				if (act == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String path = file.toString();
					name.setText(path);
					config.setValue("DIC", path);
					config.setValue("CHECKBOX", "1");
					ConstantClass.DIC = path;
					c.setSelected(true);
				}
			}
		});
		brower2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser(".");
				chooser.setDialogTitle("导入字典2");
				chooser.setApproveButtonText("导入字典2");
				int act = chooser.showOpenDialog(getRootPane());
				if (act == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					String path = file.toString();
					name2.setText(path);
					config.setValue("DIC2", path);
					config.setValue("CHECKBOX2", "1");
					ConstantClass.DIC2 = path;
					c2.setSelected(true);
				}
			}
		});
		c.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (name.getText().trim().equals("")) {
					brower.doClick();
				} else {
					config.setValue("DIC", name.getText());
					config.setValue("CHECKBOX", "1");
					ConstantClass.DIC = name.getText();
				}
			}
		});
		c2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (name2.getText().trim().equals("")) {
					brower2.doClick();
				} else {
					config.setValue("DIC2", name.getText());
					config.setValue("CHECKBOX2", "1");
					ConstantClass.DIC2 = name2.getText();
				}
			}
		});
		top2.add(c);
		top2.add(name);
		top2.add(brower);
		top2.add(margin);
		top2.add(c2);
		top2.add(name2);
		top2.add(brower2);

		cr = new JCheckBox("规则");
		rule = new CTextField("abcdefghijklmnopqrstuvwxyz0123456789");
		rule.setPreferredSize(new Dimension(145, 30));
		final JLabel label3 = new JLabel("Min");
		min = new CTextField("1");
		min.setPreferredSize(new Dimension(27, 30));
		final JLabel label4 = new JLabel("Max");
		max = new CTextField("3");
		max.setPreferredSize(new Dimension(27, 30));
		JLabel margin2 = new JLabel("");
		margin2.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 100));
		cr2 = new JCheckBox("规则2");
		rule2 = new CTextField("abcdefghijklmnopqrstuvwxyz0123456789");
		rule2.setPreferredSize(new Dimension(145, 30));
		final JLabel label5 = new JLabel("Min");
		min2 = new CTextField("1");
		min2.setPreferredSize(new Dimension(27, 30));
		final JLabel label6 = new JLabel("Max");
		max2 = new CTextField("2");
		max2.setPreferredSize(new Dimension(27, 30));
		name.setText(config.getValue("DIC"));
		name2.setText(config.getValue("DIC2"));
		if (ConstantClass.CHECKBOX.equals("1")) {
			c.setSelected(true);
		} else {
			cr.setSelected(true);
		}
		if (ConstantClass.CHECKBOX2.equals("1")) {
			c2.setSelected(true);
		} else {
			cr2.setSelected(true);
		}

		cr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.setValue("CHECKBOX", "2");
			}
		});
		cr2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				config.setValue("CHECKBOX2", "2");
			}
		});

		top3.add(cr);
		top3.add(rule);
		top3.add(label3);
		top3.add(min);
		top3.add(label4);
		top3.add(max);
		top3.add(margin2);
		top3.add(cr2);
		top3.add(rule2);
		top3.add(label5);
		top3.add(min2);
		top3.add(label6);
		top3.add(max2);

		top.add(top1);
		top.add(top2);
		top.add(top3);

		ButtonGroup group = new ButtonGroup();

		group.add(c);
		group.add(cr);

		ButtonGroup group2 = new ButtonGroup();
		group2.add(c2);
		group2.add(cr2);

		list = new JTable();
		list.setAutoCreateRowSorter(true);
		String[] headers = new String[] { "Domain", "Ip" };
		String[][] datas = {};
		dtm = new ListDefaultTableModel(datas, headers);
		list.setModel(dtm);
		main = new JScrollPane(list);
		list.getTableHeader().addMouseListener(new TableHeaderListener());
		lstatus = new JLabel("就绪");
		lstatus.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 50));
		status = new JLabel("");
		status.setPreferredSize(new Dimension(600, ConstantClass.STATUS_HEIGHT));
		JToolBar bottom = new JToolBar();
		bottom.setFloatable(false);
		bottom.add(lstatus);
		bottom.addSeparator();
		bottom.add(status);
		spop = new JPopupMenu();
		spop.add(new FuzzAction("复制域名"));
		spop.add(new FuzzAction("复制IP"));
		spop.addSeparator();
		spop.add(new FuzzAction("访问域名"));
		spop.add(new FuzzAction("访问IP"));
		spop.addSeparator();
		spop.add(new FuzzAction("删除选中项"));
		spop.addSeparator();
		spop.add(new FuzzAction("导入"));
		spop.add(new FuzzAction("添加域名"));
		spop.add(new FuzzAction("导出选中域名"));
		spop.add(new FuzzAction("导出当前域名"));
		spop.addSeparator();
		spop.add(new FuzzAction("清空"));
		pop = new JPopupMenu();
		pop.add(new FuzzAction("导入"));
		pop.add(new FuzzAction("添加域名"));
		pop.add(new FuzzAction("导出选中域名"));
		pop.add(new FuzzAction("导出当前域名"));
		pop.addSeparator();
		pop.add(new FuzzAction("清空"));
		list.addMouseListener(new SelectedMenuListener());
		main.addMouseListener(new MenuListener());
		this.add(top, BorderLayout.NORTH);
		this.add(main);
		this.add(bottom, BorderLayout.SOUTH);

		int event_ctrl_mask = Toolkit.getDefaultToolkit()
				.getMenuShortcutKeyMask();

		FuzzAction copydomaction = new FuzzAction("复制域名");
		copydomaction.putValue(Action.ACTION_COMMAND_KEY, "复制域名");
		list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke('C', event_ctrl_mask), "diycopydom");
		list.getActionMap().put("diycopydom", copydomaction);

		FuzzAction adddomaction = new FuzzAction("添加域名");
		adddomaction.putValue(Action.ACTION_COMMAND_KEY, "添加域名");
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke('I', event_ctrl_mask), "diyadddom");
		this.getActionMap().put("diyadddom", adddomaction);

		FuzzAction deleteselaction = new FuzzAction("删除选中项");
		deleteselaction.putValue(Action.ACTION_COMMAND_KEY, "删除选中项");
		list.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke('D', event_ctrl_mask), "diydeletesel");
		list.getActionMap().put("diydeletesel", deleteselaction);

		FuzzAction countaddraction = new FuzzAction("统计地址");
		countaddraction.putValue(Action.ACTION_COMMAND_KEY, "统计地址");
		this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
				KeyStroke.getKeyStroke('B', event_ctrl_mask), "diycountaddr");
		this.getActionMap().put("diycountaddr", countaddraction);
	}

	class SelectedMenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				spop.show(list, e.getX(), e.getY());
			} else if (e.getClickCount() == 2) {
				int[] drows = list.getSelectedRows();
				col = list.convertColumnIndexToView(0);
				for (int i = 0; i < drows.length; i++) {
					String domain = Common.clearNull(list.getValueAt(drows[i],
							col));
					Common.openBrowse(Common.getHttpOrHttps(domain));
				}
			}
		}
	}

	class MenuListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON3) {
				pop.show(main, e.getX(), e.getY());
				// show(e.getComponent(),e.getX(),e.getY());
			}
		}
	}

	public void start(List<String> l) {
		List<String> s_domains = new ArrayList<String>();
		boolean ismatches = Pattern.matches("[a-zA-Z0-9\\.\\-:\\{\\}\\s]+",
				root.getText().trim());
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				start.setEnabled(false);
			}
		});
		if (!ismatches) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					start.setEnabled(true);
					JOptionPane.showMessageDialog(getRootPane(), "请输正确的域名!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			return;
		}
		int mi = 0, ma = 0;
		try {
			mi = Integer.parseInt(min.getText());
			ma = Integer.parseInt(max.getText());
		} catch (Exception e) {
		}
		if (ma > 4) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					start.setEnabled(true);
					JOptionPane.showMessageDialog(getRootPane(), "最大支持4位字符",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			return;
		} else if ((mi <= 0 && ma <= 0) || (mi > ma)) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					start.setEnabled(true);
					JOptionPane.showMessageDialog(getRootPane(), "请输入正确的规则",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			return;
		}

		int threads = 0;
		int tlayers = -1;
		try {
			threads = Integer.parseInt(thread.getText().trim());
			tlayers = Integer.parseInt(layer.getText().trim());
		} catch (Exception e) {
		}
		final int layers = tlayers;
		if (threads <= 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					start.setEnabled(true);
					JOptionPane.showMessageDialog(getRootPane(), "请输入正确的线程数!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			return;
		} else if (layers < 0) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					start.setEnabled(true);
					JOptionPane.showMessageDialog(getRootPane(), "请输入正确的遍历层数!",
							"提示", JOptionPane.INFORMATION_MESSAGE);
				}
			});
			return;
		}
		if (l == null) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					lstatus.setText("正在生成前缀...");
				}
			});
			s_domains = getDomains(root.getText().trim());
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					lstatus.setText("生成前缀完毕");
				}
			});
		} else {
			s_domains = l;
		}
		String init = Common.getIp(Common.getRandomString(8) + "."
				+ root.getText().trim());
		final int size = s_domains.size();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				b_start.setEnabled(false);
				stop.setEnabled(true);
				add.setEnabled(false);
				pause.setEnabled(true);
			}
		});
		FuzzSwingWorker.getDomains().clear(); // 清空发现的域名
		FuzzSwingWorker.setCancel(false);
		FuzzSwingWorker.setCount(0);
		double d = size / (double) threads;
		int step = (int) Math.ceil(d);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				lstatus.setText("开始遍历" + root.getText().trim() + "域名");
			}
		});

		final ExecutorService pool = Executors.newFixedThreadPool(threads);
		for (int i = 0; i < threads; i++) {
			int start = i * step;
			int end = start + step;
			if (end >= size) {
				end = size;
				threads = i;
			}
			pool.execute(new FuzzSwingWorker(list, s_domains, start, end, init));
		}
		pool.shutdown();
		new SwingWorker<Void, Void>() {
			protected Void doInBackground() throws Exception {
				while (true) {
					if (pool.isTerminated()) {
						break;
					}
				}
				return null;
			}

			protected void done() {
				Set<String> tmp = FuzzSwingWorker.getDomains();
				domains.addAll(tmp);
				if (FuzzSwingWorker.isCanceled()) {
					lstatus.setText("用户已中断,本次发现" + tmp.size() + "个域名,总共发现"
							+ domains.size() + "个域名");
				} else {
					lstatus.setText("任务完成,本次发现" + tmp.size() + "个域,总共发现"
							+ domains.size() + "个域名");
				}
				start.setEnabled(true);
				b_start.setEnabled(true);
				stop.setEnabled(false);
				add.setEnabled(true);
				pause.setEnabled(false);
				int ma2 = Integer.parseInt(max2.getText().trim());
				int mi2 = Integer.parseInt(min2.getText().trim());
				if (layers <= 0 || domains.size() == 0
						|| (mi2 <= 0 && ma2 <= 0) || (mi2 > ma2)) {
					return;
				} else if (c2.isSelected() && name2.getText().trim().equals("")) {
				} else {
					b_domains.addAll(domains);
					start.setEnabled(false);
					stop.setEnabled(true);
					add.setEnabled(false);
					new Thread(new Runnable() {
						public void run() {
							traversal(layers);
						}
					}).start();
				}
			};
		}.execute();
	}

	public void bl() {
		int b_b_threads = 0;
		int tb_layers = 0;
		int ma2 = 0;
		int mi2 = 0;
		try {
			b_b_threads = Integer.parseInt(thread.getText().trim());
			tb_layers = Integer.parseInt(layer.getText().trim());
			ma2 = Integer.parseInt(max2.getText().trim());
			mi2 = Integer.parseInt(min2.getText().trim());
		} catch (Exception e) {
		}
		final int b_layers = tb_layers;
		b_domains.addAll(domains);
		if (b_b_threads <= 0) {
			JOptionPane.showMessageDialog(getRootPane(), "请输入正确的线程数!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if (b_layers <= 0) {
			JOptionPane.showMessageDialog(getRootPane(), "请输入正确的遍历层数!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if (b_domains.size() == 0) {
			JOptionPane.showMessageDialog(getRootPane(), "请导入域名!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if ((mi2 <= 0 && ma2 <= 0) || (mi2 > ma2)) {
			JOptionPane.showMessageDialog(getRootPane(), "请输入正确规则!", "提示",
					JOptionPane.INFORMATION_MESSAGE);

			return;
		} else if (c2.isSelected() && name2.getText().trim().equals("")) {
			JOptionPane.showMessageDialog(getRootPane(), "请导入字典2!", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		} else if (ma2 > 4) {
			JOptionPane.showMessageDialog(getRootPane(), "最大支持4位字符", "提示",
					JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		start.setEnabled(false);
		b_start.setEnabled(false);
		stop.setEnabled(true);
		add.setEnabled(false);
		new Thread(new Runnable() {
			public void run() {
				traversal(b_layers);
			}
		}).start();
	}

	public class FuzzAction extends AbstractAction {
		public FuzzAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			switch (e.getActionCommand()) {
			case "统计地址":
				int total = dtm.getRowCount();
				lstatus.setText("总共" + total + "个");
				status.setText("");
				break;
			case "删除选中项":
				act = JOptionPane.showConfirmDialog(getRootPane(), "确定要删除选中项？",
						"提示", JOptionPane.YES_NO_OPTION);
				if (act == 0) {
					int[] rows = list.getSelectedRows();
					for (int i = 0; i < rows.length; i++) {
						rows[i] = list.convertRowIndexToModel(rows[i]); // 排序过后视图与模型的索引将不会对应，需要使用ToModel将视图索引转换成对应的模型索引。
					}
					Arrays.sort(rows); // 排序
					lstatus.setText("正在删除...");
					status.setText("");
					Set<String> set = FuzzSwingWorker.getDomains();
					for (int j = rows.length - 1; j >= 0; j--) // 倒序，从最后开始删除。如果从前面开始删除，删除一项后面的都会变动，所以要从后面的删。
					{
						String domain = Common.clearNull(dtm.getValueAt(
								rows[j], 0));
						domains.remove(domain);
						b_domains.remove(domain);
						set.remove(domain);
						dtm.removeRow(rows[j]);
					}
					lstatus.setText("选中项已删除");
					status.setText("");
				}
				break;
			case "添加域名":
				String adddomain = JOptionPane.showInputDialog(getRootPane(),
						"请输入要遍历域名", "添加域名", JOptionPane.PLAIN_MESSAGE);
				if (adddomain != null && !adddomain.trim().equals("")) {
					String address = getFuzzDomain(adddomain);
					if (!address.trim().equals("")) {
						b_domains.add(address);
						Vector data = new Vector();
						data.add(address);
						dtm.addRow(data);
					}
					lstatus.setText("添加域名成功");
					status.setText("");
				}
				break;
			case "复制域名":
				data = "";
				rows = list.getSelectedRows();
				col = list.convertColumnIndexToView(0);
				for (int i = 0; i < rows.length; i++) {
					String domain = Common.getDomain(Common.clearNull(list
							.getValueAt(rows[i], col)));
					data = data + domain + ConstantClass.SYSTEM_SP;
				}
				cb.setContents(new StringSelection(data.trim()), null);
				break;
			case "复制IP":
				data = "";
				rows = list.getSelectedRows();
				col = list.convertColumnIndexToView(1);
				for (int i = 0; i < rows.length; i++) {
					String ip = Common.clearNull(list.getValueAt(rows[i], col));
					data = data + ip + ConstantClass.SYSTEM_SP;
				}
				cb.setContents(new StringSelection(data.trim()), null);
				break;
			case "访问域名":
				rows = list.getSelectedRows();
				col = list.convertColumnIndexToView(0);
				for (int i = 0; i < rows.length; i++) {
					String domain = Common.clearNull(list.getValueAt(rows[i],
							col));
					Common.openBrowse(Common.getHttpOrHttps(domain));
				}
				break;
			case "访问IP":
				rows = list.getSelectedRows();
				col = list.convertColumnIndexToView(1);
				for (int i = 0; i < rows.length; i++) {
					String ip = Common.clearNull(list.getValueAt(rows[i], col));
					String[] ips = ip.split(",");
					for (int j = 0; j < ips.length; j++) {
						Common.openBrowse(Common.getHttpOrHttps(ips[j]));
					}
				}
				break;
			case "清空":
				act = JOptionPane.showConfirmDialog(getRootPane(), "确定要清空列表？",
						"提示", JOptionPane.YES_NO_OPTION);
				if (act == 0) {
					dtm.setRowCount(0);
					domains.clear();
					b_domains.clear();
					lstatus.setText("列表已清空");
					status.setText("");
				}
				break;
			case "开始":
				// start2(null);
				new Thread(new Runnable() {
					public void run() {
						start(null);
					}
				}).start();
				break;
			case "暂停":
				pause.setText("继续");
				lstatus.setText("正在暂停...");
				FuzzSwingWorker.setPaused(true);
				lstatus.setText("暂停");
				break;
			case "继续":
				pause.setText("暂停");
				lstatus.setText("继续全部任务...");
				FuzzSwingWorker.setPaused(false);
				break;
			case "停止遍历":
				isStop = true;
				stop.setEnabled(false);
			case "停止":
				lstatus.setText("正在停止...");
				FuzzSwingWorker.setCancel(true);
				stop.setEnabled(false);
				break;
			case "遍历":
				bl();
				break;
			case "关于":
				new AboutDialog();
				break;
			case "快捷键":
				JOptionPane
						.showMessageDialog(
								getRootPane(),
								"Ctrl + I      添加域名\r\nCtrl + B      统计个数\r\nCtrl + C      复制域名\r\nCtrl + D      删除选中项\r\n\r\nMac下使用Command代替Ctrl",
								"快捷键", JOptionPane.INFORMATION_MESSAGE);
				break;
			case "导入":
				JFileChooser chooser = new JFileChooser(".");
				chooser.setDialogTitle("导入域名");
				chooser.setApproveButtonText("导入域名");
				int act = chooser.showOpenDialog(getRootPane());
				if (act == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						FileReader fr = new FileReader(file);
						BufferedReader br = new BufferedReader(fr);
						Set<String> iset = new LinkedHashSet<String>();
						String line;
						while ((line = br.readLine()) != null) {
							String domain = getFuzzDomain(line);
							if (!domain.equals("")) {
								// domains.add(domain);
								b_domains.add(domain);
								iset.add(domain);
							}
							add.setEnabled(true);
						}
						br.close();
						for (String d : iset) // 去重
						{
							Vector data = new Vector();
							data.add(d);
							((DefaultTableModel) list.getModel()).addRow(data);
						}
						lstatus.setText("导入域名成功");
						status.setText("");
					} catch (IOException e1) {
						lstatus.setText("导入域名失败");
						status.setText("");
					}
				}
				break;
			case "导出当前域名":
				chooser = new JFileChooser(".");
				chooser.setDialogTitle("导出当前域名");
				chooser.setSelectedFile(new File("fdomain.txt"));
				act = chooser.showSaveDialog(getRootPane());
				if (act == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						TreeSet<String> ts = new TreeSet<String>(
								new Comparator<String>() {
									public int compare(String o1, String o2) {
										return Common.compareAddress(o1, o2);
									}
								});
						row = list.getRowCount();
						col = list.convertColumnIndexToView(0);
						for (int i = 0; i < row; i++) {
							String temp = Common.getDomain(Common
									.clearNull(list.getValueAt(i, col)));
							if (temp.indexOf("{fuzz}") == -1) {
								ts.add(temp);
							}
						}
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						for (String domain : ts) {
							bw.write(domain);
							bw.newLine();
						}
						bw.flush();
						bw.close();
						lstatus.setText("导出当前域名成功");
						status.setText("");
					} catch (IOException e1) {
						lstatus.setText("导出当前域名失败");
						status.setText("");
					}
				}
				break;
			case "导出选中域名":
				chooser = new JFileChooser(".");
				chooser.setDialogTitle("导出选中域名");
				chooser.setSelectedFile(new File("fdomain.txt"));
				act = chooser.showSaveDialog(getRootPane());
				if (act == JFileChooser.APPROVE_OPTION) {
					File file = chooser.getSelectedFile();
					try {
						TreeSet<String> ts = new TreeSet<String>(
								new Comparator<String>() {
									public int compare(String o1, String o2) {
										return Common.compareAddress(o1, o2);
									}
								});
						rows = list.getSelectedRows();
						col = list.convertColumnIndexToView(0);
						for (int i = 0; i < rows.length; i++) {
							String temp = Common.getDomain(Common
									.clearNull(list.getValueAt(rows[i], col)));
							if (temp.indexOf("{fuzz}") == -1) {
								ts.add(temp);
							}
						}
						FileWriter fw = new FileWriter(file);
						BufferedWriter bw = new BufferedWriter(fw);
						for (String domain : ts) {
							bw.write(domain);
							bw.newLine();
						}
						bw.flush();
						bw.close();
						lstatus.setText("导出选中域名成功");
						status.setText("");
					} catch (IOException e1) {
						lstatus.setText("导出选中域名失败");
						status.setText("");
					}
				}
			}
		}
	}

	private boolean isReplace(String root) {
		if (root.indexOf("{fuzz}") > -1) {
			return true;
		} else {
			return false;
		}
	}

	public List<String> getDomains(String root) {
		if (c.isSelected()) // 字典
		{
			return importDomains(root, name.getText().trim());
		} else if (cr.isSelected()) // 规则
		{
			int ma = Integer.parseInt(max.getText().trim());
			int mi = Integer.parseInt(min.getText().trim());
			return makeDomains(makeFuzz(rule.getText().trim(), ma), root, mi);
		}
		return null;
	}

	private List<String> getDomains2(String root) {
		if (c2.isSelected()) // 字典2
		{
			return importDomains(root, name2.getText().trim());
		} else if (cr2.isSelected()) // 规则2
		{
			int ma2 = Integer.parseInt(max2.getText().trim());
			int mi2 = Integer.parseInt(min2.getText().trim());
			return makeDomains(makeFuzz(rule2.getText().trim(), ma2), root, mi2);
		}
		return null;
	}

	private List<String> importDomains(String root, String path) {
		List<String> list = new ArrayList<String>();
		boolean isReplace = isReplace(root);
		File file = new File(path);
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				if (isReplace) {
					list.add(root.replace("{fuzz}", line));
				} else {
					list.add(line + "." + root);
				}
			}
			br.close();
		} catch (Exception e2) {
		}
		return list;
	}

	private List<String> makeDomains(List<String> fuzz, String root, int min) {
		List<String> domains = new ArrayList<String>();
		boolean isReplace = isReplace(root);
		for (String str : fuzz) {
			int len = str.length();
			if (min <= len) {
				if (isReplace) {
					domains.add(root.replace("{fuzz}", str));
				} else {
					domains.add(str + "." + root);
				}
			}
		}
		return domains;
	}

	public static List<String> makeFuzz(String str, int max) {
		List<String> list = new ArrayList<String>();
		List<String> list2 = new ArrayList<String>();
		List<String> domains = new ArrayList<String>();
		for (int i = 0; i < max; i++) {
			if (i == 0) {
				for (int j = 0; j < str.length(); j++) {
					list.add(String.valueOf(str.charAt(j)));
				}
			}
			for (int l = 0; l < list2.size(); l++) {
				for (int k = 0; k < str.length(); k++) {
					list.add(list2.get(l) + String.valueOf(str.charAt(k)));
				}
			}
			list2.clear();
			list2.addAll(list);
			domains.addAll(list2);
			list.clear();
		}
		return domains;
	}

	private void traversal(final int b_layers) {
		isStop = false;
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				b_stop.setEnabled(true);
				b_start.setEnabled(false);
				pause.setEnabled(true);
			}
		});
		for (time = 1; time <= b_layers; time++) {
			final int b_b_size = b_domains.size();
			if (b_b_size == 0) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						lstatus.setText("第" + (time - 1) + "层未遍历到域名,任务完成,总共发现"
								+ domains.size() + "个域名");
						start.setEnabled(true);
						b_start.setEnabled(true);
						stop.setEnabled(false);
						b_stop.setEnabled(false);
						add.setEnabled(true);
						pause.setEnabled(false);
					}
				});
				return;
			}
			final Set<String> ll = new TreeSet<String>();
			int num = 0;
			b_count = num;
			for (final String tmp : b_domains) {
				if (isStop) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							stop.setEnabled(false);
							b_stop.setEnabled(false);
							start.setEnabled(true);
							b_start.setEnabled(true);
							add.setEnabled(true);
							pause.setEnabled(false);
							lstatus.setText("全部停止,任务完成,总共发现"
									+ (domains.size() + ll.size()) + "个域名");
						}
					});
					return;
				}
				int b_threads = Integer.parseInt(thread.getText().trim());
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						stop.setEnabled(true);
						lstatus.setText("开始遍历第" + time + "层(当前" + time + "/总共"
								+ b_layers + ")的" + tmp + "域名(当前" + (++b_count)
								+ "/总共" + b_b_size + ")");
					}
				});
				String str = Common.getRandomString(8) + "." + tmp;
				String b_init = Common.getIp(str);
				List<String> l = getDomains2(tmp);
				int b_size = l.size();
				FuzzSwingWorker.getDomains().clear(); // 清空发现的域名
				FuzzSwingWorker.setCancel(false);
				FuzzSwingWorker.setCount(0);
				double b_d = b_size / (double) b_threads;
				int b_step = (int) Math.ceil(b_d);
				final ExecutorService b_pool = Executors
						.newFixedThreadPool(b_threads);
				for (int i = 0; i < b_threads; i++) {
					int start = i * b_step;
					int end = start + b_step;
					if (end >= b_size) {
						end = b_size;
						b_threads = i;
					}
					b_pool.execute(new FuzzSwingWorker(list, l, start, end,
							b_init));
				}
				b_pool.shutdown();
				while (true) {
					if (b_pool.isTerminated()) {
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								if (FuzzSwingWorker.isCanceled()) {
									lstatus.setText("用户已中断遍历第" + time + "层(当前"
											+ time + "/总共" + b_layers + ")的"
											+ tmp + "域名(当前" + b_count + "/总共"
											+ b_b_size + ")");
								} else {
									lstatus.setText("遍历第" + time + "层(当前"
											+ time + "/总共" + b_layers + ")的"
											+ tmp + "域名(当前" + b_count + "/总共"
											+ b_b_size + ")完毕");
								}
							}
						});
						ll.addAll(FuzzSwingWorker.getDomains());
						break;
					}
				}
			}
			b_domains = ll;
			domains.addAll(b_domains);
			if (time == b_layers) {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						start.setEnabled(true);
						b_start.setEnabled(true);
						stop.setEnabled(false);
						b_stop.setEnabled(false);
						add.setEnabled(true);
						pause.setEnabled(true);
						lstatus.setText("任务完成,总共发现" + domains.size() + "个域名");
					}
				});
			}
		}
	}

	public String getFuzzDomain(String url) {
		Matcher m = Pattern.compile(
				"(http(s)?://)?(?<domain>[\\w\\.:\\-\\{\\}]+\\.[a-zA-Z]+)",
				Pattern.CASE_INSENSITIVE).matcher(url);
		if (m.find()) {
			return m.group("domain");
		} else {
			return "";
		}
	}

	// 暂时性解决，后面完成功能后研究下table的排序跟过滤。
	class TableHeaderListener extends MouseAdapter {
		private int count = 0;
		private int old = 0;

		public void mouseClicked(MouseEvent e) {
			int col = list.columnAtPoint(e.getPoint());
			if (old != col) {
				count = 0;
			}
			old = col;
			count++;
			if ((count % 3) == 0) // 在启用了自动排序时，单数为正序，双数为倒序，默认并没有取消排序功能，所以要在第三次单击时设置取消排序的功能。
			{
				list.getRowSorter().setSortKeys(null);
			}
		}
	}
}
