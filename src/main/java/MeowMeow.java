import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class MeowMeow {
    private static final String LINE = "    ____________________________________________________________";
    private static final String LOGO = "  /\\_/\\\n"
                                     + " ( o.o )\n"
                                     + "  > ^ <\n";

    public static void main(String[] args) {
        System.out.println(LINE);
        System.out.println(LOGO);
        System.out.println("     Hello! I'm MeowMeow.");
        System.out.println("     What can I do for you? Meow :>");
        System.out.println(LINE);

        Storage storage = new Storage();
        ArrayList<Task> taskList;
        try {
            taskList = storage.load();
        } catch (MeowMeowException e) {
            printMessage(e.getMessage() + " Starting with an empty list.");
            taskList = new ArrayList<>();
        }
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            String input = scanner.nextLine().trim();
            String[] words = input.split("\\s+", 2);
            String commandWord = words[0];
            CommandType command = CommandType.fromString(commandWord);
            String arguments = (words.length > 1) ? words[1].trim() : "";

            try {
                if (input.isEmpty()) {
                    throw new MeowMeowException("You didn't type anything. Give me something to work with!");

                }

                switch (command) {
                case BYE -> {
                    printMessage("Bye. Hope to see you again soon! Meow :>");
                    isRunning = false;
                }
                case LIST -> {
                    if (taskList.isEmpty()) {
                        printMessage("Your list is empty. Nothing to do yet!");
                    } else {
                        System.out.println(LINE);
                        System.out.println("     Here are the tasks in your list:");
                        for (int i = 0; i < taskList.size(); i++) {
                            System.out.println("     " + (i + 1) + "." + taskList.get(i));
                        }
                        System.out.println(LINE);
                    }
                }
                case MARK -> {
                    int index = parseIndex(arguments, taskList.size(), "mark");
                    taskList.get(index).markAsDone();
                    storage.save(taskList);
                    printTaskMessage("Nice! I've marked this task as done:", taskList.get(index));
                }
                case UNMARK -> {
                    int index = parseIndex(arguments, taskList.size(), "unmark");
                    taskList.get(index).markAsNotDone();
                    storage.save(taskList);
                    printTaskMessage("OK, I've marked this task as not done yet:", taskList.get(index));
                }
                case DELETE -> {
                    int index = parseIndex(arguments, taskList.size(), "delete");
                    Task removed = taskList.remove(index);
                    storage.save(taskList);
                    System.out.println(LINE);
                    System.out.println("     Noted. I've removed this task:");
                    System.out.println("       " + removed);
                    System.out.println("     Now you have " + taskList.size() + " task(s) in the list.");
                    System.out.println(LINE);
                }
                case TODO -> {
                    if (arguments.isEmpty()) {
                        throw new MeowMeowException("A todo needs a description. Try: todo borrow book");
                    }
                    taskList.add(new Todo(arguments));
                    storage.save(taskList);
                    printAdded(taskList);
                }
                case DEADLINE -> {
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
                    TaskDateTime by = TaskDateTime.parse(parts[1].trim());
                    taskList.add(new Deadline(description, by));
                    storage.save(taskList);
                    printAdded(taskList);
                }
                case EVENT -> {
                    String[] fromParts = arguments.split("/from", 2);
                    String description = fromParts[0].trim();
                    if (description.isEmpty()) {
                        throw new MeowMeowException("An event needs a description. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    if (fromParts.length < 2) {
                        throw new MeowMeowException("I need a start time after /from. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    String[] toParts = fromParts[1].split("/to", 2);
                    String fromText = toParts[0].trim();
                    if (fromText.isEmpty()) {
                        throw new MeowMeowException("The start time after /from is empty. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    if (toParts.length < 2 || toParts[1].trim().isEmpty()) {
                        throw new MeowMeowException("I need an end time after /to. "
                                + "Try: event project meeting /from 2019-08-06 1400 /to 2019-08-06 1600.");
                    }
                    TaskDateTime from = TaskDateTime.parse(fromText);
                    TaskDateTime to = TaskDateTime.parse(toParts[1].trim());
                    taskList.add(new Event(description, from, to));
                    storage.save(taskList);
                    printAdded(taskList);
                }

                case UNKNOWN -> {
                    throw new MeowMeowException("I don't know what \"" + commandWord + "\" means. "
                            + "I understand: todo, deadline, event, list, mark, unmark, delete, bye");
                }
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
        if (taskCount == 0) {
            throw new MeowMeowException("Your list is empty, so there's nothing to " + command + ".");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new MeowMeowException("\"" + arguments + "\" isn't a number. Try: " + command + " 2");
        }

        if (index < 0 || index >= taskCount) {
            throw new MeowMeowException("There's no task " + (index + 1) + ". You have " + taskCount + " task(s).");
        }
        return index;
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

    private static void printAdded(ArrayList<Task> taskList) {
        System.out.println(LINE);
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + taskList.getLast());
        System.out.println("     Now you have " + taskList.size() + " task(s) in the list.");
        System.out.println(LINE);
    }
}
