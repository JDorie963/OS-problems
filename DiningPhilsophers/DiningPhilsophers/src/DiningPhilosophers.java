
import java.util.concurrent.Semaphore;
import java.util.Random;

public class DiningPhilosophers {

    private static final int NUM_PHILOSOPHERS = 5;
    private static Semaphore[] forks = new Semaphore[NUM_PHILOSOPHERS];
    private static Semaphore maxDiners; //Limit concurrent diners to prevent deadlock


    public static void main(String[] args) {
        // Initialize semaphore (1 fork each, initially available)
        for(int i=0; i< NUM_PHILOSOPHERS; i++){
            forks[i] = new Semaphore(1);
        }
        // Allow only N-1 philosophers to dine concurrently
        maxDiners = new Semaphore(NUM_PHILOSOPHERS - 1);

        // Create and start philosopher threads
        for (int i = 0; i<NUM_PHILOSOPHERS; i++) {
            new Philosopher(i).start();
        }
    } //end main

    static class Philosopher extends Thread {
        private int id;
        private Random random = new Random();

        public Philosopher(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            while (true) {
                think();
                try {
                    maxDiners.acquire(); // Try to enter dining
                    pickUpForks(id);
                    eat();
                    putDownForks(id);
                    maxDiners.release(); // Leave dining
                } catch (InterruptedException e) {
                    System.err.println("Philosopher " + id + "interrupted.");
                }
            }
        }

        private void pickUpForks(int philosopherId) throws InterruptedException {
            int leftFork = philosopherId;
            int rightFork = (philosopherId + 1) % NUM_PHILOSOPHERS;

            // Acquire forks in a fixed order to prevent deadlock
            if (philosopherId % 2 == 0) {
                forks[leftFork].acquire();
                forks[rightFork].acquire();
            } else {
                forks[rightFork].acquire();
                forks[leftFork].acquire();
            }

            System.out.println("Philosopher " + philosopherId + "is holding forks " + leftFork + " and " + rightFork);
        }

        private void putDownForks(int philosopherId){
            int leftFork = philosopherId;
            int rightFork = (philosopherId + 1) % NUM_PHILOSOPHERS;

            forks[leftFork].release();
            forks[rightFork].release();

            System.out.println("Philosopher " + philosopherId + " put down forks " + leftFork + " and " + rightFork);
        }

        private void think(){
            try{
                int thinkTime = random.nextInt(500); // Time below 500ms
                System.out.println("Philosopher "+ id + " is thinking for " + thinkTime + "ms...");
                Thread.sleep(thinkTime);
            }catch(InterruptedException e){
                System.err.println("Philosopher " + id + " interrupted while thinking.");
            }
        }

        private void eat(){
            try{
                int eatTime = random.nextInt(500); // Time below 500ms
                System.out.println("Philosopher "+ id + " is eating for " + eatTime + "ms...");
                Thread.sleep(eatTime);
            }catch(InterruptedException e){
                System.err.println("Philosopher " + id + " interrupted while eating.");
            }
        }
    }
}




























