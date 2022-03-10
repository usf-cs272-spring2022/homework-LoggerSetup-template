package edu.usfca.cs272;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.MethodName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Runs a couple of tests to make sure Log4j2 is setup.
 *
 * NOTE: There are better ways to test log configuration---we will keep it
 * simple here because we just want to make sure you can run and configure
 * Log4j2.
 *
 * This is also not the most informative configuration---it is just one of the
 * most testable ones that require you to learn about how to handle stack trace
 * output.
 *
 * @author CS 272 Software Development (University of San Francisco)
 * @version Spring 2022
 */
@TestMethodOrder(MethodName.class)
public class LoggerSetupTest {
	/** Used to capture console output. */
	private static List<String> captured = null;

	/** Tracks where the root output starts. */
	private static int rootStart = -1;

	/** Path to expected debug.log file. */
	private static Path debug = Path.of("src", "test", "resources", "debug.txt");

	/**
	 * Setup that runs before each test.
	 *
	 * @throws IOException if an I/O error occurs
	 */
	@BeforeAll
	public static void setup() throws IOException {
		// delete any old debug files
		Files.deleteIfExists(Path.of("debug.log"));

		// capture all system console output
		PrintStream original = System.out;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		System.setOut(new PrintStream(output));

		// run main() only ONCE to avoid duplicate entries
		// and shutdown log manager to flush the debug files
		LoggerSetup.main(null);
		LogManager.shutdown();

		// restore system.out
		System.setOut(original);

		// save result and output to console
		captured = output.toString().lines().map(String::strip).toList();
		rootStart = Collections.indexOfSubList(captured, List.of("Root Logger:"));

		captured.forEach(System.out::println);
	}

	/**
	 * Tests configuration file location and name.
	 */
	@Nested
	public class A_ConfigFileTests {
		/**
		 * Make sure you are not using the log4j2-test.* name. That is the config
		 * file name used for test code not main code.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testNotTest() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2-test\\.\\w+"))
					.toList();

			assertEquals(Collections.emptyList(), found, "Do not use this filename to configure logging of main code.");
		}

		/**
		 * Make sure you are using the correct file name.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testNameCorrect() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2\\.\\w+"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file with correct name.");
		}

		/**
		 * Make sure you are using the correct location for the configuration file.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testLocationCorrect() throws IOException {
			var found = Files.walk(Path.of("src", "main", "resources"))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("log4j2.*\\.\\w+"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file in the correct location.");
		}

		/**
		 * Make sure you are using the correct extension for the configuration file.
		 *
		 * @throws IOException if IO error occurs
		 */
		@Test
		public void testExtensionCorrect() throws IOException {
			var found = Files.walk(Path.of("."))
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.filter(name -> name.matches("(?i)log4j2.*\\.(properties|ya?ml|jso?n|xml)"))
					.toList();

			assertTrue(!found.isEmpty(), "Could not find configuration file with the correct extension.");
		}
	}

	/**
	 * Tests Root logger console output.
	 */
	@Nested
	public class B_RootConsoleTests {
		/**
		 * Tests the root logger console output and compares to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testConsole() throws IOException {
			if (rootStart < 0) {
				fail("Could not find root logger console output.");
			}

			String actual = String.join("\n", captured.subList(rootStart + 1, captured.size()));
			String expected = "Catching Finch";

			assertEquals(expected, actual);
		}
	}

	/**
	 * Tests LoggerSetup logger console output.
	 */
	@Nested
	public class C_LoggerConsoleTests {
		/**
		 * Captures the console output and compares to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testConsole() throws IOException {
			if (rootStart < 0) {
				fail("Could not find root logger console output.");
			}

			String actual = String.join("\n", captured.subList(1, rootStart));
			String expected = """
					Ibis
					Wren
					Eastern Eagle
					Catching Finch
					""";

			assertEquals(expected, actual);
		}
	}

	/**
	 * Tests part of file output.
	 */
	@Nested
	public class D_FileNormalTests {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// only test the non-exception output from files
			String expected = Files.lines(debug, UTF_8)
					.map(String::strip)
					.limit(4)
					.collect(Collectors.joining("\n"));

			String actual = Files.lines(Path.of("debug.log"), UTF_8)
					.map(String::strip)
					.limit(4)
					.collect(Collectors.joining("\n"));

			assertEquals(expected, actual, "Compare debug.log and test/debug.txt in Eclipse.");
		}
	}

	/**
	 * Tests part of file output.
	 */
	@Nested
	public class E_FileExceptionCountTests {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// only test the exception output from files
			long expected = Files.lines(debug, UTF_8)
					.map(String::strip)
					.skip(4)
					.count();

			long actual = Files.lines(Path.of("debug.log"), UTF_8)
					.map(String::strip)
					.skip(4)
					.count();

			assertEquals(expected, actual, "Unexpected number of lines in debug.log " +
					"associated with exception output. Use F tests to debug or compare " +
					"debug.log and test/debug.txt in Eclipse.");
		}
	}

	/**
	 * Tests part of file output.
	 */
	@Nested
	public class F_FileExceptionTests {
		/**
		 * Open the debug.log file as a list and compare to expected.
		 *
		 * @throws IOException if an I/O error occurs
		 */
		@Test
		public void testFile() throws IOException {
			// only test the exception output from files
			String expected = Files.lines(debug, UTF_8)
					.map(String::strip)
					.skip(4)
					.collect(Collectors.joining("\n"));

			String actual = Files.lines(Path.of("debug.log"), UTF_8)
					.map(String::strip)
					.skip(4)
					.collect(Collectors.joining("\n"));

			assertEquals(expected, actual, "Compare debug.log and test/debug.txt in Eclipse.");
		}
	}
}
