import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();
    public static final int SIZE_LENGTH = 100;

    public static void main(String[] args) throws InterruptedException {

        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < SIZE_LENGTH; i++) {
            Thread thread = new Thread(() -> {
                char targetChar = 'R';
                String result = generateRoute("RLRFR", SIZE_LENGTH);

                int count = (int) result.chars()
                        .filter(c -> c == targetChar)
                        .count();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(count)) {
                        sizeToFreq.put(count, sizeToFreq.get(count) + 1);
                    } else {
                        sizeToFreq.put(count, 1);
                    }
                }
            });

            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        Optional<Map.Entry<Integer, Integer>> entry = sizeToFreq.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue());

        if (entry.isPresent()) {
            Map.Entry<Integer, Integer> maxEntry = entry.get();
            int maxValue = maxEntry.getValue();
            int maxKey = maxEntry.getKey();

            System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxValue + " раз)");
            System.out.println("Другие размеры:");

            sizeToFreq.remove(maxKey);

            sizeToFreq.forEach((key, value) -> {
                System.out.println("- " + key + "(" + value + " раз)");
            });
        }

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
