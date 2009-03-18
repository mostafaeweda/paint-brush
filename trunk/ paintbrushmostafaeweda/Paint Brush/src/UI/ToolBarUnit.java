package UI;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import Core.Tools.SelectTool;
import Core.Tools.ToolIF;

/**
 * Tool Bar unit
 * @author  Mostafa Eweda & Mohammed Abd El Salam
 * @version  1.0
 * @since  JDK 1.6
 */
public class ToolBarUnit {

	private static final String[] LINE_WIDTH = new String[] { "1", "2", "3",
			"4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
			"16", "17", "18", "19", "20" };
	private Display display;
	/**
	 * @uml.property  name="menuBar"
	 * @uml.associationEnd  
	 */
	private MenuBarUnit menuBar;
	private ArrayList<Image> images;
	private ToolBar toolBar;

	public ToolBarUnit(ToolBar toolBar, MenuBarUnit menuBarUnit) {
		display = toolBar.getDisplay();
		this.menuBar = menuBarUnit;
		images = new ArrayList<Image>();
		this.toolBar = toolBar;
		createContents(toolBar);
	}

	private void createContents(ToolBar toolBar) {
		ToolItem newPaper = createToolItem("New paper", "tool/new.png",
				"tool/new.png", SWT.PUSH);
		newPaper.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.newPaper();
			}
		});
		ToolItem save = createToolItem("save", "tool/save.png",
				"tool/save.png", SWT.PUSH);
		save.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.save();
			}
		});
		ToolItem print = createToolItem("print", "tool/print.png",
				"tool/print.png", SWT.PUSH);
		print.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.print();
			}
		});
		ToolItem undo = createToolItem("Undo", "tool/undo.png",
				"tool/hotUndo.png", SWT.PUSH);
		undo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.undo();
			}
		});
		ToolItem redo = createToolItem("Redo", "tool/redo.png",
				"tool/hotRedo.png", SWT.PUSH);
		redo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.redo();
			}
		});
		ToolItem zoomIn = createToolItem("Zoom in", "tool/zoomIn.png",
				"tool/zoomIn.png", SWT.PUSH);
		zoomIn.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.zoomIn();
			}
		});
		ToolItem zoomOut = createToolItem("Zoom out", "tool/zoomOut.png",
				"tool/zoomOut.png", SWT.PUSH);
		zoomOut.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.zoomOut();
			}
		});
		ToolItem flipHorizontal = createToolItem("Flip Horizontal",
				"tool/flipHorizontal.png", "tool/flipHorizontal.png", SWT.PUSH);
		flipHorizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.flipHorizontal();
			}
		});
		ToolItem flipVertical = createToolItem("Flip Vertical",
				"tool/flipVertical.png", "tool/flipVertical.png", SWT.PUSH);
		flipVertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.flipVertical();
			}
		});
		ToolItem snapshot = createToolItem("Takesnapshot", "tool/snapshot.png",
				"tool/snapshot.png", SWT.PUSH);
		snapshot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.snapshot();
			}
		});
		ToolItem loadSnapshot = createToolItem("Load snapshot",
				"tool/snapshot2.png", "tool/snapshot2.png", SWT.PUSH);
		loadSnapshot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				menuBar.restoreSnapshot();
			}
		});
		createCoolBar();
	}

	private void createCoolBar() {
		FormData data = new FormData();
		data.left = new FormAttachment(toolBar, 5);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment((Canvas) toolBar.getShell().getData(
				"canvas"), 0);
		Group group = new Group(toolBar.getShell(), SWT.NONE);
		group.setText("Line Width");
		group.setLayout(new RowLayout(SWT.HORIZONTAL));
		final Combo combo = new Combo(group, SWT.DROP_DOWN);
		combo.setItems(LINE_WIDTH);
		combo.select(0);
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection;
				try {
					selection = Integer.parseInt(combo.getText());
					if (selection > 20) {
						combo.setText("20");
						selection = 20;
					} else if (selection < 1) {
						combo.setText("1");
						selection = 1;
					}
				} catch (NumberFormatException e1) {
					combo.setText("1");
					selection = 1;
				}
				Application.getInstance().setWidth(selection);
				ToolIF tool = Application.getInstance().getTool();
				if (tool instanceof SelectTool)
					((SelectTool) tool).changeWidth(selection);

			}
		});
		group.setLayoutData(data);
		toolBar.getShell().setData("widthCombo", combo);
	}

	private ToolItem createToolItem(String toolTip, String img, String hotImg,
			int type) {
		ToolItem item = new ToolItem(toolBar, type);
		item.setToolTipText(toolTip);
		Image image = new Image(display, ToolBarUnit.class
				.getResourceAsStream(img));
		images.add(image);
		item.setImage(image);
		if (!img.equals(hotImg)) {
			Image hotImage = new Image(display, ToolBarUnit.class
					.getResourceAsStream(hotImg));
			item.setHotImage(hotImage);
			images.add(hotImage);
		}
		return item;
	}

	public void dispose() {
		Iterator<Image> iter = images.iterator();
		while (iter.hasNext())
			iter.next().dispose();
		images.clear();
	}
}
