package meowmeow;

import java.time.LocalDate;

/**
 * Represents a task that spans a start and an end date or time.
 */
public class Event extends Task {

    /** Date or time the event starts */
    protected TaskDateTime from;
    /** Date or time the event ends */
    protected TaskDateTime to;

    /**
     * Constructs an event with the given description, start and end times.
     *
     * @param description Description of the event.
     * @param from Date or time the event starts.
     * @param to Date or time the event ends.
     */
    public Event(String description, TaskDateTime from, TaskDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return !date.isBefore(from.toLocalDate()) && !date.isAfter(to.toLocalDate());
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from.toStorageFormat() + " | " + to.toStorageFormat();
    }
}
