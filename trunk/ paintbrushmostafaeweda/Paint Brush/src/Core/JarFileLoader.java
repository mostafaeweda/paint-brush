package Core;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import Core.plugins.ShapeIF;

/**
 * loads the plug-ins into the program as jar files at run time
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class JarFileLoader extends URLClassLoader {
	private JarFileLoader(URL[] urls) {
		super(urls);
	}

	public void addFile(String path) throws MalformedURLException {
		File file = new File(path);
		addURL(file.toURI().toURL());
	}

	@SuppressWarnings("unchecked")
	public static Class<ShapeIF> importFile(String path) {

		try {
			URL urls[] = {};
			JarFileLoader cl = new JarFileLoader(urls);
			cl.addFile(path);
		
			Class<ShapeIF> loadedClass = (Class<ShapeIF>) cl
					.loadClass("Core.plugins."
							+ path.subSequence(path.lastIndexOf("\\")+1, path
									.lastIndexOf(".")));
			return loadedClass;
		} catch (Exception ex) {

			ex.printStackTrace();
		}
		return null;
	}
}