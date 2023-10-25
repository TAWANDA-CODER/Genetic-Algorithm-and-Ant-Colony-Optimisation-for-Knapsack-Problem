import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticAlgorithm {

    private static final int TOURNAMENT_SIZE = 2;
    private static final double MUTATION_RATE = 0.2;
    private static final int MAX_ITERATIONS = 750;

    private static double[] values = {};
    private static double[] weights = {};

    private static int NUM_ITEMS = 0;
    private static double CAPACITY = 0.0;

    private static final Random random = new Random();

    private static class Individual {
        private boolean[] genes;
        private double fitness = 0.0;

        public Individual() {
            genes = new boolean[NUM_ITEMS];
            fitness = 0.0;
        }

        public Individual(boolean[] genes) {
            this.genes = genes;
            evaluateFitness();
        }

        public boolean[] getGenes() {
            return genes;
        }

        public double getFitness() {
            return fitness;
        }

        public void evaluateFitness() {
            double totalValue = 0.0;
            double totalWeight = 0.0;

            for (int i = 0; i < NUM_ITEMS; i++) {
                if (genes[i]) {
                    totalValue += values[i];
                    totalWeight += weights[i];
                }
            }

            if (totalWeight > CAPACITY) {
                totalValue = 0.0; // Penalize solutions that exceed the capacity
            }

            fitness = totalValue;
        }

        public void mutate() {
            for (int i = 0; i < NUM_ITEMS; i++) {
                if (random.nextDouble() < MUTATION_RATE) {
                    genes[i] = !genes[i]; // Flip the bit
                }
            }
        }
    }

    public void GoGA(String ProblemInstanceName, int itemcount, int knapsackcapacity, double knownOptimum, Double[][] items) {

        long startTime = System.nanoTime();

        System.out.println("Problem Instance Name: " + ProblemInstanceName);
        System.out.println("Item Count: " + itemcount);
        System.out.println("Knapsack Capacity: " + knapsackcapacity);

        values = new double[itemcount];
        weights = new double[itemcount];
        for (int ii = 0; ii < itemcount; ii++) {
            values[ii] = items[ii][0];
            weights[ii] = items[ii][1];
        }
        CAPACITY = knapsackcapacity;
        NUM_ITEMS = itemcount;

        GoGA();
        System.out.println("Known optimum: " + knownOptimum);
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        System.out.println("ELAPSED TIME: " + (elapsedTime) + " ns");
        System.out.println("--------------------------------------------------");

    }

    public static void GoGA() {
        List<Individual> population = new ArrayList<>();

        // Generate an initial population
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            population.add(generateInitialPopulation());
        }

        int iteration = 0;
        Individual bestIndividual = null;
        double bestWeight = 0.0;

        while (iteration < MAX_ITERATIONS) {
            // Perform tournament selection
            Individual parent1 = tournamentSelection(population);
            Individual parent2 = tournamentSelection(population);

            // Perform crossover
            Individual child = crossover(parent1, parent2);

            // Perform mutation
            child.mutate();

            // Calculate the fitness of the child
            child.evaluateFitness();

            // Replace a random individual in the population with the child
            int randomIndex = random.nextInt(TOURNAMENT_SIZE);
            population.set(randomIndex, child);

            // Update the best individual
            double totalWeight = calculateTotalWeight(child.getGenes());
            if (bestIndividual == null || child.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = child;
                bestWeight = totalWeight;
            }

            iteration++;
        }

        System.out.println("Best Solution: " + bestIndividual.getFitness());
        System.out.println("Best Solution Weight: " + bestWeight);
    }

    private static Individual generateInitialPopulation() {
        boolean[] genes = new boolean[NUM_ITEMS];
        for (int i = 0; i < NUM_ITEMS; i++) {
            genes[i] = random.nextBoolean();
        }
        return new Individual(genes);
    }

    private static Individual tournamentSelection(List<Individual> population) {
        Individual bestIndividual = null;
        for (int i = 0; i < TOURNAMENT_SIZE; i++) {
            Individual individual = population.get(random.nextInt(population.size()));
            if (bestIndividual == null || individual.getFitness() > bestIndividual.getFitness()) {
                bestIndividual = individual;
            }
        }
        return bestIndividual;
    }

    private static Individual crossover(Individual parent1, Individual parent2) {
        boolean[] parent1Genes = parent1.getGenes();
        boolean[] parent2Genes = parent2.getGenes();
        boolean[] childGenes = new boolean[NUM_ITEMS];
        int crossoverPoint = random.nextInt(NUM_ITEMS);
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (i < crossoverPoint) {
                childGenes[i] = parent1Genes[i];
            } else {
                childGenes[i] = parent2Genes[i];
            }
        }
        return new Individual(childGenes);
    }

    private static double calculateTotalWeight(boolean[] genes) {
        double totalWeight = 0.0;
        for (int i = 0; i < NUM_ITEMS; i++) {
            if (genes[i]) {
                totalWeight += weights[i];
            }
        }
        return totalWeight;
    }

    private static String arrayToString(boolean[] array) {
        StringBuilder sb = new StringBuilder();
        for (boolean element : array) {
            sb.append(element ? "1" : "0").append(" ");
        }
        return sb.toString().trim();
    }
}