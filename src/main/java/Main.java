import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int SIZE_LENGTH = 100;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(SIZE_LENGTH);

        for (int i = 0; i < SIZE_LENGTH; i++) {
            executor.execute(() -> {
                char targetChar = 'R';
                String result = generateRoute("RLRFR", SIZE_LENGTH);
                long count = result.chars()
                        .filter(c -> c == targetChar)
                        .count();
                System.out.println(targetChar + " -> " + count);
            });
        }

        executor.shutdown();

    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }

}
