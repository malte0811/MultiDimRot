package malte0811.multiDim;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Downloader {

	public static void main(String[] args) {
		String base = ClassLoader.getSystemResource("").toExternalForm()
				+ "lib/";
		// DEBUG
		System.out.println(base);
		try {
			if (Files.exists(Paths.get(new URI(base)))
					&& Files.exists(Paths.get(new URI(base + "JCodec")))
					&& Files.exists(Paths.get(new URI(base + "lwjgl")))) {
				URLClassLoader loader = new URLClassLoader(new URL[] {
						new URL(base), new URL(base + "JCodec/jcodec.jar"),
						new URL(base + "lwjgl/jars/lwjgl.jar"),
						new URL(base + "lwjgl/jars/lwjgl_util.jar") });

				loader.loadClass("org.lwjgl.opengl.Display");
				loader.loadClass("org.jcodec.common.model.Picture");
				// both jcodec and lwjgl are installed
				System.out.println("successful loading");
				System.setProperty("org.lwjgl.librarypath",
						Paths.get(new URI(base + "lwjgl/native")).toFile()
								.getAbsolutePath());
				Class<?> main = loader.loadClass("malte0811.multiDim.Main");
				Class<?> solid = loader
						.loadClass("malte0811.multiDim.solids.Solid");
				Class<?> hyperCube = loader
						.loadClass("malte0811.multiDim.solids.HyperCube");

				main.getConstructor(solid).newInstance(hyperCube.newInstance());

			}
		} catch (Exception x) {
			x.printStackTrace();
		}
		// download and unzip libraries
		System.out.println("starting download");
		try {
			if (!Files.exists(Paths.get(new URI(base + "JCodec"))))
				Files.createDirectories(Paths.get(new URI(base + "JCodec")));
			if (!Files.exists(Paths.get(new URI(base + "lwjgl"))))
				Files.createDirectories(Paths.get(new URI(base + "lwjgl")));

			// jcodec:
			Path out = Paths.get(new URI(base + "JCodec/jcodec.jar"));
			URL website = new URL(
					"http://jcodec.org/downloads/jcodec-0.1.5.jar");
			Files.copy(website.openStream(), out,
					StandardCopyOption.REPLACE_EXISTING);

			// lwjgl:
			out = Paths.get(new URI(base + "lwjgl/lwjgl.zip"));
			website = new URL(
					"http://downloads.sourceforge.net/project/java-game-lib/Official%20Releases/LWJGL%202.9.3/lwjgl-2.9.3.zip?r=http%3A%2F%2Fsourceforge.net%2Fprojects%2Fjava-game-lib%2Ffiles%2FOfficial%2520Releases%2FLWJGL%25202.9.3%2F&ts=1432233219&use_mirror=heanet");
			Files.copy(website.openStream(), out,
					StandardCopyOption.REPLACE_EXISTING);

			ZipFile extr = new ZipFile(new File(new URI(base
					+ "lwjgl/lwjgl.zip")));
			Enumeration<? extends ZipEntry> entries = extr.entries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();
				// DEBUG
				System.out.println(entry.getName());
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
					Files.createFile(out);
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
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
