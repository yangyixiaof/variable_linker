package cn.yyx.research.program.systemutil;

import java.net.HttpURLConnection;
import java.net.URL;

public class URLUtil {

	public static boolean IsURLValid(String url_str) {
		URL url = null;  
		HttpURLConnection con;  
		int state = -1;
		int counts = 0;
		if (url_str == null || url_str.length() <= 0) {
			return false;
		}
		while (counts < 5) {
			try {
				url = new URL(url_str);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				if (state == 200) {
					return true;
				}
			} catch (Exception ex) {
				counts++;
				continue;
			}
		}
		return false;
	}

}
