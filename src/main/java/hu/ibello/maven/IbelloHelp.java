package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

public class IbelloHelp extends IbelloMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("help");
    }
}
