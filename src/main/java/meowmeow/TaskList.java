package meowmeow;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Holds the collection of tasks and provides operations to query and modify it.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Constructs an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Constructs a task list containing the given tasks.
     *
     * @param tasks Tasks to populate the list with.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }


    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns a copy of the tasks in this list.
     * Modifying the returned list does not affect this task list.
     *
     * @return Copy of the current tasks.
     */
    public ArrayList<Task> asList() {
        return new ArrayList<>(tasks);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task Task to add.
     * @return The task that was added.
     */
    public Task add(Task task) {
        tasks.add(task);
        return task;
    }

    /**
     * Removes the task at the given index.
     *
     * @param index Zero-based index of the task to remove.
     * @return The task that was removed.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Marks the task at the given index as completed.
     *
     * @param index Zero-based index of the task.
     */
    public void markAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the given index as not yet completed.
     *
     * @param index Zero-based index of the task.
     */
    public void markAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Returns the tasks that occur on the given date.
     *
     * @param date Date to filter by.
     * @return Tasks occurring on that date, in list order.
     */
    public ArrayList<Task> findOccurringOn(LocalDate date) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.occursOn(date)) {
                matches.add(task);
            }
        }
        return matches;
    }

    /**
     * Returns the zero-based index referred to by the given user argument.
     * Validates that the argument is a number within the bounds of this list.
     *
     * @param arguments Text the user typed after the command word.
     * @param command Command name, used in error messages.
     * @return Zero-based index into this list.
     * @throws MeowMeowException If the argument is missing, is not a number,
     *         this list is empty, or the index is out of range.
     */
    public int parseIndex(String arguments, String command) throws MeowMeowException {
        if (arguments.isEmpty()) {
            throw new MeowMeowException("Which task? Give me a number, e.g. " + command + " 2");
        }
        if (tasks.isEmpty()) {
            throw new MeowMeowException("Your list is empty, so there's nothing to " + command + ".");
        }
        int index;
        try {
            index = Integer.parseInt(arguments) - 1;
        } catch (NumberFormatException e) {
            throw new MeowMeowException("\"" + arguments + "\" isn't a number. Try: " + command + " 2");
        }
        if (index < 0 || index >= tasks.size()) {
            throw new MeowMeowException("There's no task " + (index + 1) + ". You have "
                    + tasks.size() + " task(s).");
        }
        return index;
    }
}