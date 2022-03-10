package it.sevenpio.pandora.file;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static void writeOutputFile(List<Map.Entry<Integer, Integer>> sortedList, String fileName) {
        try(FileWriter fileWriter = new FileWriter("output-%s".replace("%s", fileName))) {

            for (Map.Entry<Integer, Integer> integerEntry : sortedList) {
                fileWriter.write(integerEntry.getKey() + "\n");
            }
            fileWriter.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static InputStream getFileFromResourceAsStream(String fileName) {

        // The class loader that loaded the class
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return inputStream;
        }

    }

    private String readFromInputStream(InputStream inputStream)
            throws IOException {
        StringBuilder resultStringBuilder = new StringBuilder();
        try (BufferedReader br
                     = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        }
        return resultStringBuilder.toString();
    }
}
