package hu.ibello.maven;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.util.List;


@Mojo( name = "test")
public class TestMojo extends IbelloMojo{

    @Parameter(property = "scope")
    String scope;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
//        getLog().info( "Hello, world." );
        List<Dependency> dependencies = project.getDependencies();
//        long numDependencies = dependencies.stream()
//                .filter(d -> (scope == null || scope.isEmpty()) || scope.equals(d.getScope()))
//                .count();
//        getLog().info("Number of dependencies: " + numDependencies);
        dependencies.stream().forEach(dependency -> getLog().info(dependency.getArtifactId()));
    }
}
