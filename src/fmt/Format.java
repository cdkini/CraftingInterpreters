package src.fmt;

import java.io.File;
import java.io.IOException;

public class Format {

    public static void fmt(File path) {
        for (File file: path.listFiles()) {
            if (file.isDirectory()) {
                fmt(file);
            }
            // TODO: Open to add actual formatting methods
        }
    }
}
