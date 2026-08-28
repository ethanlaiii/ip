package meowmeow;

import java.time.LocalDate;

/**
 * Represents a task with a description and a completion status.
 * Serves as the base class for all specific task types.
 */
public class Task {
    /** Description of what the task involves */
    protected String description;
    /** Whether the task has been completed */
    protected boolean isDone;

    /**
     * Constructs a task with the given description, initially not done.
     *
     * @param description Description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }
    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not yet completed.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns the icon representing this task's completion status.
     *
     * @return "X" if the task is done, a single space otherwise.
     */
    public String getStatusIcon() {
        // mark done task with X
        return (isDone ? "X" : " ");
    }

    /**
     * Returns this task encoded as a single line for file storage.
     *
     * @return Encoded string in the form "status | description".
     */
    public String toFileFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    /**
     * Returns whether this task falls on the given date.
     * Tasks with no date attached never match.
     *
     * @param date Date to check against.
     * @return True if this task occurs on that date.
     */
    public boolean occursOn(LocalDate date) {
        return false;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
