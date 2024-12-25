import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

    public static void main(String[] args) {

        System.out.println("Програма виконується. Введіть будь-що для завершення...");

        scheduleMessageTask();
        scheduleCounterTask();

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        executor.shutdownNow();
        System.out.println("Програму завершено.");
    }

    private static void scheduleMessageTask() {
        executor.scheduleAtFixedRate(() -> {
            System.out.println("5 секунд від запуску програми");
        }, 0, 5, TimeUnit.SECONDS);
    }

    private static void scheduleCounterTask() {
        executor.scheduleAtFixedRate(() -> {
            File file = new File("src/counter.txt");
            int currentCount = 0;

            if (file.exists()) {
                try {
                    String content = new String(Files.readAllBytes(file.toPath())).trim();
                    if (!content.isEmpty()) {
                        currentCount = Integer.parseInt(content);
                    }
                } catch (IOException | NumberFormatException e) {
                    System.err.println("Не вдалося прочитати файл: " + e.getMessage());
                }
            }

            currentCount += 5;

            try (FileWriter writer = new FileWriter(file, false)) {
                writer.write(String.valueOf(currentCount));
                writer.flush();
            } catch (IOException e) {
                System.err.println("Не вдалося записати у файл: " + e.getMessage());
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

}
