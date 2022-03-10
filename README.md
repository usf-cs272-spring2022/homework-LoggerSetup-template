LoggerSetup
=================================================

![Points](../../blob/badges/points.svg)

For this homework, you will setup and configure the `log4j2` logging library. The **only thing** you should do for this homework assignment is import the homework and create a `log4j2` configuration file in the correct location with the correct configuration.

## Configuration

The `log4j2` third-party package should be setup automatically by `maven` when you import this homework into Eclipse.

Configure the root logger to output `WARN` messages and higher to the console only (no output to the file). Configure the class-specific `LoggerSetup` logger to output `INFO` messages and higher (more severe) to the console and `ALL` messages to a `debug.log` file in the current working directory. You can use the `log4j2.xml` example from lecture as a starting point.

Only output the message and **short** error message (if appropriate) to the console. The expected console output should look like:

```
Class Logger:
Ibis 
Wren 
Eastern Eagle
Catching Finch

Root Logger:
Catching Finch
```

Include the 2 digit sequence number, level (using only 1 letter), simple class name, line number, thread name, 2 lines from any throwable stack trace (if appropriate), and a newline character (`%n`) in the `debug.log` file. See the `test/resources/debug.log` file for the expected file output. It is also included below:

```
[01T] LoggerSetup #22 main: Toucan 
[02D] LoggerSetup #23 main: Dove 
[03I] LoggerSetup #24 main: Ibis 
[04W] LoggerSetup #25 main: Wren 
[05E] LoggerSetup #26 main: Eastern java.lang.Exception: Eagle
	at edu.usfca.cs272.LoggerSetup.outputMessages(LoggerSetup.java:26)
[06F] LoggerSetup #27 main: Catching java.lang.RuntimeException: Finch
	at edu.usfca.cs272.LoggerSetup.outputMessages(LoggerSetup.java:27)
[07E] LoggerSetup #26 main: Eastern java.lang.Exception: Eagle
	at edu.usfca.cs272.LoggerSetup.outputMessages(LoggerSetup.java:26)
[08F] LoggerSetup #27 main: Catching java.lang.RuntimeException: Finch
	at edu.usfca.cs272.LoggerSetup.outputMessages(LoggerSetup.java:27)
```

You should **NOT** modify the `LoggerSetup.java`, `LoggerSetupTest.java`, or `test/debug.log` files. You should only create a **NEW** file in the correct location to configure log4j2.

## Hints ##

Below are some hints that may help with this homework assignment:

  - The `log4j2.xml` file in the [lecture examples](https://github.com/usf-cs272-spring2022/lectures/) is a good starting point.

  - For configuring the output locations (where to output, file versus console), take a look at the [ConsoleAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#ConsoleAppender) and [FileAppender](https://logging.apache.org/log4j/2.x/manual/appenders.html#FileAppender) information pages.

  - For configuring the output format (what to output), I recommend you take a look at the [PatternLayout](https://logging.apache.org/log4j/2.x/manual/layouts.html#PatternLayout) information page. It includes all of the possible patterns, like `class`, `date`, `throwable` :star:, `file`, `location`, `line`, `message`, `method`, `n`, `level`, `sequenceNumber`, `threadId`, and `threadName` (you will only use some of these).

  - **Do NOT overwrite the `test/debug.log` file. You should configure log4j2 to write to the path `debug.log` instead.**

These hints are *optional*. There may be multiple approaches to solving this homework.

## Requirements ##

See the Javadoc and `TODO` comments in the template code in the `src/main/java` directory for additional details. You must pass the tests provided in the `src/test/java` directory. Do not modify any of the files in the `src/test` directory.

See the [Homework Guides](https://usf-cs272-spring2022.github.io/guides/homework/) for additional details on homework requirements and submission.
