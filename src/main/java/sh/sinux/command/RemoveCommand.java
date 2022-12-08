package sh.sinux.command;

import picocli.CommandLine.*;
import sh.sinux.repository.RepositoryProxy;

import java.util.concurrent.Callable;

@Command(name = "remove", description = "Remove a snippet", mixinStandardHelpOptions = true)
public class RemoveCommand implements Callable<Integer> {

    @Parameters(index = "0", description = "The unique name of the snippet to remove", paramLabel = "NAME")
    private String name;

    @Override
    public Integer call() {
        var repo = RepositoryProxy.getInstance();
        var ok = repo.remove(name);
        return ok ? 0 : 1;
    }
}
