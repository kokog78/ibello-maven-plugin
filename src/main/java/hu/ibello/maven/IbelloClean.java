package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;

@Mojo(name = "clean")
public class IbelloClean extends IbelloMojo{

    @Parameter(property = "keep", defaultValue = "-1")
    private int keep;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("clean");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException{
        List<String> result = super.getCalculatedCommand(command);
        if (keep >= 0) {
            appendArgument(result, "--keep", Integer.toString(keep));
        }
        return result;
    }

}
