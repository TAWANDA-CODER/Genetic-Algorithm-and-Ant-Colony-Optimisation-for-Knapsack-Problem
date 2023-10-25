import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ACOKnapsackSolver {
    private double capacity;
    private Double[][] items;
    private int numAnts;
    private double alpha;
    private double beta;
    private double evaporationRate;
    private double initialPheromone;
    private int maxIterations;
    private double[][] pheromones;
    private double bestValue;
    private List<Integer> bestSolution;
    private int noImprovementThreshold;
    private int noImprovementCounter;

    public ACOKnapsackSolver(double capacity, Double[][] items2, int numAnts, double alpha, double beta,
                             double evaporationRate, double initialPheromone, int maxIterations,
                             int noImprovementThreshold) {
        this.capacity = capacity;
        this.items = items2;
        this.numAnts = numAnts;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporationRate = evaporationRate;
        this.initialPheromone = initialPheromone;
        this.maxIterations = maxIterations;
        this.pheromones = new double[items2.length][items2.length];
        this.bestValue = Double.MIN_VALUE;
        this.bestSolution = new ArrayList<>();
        this.noImprovementThreshold = noImprovementThreshold;
        this.noImprovementCounter = 0;
    }

    public void solve() {
        long startTime = System.nanoTime();
        initializePheromones();

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            List<List<Integer>> antSolutions = new ArrayList<>();

            // Construct ant solutions
            for (int ant = 0; ant < numAnts; ant++) {
                List<Integer> solution = constructSolution();
                antSolutions.add(solution);
            }

            // Update pheromones
            updatePheromones(antSolutions);

            // Update best solution
            boolean hasImproved = false;
            for (List<Integer> solution : antSolutions) {
                double value = calculateTotalValue(solution);
                if (value > bestValue) {
                    bestValue = value;
                    bestSolution = solution;
                    hasImproved = true;
                }
            }

            if (!hasImproved) {
                noImprovementCounter++;
            } else {
                noImprovementCounter = 0;
            }

            if (noImprovementCounter >= noImprovementThreshold) {
                break; // Terminate if no improvement threshold is reached
            }
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("ELAPSED TIME: " + (elapsedTime) + " ns");
        System.out.println("Best Solution: " + bestSolution);
        System.out.println("Best Value: " + bestValue);
    }

    private void initializePheromones() {
        double initialPheromoneLevel = 1.0 / (items.length * initialPheromone);
        for (int i = 0; i < items.length; i++) {
            Arrays.fill(pheromones[i], initialPheromoneLevel);
        }
    }

    private List<Integer> constructSolution() {
        List<Integer> solution = new ArrayList<>();
        boolean[] visited = new boolean[items.length];
        double remainingCapacity = capacity;

        while (true) {
            int item = selectNextItem(visited, solution, remainingCapacity);
            if (item == -1) {
                break;
            }

            solution.add(item);
            visited[item] = true;
            remainingCapacity -= items[item][1];
        }

        return solution;
    }

    private int selectNextItem(boolean [] visited, List<Integer> solution, double remainingCapacity) {
        double[] probabilities = new double[items.length];
        double totalProbability = 0.0;

        for (int i = 0; i < items.length; i++) {
            if (!visited[i] && items[i][1] <= remainingCapacity) {
                probabilities[i] = Math.pow(pheromones[i][i], alpha) * Math.pow(1.0 / items[i][0], beta);
                totalProbability += probabilities[i];
            }
        }
    
        if (totalProbability == 0.0) {
            return -1;
        }
    
        double randomValue = new Random().nextDouble();
        double cumulativeProbability = 0.0;
    
        for (int i = 0; i < items.length; i++) {
            if (!visited[i] && items[i][1] <= remainingCapacity) {
                cumulativeProbability += probabilities[i] / totalProbability;
                if (randomValue <= cumulativeProbability) {
                    return i;
                }
            }
        }
    
        return -1;
    }
    
    private void updatePheromones(List<List<Integer>> antSolutions) {
        for (int i = 0; i < items.length; i++) {
            for (int j = 0; j < items.length; j++) {
                if (i != j) {
                    pheromones[i][j] *= evaporationRate;
                }
            }
        }
    
        for (List<Integer> solution : antSolutions) {
            double solutionValue = calculateTotalValue(solution);
    
            for (int i = 0; i < solution.size(); i++) {
                int item = solution.get(i);
                pheromones[item][item] += 1.0 / solutionValue;
            }
        }
    }
    
    private double calculateTotalValue(List<Integer> solution) {
        double totalValue = 0.0;
        for (int item : solution) {
            totalValue += items[item][0];
        }
        return totalValue;
    }
}

