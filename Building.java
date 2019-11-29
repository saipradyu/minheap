import java.util.Comparator;

/**
 * The Building class stores building metadata and implements Comparable interface
 */
public class Building implements Comparable<Building> {
    private int buildingNo;
    private int executedTime;
    private int totalTime;

    public Building(int buildingNo, int totalTime) {
        this.buildingNo = buildingNo;
        this.executedTime = 0;
        this.totalTime = totalTime;
    }

    //copy constructor to maintain object integrity while swapping
    public Building (Building building) {
        this.buildingNo = building.buildingNo;
        this.executedTime = building.executedTime;
        this.totalTime = building.totalTime;
    }

    //checks if building has completed execution
    public boolean hasCompletedExecution() {
        return (totalTime - executedTime == 0);
    }

    public int getBuildingNo() {
        return buildingNo;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public int getExecutedTime() {
        return executedTime;
    }

    public void setExecutedTime(int executedTime) {
        this.executedTime = executedTime;
    }

    //compare objects on executed time and then on building no while executing min heap operations
    @Override
    public int compareTo(Building o) {
        return Comparator.comparingInt(Building::getExecutedTime)
                .thenComparingInt(Building::getBuildingNo)
                .compare(this, o);
    }
}
