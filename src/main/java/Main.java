import java.util.*;

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
                    sizeToFreq.notify();
                }
            });

            threadList.add(thread);
        }

        Thread newThread = new Thread(
                () -> {
                    while (!Thread.interrupted()) {
                        synchronized (sizeToFreq) {
                            try {
                                sizeToFreq.wait();
                            } catch (InterruptedException e) {
                                break;
                            }
                            Optional<Map.Entry<Integer, Integer>> entry = sizeToFreq.entrySet()
                                    .stream()
                                    .max(Map.Entry.comparingByValue());

                            if (entry.isPresent()) {
                                Map.Entry<Integer, Integer> maxEntry = entry.get();
                                int maxValue = maxEntry.getValue();
                                int maxKey = maxEntry.getKey();

                                System.out.println("Самое частое количество повторений " + maxKey + " (встретилось " + maxValue + " раз)");
                            }
                        }
                    }
                }
        );

        newThread.start();

        for (Thread thread : threadList) {
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        newThread.interrupt();
        newThread.join();

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
