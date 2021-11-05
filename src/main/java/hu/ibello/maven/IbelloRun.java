package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;

import java.util.List;

@Mojo(name = "run", requiresDependencyResolution = ResolutionScope.COMPILE)
public class IbelloRun extends IbelloMojo{

    @Parameter(property = "java")
    private String java;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("run");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException{
        List<String> result = super.getCalculatedCommand(command);
        appendArgument(result, "--java", java);
        return result;
    }
}
