package com.ms509.util;

import java.io.File;

public class InitConfig {
	private Configuration config = new Configuration();

	public InitConfig() {
		File conf = new File("Config.ini");
		if (conf.exists()) {
			// WriteParams(); //调试的时候取消注释，即无论怎么都要重新写一遍配置文件，发布的时候不要忘记注释掉。
			LoadParams();
		} else {
			WriteParams();
			LoadParams();
		}
	}

	private void WriteParams() {
		config.setValue("CHECKBOX", "2");
		config.setValue("CHECKBOX2", "2");
	}

	private void LoadParams() {
		ConstantClass.CHECKBOX = config.getValue("CHECKBOX");
		ConstantClass.CHECKBOX2 = config.getValue("CHECKBOX2");
		ConstantClass.DIC = config.getValue("DIC");
		ConstantClass.DIC2 = config.getValue("DIC2");
	}
}
