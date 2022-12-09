package sh.sinux.command;

import picocli.CommandLine.Option;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ArgGroup;
import sh.sinux.Snippet;
import sh.sinux.repository.RepositoryProxy;

import java.util.List;

@Command(name = "search", description = "Search in snippets name, content and tags", mixinStandardHelpOptions = true)
public class SearchCommand implements Runnable {

    @Parameters(index = "0", description = "The query to search")
    String query;

    @ArgGroup()
    SearchOptions options;
    private static class SearchOptions {
        @Option(names = {"-n", "--name"}, description = "Search in snippet name")
        boolean name;

        @Option(names = {"-c", "--content"}, description = "Search in snippet content")
        boolean content;

        @Option(names = {"-t", "--tags"}, description = "Search in snippet tags")
        boolean tags;

        @Option(names = {"-a", "--all"}, description = "Search in snippet name, content and tags (default)")
        boolean all;
    }

    @Override
    public void run() {
        List<Snippet> snippets;
        if (options == null || options.all) {
            snippets = RepositoryProxy.getInstance().searchAll(query);
        } else if (options.name) {
            snippets = RepositoryProxy.getInstance().searchName(query);
        } else if (options.content) {
            snippets = RepositoryProxy.getInstance().searchContent(query);
        } else if (options.tags) {
            snippets = RepositoryProxy.getInstance().searchTags(query);
        } else {
            snippets = RepositoryProxy.getInstance().searchAll(query);
        }

        if (snippets.isEmpty()) {
            System.out.println("No snippets found");
            return;
        }
        System.out.println("Snippets found:");
        for (var snippet : snippets) {
            System.out.println("  - " + snippet.name());
        }
    }
}
