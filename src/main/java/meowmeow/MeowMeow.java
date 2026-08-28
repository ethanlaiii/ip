package meowmeow;

import java.time.LocalDate;

public class MeowMeow {

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public MeowMeow(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (MeowMeowException e) {
            ui.showLoadingError(e.getMessage());
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isRunning = true;

        while (isRunning) {
            String input = ui.readCommand();

            try {
                if (input.isEmpty()) {
                    throw new MeowMeowException("You didn't type anything. Give me something to work with!");
                }

                CommandType command = Parser.parseCommand(input);
                String arguments = Parser.parseArguments(input);

                switch (command) {
                    case BYE -> {
                        ui.showFarewell();
                        isRunning = false;
                    }
                    case LIST -> ui.showList(tasks.asList());
                    case MARK -> {
                        int index = tasks.parseIndex(arguments, "mark");
                        tasks.markAsDone(index);
                        storage.save(tasks.asList());
                        ui.showTaskMessage("Nice! I've marked this task as done:", tasks.get(index));
                    }
                    case UNMARK -> {
                        int index = tasks.parseIndex(arguments, "unmark");
                        tasks.markAsNotDone(index);
                        storage.save(tasks.asList());
                        ui.showTaskMessage("OK, I've marked this task as not done yet:", tasks.get(index));
                    }
                    case DELETE -> {
                        int index = tasks.parseIndex(arguments, "delete");
                        Task removed = tasks.delete(index);
                        storage.save(tasks.asList());
                        ui.showRemoved(removed, tasks.size());
                    }
                    case TODO -> {
                        Task added = tasks.add(Parser.parseTodo(arguments));
                        storage.save(tasks.asList());
                        ui.showAdded(added, tasks.size());
                    }
                    case DEADLINE -> {
                        Task added = tasks.add(Parser.parseDeadline(arguments));
                        storage.save(tasks.asList());
                        ui.showAdded(added, tasks.size());
                    }
                    case EVENT -> {
                        Task added = tasks.add(Parser.parseEvent(arguments));
                        storage.save(tasks.asList());
                        ui.showAdded(added, tasks.size());
                    }
                    case ON -> {
                        LocalDate date = Parser.parseDate(arguments).toLocalDate();
                        ui.showTasksOn(date, tasks.findOccurringOn(date));
                    }
                    case UNKNOWN -> throw new MeowMeowException(
                            "I don't know what \"" + Parser.parseCommandWord(input) + "\" means. "
                                    + "I understand: todo, deadline, event, list, mark, unmark, delete, on, bye");
                }

            } catch (MeowMeowException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.close();
    }

    public static void main(String[] args) {
        new MeowMeow("data/meowmeow.txt").run();
    }
}