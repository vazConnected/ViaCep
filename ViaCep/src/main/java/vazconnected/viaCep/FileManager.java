package vazconnected.viaCep;

import java.io.*;

public final class FileManager {
    public static void writeFile(String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.write(content);
        fileWriter.close();
    }

    public static void appendToFile(String path, String content) throws IOException {
        FileWriter fileWriter = new FileWriter(path);
        fileWriter.append(content);
        fileWriter.close();
    }

    public static String readFile(String path) throws FileNotFoundException {
        BufferedReader bufferedReader = new BufferedReader( new FileReader(path) );

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
}
