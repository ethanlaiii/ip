package meowmeow;

import java.time.LocalDate;

public class Event extends Task {
    protected TaskDateTime from;
    protected TaskDateTime to;

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
