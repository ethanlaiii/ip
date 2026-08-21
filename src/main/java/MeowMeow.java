import java.util.Scanner;
import static java.lang.Integer.parseInt;

public class MeowMeow {
    private static final String LINE = "    ____________________________________________________________";
    private static final String LOGO = "  /\\_/\\\n"
                                     + " ( o.o )\n"
                                     + "  > ^ <\n";
    private static final int MAX_TASKS = 100;
    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println("     Hello! I'm MeowMeow.");
        System.out.println("     What can I do for you? Meow :>");
        System.out.println(LINE);

        Task[] taskList = new Task[MAX_TASKS];
        int taskCount = 0;
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String input = scanner.nextLine().trim();
            String[] words = input.split("\\s+", 2);
            String command = words[0];
            String arguments = (words.length > 1) ? words[1].trim() : "";

            try {
                if (input.isEmpty()) {
                    throw new MeowMeowException("You didn't type anything. Give me something to work with!");

                } else if (command.equals("bye")) {
                    printMessage("Bye. Hope to see you again soon! Meow :>");
                    isRunning = false;

                } else if (command.equals("list")) {
                    if (taskCount == 0) {
                        printMessage("Your list is empty. Nothing to do yet!");
                    } else {
                        System.out.println(LINE);
                        System.out.println("     Here are the tasks in your list:");
                        for (int i = 0; i < taskCount; i++) {
                            System.out.println("     " + (i + 1) + "." + taskList[i]);
                        }
                        System.out.println(LINE);
                    }

                } else if (command.equals("mark")) {
                    int index = parseIndex(arguments, taskCount, "mark") ;
                    taskList[index].markAsDone();
                    printTaskMessage("Nice! I've marked this task as done:", taskList[index]);

                } else if (command.equals("unmark")) {
                    int index = parseIndex(arguments, taskCount, "unmark");
                    taskList[index].markAsNotDone();
                    printTaskMessage("OK, I've marked this task as not done yet:", taskList[index]);

                } else if (command.equals("todo")) {
                    checkSpace(taskCount);
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
                    }
                    taskList[taskCount] = new Todo(arguments);
                    taskCount++;
                    printAdded(taskList[taskCount - 1], taskCount);

                } else if (command.equals("deadline")) {
                    checkSpace(taskCount);
                    String[] parts = arguments.split("/by", 2);
                    String description = parts[0].trim();
                    if (description.isEmpty()) {
                        throw new MeowMeowException("A deadline needs a description. "
                                + "Try: deadline return book /by Sunday");
                    }
                    if (parts.length < 2 || parts[1].trim().isEmpty()) {
                        throw new MeowMeowException("I need a due date after /by. "
                                + "Try: deadline return book /by Sunday");
                    }
                    taskList[taskCount] = new Deadline(description, parts[1].trim());
                    taskCount++;
                    printAdded(taskList[taskCount -1], taskCount);

                } else if (command.equals("event")) {
                    checkSpace(taskCount);
                    String[] fromParts = arguments.split("/from", 2);
                    String description = fromParts[0].trim();
                    if (description.isEmpty()) {
                        throw new MeowMeowException("An event needs a description. "
                                + "Try: event project meeting");
                    }
                    if (fromParts.length < 2) {
                        throw new MeowMeowException("I need a start time after /from. "
                                + "Try: event project meeting /from Mon 2pm /to 4pm");
                    }
                    String[] toParts = fromParts[1].split("/to", 2);
                    String from = toParts[0].trim();
                    if (from.isEmpty()) {
                        throw new MeowMeowException("The start time after /from is empty. "
                                + "Try: event project meeting /from Mon 2pm /to 4pm");
                    }
                    if (toParts.length < 2 || toParts[1].trim().isEmpty()) {
                        throw new MeowMeowException("I need an end time after /to. "
                                + "Try: event project meeting /from Mon 2pm /to 4pm");
                    }
                    taskList[taskCount] = new Event(description, from, toParts[1].trim());
                    taskCount++;
                    printAdded(taskList[taskCount - 1], taskCount);
                } else {
                    throw new MeowMeowException("I don't know what \"" + command + "\" means. "
                            + "I understand: todo, deadline, event, list, mark, unmark, bye");
                }
            } catch (MeowMeowException e) {
                printMessage(e.getMessage());
            }
        }

        scanner.close();
    }

    private static int parseIndex(String arguments, int taskCount, String command) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which task? Give me a number, e.g. " + command + " 2");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new MeowMeowException("\"" + arguments + "\" isn't a number. Try: " + command + " 2");
        }
        if (taskCount == 0) {
            throw new MeowMeowException("Your list is empty, so there's nothing to " + command + ".");
        }
        if (index < 0 || index >= taskCount) {
            throw new MeowMeowException("There's no task " + (index + 1) + ". You have " + taskCount + " task(s).");
        }
        return index;
    }

    private static void checkSpace(int taskCount) throws MeowMeowException {
        if (taskCount >= MAX_TASKS) {
            throw new MeowMeowException("Your list is full at " + MAX_TASKS + " tasks!");
        }
    }

    private static void printMessage(String message) {
        System.out.println(LINE);
        System.out.println("     " + message);
        System.out.println(LINE);
    }

    private static void printTaskMessage(String message, Task task) {
        System.out.println(LINE);
        System.out.println("     " + message);
        System.out.println("       " + task);
        System.out.println(LINE);
    }

    private static void printAdded(Task task, int taskCount) {
        System.out.println(LINE);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + task);
        System.out.println("     Now you have " + taskCount + " task(s) in the list.");
        System.out.println(LINE);
    }
}
