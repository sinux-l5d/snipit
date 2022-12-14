package sh.sinux.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.ParentCommand;
import sh.sinux.Main;

/**
 * The List command is the user api to list snippets.
 * It prints the name of the snippets, along with their tags if specified.
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "list", description = "List all snippets", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {

    @ParentCommand
    private Main main;

    /** Flag to show tags next to snippet name */
    @Option(names = {"-t", "--tag"}, description = "Show tags along with snippet names")
    Boolean showTags = false;

    /**
     * The run method is called by picocli when the command is executed.
     * It prints the name of the snippets, along with their tags if specified.
     */
    @Override
    public void run() {
        var snippetNames = main.repository().listNames();
        if (snippetNames.isEmpty()) {
            System.out.println("No snippets found");
            return;
        }
        System.out.println("Snippets:");
        for (var snippetName : snippetNames) {
            String tags = "";
            if (showTags) {
                var snippet = main.repository().get(snippetName);
                if (snippet != null) {
                    tags = String.join(",", snippet.tags());
                }
            }
            System.out.println("  - " + snippetName + (showTags ? " [" + tags + "]" : ""));
        }
    }
}
