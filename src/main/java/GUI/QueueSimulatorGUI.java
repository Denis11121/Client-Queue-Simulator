package GUI;

import BusinessLogic.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class QueueSimulatorGUI extends JFrame implements ActionListener {
    private JTextField clientsTextField, queuesTextField, maxSimulationTimeTextField, minArrivalTimeTextField, maxArrivalTimeTextField, minServiceTimeTextField, maxServiceTimeTextField;
    private JButton startButton;
    private JTextArea simulationResultTextArea;

    private Simulation simulation;
    private String outputFileName;
    private StringBuilder simulationSteps;
    private double averageWaitingTime;

    private Timer simulationTimer;
    private int currentTimeStep;



    public QueueSimulatorGUI() {
        setTitle("Queue Simulator");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(8, 2));

        clientsTextField = new JTextField();
        queuesTextField = new JTextField();
        maxSimulationTimeTextField = new JTextField();
        minArrivalTimeTextField = new JTextField();
        maxArrivalTimeTextField = new JTextField();
        minServiceTimeTextField = new JTextField();
        maxServiceTimeTextField = new JTextField();
        startButton = new JButton("Start Simulation");

        startButton.addActionListener(this);

        inputPanel.add(new JLabel("Number of Clients:"));
        inputPanel.add(clientsTextField);
        inputPanel.add(new JLabel("Number of Queues:"));
        inputPanel.add(queuesTextField);
        inputPanel.add(new JLabel("Max Simulation Time:"));
        inputPanel.add(maxSimulationTimeTextField);
        inputPanel.add(new JLabel("Min Arrival Time:"));
        inputPanel.add(minArrivalTimeTextField);
        inputPanel.add(new JLabel("Max Arrival Time:"));
        inputPanel.add(maxArrivalTimeTextField);
        inputPanel.add(new JLabel("Min Service Time:"));
        inputPanel.add(minServiceTimeTextField);
        inputPanel.add(new JLabel("Max Service Time:"));
        inputPanel.add(maxServiceTimeTextField);
        inputPanel.add(startButton);

        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());

        simulationResultTextArea = new JTextArea(20, 40);
        simulationResultTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(simulationResultTextArea);
        resultPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        getContentPane().add(resultPanel, BorderLayout.CENTER);

        setVisible(true);

        simulationTimer = new Timer();
        currentTimeStep = 0;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            startSimulation();
            startButton.setEnabled(false);
        }
    }

    void startSimulation() {
        int noOfClients = Integer.parseInt(clientsTextField.getText());
        int noOfQueues = Integer.parseInt(queuesTextField.getText());
        int maxSimulationTime = Integer.parseInt(maxSimulationTimeTextField.getText());
        int minArrivalTime = Integer.parseInt(minArrivalTimeTextField.getText());
        int maxArrivalTime = Integer.parseInt(maxArrivalTimeTextField.getText());
        int minServiceTime = Integer.parseInt(minServiceTimeTextField.getText());
        int maxServiceTime = Integer.parseInt(maxServiceTimeTextField.getText());

        simulation = new Simulation(noOfClients, noOfQueues, maxSimulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime);
        outputFileName = "simulation_output.txt";
        simulationSteps = new StringBuilder();
        simulationTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!simulation.over()) {
                    simulation.prepare();
                    simulationSteps.append(simulation.simulationStep());
                    simulationSteps.append("\n--------------------------------\n");

                    SwingUtilities.invokeLater(() -> simulationResultTextArea.setText(simulationSteps.toString()));
                    currentTimeStep++;
                } else {
                    double averageWaitingTime = simulation.computeAverageWaitingTime();
                    simulationSteps.append("Average waiting time: ").append(averageWaitingTime);
                    simulationSteps.append("\n--------------------------------\n");
                    SwingUtilities.invokeLater(() -> {
                        simulationResultTextArea.setText(simulationSteps.toString());
                        startButton.setEnabled(true);
                    });

                    simulationTimer.cancel();
                    simulationTimer.purge();
                }
            }
        }, 0, 1000);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new QueueSimulatorGUI();
            }
        });
    }
}
