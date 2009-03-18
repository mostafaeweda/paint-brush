package UI;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import Core.Tools.AbstractTool;
import Core.Tools.ClickTool;
import Core.Tools.DragTool;
import Core.Tools.NullTool;
import Core.Tools.SelectTool;
import Core.plugins.ShapeFactory;
import Core.plugins.ShapeIF;
import Core.plugins.ShapeFactory.ClassComposite;

/**
 * Tool Box Unit
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class ToolBoxUnit {
	private Shell shell;
	/**
	 * @uml.property  name="tool"
	 * @uml.associationEnd  
	 */
	private AbstractTool tool;
	private Canvas canvas;
	private static final String IMAGE_PATH = "ToolBar\\";
	private ArrayList<Image> images;
	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static ToolBoxUnit instance;
	private ToolBar toolBar;

	public synchronized static ToolBoxUnit getInstance(Shell shell,
			Canvas canvas) {
		if (instance == null)
			return (instance = new ToolBoxUnit(shell, canvas));
		else
			return instance;
	}

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public synchronized static ToolBoxUnit getInstance() {
		return instance;
	}

	private ToolBoxUnit(Shell shell, Canvas canvas) {
		images = new ArrayList<Image>();
		this.shell = shell;
		createContents();
		this.canvas = canvas;
	}

	private void createContents() {
		ArrayList<String> classDescribtions = null;
		try {
			classDescribtions = ShapeFactory.getInstance().importClasses();
		} catch (Exception e1) {
			System.err.println("mafesh Propertes File");
		}
		Iterator<String> iter = classDescribtions.iterator();
		toolBar = new ToolBar(shell, SWT.VERTICAL);
		shell.setData("toolBox2", toolBar);
		FormData data = new FormData();
		Canvas cavans = (Canvas) shell.getData("canvas");
		data.right = new FormAttachment(cavans, -3);
		data.bottom = new FormAttachment(100, -100);
		data.top = new FormAttachment((ToolBar) shell.getData("toolBar"), 40);
		data.left = new FormAttachment(0, 0);
		toolBar.setLayoutData(data);

		ToolItem fill = new ToolItem(toolBar, SWT.PUSH);
		fill.setToolTipText("Fill polygons");
		registerImage(fill, "fill.png");
		fill.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MenuBarUnit.getInstance().fill();
			}
		});
		ToolItem select = new ToolItem(toolBar, SWT.RADIO);
		select.setToolTipText("Select items");
		registerImage(select, "select.png");
		select.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Application.getInstance().setTool(SelectTool.getInstance());
			}
		});
		while (iter.hasNext()) {
			final String key = iter.next();
			importShape(key);
		}
	}

	public void importShape(final String key) {
		ToolItem currentItem = new ToolItem(toolBar, SWT.RADIO);
		ClassComposite cComp = ShapeFactory.getInstance().getClassElement(key);
		currentItem.setToolTipText(cComp.getToolTipText());
		registerImage(currentItem, cComp.getIconPath());
		currentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ShapeIF shape = ShapeFactory.getInstance().createShape(key);
				shape.setColor(Application.getInstance().getColor());
				String toolType = shape.getToolType();
				if (toolType.equals("click"))
					tool = ClickTool.getInstance(canvas);
				else if (toolType.equals("drag"))
					tool = DragTool.getInstance(canvas);
				else
					tool = NullTool.getInstance();
				tool.setElement(shape);
				Application.getInstance().setTool(tool);
			}
		});
	}

	private void registerImage(ToolItem button, String name) {
		ImageData imgData = new ImageData(ToolBarUnit.class
				.getResourceAsStream(IMAGE_PATH + name));
		imgData = imgData.scaledTo(32, 32);
		Image img = new Image(shell.getDisplay(), imgData);
		images.add(img);
		button.setImage(img);
	}

	public void dispose() {
		Iterator<Image> iter = images.iterator();
		while (iter.hasNext())
			iter.next().dispose();
		images.clear();
	}

}
