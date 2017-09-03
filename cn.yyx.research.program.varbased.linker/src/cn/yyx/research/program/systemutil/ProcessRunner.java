package cn.yyx.research.program.systemutil;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProcessRunner {

	public static void RunOneProcess(File working_directory, String cmd, int max_run_time,
			boolean redirect_standard_out_stream, File log_file) {
		try {
			List<String> commands = new LinkedList<String>();
			{
				// this block of code is important, add "sh -c" to the command to be executed
				// could simulate the console completely.
				String os_name = System.getProperty("os.name").toLowerCase();
				boolean operating_system_is_windows = os_name.indexOf("windows") >= 0;
				if (operating_system_is_windows) {
					// the operating system is windows.
					commands.add("cmd");
					commands.add("/c");
					commands.add(cmd);
				} else {
					// the operating system is linux or mac.
					commands.add("sh");
					commands.add("-c");
					commands.add(cmd);
				}
			}
			// redirect output and error streams of the newly created process to a file or
			// to the streams of current process.
			ProcessBuilder pb = new ProcessBuilder(commands);
			pb.directory(working_directory);
			if (redirect_standard_out_stream) {
				// redirect to current process.
				pb.redirectError(Redirect.INHERIT);
				pb.redirectOutput(Redirect.INHERIT);
			} else {
				if (log_file != null) {
					// redirect to a file.
					pb.redirectError(Redirect.appendTo(log_file));
					pb.redirectOutput(Redirect.appendTo(log_file));
				}
			}
			Process process = pb.start();
			if (max_run_time <= 0) {
				process.waitFor();
			} else {
				process.waitFor(max_run_time, TimeUnit.SECONDS);
				process.destroyForcibly();
			}
			Thread.sleep(1000);
			SystemUtil.Flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
