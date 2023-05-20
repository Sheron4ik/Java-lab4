import java.util.Random;
import java.util.concurrent.BlockingDeque;

public class OrderCreation extends Thread {
    private int numberOfFloors;
    private int numberOfOrders;
    private int frequency;
    private BlockingDeque<Order> orders;

    public OrderCreation(int numberOfFloors, int numberOfOrders, int frequency, BlockingDeque<Order> orders) {
        this.numberOfFloors = numberOfFloors;
        this.numberOfOrders = numberOfOrders;
        this.frequency = frequency;
        this.orders = orders;
    }

    @Override
    public void run() {
        try {
            Random rand = new Random();
            for (; this.numberOfOrders > 0; --this.numberOfOrders) {
                this.orders.add(new Order(rand.nextInt(this.numberOfFloors)+1, rand.nextBoolean()));
                Thread.sleep(this.frequency);
            }
        } catch (InterruptedException e) {
            System.out.println("Order interrupted Error");
        }
    }
}
