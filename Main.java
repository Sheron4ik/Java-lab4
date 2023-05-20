import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        BlockingDeque<Order> orders = new LinkedBlockingDeque<>();
        System.out.println("Enter the number of floors:");
        int numberOfFloors = scanner.nextInt();
        System.out.println("Enter the number of orders:");
        int numberOfOrders = scanner.nextInt();
        System.out.println("Enter order frequency:");
        int frequency = scanner.nextInt();
        System.out.println("Enter the number of lifts:");
        int numberOfLifts = scanner.nextInt();

        OrderCreation orderCreation = new OrderCreation(numberOfFloors, numberOfOrders, frequency, orders);
        orderCreation.start();
        LiftOrganiser liftOrganiser = new LiftOrganiser(numberOfLifts, orders);
        liftOrganiser.start();

        Thread.sleep((long)numberOfOrders*frequency*numberOfFloors/numberOfLifts);
        liftOrganiser.setRun(false);
        System.out.println("The end =)");
    }
}
