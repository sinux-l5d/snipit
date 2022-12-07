package sh.sinux;

import picocli.CommandLine;
import picocli.CommandLine.*;
import sh.sinux.config.Config;
import sh.sinux.config.StorageType;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "snapit", mixinStandardHelpOptions = true, version = "snapit 0.1.0",
        description = "A simple snippet manager")
public class Main implements Runnable {

    @Option(names = {"-p", "--path"}, description = "Path to the storage")
    String storagePath;
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Main()).execute(args);
        System.exit(exitCode);
    }

    @Override
    public void run() {
        if (storagePath == null) {
            Config.create();
        } else {
            Config.create(StorageType.FILESYSTEM, storagePath);
        }
        RepositoryProxy.create(Config.getInstance());
        var snippets = RepositoryProxy.getInstance().list();
        if (snippets.isEmpty()) {
            System.out.println("No snippets found");
        } else {
            System.out.println("Snippets:");
            for (var snippet : snippets) {
                System.out.println(snippet);
            }
        }
    }
}