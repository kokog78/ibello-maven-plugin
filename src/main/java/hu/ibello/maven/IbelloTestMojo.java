package hu.ibello.maven;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResolutionException;
import org.eclipse.aether.resolution.ArtifactResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class IbelloTestMojo extends IbelloMojo{

    @Parameter(defaultValue = "${localRepository}", readonly = true, required = true)
    private ArtifactRepository local;

    @Component
    private RepositorySystem repoSystem;

    @Parameter(defaultValue = "${repositorySystemSession}", readonly = true, required = true)
    private RepositorySystemSession repoSession;

    @Parameter(defaultValue = "${project.remoteProjectRepositories}", readonly = true, required = true)
    private List<RemoteRepository> repositories;

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
        getDependencyFiles();
        for (String file : getDependencyFiles()) {
            appendArgument(result, "--classpath", file);
        }
        return result;
    }

    private List<String> getDependencyFiles() throws MojoExecutionException {
        List<String> result = new ArrayList<>();
        result.add(project.getBuild().getSourceDirectory());
        Set<Artifact> artifacts = project.getDependencyArtifacts();
        for (Artifact unresolvedArtifact : artifacts) {
            String artifactId = unresolvedArtifact.getArtifactId();
            File file = getResolvedArtifact(unresolvedArtifact);
            if( file == null || ! file.exists()) {
                getLog().warn("Artifact " + artifactId + " has no attached file. Its content will not be copied in the target model directory.");
                continue;
            }
            if (isAddable(unresolvedArtifact.getScope())) {
                result.add(file.getAbsolutePath());
            }
        }
        return result;
    }

    private File getResolvedArtifact (Artifact unresolvedArtifact) throws MojoExecutionException {
        String artifactId = unresolvedArtifact.getArtifactId();
        org.eclipse.aether.artifact.Artifact aetherArtifact = new DefaultArtifact(
                unresolvedArtifact.getGroupId(),
                unresolvedArtifact.getArtifactId(),
                unresolvedArtifact.getClassifier(),
                unresolvedArtifact.getType(),
                unresolvedArtifact.getVersion());

        ArtifactRequest req = new ArtifactRequest().setRepositories(repositories).setArtifact(aetherArtifact);
        ArtifactResult resolutionResult;
        try {
            resolutionResult = repoSystem.resolveArtifact(repoSession, req);
        } catch(ArtifactResolutionException e) {
            throw new MojoExecutionException("Artifact " + artifactId + " could not be resolved", e);
        }
        return resolutionResult.getArtifact().getFile();
    }

    private boolean isAddable(String scope) {
        // TODO melyik scopnál kell?
        if (scope.equals("compile")) {
            return true;
        }
        if (scope.equals("provided")) {
            return true;
        }
        if (scope.equals("runtime")) {
            return true;
        }
        if (scope.equals("test")) {
            return false;
        }
        if (scope.equals("system")) {
            return true;
        }
        if (scope.equals("import")) {
            return true;
        }
        return false;
    }

}
