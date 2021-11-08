# ibello-maven-plugin

This maven plugin helps us to execute and manage ibello tests. To read more about ibello, please visit

https://github.com/kokog78/ibello-api

and

https://ibello.eu

Plugin version: 1.0. The ibello version should greater than or equal to 1.13.0. 

## Configuration

In the `pom.xml` file, you need to add these lines:

```xml
<dependency>
	<groupId>hu.ibello</groupId>
	<artifactId>ibello-maven-plugin</artifactId>
	<version>1.0</version>
</dependency>
```

## General parameters

You have a top-level configuration block for general parameters:

```xml
<plugin>
	<groupId>hu.ibello</groupId>
	<artifactId>ibello-maven-plugin</artifactId>
	<version>1.0</version>
		<configuration>
            <installDir>/path/to/my/ibello/installation</installDir>
            <language>hu</language>
            <directory>/path/to/my/project</directory>
    </configuration>
</plugin>
```

A few words about these parameters:

- `installDir`: you need to specify a file here which points to the ibello installation directory (where the `ibello.sh` and `ibello.cmd` scripts are). You can skip this if you added the ibello to the PATH of your operating system.
- `directory`: you need to specify a file here which points to the ibello project (where the `ibello` folder is). You can skip this and the plugin will use the current working directory.
- `language`: language code, really optional. If you do not specify it then the default 'en' will be used.

## Goals

### General goals

Many goal types are available in the plugin. Each of them has some common properties:

- `directory`: same as in the global settings, you just can specify the ibello project for each tasks if you want.
- `argumentsFile`: should be a file, ibello will load it's arguments from this file. You can read more about it in the documentation: https://ibello.eu/documentation-cli.

Here are the goal types:

- `version`: just prints the ibello version.
- `help`: prints the help message.
- `clean`: removes old test results from the project. It has a parameter:
  - `keep`: how many test results you want to keep? The newest ones will survive.
- `cleanAll`: removes all test results from the project.
- `update`: updates webdrivers. If you installed ibello as administrator then you need to have administrative privileges to run goal with this type. It has 2 additional parameters:
  - `browser`: the type of the browser, it can be: `CHROME`, `FIREFOX`, `OPERA`, `EDGE`. The plugin defines these variable for you. If you do not specify it, all available webdrivers will be updated.
  - `remove`: set it to `true` if you want to remove unused webdrivers.
- `updateAll`: updates all available webdrivers and removes old ones. If you installed ibello as administrator then you need to have administrative privileges to run this goal.
- `stop`: stops all tests started from the same ibello directory.
- `docgen`: documentation generation with ibello. You can read more about it here: https://ibello.eu/documentation-document-generation. Additional parameters:
  - `inputFile`: the input file (as a `File` instance).
  - `outputFile`: the output file (as a `File` instance). Optional, if you do not specify it then ibello will find out automatically, and the generated documentation will be in the `ibello/output` folder.
  - `overwrite`: a boolean parameter, you need to specify it if you want to forcefully overwrite existing files.

Examples:

This goal removes old test results, only the latest 20 will be available.

```xml
<plugin>
    <groupId>hu.ibello</groupId>
	<artifactId>ibello-maven-plugin</artifactId>
	<version>1.0</version>
    <executions>
        <execution>
            <id>my-clean</id>
            <goals>
                <goal>clean</goal>
            </goals>
            <configuration>
                <keep>20</keep>
            </configuration>
        </execution>
    </executions>
</plugin>
```

This goal generates documentation:

```xml
<plugin>
    <groupId>hu.ibello</groupId>
	<artifactId>ibello-maven-plugin</artifactId>
	<version>1.0</version>
    <executions>
        <execution>
            <id>my-docgen</id>
            <goals>
                <goal>docgen</goal>
            </goals>
            <configuration>
                <inputFile>ibello/docs/project.json</inputFile>
                <overwrite>true</overwrite>
            </configuration>
        </execution>
    </executions>
</plugin>
```

### Testing goals

The goals below are running tests and they have some extra parameters. These ones:

- `tags`: list of ibello tags. Read more about tags in the documentation: https://ibello.eu/documentation-configuration.
- `browser`: the type of the browser you want to use. It's value can be: `CHROME`, `FIREFOX`, `OPERA`, `EDGE`. The plugin defines these variable for you.
- `headless`: a boolean parameter, if you want to run your tests in headless mode (without a browser window), you need to set it to `true`.
- `size`: default size of the browser window (as a numeric list), for example "1024, 768".
- `repeat`: repeat number of your tests, an integer value.

The task types are:

- `run`: starts a java test. It has one more parameter:
  - `java`: you can specify the class/package names of you specifications, for example "\*Smoke\*". The "\*" character is a wildchar.
- `cucumber`: starts a cucumber test. It has 2 additional parameters:
  - `java`: you can specify the java package name of your glue-code here as a string.
  - `featuresDir`: you can specify the directory of your feature files if it is not the default one (the `features` subdirectory of you project).

The testing goals are always using the classpath and dependencies of the current maven project. If the `directory` parameter is specified then only the non-classpath related files (ibello configurations, feature files, test data files, ...) are used from that directory. If there are java classes and other dependencies in that referenced directory then they also should be defined in the current maven project.

Example:

```xml
<plugin>
	<groupId>hu.ibello</groupId>
	<artifactId>ibello-maven-plugin</artifactId>
	<version>1.0</version>
    <executions>
    	<execution>
        	<id>mySmokeTest</id>
        	<goals>
        		<goal>run</goal>
        	</goals>
            <configuration>
            	<headless>true</headless>
                <browser>CHROME</browser>
                <tags>
                    <param>one</param>
                    <param>two</param>
                    <param>three</param>
                </tags>
                <java>*Smoke*</java>
            </configuration>
        </execution>
    </executions>
</plugin>
```

Dependencies marked with the following scopes are used by the tests directly:

- compile
- runtime

Dependencies marked with the following scopes are not used by the tests directly:

- provided
- test
- system
- import

This is the reason why the `ibello-api` dependency - which is provided by the ibello installation - should be added with `provided` scope to the project. For example:

```xml
<dependency>
	<groupId>hu.ibello</groupId>
	<artifactId>ibello-api</artifactId>
	<version>1.13.0</version>
    <scope>provided</scope>
</dependency>
```

