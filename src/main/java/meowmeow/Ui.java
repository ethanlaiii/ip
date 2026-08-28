package meowmeow;

import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles all interaction with the user, including reading commands
 * from standard input and formatting output to standard output.
 */
public class Ui {
    // ... constants and scanner field unchanged
    private static final String LINE = "    ____________________________________________________________";
    private static final String LOGO = "  /\\_/\\\n"
            + " ( o.o )\n"
            + "  > ^ <\n";
    private static final DateTimeFormatter DATE_HEADING =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final Scanner scanner;

    /**
     * Constructs a user interface that reads from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Returns the next line of input typed by the user, with surrounding
     * whitespace removed.
     *
     * @return Trimmed line of user input.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    /**
     * Releases the input scanner.
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints a horizontal divider line.
     */
    public void showLine() {
        System.out.println(LINE);
    }

    /**
     * Prints the logo and welcome greeting shown at startup.
     */
    public void showWelcome() {
        showLine();
        System.out.println(LOGO);
        System.out.println("     Hello! I'm MeowMeow.");
        System.out.println("     What can I do for you? Meow :>");
        showLine();
    }

    /**
     * Prints the farewell message shown before the app exits.
     */
    public void showFarewell() {
        showMessage("Bye. Hope to see you again soon! Meow :>");
    }

    /**
     * Prints a single message framed by divider lines.
     *
     * @param message Text to show the user.
     */
    public void showMessage(String message) {
        showLine();
        System.out.println("     " + message);
        showLine();
    }

    /**
     * Prints an error message to the user.
     *
     * @param message Explanation of what went wrong.
     */
    public void showError(String message) {
        showMessage(message);
    }

    /**
     * Prints a warning that the save file could not be read.
     *
     * @param message Explanation of the loading failure.
     */
    public void showLoadingError(String message) {
        showMessage(message + " Starting with an empty list.");
    }

    /**
     * Prints a message followed by the task it refers to.
     *
     * @param message Text describing what happened to the task.
     * @param task Task the message refers to.
     */
    public void showTaskMessage(String message, Task task) {
        showLine();
        System.out.println("     " + message);
        System.out.println("       " + task);
        showLine();
    }

    /**
     * Prints confirmation that a task was added, along with the new list size.
     *
     * @param task Task that was added.
     * @param totalCount Number of tasks in the list after the addition.
     */
    public void showAdded(Task task, int totalCount) {
        showLine();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalCount + " task(s) in the list.");
        showLine();
    }

    /**
     * Prints confirmation that a task was removed, along with the new list size.
     *
     * @param task Task that was removed.
     * @param totalCount Number of tasks remaining in the list.
     */
    public void showRemoved(Task task, int totalCount) {
        showLine();
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalCount + " task(s) in the list.");
        showLine();
    }

    /**
     * Prints the given tasks as a numbered list, or a message if there are none.
     *
     * @param tasks Tasks to display.
     */
    public void showList(ArrayList<Task> tasks) {
        if (tasks.isEmpty()) {
            showMessage("Your list is empty. Nothing to do yet!");
            return;
        }
        showLine();
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println("     " + (i + 1) + "." + tasks.get(i));
        }
        showLine();
    }

    /**
     * Prints the tasks occurring on the given date, or a message if there are none.
     *
     * @param date Date the tasks were filtered by.
     * @param matches Tasks occurring on that date.
     */
    public void showTasksOn(LocalDate date, ArrayList<Task> matches) {
        showLine();
        System.out.println("     Tasks on " + date.format(DATE_HEADING) + ":");
        if (matches.isEmpty()) {
            System.out.println("     Nothing scheduled. Enjoy the free time!");
        } else {
            for (int i = 0; i < matches.size(); i++) {
                System.out.println("     " + (i + 1) + "." + matches.get(i));
            }
        }
        showLine();
    }
}