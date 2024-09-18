package utilities;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

public class ReportManager {

    private static final String REPORT_FOLDER = "test-output/";

    public static void cleanOldReports(int maxReports) {
        try {
            // Get the report folder as a File object
            File folder = new File(REPORT_FOLDER);

            // List all the files and directories in the folder, sorted by last modified date
            File[] files = folder.listFiles();
            if (files != null && files.length > maxReports) {
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

                // Keep the most recent 'maxReports' and delete the rest
                for (int i = maxReports; i < files.length; i++) {
                    // Delete files and directories recursively
                    deleteRecursively(files[i].toPath());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to delete a file or directory recursively
    private static void deleteRecursively(Path path) throws Exception {
        if (Files.isDirectory(path)) {
            // Delete all files in the directory first
            Files.walk(path)
                 .sorted(Comparator.reverseOrder())
                 .forEach(p -> {
                     try {
                         Files.delete(p);
                     } catch (Exception e) {
                         e.printStackTrace();
                     }
                 });
        } else {
            // Delete the file directly
            Files.deleteIfExists(path);
        }
    }

}
