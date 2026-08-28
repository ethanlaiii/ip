package meowmeow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ParserTest {

    @Test
    public void parseDeadline_validInputWithTime_deadlineCreated() throws MeowMeowException {
        Deadline deadline = Parser.parseDeadline("return book /by 2019-12-02 1800");
        assertEquals("[D][ ] return book (by: Dec 02 2019, 6:00PM)", deadline.toString());
    }

    @Test
    public void parseDeadline_validInputDateOnly_noTimeShown() throws MeowMeowException {
        Deadline deadline = Parser.parseDeadline("return book /by 2019-12-02");
        assertEquals("[D][ ] return book (by: Dec 02 2019)", deadline.toString());
    }

    @Test
    public void parseDeadline_slashDateFormat_deadlineCreated() throws MeowMeowException {
        Deadline deadline = Parser.parseDeadline("submit report /by 2/12/2019");
        assertEquals("[D][ ] submit report (by: Dec 02 2019)", deadline.toString());
    }

    @Test
    public void parseDeadline_extraSpacesAroundDelimiter_trimmed() throws MeowMeowException {
        Deadline deadline = Parser.parseDeadline("  return book   /by   2019-12-02  ");
        assertEquals("[D][ ] return book (by: Dec 02 2019)", deadline.toString());
    }

    @Test
    public void parseDeadline_emptyDescription_exceptionThrown() {
        MeowMeowException e = assertThrows(MeowMeowException.class, ()
                -> Parser.parseDeadline("/by 2019-12-02"));
        assertTrue(e.getMessage().contains("description"));
    }

    @Test
    public void parseDeadline_missingByDelimiter_exceptionThrown() {
        MeowMeowException e = assertThrows(MeowMeowException.class, ()
                -> Parser.parseDeadline("return book"));
        assertTrue(e.getMessage().contains("/by"));
    }

    @Test
    public void parseDeadline_emptyDateAfterBy_exceptionThrown() {
        assertThrows(MeowMeowException.class, () -> Parser.parseDeadline("return book /by"));
    }

    @Test
    public void parseDeadline_unparseableDate_exceptionThrown() {
        assertThrows(MeowMeowException.class, ()
                -> Parser.parseDeadline("return book /by next Tuesday"));
    }

    @Test
    public void parseDeadline_invalidCalendarDate_exceptionThrown() {
        assertThrows(MeowMeowException.class, ()
                -> Parser.parseDeadline("return book /by 2019-13-45"));
    }

    @Test
    public void parseTodo_emptyDescription_exceptionThrown() {
        assertThrows(MeowMeowException.class, () -> Parser.parseTodo(""));
    }

    @Test
    public void parseCommand_mixedCase_resolvedCorrectly() {
        assertEquals(CommandType.TODO, Parser.parseCommand("TODO read book"));
        assertEquals(CommandType.BYE, Parser.parseCommand("Bye"));
    }

    @Test
    public void parseCommand_unrecognisedWord_unknownReturned() {
        assertEquals(CommandType.UNKNOWN, Parser.parseCommand("blah"));
    }
}