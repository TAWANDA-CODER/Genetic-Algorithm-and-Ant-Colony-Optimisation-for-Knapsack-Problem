import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.crypto.Data;

public class Main {

    public static Double[][] readNumberPairsFromFile(String filePath) throws IOException {
        List<Double[]> numberPairs = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("DataInstances/" +filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] numbers = line.trim().split("\\s+");
                Double[] pair = new Double[2];

                pair[0] = Double.parseDouble(numbers[0]);
                pair[1] = Double.parseDouble(numbers[1]);

                numberPairs.add(pair);
            }
        }

        Double[][] items = new Double[numberPairs.size()-1][2];
        for (int i = 0; i < numberPairs.size()-1; i++) {
            items[i] = numberPairs.get(i+1);
        }

        return items;
    }

    public static int getMaxCapacity(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("DataInstances/" +filePath))) 
        {
            String line;
            line = reader.readLine();
            String[] numbers = line.trim().split("\\s+");
            int[] pair = new int[2];
            pair[0] = Integer.parseInt(numbers[0]);
            pair[1] = Integer.parseInt(numbers[1]);
            return pair[1];

             
        }

    }

    public static int getItemCount(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("DataInstances/" +filePath))) 
        {
            String line;
            line = reader.readLine();
            String[] numbers = line.trim().split("\\s+");
            int[] pair = new int[2];
            pair[0] = Integer.parseInt(numbers[0]);
            pair[1] = Integer.parseInt(numbers[1]);
            return pair[0];

             
        }

    }
    public static void main(String[] args) {
        int capacity = 0;

        String[] DataInstances = {"f1_l-d_kp_10_269.txt","f2_l-d_kp_20_878.txt","f3_l-d_kp_4_20.txt","f4_l-d_kp_4_11.txt","f5_l-d_kp_15_375.txt","f6_l-d_kp_10_60.txt","f7_l-d_kp_7_50.txt","f8_l-d_kp_23_10000.txt","f9_l-d_kp_5_80.txt","f10_l-d_kp_20_879.txt","knapPI_1_100_1000_1.txt"};
        double[] knownOptimums = {295,1024,35,23,481.07,52,107,9767,130,1025,9147};

        for (int i = 0; i < 1; i++) 
        {
            Double[][] items =null;
            try {
                items = readNumberPairsFromFile(DataInstances[i]);
                capacity = getMaxCapacity(DataInstances[i]);
                int itemCount = getItemCount(DataInstances[i]);
                int numAnts = 20;
                Double alpha = 1.0;
                Double beta = 1.0;
                Double evaporationRate = 0.5;
                Double initialPheromone = 1.0;
                int maxIterations = 500;
                int noImprovementThreshold = 50; // Set the no improvement threshold
                ACOKnapsackSolver solver = new ACOKnapsackSolver(capacity, items, numAnts, alpha, beta,
                evaporationRate, initialPheromone, maxIterations, noImprovementThreshold);
                solver.solve();  

        
        GeneticAlgorithm abc =new GeneticAlgorithm();
        abc.GoGA(DataInstances[i], itemCount, capacity, knownOptimums[i], items);
                
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } 
        }   
    }
}