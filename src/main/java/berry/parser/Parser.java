package berry.parser;

import berry.command.*;

import berry.exception.BerryException;
import berry.exception.EmptyClauseException;
import berry.exception.EmptyDescriptionException;
import berry.exception.UnknownCommandException;
import berry.exception.MissingClauseException;

import berry.task.Deadline;
import berry.task.Event;
import berry.task.Todo;

/**
 * Deals with making sense of the user command.
 */

public class Parser {

    private enum CommandType {
        LIST, MARK, UNMARK, TODO, DEADLINE, EVENT, DELETE, BYE
    }

    public static Command parseCommand(String input) throws BerryException {
        String[] splitInput = input.split(" ");
        String[] listStr;
        CommandType commandType;

        try {
            commandType = CommandType.valueOf(splitInput[0].toUpperCase());
        } catch (IllegalArgumentException e){
            throw new UnknownCommandException();
        }

        validate(commandType, input);

        switch (commandType) {
        case BYE:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case MARK:
            return new MarkCommand(Integer.parseInt(splitInput[1]));
        case UNMARK:
            return new UnmarkCommand(Integer.parseInt(splitInput[1]));
        case TODO:
            return new AddTaskCommand(new Todo(splitInput[1]));
        case DEADLINE:
            listStr = input.split(" /by ");
            return new AddTaskCommand(new Deadline(splitInput[0], listStr[1]));
        case EVENT:
            listStr = input.split(" /from ");
            String[] listStrTwo = listStr[1].split(" /to ");
            return new AddTaskCommand(new Event(splitInput[0], listStrTwo[0], listStrTwo[1]));
        case DELETE:
            return new DeleteCommand(Integer.parseInt(splitInput[1]));
        default:
            throw new UnknownCommandException();
        }
    }

    private static void validate(CommandType commandType, String input) throws BerryException {
        String com = input.split(" ")[0];

        switch(commandType) {
        case TODO:
            if (input.substring(4).isBlank()) { // empty description
                throw new EmptyDescriptionException(com);
            }
            break;
        case DEADLINE:
            if (!input.contains("/by ") || !input.contains("/by")) { // no by clause
                throw new MissingClauseException("by");
            } else if (input.split("/by")[0].substring(8).isBlank()) { // empty description
                throw new EmptyDescriptionException(com);
            } else if (input.endsWith("/by") || input.split("/by")[1].isBlank()) {
                throw new EmptyClauseException("by");
            }
            break;
        case EVENT:
            if (!input.contains("/from ") || !input.contains("/from")) {
                throw new MissingClauseException("from");
            } else if (input.split("/from")[0].substring(5).isBlank()) { // empty description
                throw new EmptyDescriptionException(com);
            } else if (!input.contains("/to ") || !input.contains("/to")) {
                throw new MissingClauseException("to");
            } else if (input.endsWith("/to") || input.split("/to")[1].isBlank()) {
                throw new EmptyClauseException("to");
            } else if (input.endsWith("/from") || input.split("/to")[0].split("/from")[1].isBlank()) {
                throw new EmptyClauseException("from");
            }
            break;
        }
    }
}
