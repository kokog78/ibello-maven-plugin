package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

import java.util.List;

@Mojo(name = "ibelloUpdateAll")
public class IbelloUpdateAll extends IbelloMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("update");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) {
        List<String> result = super.getCalculatedCommand(command);
        result.add("--remove");
        return result;
    }
}
