package malte0811.multiDim.commands;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

import malte0811.multiDim.Downloader;
import malte0811.multiDim.addons.Command;

public class CommandUpdate extends Command {
	private boolean downloadDone = false;
	private boolean cancelDownload = false;

	@Override
	public String getCommandName() {
		return "UPDATE";
	}

	@Override
	public String getCommandUsage() {
		return "\"update [branch]\" downloads and installs he newest stable version of MultiDimRot and restarts the program. If a \"branch\"-parameter is given, the newest version available on that branch will be used.";
	}

	@Override
	public int getMinParameterCount() {
		return 0;
	}

	@Override
	public void processCommand(String[] args) throws Exception {
		downloadDone = false;
		cancelDownload = false;
		try {
			File currentJar = new File(Downloader.class.getProtectionDomain()
					.getCodeSource().getLocation().toURI());
			if (!currentJar.getName().endsWith(".jar")) {
				System.out.println("Can not update a non-jar installation");
				return;
			}
			URL url = new URL(
					"https://github.com/malte0811/MultiDimRot/blob/master/MultiDimRot.jar?raw=true");
			if (args.length >= 1) {
				url = new URL("https://github.com/malte0811/MultiDimRot/blob/"
						+ args[0] + "/MultiDimRot.jar?raw=true");
			}
			JFrame progressFrame = new JFrame("Downloading...");
			JLabel info = new JLabel("Updating...");
			JProgressBar pro = new JProgressBar();
			BorderLayout bl = new BorderLayout();
			progressFrame.setLayout(bl);
			progressFrame.add(info, BorderLayout.NORTH);
			progressFrame.add(pro, BorderLayout.SOUTH);
			progressFrame.setSize(300, 80);
			progressFrame.setResizable(false);
			progressFrame.setVisible(true);
			progressFrame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					super.windowClosed(e);
					if (!downloadDone) {
						cancelDownload = true;
					}
				}
			});
			Path out = Paths.get(ClassLoader.getSystemResource(".").toURI())
					.resolve("TMP.jar");
			if (!Files.exists(out)) {
				Files.createFile(out);
			}
			download(url, out, pro);
			if (!cancelDownload) {
				if (currentJar.delete() && out.toFile().renameTo(currentJar)) {
					Downloader.restartApplication();
				} else {
					System.out
							.println("Unable to delete current jar and rename downloaded jar.");
				}
			} else {
				out.toFile().delete();
			}
		} catch (Exception x) {
			System.out.println("Update failed.");
			x.printStackTrace();
		}
	}

	private void download(URL web, Path out, JProgressBar pro)
			throws IOException {
		InputStream inSt = web.openStream();
		if (!Files.exists(out)) {
			Files.createDirectories(out.getParent());
			Files.createFile(out);
		}
		FileOutputStream fos = new FileOutputStream(out.toFile());
		int in;
		int pos = 0;
		final int bufferSize = 1024;
		byte[] buffer = new byte[bufferSize];
		pro.setIndeterminate(true);
		do {
			in = inSt.read();
			if (in != -1) {
				buffer[pos % bufferSize] = (byte) in;
				if (pos % bufferSize == bufferSize - 1) {
					fos.write(buffer);
					buffer = new byte[bufferSize];
				}
			}
			pos++;
		} while (in != -1 && !cancelDownload);
		fos.write(buffer, 0, pos % bufferSize);
		fos.flush();
		fos.close();
	}

}
