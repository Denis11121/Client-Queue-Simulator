package org.example;

import BusinessLogic.Simulation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String inputFileName = "C:\\Users\\Denis\\Desktop\\TP\\Tema2\\pt2024_30223_samoila_denis_assignment_2\\input.txt";
        String outputFileName = "output.txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFileName))) {
            Map<String, Integer> parameters = new HashMap<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    int value = Integer.parseInt(parts[1].trim());
                    parameters.put(key, value);
                }
            }


            int numberOfClients = parameters.getOrDefault("numberOfClients", 0);
            int numberOfQueues = parameters.getOrDefault("numberOfQueues", 0);
            int maxSimulationTime = parameters.getOrDefault("maxSimulationTime", 0);
            int minArrivalTime = parameters.getOrDefault("minArrivalTime", 0);
            int maxArrivalTime = parameters.getOrDefault("maxArrivalTime", 0);
            int minServiceTime = parameters.getOrDefault("minServiceTime", 0);
            int maxServiceTime = parameters.getOrDefault("maxServiceTime", 0);

            Simulation simulation = new Simulation(numberOfClients, numberOfQueues, maxSimulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
            simulation.run(outputFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
