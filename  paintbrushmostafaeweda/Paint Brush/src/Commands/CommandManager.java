package Commands;

import java.util.LinkedList;

import UI.Application;

/**
 * @author  Mostafa Eweda Lap
 */
public class CommandManager {

	/**
	 * @uml.property  name="instance"
	 * @uml.associationEnd  
	 */
	private static CommandManager instance;

	private LinkedList<AbstractCommand> history;
	private LinkedList<AbstractCommand> redo;

	/**
	 * @return
	 * @uml.property  name="instance"
	 */
	public synchronized static CommandManager getInstance() {
		if (instance == null)
			return instance = new CommandManager();
		return instance;
	}

	private CommandManager() {
		redo = new LinkedList<AbstractCommand>();
		history = new LinkedList<AbstractCommand>();
	}

	public void registerCommand(AbstractCommand command) {
		history.addFirst(command);
		redo.clear();
	}


	public void undo() {
		if (history.isEmpty())
			return;
		AbstractCommand undoCommand = history.removeFirst();
		undoCommand.undoIt();
		Application.getInstance().refresh();
		redo.addFirst(undoCommand);
	}

	public void redo() {
		if (redo.isEmpty())
			return;
		AbstractCommand redoCommand = redo.removeFirst();
		redoCommand.doIt();
		Application.getInstance().refresh();
		history.addFirst(redoCommand);
	}

	public void clearHistory() {
		history.clear();
		redo.clear();
	}

}
