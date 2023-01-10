import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class Processor {
    private final String directoryPath;

    public Processor(String directory) {
        this.directoryPath = directory;
    }

    /**
     * Объединяет содержимое файлов
     *
     * @param files лист с файлами для конкатенации
     */
    public String concatFiles(Collection<String> files) {
        StringBuilder content = new StringBuilder();
        for (String filePath : files) {
            try (BufferedReader br = new BufferedReader(new FileReader(directoryPath + '/' + filePath))) {
                String line;
                content.append(filePath).append("\n");
                while ((line = br.readLine()) != null) {
                    content.append(line).append('\n');
                }
                content.append("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content.toString();
    }

    /**
     * Запись содержимого в файл
     *
     * @param content content to write
     * @param path    путь до файла
     */
    public void fillFile(String path, String content) throws IOException {
        FileWriter writer = new FileWriter(path);
        writer.write(content);
        writer.close();
    }

    /**
     * Проверка на существование папки
     */
    public boolean isDirectoryExist(String directoryPath) {
        File dir = new File(directoryPath);
        return dir.exists() && dir.isDirectory();
    }

    /**
     * Проверка на существование файла
     */
    public boolean isFileExist(Collection<String> filePaths) {
        for (String filePath : filePaths) {
            File file = new File(directoryPath + '/' + filePath);
            if (!file.exists()) {
                return false;
            }
        }
        return true;
    }
}