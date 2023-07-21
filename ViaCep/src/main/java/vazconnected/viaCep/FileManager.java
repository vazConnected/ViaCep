package vazconnected.viaCep;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class FileManager {
    private FileManager() {}

    public static void writeFile(String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static String readFile(File file) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader(file) );
        String fileContent = "";

        String line = null;
        do {
            try {
                line = bufferedReader.readLine();
            } catch (IOException e) {
                line = null;
            }

            if (line != null) {
                fileContent = fileContent + line;
            }
        } while (line != null);

        return fileContent;
    }

    public static List<File> getListOfFiles(String directory, Pattern namePattern) {
        List<File> files = new ArrayList<File>();

        File folder = new File(directory);
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (namePattern != null) {
                    boolean isACepFile = namePattern.matcher(file.getName()).matches();

                    if (isACepFile) {
                        files.add(file);
                    }
                } else {
                    files.add(file);
                }
            }
        }

        return files;
    }

}
