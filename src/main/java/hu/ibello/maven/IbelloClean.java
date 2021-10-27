package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.util.List;

public class IbelloClean extends IbelloMojo{

    private int keep = -1;

    public int getKeep() {
        return keep;
    }

    public void setKeep(int keep) {
        this.keep = keep;
    }

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("clean");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) {
        List<String> result = super.getCalculatedCommand(command);
        if (keep >= 0) {
            appendArgument(result, "--keep", Integer.toString(keep));
        }
        return result;
    }

}
