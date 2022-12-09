package sh.sinux.command;


import picocli.CommandLine.Parameters;
import picocli.CommandLine.Command;
import sh.sinux.repository.RepositoryProxy;

/**
 * The Show command is the user api to show a snippet.
 * It prints the content of the snippet.
 * @author sinux-l5d
 * @version 1.0
 */
@Command(name = "show", description = "Show snippet content", mixinStandardHelpOptions = true)
public class ShowCommand implements Runnable{

    /** The unique name of the snippet to show */
    @Parameters(index = "0", description = "The snippet name")
    String name;

    /**
     * The run method is called by picocli when the command is executed.
     * It prints the content of the snippet.
     */
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
