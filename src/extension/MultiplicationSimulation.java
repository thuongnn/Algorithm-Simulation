package extension;

import helpers.Constant;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

import static helpers.Helper.*;

public class MultiplicationSimulation {
    private Controller controller;
    private String calculationType;
    private BigDecimal number1;
    private BigDecimal number2;

    private JLabel signLabel;
    private JLabel exponentLabel;
    private JLabel mantissaLabel;
    private JLabel[] labelsResult;
    private JLabel[] labelsNum1;
    private JLabel[] resultAdd;
    private JLabel[] labelsNum2;

    private JLabel[] txtLabels;
    private JLabel[] labelsSupport;
    private JLabel txtRemember;

    public MultiplicationSimulation(Controller controller) {
        this.controller = controller;
    }

    public void setData(String calculationType, BigDecimal number1, BigDecimal number2) {
        this.calculationType = calculationType;
        this.number1 = number1;
        this.number2 = number2;
        setupSimulation();
    }

    /**
     * Setup all attribute for simulation
     */
    private void setupSimulation() {
        //delete previous arrays and set number elements of array
        controller.getPnImitiate().removeAll();
        controller.getPnImitiate().revalidate();
        controller.getPnImitiate().repaint();
        int wParent = controller.getPnImitiate().getWidth(), hParent = controller.getPnImitiate().getHeight();
        // Structure: | (15: [element x 1] :15) + 10 | | (15: [element x 8] :15) + 10 | | (15: [element x 25] :15) |
        int wSign = 15 + 25 + 15, wExponent = 15 + 8 * 25 + 15, wMantissa = 15 + 25 * 25 + 15;
        signLabel = createElement("Sign", wSign, 50);
        exponentLabel = createElement("Exponent", wExponent, 50);
        mantissaLabel = createElement("Mantissa", wMantissa, 50);
        int xSign = (wParent - (wSign + 10 + wExponent + 10 + wMantissa)) / 2;
        int xExponent = xSign + wSign + 10;
        int xMantissa = xExponent + wExponent + 10;
        signLabel.setLocation(xSign, 70);
        exponentLabel.setLocation(xExponent, 70);
        mantissaLabel.setLocation(xMantissa, 70);
        signLabel.setBackground(Constant.SIGN);
        exponentLabel.setBackground(Constant.EXPONENT);
        mantissaLabel.setBackground(Constant.MANTISSA);
        controller.getPnImitiate().add(signLabel);
        controller.getPnImitiate().add(exponentLabel);
        controller.getPnImitiate().add(mantissaLabel);
        // Create text label
        txtLabels = new JLabel[4];
        txtLabels[0] = createElement(getTitleCalculation(calculationType, number1, number2), wParent - 6, 50);
        txtLabels[0].setLocation(3, (hParent - 50) / 2);
        txtLabels[0].setVisible(true);
        txtLabels[1] = createElement(number1.toString(), signLabel.getX() - 3, 25);
        txtLabels[1].setLocation(3, 140);
        txtLabels[1].setBackground(Constant.BAYBY_BLUE);
        txtLabels[2] = createElement(number2.toString(), signLabel.getX() - 3, 25);
        txtLabels[2].setLocation(3, 180);
        txtLabels[2].setBackground(Constant.SELECTED_YELLOW);
        txtLabels[3] = createElement(number2.toString(), signLabel.getX() - 3, 50);
        txtLabels[3].setLocation(3, 271);
        txtLabels[3].setBackground(Constant.SELECTED_GREEN);
        for (JLabel jLabel : txtLabels) controller.getPnImitiate().add(jLabel);
        // Create binary row
        labelsNum1 = createUINumber(number1, 140);
        resultAdd = createUINumber(number1, 140);
        labelsNum2 = createUINumber(number2, 180);
        labelsResult = createUINumber(number1, 230);
        // Create support JLabels
        String[] contents = new String[]{"", calculationType, "", "=", "", ""};
        labelsSupport = new JLabel[contents.length];
        int x = (wParent - 350) / 2;
        int y = hParent - 50 - 3;
        for (int i = 0; i < contents.length; i++) {
            labelsSupport[i] = createElement(contents[i], 50, 50);
            labelsSupport[i].setLocation(x, y);
            controller.getPnImitiate().add(labelsSupport[i]);
            x += 50;
        }
        txtRemember = createTxtLabel("");
        txtRemember.setLocation(labelsSupport[4].getX(), y - 50);
        controller.getPnImitiate().add(txtRemember);
        controller.getPnImitiate().setVisible(true);
        controller.getPnImitiate().validate();
        controller.getPnImitiate().repaint();
    }

    /**
     * Create arrays JLabels binary
     *
     * @param number Number need convert and create UI in screen
     * @param y      location of arrays
     * @return arrays JLabels created
     */
    private JLabel[] createUINumber(BigDecimal number, int y) {
        char[] arrBinary = getBinary32(number, true);
        JLabel[] jLabels = new JLabel[34];
        for (int i = 0; i < jLabels.length; i++) {
            jLabels[i] = createBinary(String.valueOf(arrBinary[i]), 25, 25);
            int x;
            if (i == 0) x = signLabel.getX() + 15;
            else if (i == 1) x = exponentLabel.getX() + 15;
            else if (i == 9) x = mantissaLabel.getX() + 15;
            else x = jLabels[i - 1].getX() + 25;
            if (i == 0 || i == 1 || i == 10) setBorder(jLabels[i], 1, 1, 1, 1);
            else setBorder(jLabels[i], 1, 0, 1, 1);
            jLabels[i].setLocation(x, y);
            controller.getPnImitiate().add(jLabels[i]);
        }
        return jLabels;
    }

    public void runSimulation() {
        if (txtLabels[3].isVisible()) return;
        convertBinaryVisualize(); // convert binary (number 1 & number 2)
        calculateSignExponent(); // calculate sign & exponent
        putDownMantissa();
        int count = 24;
        for (int i = labelsNum2.length - 1; i > 9; i--) {
            operation2(count);
            count--;
        }
        reLocation();
        checkAfterMultiply();
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                setContent("Convert data result into float number...");
                while (signLabel.getY() <= 270) {
                    signLabel.setLocation(signLabel.getX(), signLabel.getY() + 1);
                    exponentLabel.setLocation(exponentLabel.getX(), exponentLabel.getY() + 1);
                    mantissaLabel.setLocation(mantissaLabel.getX(), mantissaLabel.getY() + 1);
                    controller.MOVETIME();
                }
                // set result mantissa
                double resultMantissa = 1;
                for (int i = 11; i < labelsResult.length; i++) {
                    resultMantissa += Double.parseDouble(String.valueOf(labelsResult[i].getText())) * Math.pow(2, -(i - 10));
                    labelsResult[i].setBackground(Constant.MANTISSA);
                    controller.MOVETIME();
                }
                controller.SLEEP();
                mantissaLabel.setText(String.valueOf(resultMantissa));
                controller.SLEEP();
                for (int i = labelsResult.length - 1; i > 9; i--) setHidden(labelsResult[i]);
                // set result exponent
                int resultExponent = 0;
                for (int i = 0; i < 8; i++) {
                    resultExponent += Integer.parseInt(labelsResult[i + 1].getText()) * Math.pow(2, 7 - i);
                    labelsResult[i + 1].setBackground(Constant.EXPONENT);
                    controller.MOVETIME();
                }
                controller.SLEEP();
                exponentLabel.setText("2^" + (resultExponent - 127));
                controller.SLEEP();
                for (int i = 8; i > 0; i--) setHidden(labelsResult[i]);
                // set result sign
                labelsResult[0].setBackground(Constant.SIGN);
                controller.SLEEP();
                String sign;
                if (labelsResult[0].getText().equals("0")) sign = "+";
                else sign = "-";
                signLabel.setText(sign);
                controller.SLEEP();
                setHidden(labelsResult[0]);
                controller.SLEEP();
                double result = Math.pow(2, resultExponent - 127) * resultMantissa;
                txtLabels[3].setText(sign + result);
                txtLabels[3].setVisible(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Check if E of mantissa equals 1, shift right & increment exponent
     */
    private void checkAfterMultiply() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                labelsResult[9].setBackground(Constant.PROCESSING_COLOR);
                setContent("Checking E if E equals 1 then shifting left, if not two's complement before shifting left");
                if (labelsResult[9].getText().equals("1")) {
                    setContent("Because E equals 1");
                    setContent("Shifting right and increase exponent");
                    int e = 0;
                    for (int i = 1; i < 9; i++) e += Integer.parseInt(labelsResult[i].getText()) * Math.pow(2, 8 - i);
                    char[] exponentBinary = getBinaryInteger(e + 1);
                    for (int j = 1; j < 9; j++) {
                        labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                        labelsResult[j].setText(String.valueOf(exponentBinary[j - 1]));
                        controller.MOVETIME();
                        labelsResult[j].setBackground(Constant.SELECTED_GREEN);
                    }

                    String[] oldData = new String[labelsResult.length];
                    for (int j = 0; j < oldData.length; j++) oldData[j] = labelsResult[j].getText();

                    for (int j = 9; j < labelsResult.length; j++) {
                        labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                        if (j == 9) labelsResult[9].setText("0");
                        else labelsResult[j].setText(oldData[j - 1]);
                        controller.MOVETIME();
                        if (j == 9) setHidden(labelsResult[j]);
                        else labelsResult[j].setBackground(Constant.SELECTED_GREEN);
                    }
                    controller.SLEEP();
                }
                setHidden(labelsResult[9]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Calculate sign & exponent of two's number
     */
    private void calculateSignExponent() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                runCalculate(0); // calculate sign
                int e1 = 0, e2 = 0;
                for (int i = 1; i < 9; i++) {
                    e1 += Integer.parseInt(labelsNum1[i].getText()) * Math.pow(2, 8 - i);
                    e2 += Integer.parseInt(labelsNum2[i].getText()) * Math.pow(2, 8 - i);
                }
                int e = (e1 + e2) - 127;
                char[] exponentBinary = getBinaryInteger(e);
                setContent("Result Exponent: e = " + e1 + " + " + e2 + " - bias = " + (e1 + e2) + " - bias = " + e);
                for (int i = 8; i > 0; i--) {
                    labelsResult[i].setVisible(true);
                    labelsResult[i].setText(String.valueOf(exponentBinary[i - 1]));
                    if (i != 8) setBorder(labelsResult[i + 1], 1, 0, 1, 1);
                    setBorder(labelsResult[i], 1, 1, 1, 1);
                    controller.SLEEP();
                    labelsResult[i].setBackground(Constant.SELECTED_GREEN);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Reset location of result mantissa
     */
    private void reLocation() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                controller.SLEEP();
                for (int i = labelsNum1.length - 1; i > 8; i--) {
                    labelsNum1[i].setVisible(false);
                    labelsNum2[i].setVisible(false);
                    controller.MOVETIME();
                }
                while (labelsResult[9].getY() > labelsNum1[9].getY()) {
                    for (int i = 9; i < labelsResult.length; i++) {
                        labelsResult[i].setLocation(labelsResult[i].getX(), labelsResult[i].getY() - 1);
                    }
                    controller.MOVETIME();
                }
                while (labelsResult[9].getX() < mantissaLabel.getX() + 15) {
                    for (int i = 9; i < labelsResult.length; i++) {
                        labelsResult[i].setLocation(labelsResult[i].getX() + 1, labelsResult[i].getY());
                    }
                    controller.MOVETIME();
                }
                while (labelsResult[9].getY() > labelsResult[8].getY()) {
                    for (int i = 9; i < labelsResult.length; i++) {
                        labelsResult[i].setLocation(labelsResult[i].getX(), labelsResult[i].getY() - 1);
                    }
                    controller.MOVETIME();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Put down mantissa & start to multiply mantissa
     */
    private void putDownMantissa() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                setContent("Put down mantissa of number 1 & number 2");
                labelsNum2[9].setBackground(Constant.PROCESSING_COLOR);
                controller.SLEEP();
                labelsNum2[9].setVisible(false);
                while (labelsNum1[9].getY() != 290) {
                    for (int i = 9; i < labelsNum1.length; i++) {
                        labelsResult[i].setLocation(labelsResult[i].getX(), labelsResult[i].getY() + 1);
                        resultAdd[i].setLocation(resultAdd[i].getX(), resultAdd[i].getY() + 1);
                        labelsNum1[i].setLocation(labelsNum1[i].getX(), labelsNum1[i].getY() + 1);
                        labelsNum2[i].setLocation(labelsNum2[i].getX(), labelsNum2[i].getY() + 1);
                    }
                    controller.MOVETIME();
                }
                int x1 = (controller.getPnImitiate().getWidth() - 48 * 25) / 2;
                int x2 = controller.getPnImitiate().getWidth() - x1;
                while (labelsNum1[9].getX() > x1) {
                    for (int i = 9; i < labelsNum1.length; i++) {
                        labelsResult[i].setLocation(labelsResult[i].getX() - 1, labelsResult[i].getY());
                        resultAdd[i].setLocation(resultAdd[i].getX() - 1, resultAdd[i].getY());
                        labelsNum1[i].setLocation(labelsNum1[i].getX() - 1, labelsNum1[i].getY());
                    }
                    if (labelsNum2[labelsNum2.length - 1].getX() < x2 - 1)
                        for (int i = 9; i < labelsNum1.length; i++)
                            labelsNum2[i].setLocation(labelsNum2[i].getX() + 1, labelsNum2[i].getY());
                    controller.MOVETIME();
                }
                for (int i = labelsResult.length - 1; i > 8; i--) {
                    labelsResult[i].setLocation(labelsResult[i].getX(), labelsNum2[10].getY());
                    if (i == 9) setHidden(labelsResult[i]);
                    else if (i == 10) labelsResult[i].setBackground(Constant.SELECTED_GREEN);
                    else labelsResult[i].setBackground(Constant.HIDDEN_COLOR_FOREGROUND);
                    labelsResult[i].setText("0");
                    labelsResult[i].setVisible(true);
                    controller.MOVETIME();
                }
                for (int i = 9; i < resultAdd.length; i++)
                    resultAdd[i].setLocation(resultAdd[i].getX(), labelsResult[i].getY() + 40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    private void operation2(int count) {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                labelsNum2[labelsNum2.length - 1].setBackground(Constant.PROCESSING_COLOR);
                setContent("Check if Qn equals 0, shifting right EAQ. If Qn equals 1, adding EA before shifting right EAQ !");
                labelsNum2[labelsNum2.length - 1].setBackground(Constant.PROCESSING_COLOR);
                if (labelsNum2[labelsNum2.length - 1].getText().equals("0")) {
                    setContent("Qn equals 0, shifting right...");
                    shiftingRight(count);
                } else {
                    setContent("Qn equals 1, adding EA");
                    for (int i = labelsNum1.length - 1; i > 8; i--) {
                        runCalculate(i);
                    }
                    setContent("Adding to result");
                    addToResult();
                    System.out.println(count);
                    if (count != 1) {
                        setContent("Shifting right...");
                        shiftingRight(count);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * After addition, add result into count JLabels
     *
     * @throws InterruptedException
     */
    private void addToResult() throws InterruptedException {
        for (int i = labelsResult.length - 1; i > 8; i--) {
            Color defaultColor = labelsResult[i].getBackground();
            labelsResult[i].setBackground(Constant.PROCESSING_COLOR);
            resultAdd[i].setBackground(Constant.PROCESSING_COLOR);
            controller.MOVETIME();
            labelsResult[i].setText(resultAdd[i].getText());
            labelsResult[i].setBackground(defaultColor);
            resultAdd[i].setVisible(false);
        }
        controller.SLEEP();
    }

    /**
     * Calculate binary
     * If index equals 0, it's XOR calculation for sign
     *
     * @param i index
     * @throws InterruptedException error of thread
     */
    private void runCalculate(int i) throws InterruptedException {
        JLabel n1 = labelsNum1[i];
        JLabel n2;
        JLabel re;
        if (i == 0) { // for xor calculation
            re = labelsResult[i];
            n2 = labelsNum2[i];
        } else {
            re = resultAdd[i];
            n2 = labelsResult[i];
        }

        Color defaultColor = n2.getBackground();
        // STEP 1: set background color number1
        n1.setBackground(Constant.PROCESSING_COLOR);
        labelsSupport[0].setBackground(Constant.BAYBY_BLUE);
        labelsSupport[0].setText(n1.getText());
        labelsSupport[0].setVisible(true);
        controller.SLEEP();
        // STEP 2: set operation of number1 & number2
        if (i == 0) labelsSupport[1].setText(Constant.XOR_TYPE); // for xor calculation
        labelsSupport[1].setText(Constant.ADDITION_TYPE);
        labelsSupport[1].setBackground(Constant.SELECTED_BEIGE);
        labelsSupport[1].setVisible(true);
        controller.SLEEP();
        // STEP 3: set background color for number2
        n2.setBackground(Constant.PROCESSING_COLOR);
        labelsSupport[2].setBackground(defaultColor);
        labelsSupport[2].setText(n2.getText());
        labelsSupport[2].setVisible(true);
        controller.SLEEP();
        // STEP 4: set equals for operation
        labelsSupport[3].setBackground(Constant.SELECTED_BEIGE);
        labelsSupport[3].setVisible(true);
        controller.SLEEP();
        // STEP 5: show result of sub calculate
        String type;
        if (i == 0) type = Constant.XOR_TYPE; // for xor calculation
        else type = Constant.ADDITION_TYPE;
        char[] dataResult = calculateBinary(type, n1.getText(), n2.getText());
        labelsSupport[dataResult.length + 3].setText(String.valueOf(dataResult[dataResult.length - 1]));
        labelsSupport[dataResult.length + 3].setVisible(true);
        if (dataResult.length == 2) {
            labelsSupport[4].setText(String.valueOf(dataResult[0]));
            labelsSupport[4].setVisible(true);
        }
        controller.SLEEP();
        // STEP 6: check remember number, if have remember number => calculate result again
        if (txtRemember.getText().equals("Remember: 1")) {
            labelsSupport[dataResult.length + 3].setForeground(Constant.SELECTED_BLUE);
            txtRemember.setForeground(Constant.SELECTED_BLUE);
            controller.SLEEP();
            txtRemember.setText("");
            if (dataResult.length == 1) {
                dataResult = calculateBinary(Constant.ADDITION_TYPE, String.valueOf(dataResult[0]), "1");
                labelsSupport[dataResult.length + 3].setText(String.valueOf(dataResult[dataResult.length - 1]));
            } else labelsSupport[5].setText("1");
            setDefault(labelsSupport[4]);
            setDefault(labelsSupport[5]);
            controller.SLEEP();
        }
        // STEP 7: get result number of sub calculate to main calculate
        labelsSupport[dataResult.length + 3].setBackground(Constant.SELECTED_GREEN);
        if (i == 0) { // for xor calculation
            labelsResult[i].setVisible(true);
            labelsResult[i].setBackground(Constant.SELECTED_GREEN);
            labelsResult[i].setText(labelsSupport[dataResult.length + 3].getText());
        } else showResult(i, labelsSupport[dataResult.length + 3].getText());
        controller.SLEEP();
        // STEP 8: set default background for sub and main calculate
        labelsSupport[4].setBackground(controller.getPnImitiate().getBackground());
        labelsSupport[5].setBackground(controller.getPnImitiate().getBackground());
        if (i != 0) re.setBackground(controller.getPnImitiate().getBackground()); // for xor calculation
        controller.SLEEP();
        // STEP 9: if result of sub calculate is 2 number => remember first number
        if (dataResult.length == 2) {
            txtRemember.setVisible(true);
            txtRemember.setText("Remember: 1");
            txtRemember.setForeground(Constant.SELECTED_BLUE);
            labelsSupport[4].setForeground(Constant.SELECTED_BLUE);
            controller.SLEEP();
        }
        // STEP 10: remove select of 2 number needs calculate in main calculate
        for (JLabel jLabel : labelsSupport) {
            jLabel.setVisible(false);
            jLabel.setForeground(Constant.COLOR_TEXT);
            jLabel.setBackground(controller.getPnImitiate().getBackground());
        }
        if (i == 9) {
            setHidden(n1);
            setHidden(n2);
        } else {
            n1.setBackground(Constant.BAYBY_BLUE);
            n2.setBackground(defaultColor);
        }
        setDefault(txtRemember);
        controller.SLEEP();
    }

    /**
     * Set default JLabel to default attribute
     *
     * @param jLabel JLabel need to set default
     */
    private void setDefault(JLabel jLabel) {
        jLabel.setVisible(true);
        jLabel.setBackground(controller.getPnImitiate().getBackground());
        jLabel.setForeground(Constant.COLOR_TEXT);
    }

    /**
     * Shifting right after count
     *
     * @param count
     * @throws InterruptedException
     */
    private void shiftingRight(int count) throws InterruptedException {
        StringBuilder str = new StringBuilder();
        for (int i = 9; i < labelsResult.length; i++) str.append(labelsResult[i].getText());
        for (int i = 10; i < labelsNum2.length; i++) str.append(labelsNum2[i].getText());
        char[] oldData = str.toString().toCharArray();
        for (int i = 10; i < labelsResult.length; i++) { // arrays labels result start 10 -> end
            labelsResult[i].setBackground(Constant.PROCESSING_COLOR);
            if (i == 10) labelsResult[i - 1].setText("0");
            labelsResult[i].setText(String.valueOf(oldData[i - 10])); // old data start 0 -> length of mantissa
            controller.MOVETIME();
            if (i - 10 <= 24 - (count - 1)) labelsResult[i].setBackground(Constant.SELECTED_GREEN);
            else labelsResult[i].setBackground(Constant.HIDDEN_COLOR_FOREGROUND);
        }
        int countData = labelsResult.length - 10; // real length of old data after load complete arrays labels result
        for (int i = 10; i < labelsNum2.length; i++) {
            labelsNum2[i].setBackground(Constant.PROCESSING_COLOR);
            labelsNum2[i].setText(String.valueOf(oldData[countData]));
            controller.MOVETIME();
            if (i - 10 <= 24 - count) // 24 - count: check index mantissa have counted
                labelsNum2[i].setBackground(Constant.HIDDEN_COLOR_FOREGROUND);
            else labelsNum2[i].setBackground(Constant.SELECTED_YELLOW);
            countData++;
        }
    }

    /**
     * Visualize 2 number in main screen
     */
    private void convertBinaryVisualize() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                txtLabels[0].setLocation(3, 15);
                controller.SLEEP();
                signLabel.setVisible(true);
                exponentLabel.setVisible(true);
                mantissaLabel.setVisible(true);
                controller.SLEEP();
                txtLabels[1].setVisible(true);
                setContent("Convert number 1 to binary");
                convertBinary(labelsNum1, Constant.BAYBY_BLUE);
                txtLabels[2].setVisible(true);
                setContent("Convert number 2 to binary");
                convertBinary(labelsNum2, Constant.SELECTED_YELLOW);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Show JLabel of result calculate on screen
     *
     * @param index   index of arrays JLabels result
     * @param content content for JLabel
     */
    private void showResult(int index, String content) {
        showResultHelper(index, content, resultAdd, controller);
    }

    /**
     * Set content of each step in simulation
     *
     * @param content content need to set
     * @throws InterruptedException error of thread
     */
    private void setContent(String content) throws InterruptedException {
        setContentHelper(content, txtLabels, controller);
    }
}
