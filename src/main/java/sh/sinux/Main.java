package sh.sinux;

import picocli.CommandLine;
import picocli.CommandLine.*;
import sh.sinux.command.*;
import sh.sinux.config.Config;
import sh.sinux.config.StorageType;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "snipit", mixinStandardHelpOptions = true, version = "snipit 0.1.0", synopsisSubcommandLabel = "COMMAND",
        description = "A simple snippet manager", subcommands = {ListCommand.class, AddCommand.class, RemoveCommand.class})
public class Main {

    @Option(names = {"-p", "--path"}, description = "Path to the storage")
    String storagePath;

    public static void main(String[] args) {
        var app = new Main();
        int exitCode = new CommandLine(app).setExecutionStrategy(app::executionStrategy).execute(args);
        System.exit(exitCode);
    }

    private int executionStrategy(ParseResult parseResult) {
        init(); // custom initialization to be done before executing any command or subcommand
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }

    public void init() {
        if (storagePath == null) {
            Config.create();
        } else {
            Config.create(StorageType.FILESYSTEM, storagePath);
        }
        RepositoryProxy.create(Config.getInstance());
    }
}