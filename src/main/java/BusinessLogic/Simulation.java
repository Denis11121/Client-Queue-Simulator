package BusinessLogic;

import Model.Client;
import Model.Queue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Simulation {
    private int numberOfClients;
    private int numberOfQueues;
    private int maxSimTime;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int minServiceTime;
    private int maxServiceTime;
    private int time;
    private ArrayList<Client> clients;
    private ArrayList<Queue> queues;
    private ArrayList<Client> currentClientsInQueues=new ArrayList<Client>();

    public Simulation(int numberOfClients, int numberOfQueues, int maxSimTime, int minArrivalTime, int maxArrivalTime, int minServiceTime, int maxServiceTime) {
        this.numberOfClients = numberOfClients;
        this.numberOfQueues = numberOfQueues;
        this.maxSimTime = maxSimTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minServiceTime = minServiceTime;
        this.maxServiceTime = maxServiceTime;
        this.time=0;
        //generare clienti
        clients=new ArrayList<Client>(numberOfClients);
        Random randomGenerator=new Random();
        int arrivalTime=-1;
        int serviceTime=-1;
        for(int i=1;i<=numberOfClients;i++){
            arrivalTime= randomGenerator.nextInt(maxArrivalTime-minArrivalTime+1)+minArrivalTime;
            serviceTime= randomGenerator.nextInt(maxServiceTime-minServiceTime+1)+minServiceTime;
            Client newClient=new Client(i,arrivalTime,serviceTime);
            clients.add(newClient);

        }
        Collections.sort(clients);

        queues=new ArrayList<Queue>();
        for(int i=0;i<this.numberOfQueues;i++){
            queues.add(new Queue());
        }
    }



    private Queue bestQueue(ArrayList<Queue> queues){
        if(queues.size()==0){
            System.out.println("No queues");
            return null;
        }
        Queue bestQueue=queues.get(0);
        for(Queue queue: queues){
            if(queue.compareTo(bestQueue)<0){
                bestQueue=queue;
            }
        }
        return bestQueue;
    }

    public boolean over(){
        if(time>maxSimTime){
            return true;
        }
        return false;
    }

    public void prepare(){
        if(over()){
            System.out.println("Simulation over");
            return;
        }
        for(Client client: clients){
            if(client.getArrivalTime()==time){
                Queue bestQueue=bestQueue(queues);
                bestQueue.addClient(client);
                currentClientsInQueues.add(client);
            }
        }
        ArrayList<Thread> threads=new ArrayList<Thread>();
        for(Queue queue:queues){
            if(queue.getClientsQueue().size()>0){
                Thread thread=new Thread(queue);
                threads.add(thread);
                thread.start();
            }
        }
        for(Thread thread:threads){
            try{
                thread.join();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        time++;
    }
    public String simulationStep(){
        String result="";
        result=result+"Time: "+time+"\n";
        result=result+"Waiting clients: ";

        ArrayList<Client> waitingList=(ArrayList<Client>)clients.clone();
        waitingList.removeAll(currentClientsInQueues);
        for(Client client:waitingList){
            result+=client;
        }
        result+="\n";
        int i=1;
        for(Queue queue:queues){
            String string="Queue"+i+": ";
            if(queue.getClientsQueue().size()==0){
                string+="Closed\n";
            }
            else{
                string+=queue+"\n";
            }
            result+=string;
            i++;
        }
        result+="--------------------------------\n";
        return result;

    }

    public double computeAverageWaitingTime(){
        double result=0;
        for(Client client: clients){
            result+=client.getWaitingTime();
        }
        result/=clients.size();
        return result;
    }

    public void run(String outputFileName){
        try{
            FileWriter fw=new FileWriter(outputFileName);
            PrintWriter pw=new PrintWriter(fw);
            pw.println(this.toString());

            while(over()==false){
                prepare();
                pw.println(simulationStep());
            }
            pw.println("Average waiting time: "+computeAverageWaitingTime());
            fw.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        String result = "";
        result += "Clients : " + numberOfClients + "\n";
        result += "Queues : " + numberOfQueues + "\n";
        result += "MAX Simulation : " + maxSimTime + "\n";
        result += "MIN Arrival Time : " + minArrivalTime + "\n";
        result += "MAX Arrival Time : " + maxArrivalTime + "\n";
        result += "MIN Service Time : " + minServiceTime + "\n";
        result += "MAX Service Time : " + maxServiceTime + "\n";
        result += "Clients:\n";
        for (Client client : clients){
            result += client.toString() + "\n";
        }
        result += "\n";
        return result;
    }

}
