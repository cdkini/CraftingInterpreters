package rhubarb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Rhubarb {

    private static boolean hadError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Error: Must use one of the following: \n" +
                    "1) \"rhubarb [script]\" to run a .rhu file \n" +
                    "2) \"rhubarb\" to run an interactive session in the terminal \n" +
                    "3) \"rhubarb fmt [script]\" to format a .rhu file");
            System.exit(-1);
        } else if (args.length == 2) {
            if (!getFileExtension(args[0]).equals("fmt") || !getFileExtension(args[1]).equals("rhu")) {
                System.out.println("Error: Must use \"rhubarb fmt [script]\" to format a .rhu file");
                System.exit(-1);
            }
            // TODO: Open to implement formatting tool once completion of basic language / style rules
            System.out.println("fmt not yet implemented!");
        } else if (args.length == 1) {
            if (!getFileExtension(args[0]).equals("rhu")) {
                System.out.println("Error: Must use .rhu file extension");
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

    static void error(int line, String message) {
        report(line, "", message);
    }

    private static void report(int line, String where, String message) {
        System.err.println(
                "[line " + line + "] Error" + where + ": " + message);
        hadError = true;
    }
}
