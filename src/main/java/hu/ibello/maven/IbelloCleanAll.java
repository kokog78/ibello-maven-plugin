package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

@Mojo(name = "cleanAll")
public class IbelloCleanAll extends IbelloMojo{

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("clean");
    }

}
