package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "help")
public class IbelloHelp extends IbelloMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("help");
    }
}
