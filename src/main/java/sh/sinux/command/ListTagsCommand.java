package sh.sinux.command;

import picocli.CommandLine.Command;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "list-tags", description = "List all tags", mixinStandardHelpOptions = true)
public class ListTagsCommand implements Runnable {

    @Override
    public void run() {
        var tags = RepositoryProxy.getInstance().listTags();
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
