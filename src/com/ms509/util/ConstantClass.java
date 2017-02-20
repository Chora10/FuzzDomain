package com.ms509.util;

import java.util.HashMap;
import java.util.Map;

public class ConstantClass {
	public static int ADDRESS_COLUMN = 0;
	public static int IP_COLUMN = 1;
	public static int CDN_COLUMN = 2;
	public static int CODE_COLUMN = 3;
	public static int SERVER_COLUMN = 4;
	public static int TITLE_COLUMN = 5;
	public static int CONTENT_COLUMN = 6;
	public static int STATUS_HEIGHT = 19;
	public static String SYSTEM = System.getProperty("os.name");
	public static String SYSTEM_SP = System.getProperty("line.separator");	// 也可以使用System.lineSeparator(),只是兼容性没有使用中的高
	public static String GOOGLE_URL;
	public static String GOOGLE_REG;
	public static int GOOGLE_REQUEST_TIME_START;
	public static int GOOGLE_REQUEST_TIME_END;
	public static String BAIDU_URL;
	public static String BAIDU_REG;
	public static int BAIDU_REQUEST_TIME_START;
	public static int BAIDU_REQUEST_TIME_END;
	public static int CONNECT_TIME;
	public static int READ_TIME;
	public static String ENCODE;
	public static int RETRY;
	public static boolean REDIRECT;
	public static String HEADER_DATA;
	public static String API;
	public static String CHECKBOX;
	public static String CHECKBOX2;
	public static String DIC;
	public static String DIC2;
	public static Map<String,Object> header = new HashMap<String,Object>();
	public static Map<String,Object> config = new HashMap<String,Object>();
}
