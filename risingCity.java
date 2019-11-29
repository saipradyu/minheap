import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Scanner;


public class risingCity {
    public static final int MAX_HEAP_SIZE = 2000;
    public static final int CMD_INSERT = 1;
    public static final int CMD_PRINT = 2;
    public static final int CMD_PRINT_RAN = 3;

    public static final String OUTPUT_FILE = "output_file.txt";

    public static void main(String[] args) {
        List<Instruction> instructions = new ArrayList<>();

        try {
            // check and handle if the input from command prompt is empty
            if (args.length == 0)
                throw new IllegalArgumentException();

            // construct path and read file from command line args
            File file = new File(Paths.get(args[0]).toString());
            Scanner sc = new Scanner(file);

            // set console output to the required output text file
            PrintStream out = new PrintStream(new FileOutputStream(OUTPUT_FILE));
            System.setOut(out);

            while (sc.hasNextLine()) {
                String input = sc.nextLine();
                Instruction instruction = new Instruction();
                String[] a = input.split(":", 2); // split on ':' to get instruction time
                instruction.setTime(Integer.parseInt(a[0]));  // save instruction data to a pojo class
                String[] b = a[1].split("\\(", 2); // split on '(' to get instruction command
                String inputCmd = b[0].trim();
                String[] c = b[1].split(",", 2);   // split on ',' to get parameters
                // save input command as an integer type based on string match and no of params
                switch (inputCmd) {
                    case "Insert":
                        instruction.setType(CMD_INSERT);
                        instruction.setFirstParam(Integer.parseInt(c[0]));
                        String e = c[1].replace(")", "");
                        instruction.setSecondParam(Integer.parseInt(e));
                        break;
                    case "PrintBuilding":
                        if (c.length == 1) {
                            String d = c[0].replace(")", "");
                            instruction.setType(CMD_PRINT);
                            instruction.setFirstParam(Integer.parseInt(d));
                        } else if (c.length == 2) {
                            instruction.setType(CMD_PRINT_RAN);
                            instruction.setFirstParam(Integer.parseInt(c[0]));
                            String f = c[1].replace(")", "");
                            instruction.setSecondParam(Integer.parseInt(f));
                        }
                        break;
                    default:
                        break;
                }
                instructions.add(instruction); // store instructions in a list
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // initialize heap and red black tree; create an iterator to sift through instructions
        MinHeap minHeap = new MinHeap(MAX_HEAP_SIZE);
        RedBlackTree redBlackTree = new RedBlackTree();
        ListIterator<Instruction> iterator = instructions.listIterator();

        // initialize global and build timer
        int globalTime = 0;
        int buildTimer = 0;
        Instruction instruction = null;     // next instruction to be executed when matches global time
        Building currentBuilding = null;    // building which is currently being executed
        // maintain list of pending inserts to heap when a building is being executed
        List<Instruction> pendingHeapInserts = new ArrayList<>();
        // loop until instructions are finished and heap is empty
        while (iterator.hasNext() || !minHeap.isEmpty() || currentBuilding != null) {
            if (instruction == null && iterator.hasNext())
                instruction = iterator.next();
            while (instruction != null) {
                // perform instruction only when it matches global timer
                if (globalTime == instruction.getTime()) {
                    switch (instruction.getType()) {
                        // insert to red black tree and track pending inserts to heap
                        case CMD_INSERT:
                            redBlackTree.put(instruction.getFirstParam(),
                                    new Building(instruction.getFirstParam(), instruction.getSecondParam()));
                            pendingHeapInserts.add(instruction);
                            break;
                        // prints a particular building with associated building no
                        case CMD_PRINT:
                            redBlackTree.print(instruction.getFirstParam());
                            break;
                        // prints building nos in the given range
                        case CMD_PRINT_RAN:
                            redBlackTree.print(instruction.getFirstParam(), instruction.getSecondParam());
                            System.out.println();
                            break;
                        default:
                            break;
                    }
                    instruction = iterator.hasNext() ? iterator.next() : null; // iterate through instructions
                } else
                    break;
            }
            // check if current building has completed execution; output tuple; delete from heap and red black tree
            if (currentBuilding != null && currentBuilding.hasCompletedExecution()) {
                System.out.println("(" + currentBuilding.getBuildingNo() + "," + globalTime + ")");
                redBlackTree.delete(currentBuilding.getBuildingNo());
                minHeap.remove();
                // check for pending inserts to heap and iterate and insert
                if (pendingHeapInserts.size() != 0) {
                    for (Instruction pendingInsert : pendingHeapInserts)
                        minHeap.add(new Building(pendingInsert.getFirstParam(), pendingInsert.getSecondParam()));
                }
                currentBuilding = minHeap.peek(); // update current building to be executed from heap
                pendingHeapInserts = new ArrayList<>(); // reset pending heap inserts
                buildTimer = 0; //reset build timer
            } else if (buildTimer % 5 == 0) { // check if current building has been executed for 5 days
                // check for pending inserts to heap and iterate and insert
                if (pendingHeapInserts.size() != 0) {
                    for (Instruction pendingInsert : pendingHeapInserts)
                        minHeap.add(new Building(pendingInsert.getFirstParam(), pendingInsert.getSecondParam()));
                } else
                    minHeap.heapifyDown(); // heapify to maintain heap property when no inserts
                currentBuilding = minHeap.peek(); // update current building to be executed from heap
                pendingHeapInserts = new ArrayList<>(); // reset pending heap inserts
                buildTimer = 0; // reset build timer
            }
            // increment global timer, build timer and execution time of current building
            if (currentBuilding != null) {
                currentBuilding.setExecutedTime(currentBuilding.getExecutedTime() + 1);
                redBlackTree.put(currentBuilding.getBuildingNo(), currentBuilding);
                buildTimer++;
            }
            globalTime++;
        }
    }
}
