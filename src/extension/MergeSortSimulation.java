package extension;

import helpers.Constant;

import javax.swing.*;
import java.awt.*;

import static helpers.Helper.isSorted;

public class MergeSortSimulation {
    private Controller controller;
    private JLabel[] lbArrays;
    private int num;
    private int[] arrays;

    private int[] oriLocation = new int[15]; // origin x location of label
    private boolean isIncrease = true;

    public MergeSortSimulation(Controller controller) {
        this.controller = controller;
    }

    private void setupSimulation() {
        //delete previous arrays and set number elements of array
        controller.getPnImitiate().removeAll();
        controller.getPnImitiate().revalidate();
        controller.getPnImitiate().repaint();

        num = arrays.length;
        lbArrays = new JLabel[num];

        for (int i = 0; i < num; i++) {
            //create label, set text "0"
            lbArrays[i] = new JLabel();
            lbArrays[i].setText(String.valueOf(arrays[i]));
            controller.getPnImitiate().add(lbArrays[i]);

            //set size label
            lbArrays[i].setSize(50, 50);
            lbArrays[i].setOpaque(true);
            lbArrays[i].setForeground(Color.blue);

            //set location label
            if (i == 0) lbArrays[i].setLocation(((int) ((18 - num) * 0.5) * 70) + 100, 150);
            else lbArrays[i].setLocation(lbArrays[i - 1].getX() + 70, 150);

            //set fonts
            lbArrays[i].setFont(new Font("Tahoma", Font.PLAIN, 30));

            //set background color
            lbArrays[i].setBackground(SystemColor.inactiveCaption);
            //set text alignment center
            lbArrays[i].setHorizontalAlignment(SwingConstants.CENTER);
            lbArrays[i].setVerticalAlignment(SwingConstants.CENTER);
        }
        controller.getPnImitiate().setVisible(true);
        controller.getPnImitiate().validate();
        controller.getPnImitiate().repaint();
    }

    public void setData(boolean increase, int[] arrays) {
        this.isIncrease = increase;
        this.arrays = arrays;
        setupSimulation();
    }


    /**
     * Arrange the integer array in the way Merge Sort
     * In each step, call PutUp OR PutDown JLabel
     * In the end, reset (x,y) of JLabel in new location
     *
     * @param left
     * @param mid
     * @param right
     */
    private void Merge(int left, int mid, int right) {
        int n1 = mid - left + 1; // count number of box on the left
        int n2 = right - mid; // count number of box on the right
        int[] T = new int[n1 + n2]; // arrays[all] of each turn merge sort
        int[] L = new int[n1]; // arrays[left] of each turn merge sort
        int[] R = new int[n2]; // arrays[right] of each turn merge sort
        int i, j, k;
        for (i = 0; i < n1; i++)
            L[i] = arrays[left + i]; // set values for arrays[left]
        for (j = 0; j < n2; j++)
            R[j] = arrays[mid + 1 + j]; // set values for arrays[right]
        PutUp(left, right);

        // start to sort
        i = 0;
        j = 0;
        k = left;
        while (i < n1 && j < n2) {
            if (isIncrease && L[i] <= R[j] || !isIncrease && L[i] >= R[j]) { // ? Increase
                arrays[k] = L[i];
                PutDown(lbArrays[left + i], oriLocation[k], 150); // put arrays[i] (sorted) down the line sorted.
                i++;
            } else {
                arrays[k] = R[j];
                PutDown(lbArrays[mid + 1 + j], oriLocation[k], 150);
                j++;
            }
            k++;
        }
        while (i < n1) {
            arrays[k] = L[i];
            PutDown(lbArrays[left + i], oriLocation[k], 150);
            i++;
            k++;
        }
        while (j < n2) {
            arrays[k] = R[j];
            PutDown(lbArrays[mid + 1 + j], oriLocation[k], 150);
            j++;
            k++;
        }

        for (i = 0; i < n1 + n2; i++)
            T[i] = arrays[left + i];
        Relocat(left, right, T);
    }

    /**
     * Use recursion to separate the integer array into 2 new arrays as long as left < right
     *
     * @param left
     * @param right
     */
    private void MergeSortAl(int left, int right) {
        if (left < right) {
            int mid = (left + right) / 2;
            MergeSortAl(left, mid);
            MergeSortAl(mid + 1, right);
            Merge(left, mid, right);
        }
    }

    /**
     * Check if the array is not sorted then start simulation
     * Save original location (oriLocation) before run simulation
     */
    public void runSimulation() {
        if (!isSorted(isIncrease, arrays)) {
            for (int i = 0; i < num; i++) oriLocation[i] = lbArrays[i].getX();
            MergeSortAl(0, num - 1);
        }
    }

    /**
     * Put components from left to right on line top
     *
     * @param left
     * @param right
     */
    private void PutUp(int left, int right) {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // wait thread before finish then this thread is just starting
                    if (cur != 0) controller.getThreads(cur - 1).join();

                    // set background color: | left:(green) | mid | right:(yellow) |
                    int mid = (left + right) / 2;
                    for (int i = left; i <= right; i++) {
                        if (i < mid + 1) lbArrays[i].setBackground(Constant.SELECTED_GREEN);
                        else lbArrays[i].setBackground(Constant.SELECTED_YELLOW);
                    }

                    while (lbArrays[right].getY() > 50) { // goal: put up components each [controller.time]: -10 unit
                        for (int i = left; i <= right; i++) {
                            if (lbArrays[i].getY() > 50) // decrease y location of component when (y location of that component) > 50
                                lbArrays[i].setLocation(lbArrays[i].getX(), lbArrays[i].getY() - 10);
                        }
                        controller.SLEEP();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Put down component & move component into the sorted location (x,y)
     *
     * @param lb1
     * @param x
     * @param y
     */
    private void PutDown(JLabel lb1, int x, int y) {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cur != 0) controller.getThreads(cur - 1).join();

                    int x1 = lb1.getX(); // y location of this label

                    // put component down the line processing
                    lb1.setBackground(Constant.PROCESSING_COLOR);
                    while (lb1.getY() < 100) { // goal: put down component each [controller.time]: +10 unit
                        lb1.setLocation(x1, lb1.getY() + 10); // y location of this label increase 10 unit
                        controller.SLEEP();
                    }

                    int y1 = lb1.getY(); // y location of this label
                    if (x1 < x) {
                        while (lb1.getX() < x) {
                            lb1.setLocation(lb1.getX() + 10, y1);
                            controller.SLEEP();
                        }
                        while (lb1.getY() < y) {
                            lb1.setLocation(x, lb1.getY() + 10);
                            controller.SLEEP();
                        }
                    } else {
                        while (lb1.getX() > x) {
                            lb1.setLocation(lb1.getX() - 10, y1);
                            controller.SLEEP();
                        }
                        while (lb1.getY() < y) {
                            lb1.setLocation(x, lb1.getY() + 10);
                            controller.SLEEP();
                        }
                    }
                    lb1.setBackground(SystemColor.inactiveCaption);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        controller.setThreads(thread);
    }

    /**
     * After put up or put down, JLabel do not keep correct location
     * Get correct location and set location for JLabel after sort
     *
     * @param left
     * @param right
     * @param T
     */
    private void Relocat(int left, int right, int[] T) {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cur != 0) controller.getThreads(cur - 1).join();

                    for (int i = left; i <= right; i++) {
                        if (lbArrays[i].getX() != oriLocation[i]) { // check if label do not correct original location
                            lbArrays[i].setLocation(oriLocation[i], 150);
                            lbArrays[i].setText(T[i - left] + "");
                        }
                    }
                    controller.SLEEP();
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        controller.setThreads(thread);
    }
}