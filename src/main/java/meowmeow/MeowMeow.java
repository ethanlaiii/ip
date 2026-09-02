package meowmeow;

import java.time.LocalDate;

/**
 * Entry point for the MeowMeow chatbot.
 * Reads commands from standard input, updates the task list, and persists
 * changes to disk after every modification.
 */
public class MeowMeow {

    private static final String DEFAULT_FILE_PATH = "data/meowmeow.txt";

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;
    private String loadingError = null;
    private boolean isExit = false;

    /**
     * Constructs a chatbot that saves to and loads from the given file path.
     * Starts with an empty task list if the file cannot be read.
     *
     * @param filePath Path to the save file, relative to the project root.
     */
    public MeowMeow(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (MeowMeowException e) {
            loadingError = e.getMessage();
            tasks = new TaskList();
        }
    }

    /**
     * Constructs a chatbot using the default save file path.
     */
    public MeowMeow() {
        this(DEFAULT_FILE_PATH);
    }

    /**
     * Returns the greeting shown when the chatbot starts, including a
     * warning if the save file could not be read.
     *
     * @return Welcome text.
     */
    public String getWelcome() {
        if (loadingError != null) {
            return ui.formatMessage(ui.formatLoadingError(loadingError), ui.formatWelcome());
        }
        return ui.formatWelcome();
    }

    /**
     * Returns whether the user has asked to exit.
     *
     * @return True once the bye command has been given.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Returns the chatbot's reply to a single line of user input.
     * Errors are returned as text rather than thrown, so that the caller
     * can display them like any other response.
     *
     * @param input Raw command typed by the user.
     * @return Text to show the user.
     */
    public String getResponse(String input) {
        try {
            if (input.isEmpty()) {
                throw new MeowMeowException("You didn't type anything. Give me something to work with!");
            }

            CommandType command = Parser.parseCommand(input);
            String arguments = Parser.parseArguments(input);

            switch (command) {
                case BYE -> {
                    isExit = true;
                    return ui.formatFarewell();
                }
                case LIST -> {
                    return ui.formatList(tasks.asList());
                }
                case MARK -> {
                    int index = tasks.parseIndex(arguments, "mark");
                    tasks.markAsDone(index);
                    storage.save(tasks.asList());
                    return ui.formatTaskMessage("Nice! I've marked this task as done:", tasks.get(index));
                }
                case UNMARK -> {
                    int index = tasks.parseIndex(arguments, "unmark");
                    tasks.markAsNotDone(index);
                    storage.save(tasks.asList());
                    return ui.formatTaskMessage("OK, I've marked this task as not done yet:", tasks.get(index));
                }
                case DELETE -> {
                    int index = tasks.parseIndex(arguments, "delete");
                    Task removed = tasks.delete(index);
                    storage.save(tasks.asList());
                    return ui.formatRemoved(removed, tasks.size());
                }
                case TODO -> {
                    Task added = tasks.add(Parser.parseTodo(arguments));
                    storage.save(tasks.asList());
                    return ui.formatAdded(added, tasks.size());
                }
                case DEADLINE -> {
                    Task added = tasks.add(Parser.parseDeadline(arguments));
                    storage.save(tasks.asList());
                    return ui.formatAdded(added, tasks.size());
                }
                case EVENT -> {
                    Task added = tasks.add(Parser.parseEvent(arguments));
                    storage.save(tasks.asList());
                    return ui.formatAdded(added, tasks.size());
                }
                case ON -> {
                    LocalDate date = Parser.parseDate(arguments).toLocalDate();
                    return ui.formatTasksOn(date, tasks.findOccurringOn(date));
                }
                case FIND -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("What should I search for? Try: find book");
                    }
                    return ui.formatMatches(tasks.findByKeyword(arguments));
                }
                case UNKNOWN -> throw new MeowMeowException(
                        "I don't know what \"" + Parser.parseCommandWord(input) + "\" means. "
                                + "I understand: todo, deadline, event, list, mark, unmark, delete, on, find, bye");
                default -> throw new MeowMeowException("Something went wrong. Meow?");
            }

        } catch (MeowMeowException e) {
            return ui.formatError(e.getMessage());
        }
    }

    /**
     * Runs the command loop until the user exits.
     */
    public void run() {
        ui.printToConsole(getWelcome());
        while (!isExit) {
            ui.printToConsole(getResponse(ui.readCommand()));
        }
        ui.close();
    }

    /**
     * Starts the console version of chatbot.
     *
     * @param args Command line arguments, which are ignored.
     */
    public static void main(String[] args) {
        new MeowMeow(DEFAULT_FILE_PATH).run();
    }
}