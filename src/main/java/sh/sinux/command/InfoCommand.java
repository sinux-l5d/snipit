package sh.sinux.command;

import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParentCommand;
import picocli.CommandLine.Command;
import sh.sinux.Main;

/**
 * The Info command is the user api to get information about a snippet.
 * It prints the name, the tags and the location of the snippet (a string
 * meaningful for the current Repository type, like a filesystem path).
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "info", description = "Show snippet info", mixinStandardHelpOptions = true)
public class InfoCommand implements Runnable {

    @ParentCommand
    private Main main;

    /** The name of the snippet to show info about */
    @Parameters(index = "0", description = "The snippet name")
    String name;

    /**
     * The run method is called by picocli when the command is executed.
     * It prints the name, the tags and the location of the snippet, along with a note about
     * how to print the snippet content.
     */
    @Override
    public void run() {
        var snippet = main.repository().get(name);
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
