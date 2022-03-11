package it.sevenpio.pandora.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

        // the stream holding the utils content
        if (inputStream == null) {
            throw new IllegalArgumentException("utils not found! " + fileName);
        } else {
            return inputStream;
        }

    }
}
