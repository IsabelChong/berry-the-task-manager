package berry.exception;

/**
 * Signals that the given index is not present in the task list.
 */
public class IndexOutOfRangeException extends BerryException {
    public IndexOutOfRangeException () {
        super("Oh no! I cannot find a task with that task number.\n" +
                "You can check them again by asking me to 'list'");
    }
}
