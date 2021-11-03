package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

@Mojo(name = "update")
public class IbelloUpdate extends IbelloMojo{

    @Parameter(property = "browser")
    private String browser;

    @Parameter(property = "remove")
    private boolean remove;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("update");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException{
        List<String> result = super.getCalculatedCommand(command);
        appendArgument(result, "--browser", browser);
        if (remove) {
            result.add("--remove");
        }
        return result;
    }
}
