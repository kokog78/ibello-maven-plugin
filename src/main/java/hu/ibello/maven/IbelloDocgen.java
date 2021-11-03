package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

@Mojo(name = "ibelloDocgen")
public class IbelloDocgen extends IbelloMojo{

    @Parameter(property = "inputFile")
    private File inputFile;

    @Parameter(property = "outputFile")
    private File outputFile;

    @Parameter(property = "overwrite")
    private boolean overwrite;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        runProcess("docgen");
    }

    @Override
    protected List<String> getCalculatedCommand(String command) {
        List<String> result = super.getCalculatedCommand(command);
        if (inputFile != null) {
            appendArgument(result, "--input", inputFile.getAbsolutePath());
        }
        if (outputFile != null) {
            appendArgument(result, "--output", outputFile.getAbsolutePath());
        }
        if (overwrite) {
            result.add("--overwrite");
        }
        return result;
    }

}
