package UI;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import Core.Tools.ToolIF;
import Core.plugins.ShapeIF;

/**
 * Provides default behavior for the tool interface
 * @see ToolIF implemented interface
 * @see ShapeIF
 * @author Mostafa Eweda & Mohammed Abd El Salam
 * @version 1.0
 * @since JDK 1.6
 */
public class Statistic {

	private ArrayList<ShapeIF> list;
	private Shell shell;

	public Statistic(Shell parent) {
		list = Application.getInstance().getShapes();
		shell = new Shell(parent);
		try {
			open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Names for each of the columns
	private static final String[] COLUMN_NAMES = { "Type", "numberOfPoints",
			"startedLocation", "Width", "Hight","RGB" };

	public void open() throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		shell.setText("Statistics");
		createContents(shell);
		shell.pack();
		shell.open();
	}

	/**
	 * Creates the columns for the table
	 * 
	 * @param table
	 *            the table
	 * @return TableColumn[]
	 */
	private TableColumn[] createColumns(Table table) {
		TableColumn[] columns = new TableColumn[COLUMN_NAMES.length];
		for (int i = 0, n = columns.length; i < n; i++) {
			// Create the TableColumn with right alignment
			columns[i] = new TableColumn(table, SWT.CENTER);

			// This text will appear in the column header
			columns[i].setText(COLUMN_NAMES[i]);
		}
		return columns;
	}

	/**
	 * Creates the window's contents (the table)
	 * 
	 * @param composite
	 *            the parent composite
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	@SuppressWarnings("unchecked")
	private void createContents(Composite composite)
			throws IllegalArgumentException, SecurityException,
			IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		composite.setLayout(new FillLayout());
		Table table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setRedraw(false);
		// Create the columns
		TableColumn[] columns = createColumns(table);
		int lenght = list.size();
		for (int i = 0; i < lenght; i++) {
			int c = 0;
			TableItem item = new TableItem(table, SWT.NONE);
			ShapeIF shape = list.get(i);
			Class<ShapeIF> classShape = (Class<ShapeIF>) shape
					.getClass();
			String shapeName = (String) classShape.getMethod("getShapeName",
					(Class<?>[]) null).invoke(null);
			item.setText(c++, shapeName);
			item.setText(c++, "" + shape.getAddedPoints());
			item.setText(c++, "(" + shape.getBounds().x + ","
					+ shape.getBounds().y + ")");
			item.setText(c++, "" + shape.getBounds().width);
			item.setText(c++, "" + shape.getBounds().height);
			RGB rgb = shape.getColor().getRGB();
			item.setText(c++, "(" + rgb.red+","+rgb.green+","+rgb.blue+")");
		}


		// Now that we've set the text into the columns,
		// we call pack() on each one to size it to the
		// contents
		for (int i = 0, n = columns.length; i < n; i++) {
			columns[i].pack();
		}

		// Set redraw back to true so that the table
		// will paint appropriately
		table.setRedraw(true);
	}

	

}
