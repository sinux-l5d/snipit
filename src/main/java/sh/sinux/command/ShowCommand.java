package sh.sinux.command;


import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import sh.sinux.repository.RepositoryProxy;

@Command(name = "show", description = "Show snippet content", mixinStandardHelpOptions = true)
public class ShowCommand implements Runnable{
    @Parameters(index = "0", description = "The snippet name")
    String name;

    @Override
    public void run() {
        var snippet = RepositoryProxy.getInstance().get(name);
        if (snippet == null) {
            System.out.println("Snippet not found");
            return;
        }
        System.out.println(snippet.content());
    }
}
