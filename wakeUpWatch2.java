import java.util.*;
import java.util.concurrent.TimeUnit;

public class wakeUpWatch2 {

    public static void main(String[] args) {
        deploy();

    }

    //CUSTOM VALUES:
    //to control simulation
    static int timeout = 0; //for sequence progression
    static double variability = 0.05; //how variable your heart rate is
    static double arbitraryNormal = 70;
    static double trendConstant =  1; //how much current wakefulness affects future wakefullness
    static double trendCap = 0.10;

    //to control monitoring, waking
    static int listCap = 50000; //how many values to store in memory (accuracy of avg)
    static double threshold = 0.85; //what percent heart rate drop to buzz at


    static ArrayList<Double> list = new ArrayList<Double>();
    static double delay;
    static double currentRate;
    static double averageRate;

    public static void deploy() {
        int count = 0;

        while (true) {
            count++;
            try {
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //generate random heartbeat delays
            delay = Math.pow(Math.random(), variability) * 60.0 / (0.94 * arbitraryNormal); //0.94 is for math adjustment

            //collect rates
            list.add((delay));
            if (list.size() > listCap) {
                list.remove(0);
            }

            //find current and average rates to print and use for buzz
            if (list.size() > 5) {
                System.out.println("beat #: " + (count - 5));

                currentRate = 60 / ((list.get(list.size() - 1)
                        + list.get(list.size() - 2)
                        + list.get(list.size() - 3)
                        + list.get(list.size() - 4)
                        + list.get(list.size() - 5)) / 5);
                //the below lines simulate trends.
                // Awakeness encourages awakeness, sleepiness encourages sleepiness, up until certain point.
                if (Math.abs(currentRate - averageRate) < trendCap * averageRate) {
                    currentRate += trendConstant * (currentRate - averageRate);
                }

                currentRate = ((double) (int) (currentRate * 10)) / 10; //rounds to tenths place
                System.out.println("current BPM: " + currentRate);

                double sum = 0;
                for (int i = 0; i < list.size(); i++) { //even the average rate only looks at past 30 beats
                    sum += list.get(i);
                }
                averageRate = 60 / (sum / list.size());

                System.out.println("average BPM: " + averageRate);
                System.out.println();
            } else {
                System.out.println("Initializing BPM...");
            }

            if (list.size() > 15) { //so that it doesn't activate right at the beginning
                buzz(currentRate, averageRate, count);
            }


        }

    }

    public static void buzz(double currBPM, double avgBPM, int countBPM) {
        if (currBPM < threshold * avgBPM) {
            System.out.println("Wake up.");
            System.out.println("You almost fell asleep after about " + (int) ((5 + countBPM)/avgBPM) + " minutes.");

            System.exit(0); //normal termination
        }
    }

}