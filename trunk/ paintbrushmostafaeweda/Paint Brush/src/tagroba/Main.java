package tagroba;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import Core.plugins.TriangleShape;

public class Main {	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		Display display = new Display();

		TriangleShape shape = new TriangleShape();
		shape.setColor(new Color(display, new RGB(0, 0, 0)));
		FileOutputStream stream = new FileOutputStream("kkk.txt");
		ObjectOutput  out = new ObjectOutputStream(stream);
		out.writeObject(shape);
		out.close();
		ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("kkk.txt"));
		try {
			TriangleShape shape2 = (TriangleShape) inputStream.readObject();
			System.out.println(shape2.getColor().getRGB().red);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

}
