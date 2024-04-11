package Model;

public class Client implements Comparable{
    private int id;
    private int arrivalTime;
    private int serviceTime;

    private int waitingTime=0;

    public Client(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    @Override
    public int compareTo(Object obj){
        if(obj instanceof Client){
            Client comparableObject=(Client) obj;
            if(arrivalTime<comparableObject.arrivalTime){
                return -1;
            }
            else{
                if(arrivalTime==comparableObject.arrivalTime){
                    if(serviceTime<comparableObject.serviceTime){
                        return -1;
                    }
                    else{
                        return 1;
                    }
                }
            }
        }
        return 0;
    }
    @Override
    public String toString() {
        return "Client: " +
                "id=" + id +
                ", arrivalTime=" + arrivalTime +
                ", serviceTime=" + serviceTime +
                ", waitingTime=" + waitingTime +
                '\n';
    }


}
