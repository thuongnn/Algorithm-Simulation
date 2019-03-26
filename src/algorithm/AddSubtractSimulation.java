package algorithm;

import extension.Controller;
import helpers.Constant;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;

import static helpers.Helper.*;

public class AddSubtractSimulation {
    private Controller controller;
    private String calculationType;
    private BigDecimal number1;
    private BigDecimal number2;
    private boolean isTwosComplement = false;
    private boolean isSubtraction = false;

    private JLabel signLabel;
    private JLabel exponentLabel;
    private JLabel mantissaLabel;
    private JLabel[] labelsNum1;
    private JLabel[] labelsNum2;
    private JLabel[] labelsResult;

    private JLabel[] txtLabels;
    private JLabel[] labelsSupport;
    private JLabel txtRemember;

    public AddSubtractSimulation(Controller controller) {
        this.controller = controller;
    }

    public void setData(String calculationType, BigDecimal number1, BigDecimal number2) {
        this.calculationType = calculationType;
        this.number1 = number1;
        this.number2 = number2;
        setRealCalculate();
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
        int wSign = 15 + 30 + 15, wExponent = 15 + 8 * 30 + 15, wMantissa = 15 + 26 * 30 + 15;
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
        txtLabels = new JLabel[6];
        txtLabels[0] = createElement(getTitleCalculation(calculationType, number1, number2), wParent - 6, 50);
        txtLabels[0].setLocation(3, (hParent - 50) / 2);
        txtLabels[0].setVisible(true);
        txtLabels[1] = createElement(number1.toString(), signLabel.getX() - 3, 30);
        txtLabels[1].setLocation(3, 140);
        txtLabels[1].setBackground(Constant.BAYBY_BLUE);
        txtLabels[2] = createElement(number2.toString(), signLabel.getX() - 3, 30);
        txtLabels[2].setLocation(3, 180);
        txtLabels[2].setBackground(Constant.SELECTED_YELLOW);
        txtLabels[3] = createElement(number2.toString(), signLabel.getX() - 3, 50);
        txtLabels[3].setLocation(3, 271);
        txtLabels[3].setBackground(Constant.SELECTED_GREEN);
        txtLabels[4] = createElement("", 60, 30);
        txtLabels[4].setLocation(mantissaLabel.getX() + 15, 290);
        txtLabels[4].setHorizontalAlignment(SwingConstants.LEFT);
        txtLabels[5] = createElement("", 60, 30);
        txtLabels[5].setLocation(mantissaLabel.getX() + 15, 330);
        txtLabels[5].setHorizontalAlignment(SwingConstants.LEFT);
        for (JLabel jLabel : txtLabels) controller.getPnImitiate().add(jLabel);
        // Create binary row
        labelsNum1 = createUINumber(number1, 140);
        labelsNum2 = createUINumber(number2, 180);
        labelsResult = createUINumber(number1, 230);
        String resultSign = String.valueOf(calculateBinary(Constant.XOR_TYPE, labelsNum1[0].getText(), labelsNum2[0].getText())[0]);
        isTwosComplement = calculationType.equals(Constant.ADDITION_TYPE) && resultSign.equals("1")
                || calculationType.equals(Constant.SUBTRACTION_TYPE) && resultSign.equals("0");
        isSubtraction = calculationType.equals(Constant.SUBTRACTION_TYPE);
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
        char[] arrBinary = getBinary32(number, false);
        JLabel[] jLabels = new JLabel[35];
        for (int i = 0; i < jLabels.length; i++) {
            jLabels[i] = createBinary(String.valueOf(arrBinary[i]), 30, 30);
            int x;
            if (i == 0) x = signLabel.getX() + 15;
            else if (i == 1) x = exponentLabel.getX() + 15;
            else if (i == 9) x = mantissaLabel.getX() + 15;
            else x = jLabels[i - 1].getX() + 30;
            if (i == 0 || i == 1 || i == 10) jLabels[i].setBorder(new MatteBorder(1, 1, 1, 1, Constant.COLOR_TEXT));
            else jLabels[i].setBorder(new MatteBorder(1, 0, 1, 1, Constant.COLOR_TEXT));
            jLabels[i].setLocation(x, y);
            controller.getPnImitiate().add(jLabels[i]);
        }
        return jLabels;
    }

    public void runSimulation() {
        if (txtLabels[3].isVisible()) return;
        convertBinaryVisualize();
        if (isZero(number1) || isZero(number2)) checkForZero();
        else {
            alignMantissas(); // balance exponent
            runCalculate(0); // calculate sign
            if (isTwosComplement) {
                int cur = controller.increaseCurT();
                Thread thread = new Thread(() -> {
                    try {
                        if (cur != 0) controller.getThreads(cur - 1).join();
                        twosComplement(labelsNum2, Constant.SELECTED_YELLOW);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                controller.setThreads(thread);
            }
            for (int i = 8; i > 0; i--) copyPaste(i);
            for (int i = labelsNum1.length - 1; i >= 9; i--)
                if (labelsNum1[i].getText().equals(".")) copyPaste(i);
                else runCalculate(i);

            if (isTwosComplement) subtractionCase();
            else additionCase();
        }

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
                double resultMantissa = 0;
                for (int i = 10; i < labelsResult.length; i++) {
                    if (i > 11)
                        resultMantissa += Double.parseDouble(String.valueOf(labelsResult[i].getText())) * Math.pow(2, -(i - 11));
                    else if (i == 10 && labelsResult[i].getText().equals("1")) resultMantissa += 1;
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
                if (isZero(new BigDecimal(result))) txtLabels[3].setText(String.valueOf(result));
                else txtLabels[3].setText(sign + result);
                txtLabels[3].setVisible(true);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Visualize 2 number in main screen
     */
    private void convertBinaryVisualize() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                txtLabels[0].setLocation(3, 20);
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
     * Visualize for the case: number 1 or number 2 may be zero
     */
    private void checkForZero() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                JLabel[] jLabels;
                if (isZero(number2)) {
                    jLabels = labelsNum1;
                    processZero(labelsNum2, 2);
                } else {
                    jLabels = labelsNum2;
                    processZero(labelsNum1, 1);
                }
                for (JLabel jLabel : jLabels) jLabel.setBackground(controller.getPnImitiate().getBackground());
                controller.READTEXT();
                for (int i = jLabels.length - 1; i >= 0; i--) {
                    showResult(i, jLabels[i].getText());
                    setHidden(jLabels[i]);
                    controller.SLEEP();
                }
                if (isSubtraction) {
                    labelsResult[0].setBackground(Constant.PROCESSING_COLOR);
                    setContent("Invert sign of result (with subtraction)");
                    if (labelsResult[0].getText().equals("0")) showResult(0, "1");
                    else showResult(0, "0");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * If calculate type is subtraction, calculate sign is 1
     * If calculate type is addition, calculate sign is 0
     */
    private void additionCase() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                if (labelsResult[9].getText().equals("1")) {
                    labelsResult[9].setBackground(Constant.PROCESSING_COLOR);
                    setContent("Because A1 equals 1");
                    setContent("Shifting right and increase exponent");
                    int e = getExponent();
                    char[] exponentBinary = getBinaryInteger(e + 1);
                    for (int j = 1; j < 9; j++) {
                        labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                        labelsResult[j].setText(String.valueOf(exponentBinary[j - 1]));
                        controller.MOVETIME();
                        labelsResult[j].setBackground(controller.getPnImitiate().getBackground());
                    }

                    String[] oldData = new String[labelsResult.length];
                    for (int j = 0; j < oldData.length; j++) oldData[j] = labelsResult[j].getText();

                    for (int j = 9; j < labelsResult.length; j++) {
                        labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                        if (j == 9) {
                            labelsResult[9].setText("0");
                            setHidden(labelsResult[9]);
                            setBorder(labelsResult[10], 1, 1, 1, 1);
                        } else if (j == 12) labelsResult[j].setText(oldData[10]);
                        else if (j != 11) labelsResult[j].setText(oldData[j - 1]);
                        controller.MOVETIME();
                        if (j != 10 && j != 9)
                            labelsResult[j].setBackground(controller.getPnImitiate().getBackground());
                    }
                    controller.SLEEP();
                    labelsResult[10].setBackground(controller.getPnImitiate().getBackground());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * If calculate type is subtraction, calculate sign is 0
     * If calculate type is addition, calculate sign is 1
     */
    private void subtractionCase() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                labelsResult[9].setBackground(Constant.PROCESSING_COLOR);
                if (labelsResult[9].getText().equals("1") && isZeroMantissa(labelsResult)) {
                    setContent("Because E equals 1");
                    for (int i = 10; i < labelsResult.length; i++) {
                        labelsResult[i].setBackground(Constant.PROCESSING_COLOR);
                        controller.MOVETIME();
                    }
                    setContent("Because E equals 1, A is equals 0");
                    setContent("The result of this calculation will be zero.");
                    for (int i = 10; i < labelsResult.length; i++) {
                        labelsResult[i].setBackground(controller.getPnImitiate().getBackground());
                        controller.MOVETIME();
                    }
                } else {
                    setContent("Checking E if E equals 1 then shifting left, if E equals 0 then two's complement before shifting left.");
                    if (labelsResult[9].getText().equals("1")) {
                        setContent("Because E equals 1, A is not equals to 0");
                        labelsResult[9].setBackground(controller.getPnImitiate().getBackground());
                    } else {
                        setContent("Because E equals 0");
                        twosComplement(labelsResult, controller.getPnImitiate().getBackground());
                        labelsResult[0].setBackground(Constant.PROCESSING_COLOR);
                        setContent("Invert sign of result (with subtraction)");
                        if (labelsResult[0].getText().equals("0")) showResult(0, "1");
                        else showResult(0, "0");
                    }
                    setContent("Shifting left until A1 equals 1");
                    shiftingLeft();
                }
                setHidden(labelsResult[9]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Shifting number on the right
     *
     * @param jLabels arrays JLabels need to shifting
     * @param n       number time of shifting
     * @param num     JLabel show result calculate exponent
     * @param color   default color after shifting
     * @throws InterruptedException error of thread
     */
    private void shiftingRight(JLabel[] jLabels, int n, JLabel num, Color color) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            int increaseNum = Integer.parseInt(num.getText().replace("~ ", "")) + 1;
            char[] exponentBinary = getBinaryInteger(increaseNum);
            for (int j = 1; j < 9; j++) {
                jLabels[j].setBackground(Constant.PROCESSING_COLOR);
                jLabels[j].setText(String.valueOf(exponentBinary[j - 1]));
                controller.MOVETIME();
                jLabels[j].setBackground(color);
            }
            num.setText("~ ".concat(String.valueOf(increaseNum)));
            num.setForeground(Constant.PROCESSING_COLOR);
            controller.MOVETIME();
            String[] oldData = new String[jLabels.length];
            for (int j = 0; j < oldData.length; j++) oldData[j] = jLabels[j].getText();
            for (int j = 12; j < jLabels.length; j++) {
                jLabels[j].setBackground(Constant.PROCESSING_COLOR);
                if (j == 12) {
                    jLabels[j].setText(oldData[10]);
                    jLabels[10].setText("0");
                } else {
                    jLabels[j].setText(oldData[j - 1]);
                }
                controller.MOVETIME();
                jLabels[j].setBackground(color);
            }
        }
    }

    /**
     * Shifting on the right if A1 = 0
     *
     * @throws InterruptedException error of thread
     */
    private void shiftingLeft() throws InterruptedException {
        while (labelsResult[10].getText().equals("0")) {
            labelsResult[10].setBackground(Constant.PROCESSING_COLOR);
            setContent("Because A1 not equals 0, then continue shifting left");
            labelsResult[10].setBackground(controller.getPnImitiate().getBackground());
            setContent("Shifting left...");
            int e = getExponent();
            char[] exponentBinary = getBinaryInteger(e - 1);
            for (int j = 1; j < 10; j++) {
                if (j < 9) {
                    labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                    labelsResult[j].setText(String.valueOf(exponentBinary[j - 1]));
                }
                controller.MOVETIME();
                if (j == 9) {
                    labelsResult[j].setText("0");
                    setHidden(labelsResult[j]);
                } else {
                    labelsResult[j].setBackground(controller.getPnImitiate().getBackground());
                }
            }
            String[] oldData = new String[labelsResult.length];
            for (int j = 0; j < oldData.length; j++) oldData[j] = labelsResult[j].getText();
            for (int j = 10; j < labelsResult.length; j++) {
                labelsResult[j].setBackground(Constant.PROCESSING_COLOR);
                if (j == 10) labelsResult[j].setText(oldData[12]);
                else if (j == labelsResult.length - 1) labelsResult[j].setText("0");
                else if (j != 11) labelsResult[j].setText(oldData[j + 1]);
                controller.MOVETIME();
                labelsResult[j].setBackground(controller.getPnImitiate().getBackground());
            }
        }
        labelsResult[10].setBackground(Constant.SELECTED_GREEN);
        txtLabels[0].setText("A1 equals 1, finish shifting left");
        controller.SLEEP();
        labelsResult[10].setBackground(controller.getPnImitiate().getBackground());
    }

    /**
     * The smaller exponent is incremented and the mantissa is shifted right until the exponents are equal
     */
    private void alignMantissas() {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(() -> {
            try {
                if (cur != 0) controller.getThreads(cur - 1).join();
                setContent("Put down exponent part and exponential balance");
                putExponent(true);
                txtLabels[4].setVisible(true);
                txtLabels[5].setVisible(true);
                int e1 = 0, e2 = 0;
                for (int i = 1; i < 9; i++) {
                    e1 += Integer.parseInt(labelsNum1[i].getText()) * Math.pow(2, 8 - i);
                    e2 += Integer.parseInt(labelsNum2[i].getText()) * Math.pow(2, 8 - i);
                    labelsNum1[i].setBackground(Constant.SELECTED_GREEN);
                    labelsNum2[i].setBackground(Constant.SELECTED_GREEN);
                    txtLabels[4].setText("~ ".concat(String.valueOf(e1)));
                    txtLabels[5].setText("~ ".concat(String.valueOf(e2)));
                    controller.SLEEP();
                    labelsNum1[i].setBackground(txtLabels[1].getBackground());
                    labelsNum2[i].setBackground(txtLabels[2].getBackground());
                    controller.SLEEP();
                }
                if (e1 < e2) {
                    txtLabels[4].setForeground(Constant.PROCESSING_COLOR);
                    putMantissa(labelsNum1, true);
                    controller.SLEEP();
                    setContent("The smaller exponent is incremented and the mantissa is shifted right until the exponents are equal");
                    shiftingRight(labelsNum1, e2 - e1, txtLabels[4], Constant.BAYBY_BLUE);
                } else if (e1 > e2) {
                    txtLabels[5].setForeground(Constant.PROCESSING_COLOR);
                    putMantissa(labelsNum2, true);
                    controller.SLEEP();
                    setContent("The smaller exponent is incremented and the mantissa is shifted right until the exponents are equal");
                    shiftingRight(labelsNum2, e1 - e2, txtLabels[5], Constant.SELECTED_YELLOW);
                }
                txtLabels[4].setForeground(Constant.PROCESSING_COLOR);
                txtLabels[5].setForeground(Constant.PROCESSING_COLOR);
                controller.SLEEP();
                txtLabels[4].setVisible(false);
                txtLabels[5].setVisible(false);
                controller.SLEEP();
                putExponent(false);
                if (e1 < e2) putMantissa(labelsNum1, false);
                else if (e1 > e2) putMantissa(labelsNum2, false);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Processing calculate xor or addition
     *
     * @param index index of column need to calculate
     */
    private void runCalculate(int index) {
        JLabel n1 = labelsNum1[index];
        JLabel n2 = labelsNum2[index];
        JLabel re = labelsResult[index];
        int cur = controller.increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cur != 0) controller.getThreads(cur - 1).join();
                    // STEP 1: set background color number1
                    n1.setBackground(controller.getPnImitiate().getBackground());
                    labelsSupport[0].setBackground(Constant.BAYBY_BLUE);
                    labelsSupport[0].setText(n1.getText());
                    labelsSupport[0].setVisible(true);
                    controller.SLEEP();
                    // STEP 2: set operation of number1 & number2
                    if (index == 0) labelsSupport[1].setText(Constant.XOR_TYPE);
                    else labelsSupport[1].setText(Constant.ADDITION_TYPE);
                    labelsSupport[1].setBackground(Constant.SELECTED_BEIGE);
                    labelsSupport[1].setVisible(true);
                    controller.SLEEP();
                    // STEP 3: set background color for number2
                    n2.setBackground(controller.getPnImitiate().getBackground());
                    labelsSupport[2].setBackground(Constant.SELECTED_YELLOW);
                    labelsSupport[2].setText(n2.getText());
                    labelsSupport[2].setVisible(true);
                    controller.SLEEP();
                    // STEP 4: set equals for operation
                    labelsSupport[3].setBackground(Constant.SELECTED_BEIGE);
                    labelsSupport[3].setVisible(true);
                    controller.SLEEP();
                    // STEP 5: show result of sub calculate
                    String type;
                    if (index == 0) type = Constant.XOR_TYPE;
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
                        if (dataResult.length == 2) setDefault(labelsSupport[5]);
                        controller.SLEEP();
                    }
                    // STEP 7: get result number of sub calculate to main calculate
                    labelsSupport[dataResult.length + 3].setBackground(Constant.SELECTED_GREEN);
                    showResult(index, labelsSupport[dataResult.length + 3].getText());
                    controller.SLEEP();
                    // STEP 8: set default background for sub and main calculate
                    labelsSupport[4].setBackground(controller.getPnImitiate().getBackground());
                    labelsSupport[5].setBackground(controller.getPnImitiate().getBackground());
                    re.setBackground(controller.getPnImitiate().getBackground());
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
                    setHidden(n1);
                    setHidden(n2);
                    setDefault(txtRemember);
                    controller.SLEEP();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        controller.setThreads(thread);
    }

    /**
     * Copy and paste to JLabel result
     *
     * @param index index of JLabel need to copy paste
     */
    private void copyPaste(int index) {
        int cur = controller.increaseCurT();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (cur != 0) controller.getThreads(cur - 1).join();
                    labelsNum1[index].setBackground(controller.getPnImitiate().getBackground());
                    labelsNum2[index].setBackground(controller.getPnImitiate().getBackground());
                    controller.SLEEP();
                    showResult(index, labelsNum1[index].getText());
                    controller.SLEEP();
                    setHidden(labelsNum1[index]);
                    setHidden(labelsNum2[index]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        controller.setThreads(thread);
    }

    private void setDefault(JLabel jLabel) {
        jLabel.setVisible(true);
        jLabel.setBackground(controller.getPnImitiate().getBackground());
        jLabel.setForeground(Constant.COLOR_TEXT);
    }

    /**
     * Convert all arrays JLabels 1 >> 0 & 0 >> 1
     *
     * @param jLabels      JLabels need to use two's complement
     * @param defaultColor default color after two's complement
     * @throws InterruptedException error of thread
     */
    private void twosComplement(JLabel[] jLabels, Color defaultColor) throws InterruptedException {
        setContent("Two's complement processing");
        boolean check = false;
        String[] oldData = new String[jLabels.length];
        for (int j = 0; j < oldData.length; j++) oldData[j] = jLabels[j].getText();
        for (int i = jLabels.length - 1; i >= 10; i--) {
            jLabels[i].setBackground(Constant.PROCESSING_COLOR);
            if (check) {
                if (oldData[i].equals("0")) jLabels[i].setText("1");
                else if (oldData[i].equals("1")) jLabels[i].setText("0");
            }
            if (!check && jLabels[i].getText().equals("1")) check = true;
            controller.MOVETIME();
            jLabels[i].setBackground(defaultColor);
        }
    }

    /**
     * Put down (or up) mantissa part to shifting binary
     *
     * @param jLabels   arrays JLabels need to shiftings
     * @param isPutDown check mantissa up (or down)
     * @throws InterruptedException error of thread
     */
    private void putMantissa(JLabel[] jLabels, boolean isPutDown) throws InterruptedException {
        int y;
        if (isPutDown) y = 250;
        else if (jLabels == labelsNum1) y = labelsNum1[0].getY();
        else y = labelsNum2[0].getY();
        while (jLabels[9].getY() != y) {
            for (int i = 9; i < jLabels.length; i++) {
                int newY;
                if (isPutDown) newY = jLabels[i].getY() + 1;
                else newY = jLabels[i].getY() - 1;

                jLabels[i].setLocation(jLabels[i].getX(), newY);
            }
            controller.MOVETIME();
        }
        controller.SLEEP();
    }

    /**
     * Put down (or up) exponent part to balance exponent
     *
     * @param isDown check to set function is up (or down)
     * @throws InterruptedException error of thread
     */
    private void putExponent(boolean isDown) throws InterruptedException {
        boolean check = true;
        int x = 1;
        if (!isDown) x = -1;
        while (check) {
            for (int i = 1; i < 9; i++) {
                labelsNum1[i].setLocation(labelsNum1[i].getX(), labelsNum1[i].getY() + x);
                labelsNum2[i].setLocation(labelsNum2[i].getX(), labelsNum2[i].getY() + x);
            }
            if (isDown && labelsNum1[1].getY() == 290) check = false;
            else if (!isDown && labelsNum1[1].getY() == 140) check = false;
            controller.MOVETIME();
        }
        controller.SLEEP();
    }

    /**
     * Visualize zero number process
     *
     * @param zeroLabels array JLabels of number is zero
     * @param number     describe number zero
     * @throws InterruptedException throw error of SLEEP function
     */
    private void processZero(JLabel[] zeroLabels, int number) throws InterruptedException {
        for (JLabel jLabel : zeroLabels) jLabel.setBackground(Constant.PROCESSING_COLOR);
        setContent("Number " + number + " equals 0, Skip number " + number);
        setHidden(txtLabels[number]);
        for (JLabel jLabel : zeroLabels) setHidden(jLabel);
    }

    /**
     * Show JLabel of result calculate on screen
     *
     * @param index   index of arrays JLabels result
     * @param content content for JLabel
     */
    private void showResult(int index, String content) {
        showResultHelper(index, content, labelsResult, controller);
    }

    private int getExponent() {
        int e = 0;
        for (int i = 1; i < 9; i++) e += Integer.parseInt(labelsResult[i].getText()) * Math.pow(2, 8 - i);
        return e;
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

    /**
     * Swap sign and calculation type if calculation is special case
     */
    private void setRealCalculate() {
        String swapType = calculationType;
        if (swapType.equals(Constant.ADDITION_TYPE)) swapType = Constant.SUBTRACTION_TYPE;
        else if (swapType.equals(Constant.SUBTRACTION_TYPE)) swapType = Constant.ADDITION_TYPE;

        if (!isNegative(number1) && isNegative(number2)) {
            calculationType = swapType;
            number2 = number2.abs();
        } else if (isNegative(number1) && isNegative(number2)) {
            calculationType = swapType;
            number2 = number2.abs();
        }
    }
}