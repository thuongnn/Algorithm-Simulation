package extension;

import dialog.FloatingPointNumber;
import dialog.MergeSort;
import helpers.Constant;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class Controller {
    public JPanel contentPane;
    private JPanel pnImitiate;
    private JComboBox cbxSelectAlgorithm;
    private JButton btnCreateData;
    private JSlider slSpeed;
    private JButton btnStart;
    private JButton btnPause;
    private JButton btnAbout;

    private int CURRENT_SELECT_SIMULATION = 0;
    private Thread[] threads = new Thread[1000000];
    private int curT = -1;
    private int time = 50;
    private boolean checkPause = true;

    private MergeSort mergeSortDialog;
    private FloatingPointNumber floatingPointNumberDialog;

    private MergeSortSimulation mergeSortSimulation;
    private AddSubtractSimulation addSubtractSimulation;
    private MultiplicationSimulation multiplicationSimulation;

    public Controller() {
        setupCurrentAlgorithm();

        cbxSelectAlgorithm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setupCurrentAlgorithm();
            }
        });
        btnCreateData.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch (CURRENT_SELECT_SIMULATION) {
                    case Constant.MERGE_SORT_SIMULATION:
                        mergeSortDialog.setVisible(true);
                        break;
                    case Constant.FLOATING_POINT_SIMULATION:
                        floatingPointNumberDialog.setVisible(true);
                        break;
                }
            }
        });
        slSpeed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                switch (CURRENT_SELECT_SIMULATION) {
                    case Constant.MERGE_SORT_SIMULATION:
                        time = 100 - slSpeed.getValue() * 10;
                        break;
                    case Constant.FLOATING_POINT_SIMULATION:
                        time = 1000 - slSpeed.getValue() * 100; // min: 300
                        break;
                }
            }
        });
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changedStatus(Constant.RUNNING);
                switch (CURRENT_SELECT_SIMULATION) {
                    case Constant.MERGE_SORT_SIMULATION:
                        mergeSortSimulation.runSimulation();
                        break;
                    case Constant.FLOATING_POINT_SIMULATION:
                        if (addSubtractSimulation != null) addSubtractSimulation.runSimulation();
                        else if (multiplicationSimulation != null) multiplicationSimulation.runSimulation();
                        break;
                }
                finishSimulation();
            }
        });
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkPause) pauseSimulation();
                else resumeSimulation();
            }
        });
        btnAbout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new Frame(), "This tool developed by Nguyen Nhu Thuong");
            }
        });
    }

    /**
     * Setup default UI of algorithm simulation
     */
    private void setupCurrentAlgorithm() {
        CURRENT_SELECT_SIMULATION = cbxSelectAlgorithm.getSelectedIndex();
        if (CURRENT_SELECT_SIMULATION == Constant.MERGE_SORT_SIMULATION) time = 50;
        else time = 1000;

        pnImitiate.removeAll();
        pnImitiate.revalidate();
        pnImitiate.repaint();

        // set layout of UI display algorithm
        pnImitiate.setLayout(null);
        // set input data for dialog
        mergeSortDialog = new MergeSort(this);
        mergeSortDialog.pack();
        mergeSortDialog.setLocationRelativeTo(null);
        floatingPointNumberDialog = new FloatingPointNumber(this);
        floatingPointNumberDialog.pack();
        floatingPointNumberDialog.setLocationRelativeTo(null);
    }

    /**
     * Pause simulation
     */
    private void pauseSimulation() {
        for (int i = 0; i < curT; i++) {
            try {
                threads[i].suspend();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        changedStatus(Constant.PAUSE);
    }

    /**
     * Resume simulation
     */
    private void resumeSimulation() {
        for (int i = 0; i < curT; i++) {
            try {
                threads[i].resume();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        changedStatus(Constant.RESUME);
    }

    /**
     * Stop all thread is running
     */
    private void stopAllThreads() {
        for (int i = 0; i < curT; i++) {
            try {
                threads[i].interrupt();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        curT = -1;
    }

    /**
     * If all thread is run complete
     */
    private void finishSimulation() {
        int cur = increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cur != 0) {
                        threads[cur - 1].join();
                    }
                    JOptionPane.showMessageDialog(new Frame(), "END");
                    changedStatus(Constant.FINISH);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        setThreads(thread);
    }

    /**
     * Changed status UI simulation
     *
     * @param status type of status
     */
    private void changedStatus(int status) {
        switch (status) {
            case Constant.HAS_DATA:
                btnStart.setEnabled(true);
                btnPause.setEnabled(false);
                btnPause.setText("Pause");
                checkPause = true;
                break;
            case Constant.RUNNING:
                btnStart.setEnabled(false);
                btnCreateData.setEnabled(false);
                btnPause.setEnabled(true);
                break;
            case Constant.PAUSE:
                btnCreateData.setEnabled(true);
                btnPause.setText("Resume");
                checkPause = false;
                break;
            case Constant.RESUME:
                btnCreateData.setEnabled(false);
                btnPause.setText("Pause");
                checkPause = true;
                break;
            case Constant.FINISH:
                btnPause.setText("Pause");
                btnPause.setEnabled(false);
                btnStart.setEnabled(true);
                btnCreateData.setEnabled(true);
                addSubtractSimulation = null;
                multiplicationSimulation = null;
                break;
        }
    }

    /**
     * Set data for merge simulation after user input in MergeSort dialog
     *
     * @param isIncrease true if type sort is increase, false if type sort is decrease
     * @param arrays     arrays need to sort
     */
    public void setDataForMergeSortSimulation(boolean isIncrease, int[] arrays) {
        changedStatus(Constant.HAS_DATA);
        stopAllThreads(); // when changed data, stop all thread & start new simulation
        mergeSortSimulation = new MergeSortSimulation(this);
        mergeSortSimulation.setData(isIncrease, arrays);
    }

    /**
     * set data for floating point number simulation after user input in FloatingPointNumber dialog
     *
     * @param calculationType calculation type
     * @param number1         number 1
     * @param number2         number 2
     */
    public void setDataForFPNSimulation(String calculationType, BigDecimal number1, BigDecimal number2) {
        stopAllThreads(); // when changed data, stop all thread & start new simulation
        if (calculationType.equals(Constant.ADDITION_TYPE) || calculationType.equals(Constant.SUBTRACTION_TYPE)) {
            changedStatus(Constant.HAS_DATA);
            addSubtractSimulation = new AddSubtractSimulation(this);
            addSubtractSimulation.setData(calculationType, number1, number2);
        } else if (calculationType.equals(Constant.MULTIPLICATION_TYPE)) {
            changedStatus(Constant.HAS_DATA);
            multiplicationSimulation = new MultiplicationSimulation(this);
            multiplicationSimulation.setData(calculationType, number1, number2);
        }
    }

    public JPanel getPnImitiate() {
        return pnImitiate;
    }

    public Thread getThreads(int curT) {
        return threads[curT];
    }

    public void setThreads(Thread thread) {
        this.threads[this.curT] = thread;
        thread.start();
    }

    public int increaseCurT() {
        this.curT++;
        return curT;
    }

    public void SLEEP() throws InterruptedException {
        Thread.sleep(this.time);
    }

    public void READTEXT() throws InterruptedException {
        Thread.sleep(this.time * 2);
    }

    public void MOVETIME() throws InterruptedException {
        int levelTime = (1000 - time) / 100;
        Thread.sleep(100 - levelTime * 10);
    }
}
