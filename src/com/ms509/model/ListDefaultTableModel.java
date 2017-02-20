package com.ms509.model;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class ListDefaultTableModel extends DefaultTableModel{
	public ListDefaultTableModel(Vector datas,Vector header)
	{
		super(datas, header);
	}
	public ListDefaultTableModel(Object[][] datas,Object[] header)
	{
		super(datas, header);
	}
	public int getRow(Object search)
	{
		for(int i=0;i<getRowCount();i++)
		{
			Object domain = ((Vector)getDataVector().elementAt(i)).elementAt(0);
			if(search.equals(domain))
			{
				return i;
			}
		}
		return -1;
	}
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}
