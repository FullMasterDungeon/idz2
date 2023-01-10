import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Введите имя файла");
        String input = sc.nextLine();

        Processor processor = new Processor(input);
        if (processor.isDirectoryExist(input) == false) {
            System.exit(0);
        }

        Graph graph = new Graph(input);

        Set<String> cycle = graph.cycling();

        if (cycle != null) {
            for (String s : cycle) {
                System.out.println(s);
            }
            System.exit(0);
        }

        if (processor.isFileExist(graph.getAllFiles()) == false) {
            System.out.println("Файл не найден");
            System.exit(0);
        }

        List<String> files = graph.sort();
        for (String file : files) {
            System.out.println(file);
        }

        String text = processor.concatFiles(files);
        try {
            processor.fillFile("output.txt", text);
            System.out.println(text);
        } catch (Exception e) {
            System.out.println("Невозможно записать содержимое в файл");
        }
    }
}