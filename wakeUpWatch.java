//feeds a heartrate every second, decides whether to buzz


import java.util.*;
import java.util.concurrent.TimeUnit;

public class wakeUpWatch {
    public static void main(String[] args) {
        beginRate();
    }


    public static void beginRate() {
        double beat;
        while (true) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //generates relatively stable heart rate, with variations
            beat = Math.pow(Math.random(), 0.1) * 80;
            System.out.println("beat: " + beat);

            buzz(beat);
        }
    }

    public static void buzz(double heartRate) {
        if (heartRate < 68) {
            System.out.println("Wake up.");
        }
    }
}
