import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;

public class LiftOrganiser extends Thread {
    private List<Lift> lifts = new ArrayList<>();
    private List<Thread> threads = new ArrayList<>();
    private BlockingDeque<Order> orders;
    private boolean isRun = true;

    public LiftOrganiser(int numberOfLifts, BlockingDeque<Order> orders) {
        this.orders = orders;
        for (int i = 0; i < numberOfLifts; ++i) {
            this.lifts.add(new Lift(i+1, orders));
            this.threads.add(new Thread(this.lifts.get(i)));
        }
    }

    @Override
    public void run() {
        try {
            while(isRun) {
                if (this.orders.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }
                if (isThereFreeLift()) {
                    Order order = orders.poll();
                    if (order != null) {
                        int nearestLift = getNearestLift(order);
                        Lift lift = lifts.get(nearestLift);
                        lift.setCurrentOrder(order);
                        lift.setFree(false);
                        this.threads.set(nearestLift, new Thread(lift));
                        this.threads.get(nearestLift).start();
                    }
                }
            }
            while (!isAllLiftFree()) { }
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Lift Organiser interrupted Error");
        }
    }

    private boolean isThereFreeLift() {
        for (Lift lift : this.lifts) {
            if (lift.isFree()) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllLiftFree() {
        for (Lift lift : this.lifts) {
            if (!lift.isFree()) {
                return false;
            }
        }
        return true;
    }

    private int getNearestLift(Order order) {
        int nearestLift = 0;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < this.lifts.size(); ++i) {
            if (this.lifts.get(i).isFree()) {
                int curDistance = Math.abs(lifts.get(i).getCurrentFloor() - order.floor());
                if (curDistance < minDistance) {
                    minDistance = curDistance;
                    nearestLift = i;
                }
            }
        }
        return nearestLift;
    }

    public void setRun(boolean run) {
        this.isRun = run;
    }
}
