package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "stop")
public class IbelloStop extends IbelloMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("stop");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException{
        List<String> result = super.getCalculatedCommand(command);
        appendArgument(result, "--pid", getPidFile());
        return result;
    }
}
