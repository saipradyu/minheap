/**
 * Pojo class which stores Instruction metadata
 */
public class Instruction {
    private int time;       // instruction time
    private int type;       // instruction type
    private int firstParam; // params for insert, print or print range
    private int secondParam;// params for insert or print range

    public Instruction() {
    }

    @Override
    public String toString() {
        return String.format("%d, %d, %d, %d", time, type, firstParam, secondParam);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getFirstParam() {
        return firstParam;
    }

    public void setFirstParam(int firstParam) {
        this.firstParam = firstParam;
    }

    public int getSecondParam() {
        return secondParam;
    }

    public void setSecondParam(int secondParam) {
        this.secondParam = secondParam;
    }
}