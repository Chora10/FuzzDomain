package com.ms509.ui.renderer;

import java.awt.Color;
import java.awt.Component;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import com.ms509.util.Common;
import com.ms509.util.ConstantClass;

public class ExtendDefaultTableCellRenderer extends DefaultTableCellRenderer {
	private Set<String> highColor = new TreeSet<String>();
	private Set<String> midColor = new TreeSet<String>();
	private Set<String> lowColor = new TreeSet<String>();
	private Set<String> cnetColor = new TreeSet<String>();

	public Set<String> getHighColor() {
		return highColor;
	}

	public Set<String> getMidColor() {
		return midColor;
	}

	public Set<String> getLowColor() {
		return lowColor;
	}

	public Set<String> getCnetColor() {
		return cnetColor;
	}

	private JTable table;

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
				row, column);
		this.table = table;
		// 必须在设置颜色前
		if (isSelected) {
			setForeground(table.getSelectionForeground());
			setBackground(table.getSelectionBackground());
		} else {
			setForeground(table.getForeground());
			setBackground(table.getBackground());
		}
		int mcolumn = table.convertColumnIndexToModel(column);
		if (mcolumn == ConstantClass.IP_COLUMN) {
			if (isMark(row, cnetColor)) {
				setBackground(Color.GREEN);
				// setForeground(Color.GREEN);
			}
		} else if (mcolumn == ConstantClass.ADDRESS_COLUMN) {
			if (isMark(row, highColor)) {
				setBackground(Color.RED);
				// setForeground(Color.RED);
			}
			if (isMark(row, midColor)) {
				setBackground(Color.ORANGE);
				// setForeground(Color.ORANGE);
			}
			if (isMark(row, lowColor)) {
				setBackground(Color.GRAY);
				// setForeground(Color.GRAY);
			}
		}
		return this;
	}

	private boolean isMark(int row, Set<String> set) {
		int col = table.convertColumnIndexToModel(ConstantClass.ADDRESS_COLUMN);
		for (String str : set) {
			if (str.equals(Common.clearNull(table.getValueAt(row, col)))) {
				return true;
			}
		}
		return false;
	}

	public void fromString(String data) {
		Matcher m = Pattern
				.compile(
						"highColor:([\\w-:\\.,]*?)midColor:([\\w-:\\.,]*?)lowColor:([\\w-:\\.,]*?)cnetColor:([\\w-:\\.,]*)")
				.matcher(data);
		if (m.find()) {
			String hdata = m.group(1);
			String mdata = m.group(2);
			String ldata = m.group(3);
			String cdata = m.group(4);
			if (!hdata.equals("")) {
				String[] tmp = hdata.split(",");
				Set<String> ts = new TreeSet<String>();
				for (int i = 0; i < tmp.length; i++) {
					ts.add(tmp[i]);
				}
				highColor.addAll(ts);
			}
			if (!mdata.equals("")) {

				String[] tmp = mdata.split(",");
				Set<String> ts = new TreeSet<String>();
				for (int i = 0; i < tmp.length; i++) {
					ts.add(tmp[i]);
				}
				midColor.addAll(ts);
			}
			if (!ldata.equals("")) {
				String[] tmp = ldata.split(",");
				Set<String> ts = new TreeSet<String>();
				for (int i = 0; i < tmp.length; i++) {
					ts.add(tmp[i]);
				}
				lowColor.addAll(ts);
			}
			if (!cdata.equals("")) {
				String[] tmp = cdata.split(",");
				Set<String> ts = new TreeSet<String>();
				for (int i = 0; i < tmp.length; i++) {
					ts.add(tmp[i]);
				}
				cnetColor.addAll(ts);
			}
		}
	}

	public void clear() {
		highColor.clear();
		midColor.clear();
		lowColor.clear();
		cnetColor.clear();
	}

	@Override
	public String toString() {
		String hdata = "";
		for (String str : highColor) {
			hdata = hdata + str + ",";
		}
		String mdata = "";
		for (String str : midColor) {
			mdata = mdata + str + ",";
		}
		String ldata = "";
		for (String str : lowColor) {
			ldata = ldata + str + ",";
		}
		String cdata = "";
		for (String str : cnetColor) {
			cdata = cdata + str + ",";
		}
		String data = "highColor:" + hdata + "midColor:" + mdata + "lowColor:"
				+ ldata + "cnetColor:" + cdata;
		return data;
	}
}
