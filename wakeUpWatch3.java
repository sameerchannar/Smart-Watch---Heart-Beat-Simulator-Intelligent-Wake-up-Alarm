import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class wakeUpWatch3 {

    //ADJUSTABLE CUSTOM VALUES FOR USER
    static final int listCap = 10000;
    static final double variability = 0.085;
    static final int goalRate = 78;
    static final double threshold = 0.9;
    static final int calibrationCount = 15;
    static final int currAmntValues = 15;
    static final double trendConstant = 0.35;
    static final int simCap = 10;


    //User does not edit below
    static ArrayList<Double> list = new ArrayList<Double>();
    static int count;
    static double currBPM;
    static double avgBPM;
    static double sumFiftyRecent;
    static double sumAll;
    static double newBeat;
    static FileWriter writer;
    static int simCount = 1;
    static ArrayList<Integer> record = new ArrayList<Integer>();



    public static void main(String[] args) throws IOException {
        //clear the record each time program is run
        PrintWriter writer = new PrintWriter("C:\\Users\\Sameer\\IdeaProjects\\wakeUpWatch\\src\\sleepWakeData.txt");
        writer.print("");
        writer.close();

        simulate(); //this method exits the program when finished

    }

    public static void simulate() throws IOException {
        //reset before next simulation
        count = 0;
        list.clear();

        System.out.println("Simulation #: " + simCount);

        while (true) {
            //generate new beats, store in arraylist
            newBeat = Math.pow(Math.random(), variability) * goalRate;
            list.add(newBeat);
            if (list.size() > listCap) {
                list.remove(0);
            }

            //allow the system to get calibrated
            if (list.size() > calibrationCount) {
                count++;

                //find currBPM (avg of last currAmntValues beats), and then add trend/momentum
                sumFiftyRecent = 0;
                for (int i = list.size() - currAmntValues; i < list.size(); i++) {
                    sumFiftyRecent += list.get(i);
                }
                currBPM = sumFiftyRecent / currAmntValues;
                currBPM += trendConstant * (currBPM - avgBPM); //this simulates trend/momentum in sleepiness.

                //find resting normal BPM for day
                //takes random sample every 50 beats
                sumAll = 0;
                for (int i = 0; i < list.size(); i++) {
                    sumAll += list.get(i);
                }
                avgBPM = sumAll / list.size();

                display(count, currBPM, avgBPM);
                buzz(count, currBPM, avgBPM, threshold);

            }
            else {
                System.out.println("Calibrating...");
            }
        }


    }

    public static void buzz(int count, double current, double average, double threshold) throws IOException {
        if (current < threshold * average) {
            System.out.println("\n\nWake up. Your BPM fell below " + threshold
                    + " of your average waking BPM in " + (int) (count/average) + " minutes.\n\n");
            recordAndRerun((int) (count/average));

            printAvg();

            System.exit(0);
        }

    }

    public static void display(int beatNumber, double current, double average) {
        System.out.println("Beat #: " + beatNumber);
        System.out.println("Current BPM: " + current);
        System.out.println("Average BPM: " + average);
        System.out.println();
    }

    public static void recordAndRerun(int time) throws IOException {
        //record
        writer = new FileWriter("C:\\Users\\Sameer\\IdeaProjects\\wakeUpWatch\\src\\sleepWakeData.txt", true);
        writer.write("Sim " + simCount + ":\t\t" + time + "\tmin\n");
        writer.flush();
        writer.close();

        //rerun
        if (simCount < simCap) {
            System.out.println("End of sim: " + simCount + "\n\n\n\n");
            record.add(time);
            simCount++;
            simulate();
        }
        else {
            System.out.println("End of sim: " + simCount + "\n\n\n\n");
        }

    }

    public static void printAvg() {
        int sum = 0;
        int avg = 0;
        for (int i = 0; i < record.size(); i++) {
            sum += record.get(i);
        }
        avg = sum / record.size();
        System.out.println("Average time to fall asleep over " + simCount + " simulations: " + avg + " minutes.\n\n\n");
    }

}
