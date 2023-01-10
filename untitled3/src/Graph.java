import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Graph {
    private final Map<String, List<String>> dependencies;

    private final String directoryPath;

    public Graph(String directory) {
        dependencies = new HashMap<>();
        directoryPath = directory;
        read(directory);
    }

    /**
     * Возвращает все файлы в папке
     */

    public Set<String> getAllFiles() {
        Set<String> files = new HashSet<>();
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            files.add(entry.getKey());
            files.addAll(entry.getValue());
        }
        return files;
    }


    /**
     * Чтение всех фалов из папки
     *
     * @param directory путь до папки
     */
    private void read(String directory) {
        File dir = new File(directory);
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    read(file.getAbsolutePath());
                } else {
                    readFile(file);
                }
            }
        }
    }

    /**
     * Чтение содержимого фала
     *
     * @param file путь до файла
     */
    private void readFile(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> fileDependencies = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("require '")) {
                    String dependency = line.substring("require '".length()).trim();
                    fileDependencies.add(dependency.substring(0, dependency.length() - 1));
                }
            }
            Path dirPath = Paths.get(this.directoryPath);
            Path filePath = Paths.get(file.getAbsolutePath());
            Path relativePath = dirPath.relativize(filePath);
            dependencies.put(relativePath.toString(), fileDependencies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * возвращает отсортерованный лист файлов
     */
    public List<String> sort() {
        Set<String> visited = new HashSet<>();
        List<String> files = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            String file = entry.getKey();
            List<String> fileDependencies = entry.getValue();
            findDependencies(file, fileDependencies, visited, files);
        }
        return files;
    }

    /**
     * Возвращает set файлов
     */
    public Set<String> cycling() {
        for (Map.Entry<String, List<String>> entry : dependencies.entrySet()) {
            String file = entry.getKey();
            Set<String> visited = new HashSet<>();
            if (cycling(file, visited) != null) {
                return visited;
            }
        }
        return null;
    }

    private Set<String> cycling(String file, Set<String> visited) {
        if (visited.contains(file)) {
            return visited;
        }
        visited.add(file);
        List<String> fileDependencies = dependencies.get(file);
        if (fileDependencies != null) {
            for (String dependency : fileDependencies) {
                if (cycling(dependency, visited) != null) {
                    return visited;
                }
                visited.clear();
                visited.add(file);
            }
        }
        return null;
    }

    /**
     * Ищет зависимость внутри фалов
     *
     * @param dependencies список зависимостей для файла
     * @param files        отсортированный лист файлов
     */
    private void findDependencies(String file, List<String> dependencies, Set<String> visited, List<String> files) {
        if (visited.contains(file)) {
            return;
        }
        visited.add(file);
        for (String dependency : dependencies) {
            findDependencies(dependency, this.dependencies.get(dependency), visited, files);
        }
        files.add(file);
    }
}