package XML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import Core.plugins.ImageShape;
import Core.plugins.ShapeFactory;
import Core.plugins.ShapeIF;
import UI.Application;

/**
 * Manages XML read/write process {@link #load(String path)} {@link #save(String path)}
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class XML_Management {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static XML_Management instance;

	private XML_Management() {
		
	}

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public static synchronized XML_Management getInstance() {
		if (instance == null)
			return instance = new XML_Management();
		return instance;
	}

	public void save(String path) {
		try {
			Class<?>[] empty = new Class<?>[0];
			StringWriter stringWriter = new StringWriter();
			XmlWriter xmlWriter = new XmlWriter(stringWriter);
			List<ShapeIF> shapes = Application.getInstance().getShapes();
			Iterator<ShapeIF> iter = shapes.iterator();
			ShapeIF current;
			xmlWriter.writeEntity("LoadedFile");
			while (iter.hasNext()) {
				current = iter.next();
				if (current instanceof ImageShape) {
					String imagePath = current.getToolType();
					xmlWriter.writeEntity("Shape");
					xmlWriter.writeAttribute("imagePath", imagePath);
					xmlWriter.endEntity();
					continue;
				}
				xmlWriter.writeEntity("Shape");
				String name = (String) current.getClass().getMethod("getShapeName", empty)
																.invoke(null, (Object[])null);
				xmlWriter.writeEntity("Definition");
				xmlWriter.writeAttribute("name", name);
				xmlWriter.endEntity();
				ArrayList<Point> points = current.getPoints();
				Iterator<Point> pointIter = points.iterator();
				Point currentPoint;
				xmlWriter.writeEntity("Points");
				while (pointIter.hasNext()) {
					currentPoint = pointIter.next();
					xmlWriter.writeEntity("Point");
					xmlWriter.writeAttribute("x", currentPoint.x+"");
					xmlWriter.writeAttribute("y", currentPoint.y+"");
					xmlWriter.endEntity();
				}
				xmlWriter.endEntity();
				Color color = current.getColor();
				xmlWriter.writeEntity("Color");
				xmlWriter.writeAttribute("red", color.getRed()+"");
				xmlWriter.writeAttribute("green", color.getGreen()+"");
				xmlWriter.writeAttribute("blue", color.getBlue()+"");
				xmlWriter.endEntity();
				xmlWriter.writeEntity("Filled");
				xmlWriter.writeAttribute("state", current.getFilled()+"");
				xmlWriter.endEntity();
				xmlWriter.endEntity();
			}
			xmlWriter.endEntity();
			PrintWriter printWriter = new PrintWriter(new FileWriter(path));
			printWriter.write(stringWriter.toString());
			printWriter.close();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (WritingException e) {
			e.printStackTrace();
		} catch (XmlWritingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ShapeIF> load(String path) { // "E:\\COLLEGE\\Workspace\\Paint temp\\sossa.xml"
		try {
			SAXParser parser = SAXParserFactory.newInstance()
					.newSAXParser();
			ShapeParser shapeParser = new ShapeParser();
			parser.parse(new File(path), shapeParser);
			System.out.println(shapeParser.getData());
			return shapeParser.getData();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	

/**
 * @author  Mostafa Eweda Lap
 */
private	class ShapeParser extends DefaultHandler {

		/**
		 * @uml.property  name="data"
		 */
		private ArrayList<ShapeIF> data = new ArrayList<ShapeIF>();
		private ArrayList<Point> points;
		private boolean first = true;
		private String name;
		private Color color;
		private int width;
		private boolean filled;
		private boolean insidePoints = false;
		private boolean inImage;

		public void startElement(String namespaceURI, String localName,
				String qName, Attributes attr) throws SAXException {
			if (insidePoints) {
				points.add(new Point(Integer.parseInt(attr.getValue(0)),
						Integer.parseInt(attr.getValue(1))));
			} else if (qName.equals("Points")) {
				insidePoints = true;
				points = new ArrayList<Point>();
			} else if (qName.equals("Definition")) {
				name = attr.getValue(0);
			} else if (qName.equals("Color")) {
				RGB rgb = new RGB(Integer.parseInt(attr.getValue(0)), Integer
						.parseInt(attr.getValue(1)), Integer.parseInt(attr
						.getValue(2)));
				color = new Color(Display.getCurrent(), rgb);
			} else if (qName.equals("Width")) {
				width = Integer.parseInt(attr.getValue(0));
			} else if (qName.equals("Filled")) {
				filled = Boolean.parseBoolean(attr.getValue(0));
			} else if (qName.equals("Shape")) {
				if (attr.getLength() == 0)
					return;
				else {
					inImage = true;
					data.add(new ImageShape(new Image(Display.getCurrent(), attr.getValue(0)), attr.getValue(0)));
				}
			}
			else if (first) {
				first = false;
				return;
			}
		}

		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if (qName.equals("Points"))
				insidePoints = false;
			else if (qName.equals("Shape") && ! inImage) {
				ShapeIF shape = ShapeFactory.getInstance()
						.createShape(name);
				shape.getPoints().addAll(points);
				shape.setColor(color);
				shape.setWidth(width);
				shape.setFilled(filled);
				data.add(shape);
			}
			inImage = false;
		}

		/**
		 * @return
		 * @uml.property  name="data"
		 */
		public ArrayList<ShapeIF> getData() {
			return data;
		}
	}
}