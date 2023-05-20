import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class Lift implements Runnable {
    private int id;
    private boolean isFree = true;
    private int currentFloor = 1;
    private boolean isToUp = true;
    private Order currentOrder = new Order(1, true);
    private BlockingDeque<Order> orders;

    public Lift(int id, BlockingDeque<Order> orders) {
        this.id = id;
        this.orders = orders;
    }

    @Override
    public void run() {
        System.out.println("LIFT : " + this.id + ", JOURNEY : [from : " + this.currentFloor + ", to : " + this.currentOrder.floor() + "]");
        try {
            List<Integer> pickUpOrders = new ArrayList<>();
            while (this.currentFloor != this.currentOrder.floor()) {
                if (this.isToUp) {
                    pickUpOrders.addAll(orders.stream().filter(
                            order -> this.currentFloor < order.floor() && order.floor() < this.currentOrder.floor() &&
                                    order.isToUp() == this.isToUp).map(Order::floor).toList());
                } else {
                    pickUpOrders.addAll(orders.stream().filter(
                            order -> this.currentOrder.floor() < order.floor() && order.floor() < this.currentFloor &&
                                    order.isToUp() == this.isToUp).map(Order::floor).toList());
                }
                if (pickUpOrders.contains(this.currentFloor)) {
                    System.out.println("LIFT : " + this.id + ", PICK UP ORDER : " + this.currentFloor);
                    this.orders.removeAll(this.orders.stream().filter(
                            order -> order.floor() == this.currentFloor && order.isToUp() == this.isToUp).toList());
                    pickUpOrders.removeAll(pickUpOrders.stream().filter(
                            pickUpOrder -> pickUpOrder == this.currentFloor).toList());
                }
                if (this.isToUp) {
                    System.out.println("LIFT : " + this.id + ", FLOOR : " + this.currentFloor + "->" + (++this.currentFloor));
                } else {
                    System.out.println("LIFT : " + this.id + ", FLOOR : " + this.currentFloor + "->" + (--this.currentFloor));
                }
                Thread.sleep(1000);
            }
            System.out.println("LIFT : " + this.id + ", JOURNEY FINISH : " + this.currentFloor);
            this.isFree = true;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Lift interrupted Error");
        }
    }

    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
        this.isToUp = this.currentFloor < currentOrder.floor();
    }

    public void setFree(boolean free) {
        this.isFree = free;
    }

    public boolean isFree() {
        return this.isFree;
    }

    public int getCurrentFloor() {
        return this.currentFloor;
    }
}
