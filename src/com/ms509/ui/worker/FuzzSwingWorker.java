package com.ms509.ui.worker;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import com.ms509.ui.FuzzPanel;
import com.ms509.util.Common;

public class FuzzSwingWorker extends SwingWorker<Void, Integer> {

	private JTable list;
	private DefaultTableModel dtm;
	private FuzzPanel fp;
	private List<String> l;
	private int start;
	private int end;
	private String init;
	private static int count = 0;
	private static volatile boolean canceled = false;
	private static volatile boolean paused = false;
	private static Set<String> domains = new LinkedHashSet<String>();
	
	private synchronized void workDone()
	{
		count++;
	}

	public static boolean isCanceled() {
		return canceled;
	}

	public static void setCancel(boolean b) {
		canceled = b;
	}

	public static boolean isPaused() {
		return paused;
	}

	public static void setPaused(boolean paused) {
		FuzzSwingWorker.paused = paused;
	}

	public static void setCount(int count) {
		FuzzSwingWorker.count = count;
	}

	public static Set<String> getDomains() {
		return domains;
	}

	public FuzzSwingWorker(JTable list, List<String> l, int start, int end,
			String init) {
		this.list = list;
		this.dtm = (DefaultTableModel) list.getModel();
		fp = (FuzzPanel) list.getParent().getParent().getParent();
		this.l = l;
		this.start = start;
		this.end = end;
		this.init = init;
	}

	protected Void doInBackground() throws Exception {
		for (; this.start < this.end; this.start++) {
			setList(l.get(this.start));
			if (!canceled) {
				publish(this.start);
			} else {
				break;
			}
			// 暂停，必须使用Thread.sleep，如果不使用虽然也能实现暂停，但是CPU使用率会高达90%。也不能使用Thread.currentThread().wait()会直接结束。
			while (paused) {
				Thread.sleep(1000);
			}
		}
		return null;
	}

	private boolean notFound(String ip, String init) {
		// String ip1 = ip.split(",")[0]; // 只查找第一个IP
		// return init.indexOf(ip1) == -1 ? true : false;

		String ips[] = ip.split(",");
		for (String ip1 : ips) {
			if (init.indexOf(ip1) != -1) {
				return false;
			}
		}
		return true;
	}

	private void setList(String domain) {
		String ip = Common.getIp(domain);
		if (!ip.startsWith("java.net.UnknownHostException")) {
			if (init.startsWith("java.net.UnknownHostException")) {
				addData(domain, ip);
			} else if (notFound(ip, init)) {
				addData(domain, ip);
			}
		}
	}

	private void setStatus(final String str) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (!isCancelled()) {
					fp.getStatus().setText(str);
				}
			}
		});
	}

	private void addData(final String domain, final String ip) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					domains.add(domain);
					Vector<String> row = new Vector<String>();
					row.add(domain);
					row.add(ip);
					dtm.addRow(row);
				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
		}
	}

	protected void process(List<Integer> chunks) {
		for (Integer i : chunks) {
			workDone();
			fp.getStatus().setText("当前" + count + "/" + "总共" + l.size());
		}
	}
}
