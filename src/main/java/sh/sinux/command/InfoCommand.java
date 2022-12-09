package sh.sinux.command;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "info", description = "Show snippet info", mixinStandardHelpOptions = true)
public class InfoCommand implements Runnable {
    @Parameters(index = "0", description = "The snippet name")
    String name;

    @Override
    public void run() {
        var snippet = RepositoryProxy.getInstance().get(name);
        if (snippet == null) {
            System.out.println("Snippet not found");
            return;
        }
        System.out.println("Name: " + snippet.name());

        var tags = String.join(", ", snippet.tags());
        System.out.println("Tags: " + (tags.isEmpty() ? "none" : tags));

        // location
        var location = snippet.location();
        System.out.println("Location: " + (location.isEmpty() ? "none" : location));

        System.out.println("To show content, use the 'show' command");
    }
}
