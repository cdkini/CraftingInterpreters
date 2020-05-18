package okra.tools;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class GenerateAST {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.out.println("Error: Must pass a directory path to store output.");
            System.exit(-1);
        }
        String dir = args[0];
        defineAST(dir, "Expr", Arrays.asList(
            "Binary : Expr left, Token operator, Expr right",
            "Grouping: Expr expression",
            "Literal: Object value",
            "Unary: Token operator, Expr right"
        ));
    }

    private static void defineAST(String dir, String baseName, List<String> types) throws IOException {
        String path = dir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);

        writer.println("package okra;\n");
        writer.println("import java.util.List;\n");
        writer.println("abstract class " + baseName + " {");

        for (String type: types) {
            String[] typeArr = type.split(":");
            String className = typeArr[0].trim();
            String fields = typeArr[1].trim();
            defineType(writer, baseName, className, fields);
        }

        writer.println("}");
        writer.close();
    }

    private static void defineType(PrintWriter writer, String baseName, String className, String fieldList) {
        writer.println("  static class " + className + " extends " + baseName + " {");

        // Constructor
        writer.println("    " + className + "(" + fieldList + ") {");

        // Store parameters in fields
        String[] fields = fieldList.split(", ");
        for (String field: fields) {
            String name = field.split(" ")[1];
            writer.println("    this." + name + " = " + name + ";");
        }

        writer.println("    }");

        // Fields
        writer.println()
    }

}
