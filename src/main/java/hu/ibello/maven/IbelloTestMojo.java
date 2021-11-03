package hu.ibello.maven;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.util.List;

public abstract class IbelloTestMojo extends IbelloMojo{

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
        if (size != null) {
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
//        for (File file : getDependencyFiles()) {
//            appendArgument(result, "--classpath", file.getAbsolutePath());
//        }
        return result;
    }

    private List<File> getDependencyFiles() {
//        List<File> result = new ArrayList<>();
//        List<Configuration> configurations = getConfigurations();
//        for (Configuration config : configurations) {
//            Set<File> files = config.getFiles();
//            if (files != null) {
//                for (File file : files) {
//                    if (file.getName().startsWith("ibello")) {
//                        // skip this file
//                    } else if (!result.contains(file)) {
//                        result.add(file);
//                    }
//                }
//            }
//        }
//        SourceSet sourceSet = getMainSourceSet();
//        if (sourceSet != null && sourceSet.getOutput() != null) {
//            FileCollection files = sourceSet.getOutput().getClassesDirs();
//            if (files != null) {
//                for (File dir : files) {
//                    result.add(dir);
//                }
//            }
//            if (sourceSet.getOutput().getResourcesDir() != null) {
//                result.add(sourceSet.getOutput().getResourcesDir());
//            }
//        }
//        Collections.sort(result);
//        return result;
        return null;
    }

//    private List<Configuration> getConfigurations() {
//        List<Configuration> result = new ArrayList<>();
//        ConfigurationContainer configurations = getProject().getConfigurations();
//        if (configurations != null) {
//            Configuration config = getResolvedConfiguration(configurations, "runtime");
//            if (config != null) {
//                result.add(config);
//            }
//            config = getResolvedConfiguration(configurations, "runtimeClasspath");
//            if (config != null) {
//                result.add(config);
//            }
//            config = getResolvedConfiguration(configurations, "implementation");
//            if (config != null) {
//                result.add(config);
//            }
//            config = getResolvedConfiguration(configurations, "default");
//            if (config != null) {
//                result.add(config);
//            }
//        }
//        return result;
//    }
//
//    private Configuration getResolvedConfiguration(ConfigurationContainer configurations, String name) {
//        Configuration config = configurations.getByName(name);
//        if (config != null && config.isCanBeResolved()) {
//            return config;
//        } else {
//            return null;
//        }
//    }

//    private SourceSet getMainSourceSet() {
//        JavaPluginConvention java = getProject().getConvention().getPlugin(JavaPluginConvention.class);
//        if (java != null) {
//            return java.getSourceSets().getByName("main");
//        }
//        return null;
//    }

}
