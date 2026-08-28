package meowmeow;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private static final String LINE = "    ____________________________________________________________";
    private static final String LOGO = "  /\\_/\\\n"
            + " ( o.o )\n"
            + "  > ^ <\n";
    private static final DateTimeFormatter DATE_HEADING =
            DateTimeFormatter.ofPattern("MMM dd yyyy");

    private final Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void close() {
        scanner.close();
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showWelcome() {
        showLine();
        System.out.println(LOGO);
        System.out.println("     Hello! I'm MeowMeow.");
        System.out.println("     What can I do for you? Meow :>");
        showLine();
    }

    public void showFarewell() {
        showMessage("Bye. Hope to see you again soon! Meow :>");
    }

    public void showMessage(String message) {
        showLine();
        System.out.println("     " + message);
        showLine();
    }

    public void showError(String message) {
        showMessage(message);
    }

    public void showLoadingError(String message) {
        showMessage(message + " Starting with an empty list.");
    }

    public void showTaskMessage(String message, Task task) {
        showLine();
        System.out.println("     " + message);
        System.out.println("       " + task);
        showLine();
    }

    public void showAdded(Task task, int totalCount) {
        showLine();
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalCount + " task(s) in the list.");
        showLine();
    }

    public void showRemoved(Task task, int totalCount) {
        showLine();
        System.out.println("     Noted. I've removed this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + totalCount + " task(s) in the list.");
        showLine();
    }

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

    /**
     * Prints the tasks matching a search, or a message if there are none.
     *
     * @param matches Tasks whose descriptions matched the keyword.
     */
    public void showMatches(ArrayList<Task> matches) {
        showLine();
        if (matches.isEmpty()) {
            System.out.println("     No matching tasks found. Meow?");
        } else {
            System.out.println("     Here are the matching tasks in your list:");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println("     " + (i + 1) + "." + matches.get(i));
            }
        }
        showLine();
    }
}