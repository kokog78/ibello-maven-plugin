package hu.ibello.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class IbelloTestMojo extends IbelloMojo{

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    private ArtifactRepository local;

    @Parameter(property = "tags")
    private String[] tags;

    @Parameter(property = "headless")
    private boolean headless;

    @Parameter(property = "browser")
    private String browser;

    @Parameter(property = "size")
    private Integer[] size;

    @Parameter(property = "repeat", defaultValue = "0")
    private int repeat;

    @Override
    protected List<String> getCalculatedCommand(String command) throws MojoExecutionException {
        List<String> result = super.getCalculatedCommand(command);
        if (headless) {
            result.add("--headless");
        }
        if (tags != null) {
            for (String tag : tags) {
                appendArgument(result, "--tag", tag);
            }
        }
        appendArgument(result, "--browser", browser);
        if (size != null && size.length != 0) {
            if (size.length == 2) {
                String value = String.format("%dx%d", size[0], size[1]);
                appendArgument(result, "--size", value);
            } else {
                throw new MojoExecutionException(String.format("Size should contain two values but it contains %d", size.length));
            }
        }
        if (repeat > 1) {
            appendArgument(result, "--repeat", Integer.toString(repeat));
        }
        appendArgument(result, "--pid", getPidFile());
        for (String file : getDependencyFiles()) {
            appendArgument(result, "--classpath", file);
        }
        return result;
    }

    private List<String> getDependencyFiles() {
        List<String> result = new ArrayList<>();
        result.add(project.getBuild().getSourceDirectory());
        Set<Artifact> artifacts = project.getArtifacts();
        for (Artifact artifact : artifacts) {
            if (isAddable(artifact.getScope())) {
                result.add(artifact.getFile().getAbsolutePath());
            }
        }
        return result;
    }

    private boolean isAddable(String scope) {
        if (scope.equals("compile")) {
            return true;
        }
        if (scope.equals("runtime")) {
            return true;
        }
        return false;
    }

}
