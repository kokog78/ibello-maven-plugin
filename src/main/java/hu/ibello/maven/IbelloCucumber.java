package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo(name = "cucumber")
public class IbelloCucumber extends IbelloTestMojo{

    @Parameter(property = "featuresDir")
    File featuresDir;

    @Parameter(property = "java")
    String java;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("cucumber");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException{
        List<String> result = super.getCalculatedCommand(command);
        appendArgument(result, "--java", java);
        if (featuresDir != null) {
            appendArgument(result, "--features", featuresDir.getAbsolutePath());
        }
        return result;
    }

}
