package com.ms509.ui.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import com.ms509.ui.FuzzPanel;

public class Menu extends JMenuBar{
	public Menu()
	{
		FuzzPanel fp = new FuzzPanel();
		JMenu file = new JMenu("文件");
		file.add(fp.new FuzzAction("导入"));
		file.add(fp.new FuzzAction("添加域名"));
		file.add(fp.new FuzzAction("导出选中域名"));
		file.add(fp.new FuzzAction("导出当前域名"));
		file.addSeparator();
		file.add(fp.new FuzzAction("清空"));
		
		JMenu help = new JMenu("帮助");
		help.add(fp.new FuzzAction("关于"));
		help.add(fp.new FuzzAction("快捷键"));
		
		this.add(file);
		this.add(help);
	}
}
