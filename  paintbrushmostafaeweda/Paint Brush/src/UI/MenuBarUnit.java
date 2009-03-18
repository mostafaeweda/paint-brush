package UI;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.printing.PrintDialog;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.printing.PrinterData;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import Commands.CommandManager;
import Core.JarFileLoader;
import Core.MemoryController;
import Core.Tools.Constants;
import Core.Tools.SelectTool;
import Core.Tools.ToolIF;
import Core.plugins.ImageShape;
import Core.plugins.ShapeFactory;
import Core.plugins.ShapeIF;
import XML.XML_Management;

/**
 * @author  Mostafa Eweda Lap
 */
public class MenuBarUnit {

	private Shell shell;
	private Display display;
	private static final String IMAGE_PATH = "MenuBar\\";
	private ArrayList<Image> images;
	private boolean previouslySaved;
	private int previousType;
	private String previousPath;
	/**
	 * @uml.property  name="menuBarUnit"
	 * @uml.associationEnd  
	 */
	private static MenuBarUnit menuBarUnit;
	private static final String[] EXTENSIONS = new String[] { "*.XML", "*.txt",
			"*.JPEG", "*.JPG", "*.GIF", "*.BMP", "*.ICO" };
	private static final String[] FILTERS = new String[] {
			"Extneded Markup Langiage  (*.XML)",
			"Eweda & 3abd El Salam  (*.txt)",
			"Joint Photographic Experts Group  (*.JPEG)",
			"Joint Photographic Group  (*.JPG)", "Graphics Interchange Format",
			"GIF image depth = 8  (*.GIF)",
			"Bitmap image scelled to each pixel  (*.BMP)", "Icon image (*.ICO)" };
	public static final String[] FILTERS_IMPORT = new String[] { "Runnable Jar File (*JAR)" };
	private static final String[] EXTENTIONS_IMPORT = new String[] { "*.JAR" };

	public static MenuBarUnit getInstance() {
		return menuBarUnit;
	}

	public MenuBarUnit(Shell shell) {
		menuBarUnit = this;
		display = Display.getCurrent();
		images = new ArrayList<Image>();
		previouslySaved = false;
		this.shell = shell;
		createMenuBar();
	}

	private void createMenuBar() {
		Menu menu = new Menu(shell, SWT.BAR);
		createFileMenu(menu);
		createEditMenu(menu);
		createToolsMenu(menu);
		createViewMenu(menu);
		createHelpMenu(menu);
		shell.setMenuBar(menu);
	}

	private void createToolsMenu(Menu menu) {
		MenuItem toolsItem = new MenuItem(menu, SWT.CASCADE);
		toolsItem.setText("Tools");
		Menu toolsMenu = new Menu(toolsItem);
		MenuItem flipHorizontal = new MenuItem(toolsMenu, SWT.PUSH);
		flipHorizontal.setText("Flip horizontal");
		registerImage(flipHorizontal, "flipHorizontal");
		flipHorizontal.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				flipHorizontal();
			}
		});
		MenuItem flipVertical = new MenuItem(toolsMenu, SWT.PUSH);
		flipVertical.setText("Flip vertical");
		registerImage(flipVertical, "flipVertical");
		flipVertical.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				flipVertical();
			}
		});
		MenuItem delete = new MenuItem(toolsMenu, SWT.PUSH);
		delete.setText("Delete CRL+D");
		registerImage(delete, "delete");
		delete.setAccelerator(SWT.MOD1 + 'D');
		delete.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				deleteSelected();
			}
		});
		MenuItem snapshot = new MenuItem(toolsMenu, SWT.PUSH);
		snapshot.setText("Take Snapshot");
		registerImage(snapshot, "snapshot");
		snapshot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				snapshot();
			}
		});
		MenuItem loadSnapshot = new MenuItem(toolsMenu, SWT.PUSH);
		loadSnapshot.setText("Load Snapshot");
		registerImage(loadSnapshot, "snapshot2");
		loadSnapshot.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				restoreSnapshot();
			}
		});
		MenuItem imageMode = new MenuItem(toolsMenu, SWT.PUSH);
		imageMode.setText("Image Mode");
		registerImage(imageMode, "imageMode");
		imageMode.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				new Thread(new ImageMode()).start();
			}
		});
		toolsItem.setMenu(toolsMenu);

	}

	protected void deleteSelected() {
		if (!SelectTool.getInstance().deleteSelected())
			errorDialog("Flip Error", "NO items selected");
	}

	protected void flipVertical() {
		if (!SelectTool.getInstance().flip(Constants.FLIP_VERTICAL))
			errorDialog("Flip Error", "NO items selected");
	}

	protected void flipHorizontal() {
		if (!SelectTool.getInstance().flip(Constants.FLIP_HORIZONTAL))
			errorDialog("Flip Error", "NO items selected");
	}

	protected void errorDialog(String text, String message) {
		MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
		box.setText(text);
		box.setMessage(message);
		box.open();
	}

	private void createFileMenu(Menu menu) {
		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		fileItem.setText("File");
		Menu fileMenu = new Menu(fileItem);
		MenuItem newPaper = new MenuItem(fileMenu, SWT.PUSH);
		newPaper.setText("New    CRL+N");
		registerImage(newPaper, "new");
		newPaper.setAccelerator(SWT.MOD1 | 'N');
		newPaper.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				newPaper();
			}
		});

		MenuItem open = new MenuItem(fileMenu, SWT.PUSH);
		open.setText("Open    CRL+O");
		registerImage(open, "new");
		open.setAccelerator(SWT.MOD1 | 'O');
		open.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				openFile();
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		final MenuItem saveAs = new MenuItem(fileMenu, SWT.PUSH);
		saveAs.setText("Save As...");
		registerImage(saveAs, "saveAs");
		saveAs.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				saveAs();
			}
		});

		MenuItem save = new MenuItem(fileMenu, SWT.PUSH);
		save.setText("Save    CRL+S");
		registerImage(save, "save");
		save.setAccelerator(SWT.CONTROL | 'S');
		save.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				save();
			}
		});

		new MenuItem(fileMenu, SWT.SEPARATOR);

		MenuItem importItem = new MenuItem(fileMenu, SWT.PUSH);
		importItem.setText("Import..."); // kanet na2sa al asm we al icon
		registerImage(importItem, "import");

		importItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				importClasses();
			}
		});

		MenuItem print = new MenuItem(fileMenu, SWT.PUSH);
		print.setText("Print    CRL+P");
		registerImage(print, "printer");
		print.setAccelerator(SWT.MOD1 | 'P');
		print.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				print();
			}
		});

		MenuItem exit = new MenuItem(fileMenu, SWT.PUSH);
		exit.setText("Exit    ALT+X");
		registerImage(exit, "Exit");
		exit.setAccelerator(SWT.ALT | 'X');
		exit.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				exit();
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				exit();
			}
		});
		fileItem.setMenu(fileMenu);
	}

	protected void exit() {
		int decision;
		if ((decision = preCloseDialog()) == SWT.NO)
			System.exit(0);
		else if (decision == SWT.YES) {
			save();
			System.exit(0);
		}
	}

	protected void print() {
		try {
			ImageData imageData = Application.getInstance().getImage()
					.getImageData();
			// Ask the user to specify the printer.
			PrintDialog dialog = new PrintDialog(shell, SWT.NULL);
			PrinterData printerData = dialog.open();
			if (printerData == null)
				return;

			Printer printer = new Printer(printerData);
			Point screenDPI = display.getDPI();
			Point printerDPI = printer.getDPI();
			int scaleFactor = printerDPI.x / screenDPI.x;
			Rectangle trim = printer.computeTrim(0, 0, 0, 0);
			if (printer.startJob("Sossa")) {
				if (printer.startPage()) {
					GC gc = new GC(printer);
					Image printerImage = new Image(printer, imageData);
					gc.drawImage(printerImage, 0, 0, imageData.width,
							imageData.height, -trim.x, -trim.y, scaleFactor
									* imageData.width, scaleFactor
									* imageData.height);
					printerImage.dispose();
					gc.dispose();
					printer.endPage();
				}
				printer.endJob();
			}
			printer.dispose();
		} catch (SWTError e) {
			MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
			box.setMessage("Printing_error" + e.getMessage());
			box.open();
		}
	}

	protected void importClasses() {
		FileDialog dialog = new FileDialog(shell, SWT.OPEN | SWT.MULTI);
		dialog.setFilterNames(FILTERS_IMPORT);
		dialog.setFilterExtensions(EXTENTIONS_IMPORT);

		if (previousPath != null)
			dialog.setFilterPath(previousPath);
		String path;
		if ((path = dialog.open()) != null) {
			Class<ShapeIF> shapeClass = JarFileLoader.importFile(path);
			String key = ShapeFactory.getInstance().importClass(shapeClass);
			ToolBoxUnit.getInstance().importShape(key);
			previousPath = path;
		}
	}

	protected void openFile() {
		FileDialog dialog = prepareDialog(SWT.OPEN);
		String path = dialog.open();
		if (path != null) {
			int type = dialog.getFilterIndex();
			previouslySaved = true;
			previousPath = path;
			previousType = type;
			loadFile(path, type);
			new Statistic(shell);
			Application.getInstance().refresh();
		}
	}

	protected void newPaper() {
		MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO | SWT.CANCEL
				| SWT.ICON_INFORMATION);
		box.setText("Confitm new paper");
		box
				.setMessage("You didn't save changes\nDo you want to save changes ?");
		int selection = box.open();
		if (selection == SWT.NO) {
			newPaperDone();
		} else if (selection == SWT.YES) {
			save();
			newPaperDone();
		}
		// else --> do nothing
	}

	private void newPaperDone() {
		Application app = Application.getInstance();
		app.getShapes().clear();
		CommandManager.getInstance().clearHistory();
		Application.getInstance().setZoom(100);
		app.setColor(new Color(display, 0, 0, 0));
		app.setWidth(1);
		((Combo) shell.getData("widthCombo")).select(0);
	}

	private int preCloseDialog() {
		MessageBox box = new MessageBox(shell, SWT.YES | SWT.NO | SWT.CANCEL
				| SWT.ICON_WARNING);
		box.setText("Confitm closing");
		box
				.setMessage("You didn't save changes\nDo you want to save changes ?");
		return box.open();
	}

	protected void save() {
		if (!previouslySaved)
			saveAs();
		else
			saveToFile(previousPath, previousType);
	}

	protected void saveAs() {
		FileDialog dialog = prepareDialog(SWT.SAVE);
		dialog.setOverwrite(true);
		String path = dialog.open();
		if (path != null) {
			int type = dialog.getFilterIndex();
			saveToFile(path, type);
			previouslySaved = true;
			previousPath = path;
			previousType = type;
		}
	}

	private void saveToFile(String path, int type) {
		switch (type) {
		case 0:
			saveXML(path);
			break;
		case 1:
			saveSerialized(path);
		default:
			saveImage(path, getType(type));
		}
	}

	private int getType(int type) {
		switch (type) {
		case 2:
			return SWT.IMAGE_JPEG;
		case 3:
			return SWT.IMAGE_GIF;
		case 4:
			return SWT.IMAGE_BMP;
		case 5:
			return SWT.IMAGE_PNG;
		case 6:
			return SWT.IMAGE_ICO;
		default:
			return 0;
		}
	}

	private void saveXML(String path) {
		XML_Management.getInstance().save(path);
	}

	private void saveImage(String path, int imageExt) {
		ImageLoader loader = new ImageLoader();
		Image image = Application.getInstance().getImage();
		loader.data = new ImageData[] { image.getImageData() };
		loader.save(path, imageExt);
	}

	private void saveSerialized(String path) {
		try {
			File file = new File(path);
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			// out.writeObject(shell.getData("shapes"));
			out.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private FileDialog prepareDialog(int operation) {
		String text = null;
		switch (operation) {
		case SWT.SAVE:
			text = "Save Dialog";
			break;
		case SWT.OPEN:
			text = "Open Dialog";
			break;
		default:
			break;
		}
		FileDialog dialog = new FileDialog(shell, operation);
		dialog.setFilterNames(FILTERS);
		dialog.setFilterExtensions(EXTENSIONS);
		dialog.setText(text);
		return dialog;
	}

	private void createEditMenu(Menu menu) {
		MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
		editItem.setText("Edit");
		Menu editMenu = new Menu(editItem);
		editItem.setMenu(editMenu);

		MenuItem undo = new MenuItem(editMenu, SWT.PUSH);
		undo.setText("Undo    CRL+Z");
		undo.setAccelerator(SWT.CONTROL | 'Z');
		registerImage(undo, "undo");
		undo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				undo();
			}
		});

		MenuItem redo = new MenuItem(editMenu, SWT.PUSH);
		redo.setText("Redo    CRL+Y");
		redo.setAccelerator(SWT.MOD1 | 'Y');
		registerImage(redo, "redo");
		redo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				redo();
			}
		});

		new MenuItem(editMenu, SWT.SEPARATOR);

		MenuItem selectAll = new MenuItem(editMenu, SWT.PUSH);
		selectAll.setText("Select All    CRL+A");
		selectAll.setAccelerator(SWT.MOD1 | 'A');
		selectAll.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectAll();
			}
		});
	}

	protected void selectAll() {
		SelectTool.getInstance().selectAll();
		Application.getInstance().setTool(SelectTool.getInstance());
	}

	protected void redo() {
		CommandManager.getInstance().redo();
	}

	protected void undo() {
		CommandManager.getInstance().undo();
	}

	private void createViewMenu(Menu menu) { // zawedt al Menu Item
		MenuItem viewItem = new MenuItem(menu, SWT.CASCADE);
		viewItem.setText("View");
		Menu viewMenu = new Menu(viewItem);
		viewItem.setMenu(viewMenu);

		MenuItem toolBox = new MenuItem(viewMenu, SWT.CHECK);
		toolBox.setText("ColorBox      CRL+T");
		toolBox.setAccelerator(SWT.MOD1 | 'T');
		toolBox.setSelection(true);
		toolBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				toolBoxAppear();
			}
		});

		MenuItem statuesBar = new MenuItem(viewMenu, SWT.CHECK);
		statuesBar.setText("Status bar");
		statuesBar.setSelection(true);
		statuesBar.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				statusShow();
			}
		});

		new MenuItem(viewMenu, SWT.SEPARATOR);

		MenuItem colorChooser = new MenuItem(viewMenu, SWT.PUSH);
		colorChooser.setText("Colo chooser");
		colorChooser.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ColorDialog dialog = new ColorDialog(shell);
				dialog.setRGB(Application.getInstance().getColor().getRGB());
				RGB rgb = dialog.open();
				Application.getInstance().setColor(new Color(display, rgb));
			}
		});

		MenuItem statistic = new MenuItem(viewMenu, SWT.PUSH);
		statistic.setText("Statistic");
		statistic.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				new Statistic(shell);
			}
		});

		// new MenuItem(viewMenu, SWT.SEPARATOR);

		Menu zoomMenu = new Menu(menu);
		MenuItem zoomItem = new MenuItem(viewMenu, SWT.CASCADE);
		zoomItem.setText("Zoom");
		zoomItem.setMenu(zoomMenu);

		MenuItem zoomIn = new MenuItem(zoomMenu, SWT.NONE);
		zoomIn.setText("ZOOM IN      CRL+pgUp");
		zoomIn.setAccelerator(SWT.CTRL | SWT.PAGE_UP);
		registerImage(zoomIn, "zoomIn");
		zoomIn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				zoomIn();
			}
		});

		MenuItem zoomOut = new MenuItem(zoomMenu, SWT.NONE);
		zoomOut.setText("ZOOM OUT   CRL+pgDn");
		zoomOut.setAccelerator(SWT.PAGE_DOWN | SWT.CTRL);
		registerImage(zoomOut, "zoomOut");
		zoomOut.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				zoomOut();
			}
		});
		shell.setData("zoomOutItem", zoomOut);
	}

	protected void statusShow() {
		CLabel label = (CLabel) shell.getData("status");
		label.setVisible(!label.getVisible());
	}

	protected void zoomOut() {
		Application.getInstance().zoomOut();
	}

	protected void zoomIn() {
		Application.getInstance().zoomIn();
	}

	protected void toolBoxAppear() {
		Canvas canvas = (Canvas) shell.getData("palette");
		canvas.setVisible(!canvas.getVisible());
		Label colorLabel = (Label) shell.getData("colorLabel");
		colorLabel.setVisible(!colorLabel.getVisible());

	}

	private void createHelpMenu(Menu menu) {
		MenuItem helpItem = new MenuItem(menu, SWT.CASCADE);
		helpItem.setText("Help");
		Menu helpMenu = new Menu(helpItem);
		helpItem.setMenu(helpMenu);

		final MenuItem about = new MenuItem(helpMenu, SWT.PUSH);
		about.setText("About");
		registerImage(about, "about");
		about.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Program.findProgram(".txt").execute("README.txt");
			}
		});
		shell.addHelpListener(new HelpListener() {
			public void helpRequested(HelpEvent e) {
				about.setSelection(true);
			}
		});

		MenuItem creators = new MenuItem(helpMenu, SWT.PUSH);
		creators.setText("Paint Creators");
		creators.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});
	}

	private void loadFile(String path, int type) {
		switch (type) {
		case 0:
			loadXML(path);
			break;
		case 1:
			loadSerialized(path);
			break;
		default:
			loadImage(path);
		}
	}

	@SuppressWarnings("unchecked")
	private void loadImage(String path) {
		ArrayList<ShapeIF> shapes = (ArrayList<ShapeIF>) shell
				.getData("shapes");
		shapes.clear();
		Image image = new Image(display, path);
		shapes.add(new ImageShape(image, path));
		Canvas canvas = (Canvas) shell.getData("canvas");
		ImageData data = image.getImageData();
		canvas.setSize(data.width, data.height);
		canvas.getParent().layout();
	}

	@SuppressWarnings("unchecked")
	private void loadSerialized(String path) {
		ArrayList<ShapeIF> shapes = (ArrayList<ShapeIF>) shell
				.getData("shapes");
		shapes.clear();
		try {
			FileInputStream fileIn = new FileInputStream(path);

			ObjectInputStream out = new ObjectInputStream(fileIn);
			ArrayList<ShapeIF> loaded = (ArrayList<ShapeIF>) out
					.readObject();
			out.close();
			fileIn.close();
			Iterator<ShapeIF> iter = loaded.iterator();
			while (iter.hasNext())
				shapes.add(iter.next());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private void loadXML(String path) {
		String selection;
		ArrayList<ShapeIF> loaded = XML_Management.getInstance().load(path);
		ArrayList<ShapeIF> shapes = Application.getInstance().getShapes();
		if ((selection = preLoadDialog()).equals("reset")) {
			shapes.clear();
			shapes.addAll(loaded);
		} else if (selection.equals("append")) {
			shapes.addAll(loaded);
		}
	}

	/**
	 * @return a string that is "reser" to erase all and start over ,"append" to
	 *         append the current drawings and"cancel" to cancel the opening
	 */
	private String preLoadDialog() {
		// TODO 3ashan yet2akked enno 3ayz yemsa7 we yeload 7aga gedida
		return "reset";
	}

	private void registerImage(MenuItem item, String name) {
		ImageData imgData = new ImageData(MenuBarUnit.class.getResourceAsStream(IMAGE_PATH + name + ".png"));
		imgData = imgData.scaledTo(16, 16);
		Image img = new Image(display, imgData);
		images.add(img);
		item.setImage(img);
	}

	public void dispose() {
		Iterator<Image> iter = images.iterator();
		while (iter.hasNext())
			iter.next().dispose();
		images.clear();
	}

	protected void snapshot() {
		final MemoryController controller = MemoryController.getInstance();
		InputDialog dialog = new InputDialog(shell, "Take snapshot",
				"Enter the name of the Snapshot", "snapshot...",
				new IInputValidator() {
					@Override
					public String isValid(String newText) {
						if (controller.contains(newText))
							return newText;
						return null;
					}
				});
		if (dialog.open() == SWT.CANCEL)
			return;
		controller.snapshot(dialog.getValue());
	}

	protected void restoreSnapshot() {
		final MemoryController controller = MemoryController.getInstance();
		InputDialog dialog = new InputDialog(shell, "Restore snapshot",
				"Enter the name of the Snapshot", "snapshot...",
				new IInputValidator() {
					@Override
					public String isValid(String newText) {
						if (controller.contains(newText))
							return null;
						return newText;
					}
				});
		if (dialog.open() == SWT.CANCEL)
			return;
		controller.restoreSnapshot(dialog.getValue());
	}

	protected void fill() {
		ToolIF tool = Application.getInstance().getTool();
		if (!(tool instanceof SelectTool)) {
			errorDialog("Fill Error", "No Items Selected");
		} else {
			SelectTool selectTool = (SelectTool) tool;
			selectTool.fill();

		}
		Application.getInstance().refresh();
	}
}
