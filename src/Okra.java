package src;

import src.parser.Token;
import src.parser.TokenScanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Okra {

    private static boolean hadError = false;

    /**
     * Allows the user to run a file through the interpreter or create an interactive session in the command line.
     * Additionally, there is an option to format files based on the language's stylistic preferences.
     * @param args -> Command line arguments passed by user (either nothing, script name, or fmt and script name)
     * @throws IOException -> If the file or file path passed is not valid
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Error: Must use one of the following: \n" +
                    "1) \"okra [script]\" to run a .okra file \n" +
                    "2) \"okra\" to run an interactive session in the terminal \n" +
                    "3) \"okra fmt [script]\" to format a .okra file");
            System.exit(-1);
        } else if (args.length == 2) {
            if (!getFileExtension(args[0]).equals("fmt") || !getFileExtension(args[1]).equals("src")) {
                System.out.println("Error: Must use \"okra fmt [script]\" to format a .okra file");
                System.exit(-1);
            }
            // TODO: Open to implement formatting tool once completion of basic language / style rules
            System.out.println("fmt not yet implemented!");
        } else if (args.length == 1) {
            if (!getFileExtension(args[0]).equals("src")) {
                System.out.println("Error: Must use .okra file extension");
                System.exit(-1);
            }
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static String getFileExtension(String script) {
        if (script.lastIndexOf(".") != -1 && script.lastIndexOf(".") != 0) {
            return script.substring(script.lastIndexOf(".") + 1);
        }
        return "";
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if (hadError) {
            System.exit(-1);
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while (true) { // Need to use Ctrl + C to escape
            System.out.println("> ");
            run(reader.readLine());
            hadError = false;
        }
    }

    private static void run(String source) {
        TokenScanner tokenScanner = new TokenScanner(source);
        List<Token> tokens = tokenScanner.scanTokens();

        for (Token token: tokens) {
            System.out.println(token);
        }
    }

    /**
     * Generic error handling that reports to the user the location of the error
     * @param line -> Line the error occurred on
     * @param message -> What to return to the user about the error
     */
    public static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
