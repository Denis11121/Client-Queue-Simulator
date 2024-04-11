package Model;

import java.util.ArrayList;

public class Queue extends Thread implements Comparable{
    private ArrayList<Client> clientsQueue;
    public Queue(){
        clientsQueue=new ArrayList<Client>();
    }
    public ArrayList<Client> getClientsQueue(){
        return clientsQueue;
    }

    public synchronized void addClient(Client client){
        clientsQueue.add(0,client);
    }

    public synchronized void removeFirstClient(){
        if(clientsQueue.size()>0){
            clientsQueue.remove(clientsQueue.size()-1);
        }
        else{
            System.out.println("EMPTY QUEUE");
        }
    }


    public Client peek(){
        if(clientsQueue.size()>0){
            return clientsQueue.get(clientsQueue.size()-1);
        }else{
            System.out.println("EMPTY QUEUE");
            return null;
        }
    }
    public synchronized int computeServiceTime() {
        int totalServiceTime = 0;
        for (Client client : clientsQueue) {
            totalServiceTime += client.getServiceTime();
        }
        return totalServiceTime;
    }

    public void serve(){
        if(clientsQueue.size()>0){
            Client firstClient=peek();
        if(firstClient.getServiceTime()==1){
            removeFirstClient();
        }
        else{
            firstClient.setServiceTime(firstClient.getServiceTime()-1);
            for(Client client:clientsQueue){
                client.setWaitingTime((client.getWaitingTime()+1));
            }
        }
        }
    }

    @Override
    public synchronized void start(){
        serve();
    }

    @Override
    public void run(){
        serve();
    }

    @Override
    public String toString(){
        String result="";
        for(Client client:clientsQueue){
            result+=client;
        }
        return result;
    }

    @Override
    public int compareTo(Object obj) {
        Queue comparableQueue=(Queue) obj;
        int currentQueueServiceTime=this.computeServiceTime();
        int comparbleQueueServiceTime=comparableQueue.computeServiceTime();

        if(currentQueueServiceTime<comparbleQueueServiceTime){
            return -1;
        }
        if(currentQueueServiceTime==comparbleQueueServiceTime){
            return 0;
        }
        return 1;
    }
}
