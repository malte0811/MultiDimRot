package malte0811.multiDim.addons;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class AddonLoader {

	public static void load() throws Exception {
		String base;
		String sep = System.getProperty("file.separator");
		base = System.getProperty("user.dir") + sep + "addons" + sep;
		// URL toLoad = new File(base).toURI().toURL();
		File folder = new File(base);
		HashSet<String> jars = listFilesForFolder(folder);
		for (String s : jars) {
			if (s.substring(s.length() - 4, s.length()).equals(".jar")) {
				System.out.println("Found jar: " + s);
				File file = new File(base + s);
				URLClassLoader loader = null;
				Class<? extends Addon> c = getJarContent(file, loader);
				Method load = c.getDeclaredMethod("load");
				Addon a = c.newInstance();
				load.invoke(a);
			}
		}
		if (System.getProperty("AddonDev") != null) {
			loadEclipseFolder();
		}
	}

	private static HashSet<String> listFilesForFolder(File folder) {
		HashSet<String> ret = new HashSet<>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				ret.add(fileEntry.getName());
			}
		}
		return ret;
	}

	private static Class<? extends Addon> getJarContent(File zip,
			URLClassLoader loader) {
		try {
			ZipFile file = new ZipFile(zip);
			Enumeration<? extends ZipEntry> e = file.entries();
			while (e.hasMoreElements()) {
				System.out
						.println("Entry in jar: " + e.nextElement().getName());
			}
			InputStream s = file.getInputStream(file.getEntry("addon.txt"));
			String cont = "";
			int tmp = 0;
			while ((tmp = s.read()) != -1) {
				cont += (char) tmp;
			}
			loader = new URLClassLoader(new URL[] { zip.toURI().toURL() });
			Class<?> c = loader.loadClass(cont);
			file.close();
			return c.asSubclass(Addon.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void loadEclipseFolder() throws Exception {
		String base = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "bin/";
		InputStream s = new FileInputStream(base + "addon.txt");
		String cont = "";
		int tmp = 0;
		while ((tmp = s.read()) != -1) {
			cont += (char) tmp;
		}
		URLClassLoader loader = new URLClassLoader(new URL[] { new File(base)
				.toURI().toURL() });
		Class<?> c = loader.loadClass(cont);
		s.close();
		Class<? extends Addon> real = c.asSubclass(Addon.class);
		Method load = real.getDeclaredMethod("load");
		Addon a = real.newInstance();
		load.invoke(a);
	}

}
