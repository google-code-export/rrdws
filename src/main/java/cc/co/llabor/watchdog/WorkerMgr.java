package cc.co.llabor.watchdog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeoutException;

/**
 * <b>Description:TODO</b>
 * 
 * @author gennady<br>
 *         <br>
 *         <b>Copyright:</b> Copyright (c) 2006-2008 Monster AG <br>
 *         <b>Company:</b> Monster AG <br>
 * 
 * Creation: Mar 22, 2011::10:34:06 PM<br>
 */
public class WorkerMgr {
	public static int executeCommandLine(final String commandLine,
			final boolean printOutput, final boolean printError,
			final long timeout,
			StringBuffer procOut 
			) throws IOException, InterruptedException,
			TimeoutException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(commandLine);
		/* Set up process I/O. */

		Worker worker = new Worker(process, procOut);
		worker.start();
		try {
			worker.join(timeout);
			if (worker.exit != null)
				return worker.exit;
			else
				throw new TimeoutException();
		} catch (InterruptedException ex) {
			worker.interrupt();
			Thread.currentThread().interrupt();
			throw ex;
		} finally {
			process.destroy();
		}
	}

	private static class Worker extends Thread {
		private final Process process;
		private Integer exit;
		StringBuffer procOut ;
 

		private Worker(Process process, StringBuffer procOutPar) {
			this.process = process;
			this.procOut = procOutPar;
		}

		public void run() {
			try {
				java.io.InputStream inputstream = process.getInputStream();
			    BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
			    String s3;
			    while((s3 = bufferedreader.readLine()) != null) 
			    {
			    	procOut.append(s3);
			    	procOut.append("\n");
			    }				
				exit = process.waitFor();
			} catch (InterruptedException ignore) {
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
		}
	}
}
