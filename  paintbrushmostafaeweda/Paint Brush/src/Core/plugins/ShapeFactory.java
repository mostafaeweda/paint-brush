package Core.plugins;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Takes the responsibility of creating shapes given its name as strings
 * @see AbstractShape  extending from
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class ShapeFactory {

	private static final String PROPERTY_FILE_NAME = "defultShapes.properties";
	private static Hashtable<String, ClassComposite> classes;

	/**
	 * @uml.property  name="factory"
	 * @uml.associationEnd  
	 */
	private static ShapeFactory factory;

	public static synchronized ShapeFactory getInstance() {
		if (factory == null)
			factory = new ShapeFactory();
		return factory;
	}

	private ShapeFactory() {
		classes = new Hashtable<String, ClassComposite>(100);
	}

	public ShapeIF createShape(String describtion) {
		return manipulateProperties(describtion);
	}

	public ClassComposite getClassElement(String describtion) {
		return classes.get(describtion);
	}

	private static ShapeIF manipulateProperties(String describtion) {
		ClassComposite c = classes.get(describtion);
		Constructor<ShapeIF> constructor = c.getConstructor();
		if (constructor != null) {
			try {
				return constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> importClasses() { // File alay atafa2na 3aleeh

		ArrayList<String> describtions = new ArrayList<String>();
		BufferedReader inputStream = null;

		try {
			inputStream = new BufferedReader(new InputStreamReader(
					ShapeFactory.class.getResourceAsStream(PROPERTY_FILE_NAME)));
			String className;
			while ((className = inputStream.readLine()) != null) {
				Class<ShapeIF> now = (Class<ShapeIF>) Class
						.forName("Core.plugins." + className);
				describtions.add(importClass(now));
			}
			inputStream.close();
		} catch (IOException e) {

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return describtions;
	}

	@SuppressWarnings("unchecked")
	public String importClass(Class now) {
		String shapeName = null;
		try {
			Class<?>[] empty = new Class[0];
			Method describtionMethod = now.getMethod("getShapeName", empty);
			shapeName = (String) describtionMethod
					.invoke(null, (Object[]) null);
			Method iconMethod = now.getMethod("getIconPath", empty);
			String iconPath = (String) iconMethod.invoke(null, (Object[]) null);
			Method toolTipMethod = now.getMethod("getToolTip", empty);
			String toolTip = (String) toolTipMethod.invoke(null,
					(Object[]) null);
			Constructor constructor = now.getConstructors()[0];
			classes.put(shapeName, new ClassComposite(constructor, toolTip,
					iconPath));
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return shapeName;
	}

	/**
	 * @author  Mostafa Eweda Lap
	 */
	public static class ClassComposite {
		/**
		 * @uml.property  name="constructor"
		 */
		private Constructor<ShapeIF> constructor;
		/**
		 * @uml.property  name="iconPath"
		 */
		private String iconPath;
		/**
		 * @uml.property  name="toolTipText"
		 */
		private String toolTipText;

		public ClassComposite(Constructor<ShapeIF> constructor,
				String toolTipText, String iconPath) {
			this.constructor = constructor;
			this.toolTipText = toolTipText;
			this.iconPath = iconPath;
		}

		/**
		 * @return
		 * @uml.property  name="constructor"
		 */
		public Constructor<ShapeIF> getConstructor() {
			return constructor;
		}

		/**
		 * @return
		 * @uml.property  name="iconPath"
		 */
		public String getIconPath() {
			return iconPath;
		}

		/**
		 * @return
		 * @uml.property  name="toolTipText"
		 */
		public String getToolTipText() {
			return toolTipText;
		}
	}
}