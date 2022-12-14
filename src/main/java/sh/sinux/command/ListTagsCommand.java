package sh.sinux.command;

import picocli.CommandLine.Command;
import picocli.CommandLine.ParentCommand;
import sh.sinux.Main;

/**
 * The ListTags command is the user api to list all tags ever used.
 * It prints the name of the tags.
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "list-tags", description = "List all tags", mixinStandardHelpOptions = true)
public class ListTagsCommand implements Runnable {

    @ParentCommand
    private Main main;
    /**
     * The run method is called by picocli when the command is executed.
     * It prints the name of the tags.
     */
    @Override
    public void run() {
        var tags = main.repository().listTags();
        if (tags.isEmpty()) {
            System.out.println("No tags found");
            return;
        }
        System.out.println("Tags:");
        for (var tag : tags) {
            System.out.println("  - " + tag);
        }
    }
}
