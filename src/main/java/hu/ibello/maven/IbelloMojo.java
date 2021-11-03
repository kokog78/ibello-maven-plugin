package hu.ibello.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executors;

@Mojo(name = "ibello")
public abstract class IbelloMojo extends AbstractMojo {
	
	private final static String PID_FILE = "./ibello/ibello.pid";
	
	@Parameter(defaultValue = "${project}", required = true, readonly = true)
	MavenProject project;
	
	@Parameter(property = "install-dir")
	File installDir;
	
	@Parameter(property = "directory")
	File directory;
	
	@Parameter(property = "arguments")
	File arguments;
	
	@Parameter(property = "language")
	String language;
	
	@Parameter(property = "property")
	String property;
	
	@Parameter(property = "tags")
	List<String>  tags;
	
	protected void runProcess(String command) throws MojoExecutionException, MojoFailureException {
		ProcessBuilder builder = new ProcessBuilder();
		List<String> calculated = getCalculatedCommand(command);
		getLog().info("Running command: " + String.join(" ", calculated));
		builder.command(calculated);
		builder.directory(project.getBasedir());
		builder.redirectErrorStream(true);
		Process process;
		try {
			process = builder.start();
		} catch (IOException ex) {
			throw new MojoExecutionException("Cannot run ibello process", ex);
		}
		StreamGobbler stdout = new StreamGobbler(process.getInputStream(), System.out::println);
		Executors.newSingleThreadExecutor().submit(stdout);
		try {
			int exitCode = process.waitFor();
			if (exitCode > 0) {
				throw new MojoFailureException("Process exited with code " + exitCode);
			}
		} catch (InterruptedException ex) {
			// TODO stop ibello
			ex.printStackTrace();
		}
	}
	
	protected List<String> getCalculatedCommand(String command) throws MojoExecutionException {
		List<String> result = new ArrayList<>();
		// script file
		if (installDir != null) {
			File file = new File(installDir, calculateCommandName());
			result.add(file.getAbsolutePath());
		} else {
			result.add(calculateCommandName());
		}
		// command
		result.add(command);
		// directory
		if (directory != null) {
			appendArgument(result, "--directory", directory.getAbsolutePath());
		}
		// arguments
		if (arguments != null) {
			appendArgument(result, "--arguments", arguments.getAbsolutePath());
		}
		// language
		appendArgument(result, "--language", language);
		return result;
	}
	
	protected void appendArgument(List<String> arguments, String key, String value) {
		if (value != null) {
			value = value.trim();
			if (!value.isEmpty()) {
				arguments.add(key);
				if (value.contains(" ")) {
					value = String.format("\"%s\"", value);
				}
				arguments.add(value);
			}
		}
	}
	
	protected String getPidFile() {
		return PID_FILE;
	}
	
	private String calculateCommandName() {
		if (isWindows()) {
			return "ibello.cmd";
		} else {
			return "ibello";
		}
	}
	
	private boolean isWindows() {
		String os = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
		return os.indexOf("win") >= 0;
	}
}
