package cn.yyx.research.program.systemutil;

import java.lang.ProcessBuilder.Redirect;
import java.util.LinkedList;
import java.util.List;
// import java.util.Map;

public class ProcessRunner {
	
	public static void RunOneProcess(String cmd, int max_run_time) {
		try {
			List<String> commands = new LinkedList<String>();
			if (EnvironmentUtil.IsWindows()) {
				commands.add("cmd");
				commands.add("/c");
				String[] cmds = cmd.split(" ");
				for (int i=0;i<cmds.length;i++) {
					commands.add(cmds[i]);
				}
			} else {
				commands.add("sh");
				commands.add("-c");
				commands.add(cmd);
			}
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.redirectInput(Redirect.INHERIT);
			pb.redirectOutput(Redirect.INHERIT);
			pb.redirectError(Redirect.INHERIT);
			Process process = pb.start();
			if (max_run_time <= 0) {
				process.waitFor();
			} else {
				int total_seconds = 0;
				while (process.isAlive() && total_seconds < max_run_time)
				{
					total_seconds += 5;
					try {
						Thread.sleep(total_seconds * 1000);
					} catch (Exception e) {
					}
				}
				process.destroyForcibly();
			}
			Thread.sleep(1000);
			SystemUtil.Flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
