package com.ms509.util;

import java.awt.Desktop;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {
	public static boolean isHttps = false;

	public static int compareColonIp(String address1, String address2) {
		String[] t1 = address1.split(":");
		String[] t2 = address2.split(":");
		int n = compare(t1[0], t2[0]);
		if (n == 0) {
			int n1 = t1.length;
			int n2 = t2.length;
			if (n1 == n2) {
				if (n1 == 2) {
					return Integer.parseInt(t1[1]) - Integer.parseInt(t2[1]);
				} else {
					return 0;
				}
			} else {
				return n1 > n2 ? 1 : -1;
			}
		} else {
			return n;
		}
	}

	public static int compareAddress(String address1, String address2) {
		String ipregex = "(\\d{1,3}\\.){3}\\d{1,3}(:\\d+)?";
		String domainregex = "(?i)[\\w\\.:\\-\\{\\}]+\\.[a-zA-Z]+(:\\d+)?";
		boolean i1 = Pattern.matches(ipregex, address1);
		boolean i2 = Pattern.matches(ipregex, address2);
		boolean d1 = Pattern.matches(domainregex, address1);
		boolean d2 = Pattern.matches(domainregex, address2);
		if (i1 && i2) {
			return compareColonIp(address1, address2);
		} else if (d1 && d2) {
			return address1.compareToIgnoreCase(address2);

		} else {
			return i1 ? 1 : -1;
		}
	}

	public static int compare(String ip1, String ip2) {
		String ipregex = "(\\d{1,3}\\.){3}\\d{1,3}(:\\d+)?";
		boolean i1 = Pattern.matches(ipregex, ip1);
		boolean i2 = Pattern.matches(ipregex, ip2);
		if (i1 && i2) {
			String[] t1 = ip1.split("\\.");
			String[] t2 = ip2.split("\\.");
			for (int i = 0; i < t1.length; i++) {
				int n = Integer.parseInt(t1[i]) - Integer.parseInt(t2[i]);
				if (n != 0) {
					return n;
				}
			}
			return 0;
		} else if (!i1 && !i2) {
			return ip1.compareToIgnoreCase(ip2);
		} else {
			return i1 ? -1 : 1;
		}
	}

	public static long getSleepTime(int start, int end) {
		double time;
		if (start <= end) {
			time = start + Math.random() * (end - start);
			return (long) (time * 1000);
		} else {
			return 0;
		}
	}

	public static String getDomain(String address) {
		Matcher m = Pattern
				.compile(
						"(http(s)?://)?(?<domain>([\\w-]+\\.)+[a-z]+|(\\d{1,3}\\.){3}\\d{1,3})",
						Pattern.CASE_INSENSITIVE).matcher(address);
		if (m.find()) {
			return m.group("domain").toLowerCase();
		} else {
			return "";
		}
	}

	public static String getHttpOrHttps(String address) {
		if (address.indexOf("443") != -1) {
			return "https://" + address;
		} else {
			return "http://" + address;
		}
	}

	public static String clearNull(Object object) {
		return object != null ? object.toString() : "";
	}

	public static String getRandomString(int length) { // length表示生成字符串的长度
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	public static String getIp(String domain) {
		String ip = "";
		try {
			InetAddress[] ias = InetAddress.getAllByName(domain);
			for (InetAddress ia : ias) {
				ip = ip + ia.toString().split("/")[1] + ",";
			}
		} catch (UnknownHostException e) {
			ip = e.toString();
		}
		if (ip.length() > 0) {
			if (ip.substring(0, ip.length()).endsWith(",")) {
				return ip.substring(0, ip.length() - 1);
			}
		}
		return ip;
	}

	public static void openBrowse(String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (Exception e) {
		}
	}
}
