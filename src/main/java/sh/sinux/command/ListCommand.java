package sh.sinux.command;

import picocli.CommandLine.*;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "list", description = "List all snippets", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {

    @Option(names = {"-t", "--tag"}, description = "Show tags along with snippet names")
    Boolean showTags = false;

    @Override
    public void run() {
        var snippetNames = RepositoryProxy.getInstance().list();
        if (snippetNames.isEmpty()) {
            System.out.println("No snippets found");
            return;
        }
        System.out.println("Snippets:");
        for (var snippetName : snippetNames) {
            String tags = "";
            if (showTags) {
                var snippet = RepositoryProxy.getInstance().get(snippetName);
                if (snippet != null) {
                    tags = String.join(",", snippet.tags());
                }
            }
            System.out.println("  - " + snippetName + (showTags ? " [" + tags + "]" : ""));
        }
    }
}
