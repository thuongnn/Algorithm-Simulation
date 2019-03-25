package dialog;

import extension.Controller;
import helpers.Constant;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.*;
import java.util.Random;

public class MergeSort extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSpinner spNum;
    private JButton btnCreateArray;
    private JButton btnRandomArray;
    private JPanel pnArrayValues;
    private JRadioButton rdIncrease;
    private JRadioButton rdDecrease;

    private Controller controller;
    private JSpinner[] txtArrays;
    private JLabel[] lbArrays;
    private boolean isIncrease = true;
    private int num = 0;
    private int[] arrays;

    public MergeSort(Controller controller) {
        this.controller = controller;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setTitle("Enter array data");

        spNum.setModel(new SpinnerNumberModel(2, 2, 15, 1)); // set min max value for number of arrays
        pnArrayValues.setLayout(null); // set layout of UI display custom values
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdIncrease);
        buttonGroup.add(rdDecrease);

        rdIncrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isIncrease = true;
            }
        });
        rdDecrease.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isIncrease = false;
            }
        });
        btnCreateArray.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                createArray();
            }
        });
        btnRandomArray.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRandom();
            }
        });
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * Create an arrays with size input
     */
    private void createArray() {
        deleteArrays();
        num = (Integer) spNum.getValue();
        arrays = new int[num];
        lbArrays = new JLabel[num];
        txtArrays = new JSpinner[num];

        for (int i = 0; i < num; i++) {
            lbArrays[i] = new JLabel("A[" + i + "]:");
            SpinnerModel smValue = new SpinnerNumberModel(0, Constant.MIN_VALUE_INPUT, Constant.MAX_VALUE_INPUT, Constant.STEP_SIZE);
            txtArrays[i] = new JSpinner(smValue);
            JFormattedTextField txt = ((JSpinner.NumberEditor) txtArrays[i].getEditor()).getTextField();
            ((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);
            pnArrayValues.add(lbArrays[i]);
            pnArrayValues.add(txtArrays[i]);
            lbArrays[i].setSize(40, 30);

            if (i == 0 || i == 5 || i == 10) lbArrays[i].setLocation(150 * (i + 1) / 5, 20);
            else lbArrays[i].setLocation(lbArrays[i - 1].getX(), lbArrays[i - 1].getY() + 40);

            txtArrays[i].setSize(50, 30);
            txtArrays[i].setLocation(lbArrays[i].getX() + 40, lbArrays[i].getY());
        }
        pnArrayValues.setVisible(true);
        pnArrayValues.validate();
        pnArrayValues.repaint();
    }

    /**
     * Create random number into arrays number created
     */
    private void createRandom() {
        Random rand = new Random();
        for (int i = 0; i < num; i++) {
            int ranNum = rand.nextInt(101);
            SpinnerModel smValue = new SpinnerNumberModel(ranNum, Constant.MIN_VALUE_INPUT, Constant.MAX_VALUE_INPUT, Constant.STEP_SIZE);
            txtArrays[i].setModel(smValue);
        }
    }

    /**
     * Delete all arrays in dialog
     */
    private void deleteArrays() {
        for (int i = 0; i < num; i++) {
            lbArrays[i].setVisible(false);
            txtArrays[i].setVisible(false);
            pnArrayValues.remove(lbArrays[i]);
            pnArrayValues.remove(txtArrays[i]);
        }
    }

    private void onOK() {
        if (num > 1) {
            for (int i = 0; i < num; i++) arrays[i] = (int) txtArrays[i].getValue();
            controller.setDataForMergeSortSimulation(isIncrease, arrays);
        }
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
