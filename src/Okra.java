package src;

import src.fmt.Format;
import src.parser.Token;
import src.parser.TokenScanner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Okra {

    private static boolean hadError = false;

    /**
     * Allows the user to run a file through the interpreter.
     * Additionally, there is an option to format files based on the language's stylistic preferences.
     *
     * @param args -> Command line arguments passed by user (either script name, or fmt and script name)
     * @throws IOException -> If the file or file path passed is not valid
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 2) {
            if (!args[0].equals("fmt") || !isValidFileExtension(args[1])) {
                System.out.println("Error: Must use \"okra fmt [script]\" to format an .okra file");
                System.exit(-1);
            }
            if (!isValidFilePath(args[1])) {
                System.out.println("Error: Not a valid file path.");
                System.exit(-1);
            }
            Format.fmt(new File(args[1]));
        } else if (args.length == 1) {
            if (!isValidFileExtension(args[0])) {
                System.out.println("Error: Must use .okra file extension");
                System.exit(-1);
            }
            if (!isValidFilePath(args[0])) {
                System.out.println("Error: Not a valid file path.");
                System.exit(-1);
            }
            runFile(args[0]);
        } else {
            System.out.println("Error: Must use one of the following: \n" +
                    "1) \"okra [script]\" to run an .okra file \n" +
                    "2) \"okra fmt [script]\" to format an .okra file");
            System.exit(-1);
        }
    }

    private static boolean isValidFilePath(String path) {
        File f = new File(path);
        try {
            f.getCanonicalPath();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static boolean isValidFileExtension(String script) {
        if (script.lastIndexOf(".") != -1 && script.lastIndexOf(".") != 0) {
            return script.substring(script.lastIndexOf(".") + 1).equals("okra");
        }
        return false;
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        TokenScanner tokenScanner = new TokenScanner(new String(bytes, Charset.defaultCharset()));
        List<Token> tokens = tokenScanner.scanTokens();

        for (Token token : tokens) {
            System.out.println(token);
        }

        if (hadError) {
            System.exit(-1);
        }
    }

    /**
     * Generic error handling that reports to the user the location of the error
     *
     * @param line    -> Line the error occurred on
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
