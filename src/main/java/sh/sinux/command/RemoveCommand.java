package sh.sinux.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Parameters;
import sh.sinux.Main;

import java.util.concurrent.Callable;

/**
 * The Remove command is the user api to remove a snippet.
 * It removes the snippet from the current repository.
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "remove", description = "Remove a snippet", mixinStandardHelpOptions = true)
public class RemoveCommand implements Callable<Integer> {

    @ParentCommand
    private Main main;
    /** The unique name of the snippet to remove */
    @Parameters(index = "0", description = "The unique name of the snippet to remove", paramLabel = "NAME")
    private String name;

    /**
     * The call method is called by picocli when the command is executed.
     * It removes the snippet from the current repository.
     * @return 0 if the snippet was removed, 1 if the snippet was not found or an error occurred
     */
    @Override
    public Integer call() {
        var repo = main.repository();
        var ok = repo.remove(name);
        return ok ? 0 : 1;
    }
}
