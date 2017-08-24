package cn.yyx.research.program.systemutil;

import java.io.File;

public class CommandLineUtil {

	public static void ExecuteCommand(File dir, String command) {
		try {
			// String command="netstat -an";
			// String command = "c:\\windows\\system32\\cmd.exe /c netstat -an";
			ProcessRunner.RunOneProcess(command, -1);
//			Runtime r = Runtime.getRuntime();
//			Process p = r.exec(command, null, dir);
//			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
//			StringBuffer sb = new StringBuffer();
//			String inline = null;
//			while (null != (inline = br.readLine())) {
//				sb.append(inline).append("\n");
//			}
//			DebugLogger.Log(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
