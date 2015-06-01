package malte0811.multiDim;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class Downloader {
	private static boolean downloadDone = false;

	public static void main(String[] args) {
		String base = ClassLoader.getSystemResource("").toExternalForm()
				+ "MultiDimRot/lib/";
		try {
			start(base);
			return;
		} catch (Exception x) {
			x.printStackTrace();
		} catch (Error r) {
			r.printStackTrace();
		}
		// download and unzip libraries

		try {
			if (!Files.exists(Paths.get(new URI(base + "JCodec"))))
				Files.createDirectories(Paths.get(new URI(base + "JCodec")));
			if (!Files.exists(Paths.get(new URI(base + "lwjgl"))))
				Files.createDirectories(Paths.get(new URI(base + "lwjgl")));
			JFrame progressFrame = new JFrame("Downloading...");
			JLabel info = new JLabel("Downloading JCodec...");
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
						System.exit(0);
					}
				}
			});
			// jcodec:
			Path out = Paths.get(new URI(base + "JCodec/jcodec.jar"));
			URL website = new URL(
					"http://jcodec.org/downloads/jcodec-0.1.5.jar");
			download(website, out, pro, 912);

			// lwjgl:
			info.setText("Downloading LWJGL...");
			pro.setValue(0);
			out = Paths.get(new URI(base + "lwjgl/lwjgl.zip"));
			website = new URL(
					"http://downloads.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.3/lwjgl-2.9.3.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fjava-game-lib%2Ffiles%2FOfficial%2520Releases%2FLWJGL%25202.9.3%2F&ts=1432233219&use_mirror=heanet");
			download(website, out, pro, 7805);

			pro.setStringPainted(false);
			pro.setIndeterminate(true);
			info.setText("Unpacking LWJGL...");
			ZipFile extr = new ZipFile(new File(new URI(base
					+ "lwjgl/lwjgl.zip")));
			Enumeration<? extends ZipEntry> entries = extr.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				Path tmp = Paths.get(new URI(base + entry.getName()));
				// solaris is not supported
				if (!entry.isDirectory()
						&& (entry.getName().startsWith("lwjgl-2.9.3/native/")
								&& !entry.getName().contains("solaris") || entry
								.getName().startsWith("lwjgl-2.9.3/jar/lwjgl"))) {
					out = Paths.get(new URI(base
							+ "lwjgl/"
							+ (entry.getName().startsWith(
									"lwjgl-2.9.3/jar/lwjgl") ? "jar/"
									: "native/") + tmp.getFileName()));
					Files.createDirectories(out.getParent());
					if (!Files.exists(out)) {
						Files.createFile(out);
					}
					FileOutputStream fos = new FileOutputStream(out.toFile());
					InputStream in = extr.getInputStream(entry);
					int r = in.read();
					while (r != -1) {
						fos.write(r);
						r = in.read();
					}
					fos.flush();
					fos.close();
				}
			}
			extr.close();
			downloadDone = true;
			progressFrame.dispose();
			restartApplication();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void download(URL web, Path out, JProgressBar pro, int size)
			throws IOException {
		InputStream inSt = web.openStream();
		if (!Files.exists(out)) {
			Files.createDirectories(out.getParent());
			Files.createFile(out);
		}
		FileOutputStream fos = new FileOutputStream(out.toFile());
		int in;
		int pos = 0;
		pro.setMinimum(0);
		pro.setMaximum(size);
		pro.setStringPainted(true);
		do {
			in = inSt.read();
			if (in != -1) {
				fos.write(in);
			}
			pos++;
			// Update progress bar
			pro.setValue(pos / 1024);
			pro.setString((pos / 10) / size + "%");
		} while (in != -1);
		fos.flush();
		fos.close();
	}

	private static void start(String base) throws Exception, Error {
		if (System.getProperty("MultiDimRot.glLinked") == null) {
			System.setProperty("org.lwjgl.librarypath",
					Paths.get(new URI(base + "lwjgl/native")).toFile()
							.getAbsolutePath());
		}
		Class.forName("org.lwjgl.Sys");
		Class.forName("org.jcodec.common.model.Picture");
		// both jcodec and lwjgl are installed
		Class<?> main = Class.forName("malte0811.multiDim.Main");
		Class<?> solid = Class.forName("malte0811.multiDim.solids.Solid");
		Class<?> hyperCube = Class
				.forName("malte0811.multiDim.solids.HyperCube");

		main.getConstructor(solid).newInstance(hyperCube.newInstance());
		return;
	}

	public static void restartApplication() throws Exception {
		final String javaBin = System.getProperty("java.home") + File.separator
				+ "bin" + File.separator + "java";
		final File currentJar = new File(Downloader.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI());
		final ArrayList<String> command = new ArrayList<String>();
		command.add(javaBin);
		command.add("-jar");
		command.add(currentJar.getPath());

		final ProcessBuilder builder = new ProcessBuilder(command);
		builder.start();
		System.exit(0);
	}
}
