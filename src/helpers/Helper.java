package helpers;

import extension.Controller;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.math.BigDecimal;

public class Helper {
    /**
     * Check if an arrays is sorted then return true
     *
     * @param isIncrease Status of sort simulation
     * @param arrays     Arrays need to check sorted
     * @return True if arrays is sorted
     */
    public static boolean isSorted(boolean isIncrease, int[] arrays) {
        if (isIncrease) {
            for (int i = 0; i < arrays.length - 1; i++)
                if (arrays[i] > arrays[i + 1])
                    return false;
        } else {
            for (int i = 0; i < arrays.length - 1; i++)
                if (arrays[i] < arrays[i + 1])
                    return false;
        }
        return true;
    }

    /**
     * Create title calculation
     *
     * @param type    Calculation type such as: addition, subtraction, multiplication, division
     * @param number1
     * @param number2
     * @return
     */
    public static String getTitleCalculation(String type, BigDecimal number1, BigDecimal number2) {
        String name = "";
        switch (type) {
            case Constant.ADDITION_TYPE:
                name = "Addition of ";
                break;
            case Constant.SUBTRACTION_TYPE:
                name = "Subtraction of ";
                break;
            case Constant.MULTIPLICATION_TYPE:
                name = "Multiplication of ";
                break;
            case Constant.DIVISION_TYPE:
                name = "Division of ";
                break;
        }
        return name.concat(number1.toString()).concat(" and ").concat(number2.toString()).concat(" ?");
    }

    /**
     * Create a JLabel text
     *
     * @param content Content of JLabel
     * @return JLabel after control attribute
     */
    public static JLabel createTxtLabel(String content) {
        JLabel jLabel = new JLabel(content);
        //set size label
        jLabel.setSize(200, 50);
        jLabel.setOpaque(true);
        jLabel.setForeground(Constant.COLOR_TEXT);
        //set fonts
        jLabel.setFont(new Font("Tahoma", Font.ITALIC, 12));
        //set text alignment center
        jLabel.setHorizontalAlignment(SwingConstants.LEFT);
        jLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        jLabel.setVisible(false);
        return jLabel;
    }

    /**
     * Create JLabel binary
     *
     * @param content content of JLabel
     * @param w       width
     * @param h       height
     * @return JLabel created
     */
    public static JLabel createBinary(String content, int w, int h) {
        JLabel jLabel = new JLabel(String.valueOf(content));
        //set size label
        jLabel.setSize(w, h);
        jLabel.setOpaque(true);
        jLabel.setForeground(Constant.COLOR_TEXT);
        //set fonts
        jLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
        //set text alignment center
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setVisible(false);
        return jLabel;
    }

    /**
     * Create support JLabel
     *
     * @param content content of JLabel
     * @param w       width
     * @param h       height
     * @return JLabel created
     */
    public static JLabel createElement(String content, int w, int h) {
        JLabel jLabel = new JLabel(content);
        //set size label
        jLabel.setSize(w, h);
        jLabel.setOpaque(true);
        jLabel.setForeground(Constant.COLOR_TEXT);
        //set fonts
        jLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        //set text alignment center
        jLabel.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel.setVerticalAlignment(SwingConstants.CENTER);
        jLabel.setVisible(false);
        return jLabel;
    }

    /**
     * Process calculate binary
     *
     * @param type type of calculation
     * @param num1 number 1
     * @param num2 number 2
     * @return result of calculation
     */
    public static char[] calculateBinary(String type, String num1, String num2) {
        String result = "";
        switch (type) {
            case Constant.ADDITION_TYPE:
                if (num1.equals("0") && num2.equals("0")) result = "0";
                else if (num1.equals("0") && num2.equals("1")) result = "1";
                else if (num1.equals("1") && num2.equals("0")) result = "1";
                else result = "10";
                break;
            case Constant.SUBTRACTION_TYPE:
                break;
            case Constant.XOR_TYPE:
                result = String.valueOf(Integer.parseInt(num1) ^ Integer.parseInt(num2));
                break;
        }
        return result.toCharArray();
    }

    /**
     * Check a number is zero or not
     *
     * @param number number need to check
     * @return is zero
     */
    public static boolean isZero(BigDecimal number) {
        return number.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Check number is negative number or not
     *
     * @param number number need to check
     * @return true if number is negative number, false if number not is negative number
     */
    public static boolean isNegative(BigDecimal number) {
        return number.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Get 32-bit IEEE 754 format of the decimal value
     *
     * @param number           number need to get binary 32
     * @param isMultiplication check type calculation
     * @return result of number convert to binary
     */
    public static char[] getBinary32(BigDecimal number, boolean isMultiplication) {
        int intBits = Float.floatToIntBits(number.floatValue());
        StringBuilder stringBuilder = new StringBuilder(Integer.toBinaryString(intBits));

        while (stringBuilder.toString().toCharArray().length < 32) {
            stringBuilder.insert(0, "0");
        }

        String binaryStr;
        if (isMultiplication) binaryStr = stringBuilder.substring(0, 9) + "01" + stringBuilder.substring(9);
        else if (isZero(number)) binaryStr = stringBuilder.substring(0, 9) + "00." + stringBuilder.substring(9);
        else binaryStr = stringBuilder.substring(0, 9) + "01." + stringBuilder.substring(9);
        return binaryStr.toCharArray();
    }

    /**
     * Get binary of an integer
     *
     * @param num number need to get binary
     * @return result of an integer convert to binary
     */
    public static char[] getBinaryInteger(int num) {
        StringBuilder exponentBinary = new StringBuilder(Integer.toBinaryString(num));
        while (exponentBinary.length() < 8) {
            exponentBinary.insert(0, "0");
        }

        return exponentBinary.toString().toCharArray();
    }

    /**
     * Set border for JLabel
     *
     * @param jLabel JLabel need to set border
     * @param top    top of JLabel
     * @param left   left of JLabel
     * @param bottom bottom of JLabel
     * @param right  right of JLabel
     */
    public static void setBorder(JLabel jLabel, int top, int left, int bottom, int right) {
        jLabel.setBorder(new MatteBorder(top, left, bottom, right, Constant.COLOR_TEXT));
    }

    /**
     * Set JLabel as hidden element
     *
     * @param jLabel JLabel need to set hidden
     */
    public static void setHidden(JLabel jLabel) {
        jLabel.setForeground(Constant.HIDDEN_COLOR_FOREGROUND);
        jLabel.setBackground(Constant.HIDDEN_COLOR_BACKGROUND);
        jLabel.setBorder(new MatteBorder(1, 1, 1, 1, Constant.HIDDEN_COLOR_BACKGROUND));
    }

    /**
     * Set content of each step in simulation
     *
     * @param content content need to set
     * @throws InterruptedException error of thread
     */
    public static void setContentHelper(String content, JLabel[] txtLabels, Controller controller) throws InterruptedException {
        txtLabels[0].setText(content);
        for (int i = 0; i < 3; i++) {
            txtLabels[0].setForeground(Constant.SELECTED_BLUE);
            controller.READTEXT();
            txtLabels[0].setForeground(Constant.COLOR_TEXT);
        }
    }

    /**
     * Show JLabel of result calculate on screen
     *
     * @param index   index of arrays JLabels result
     * @param content content for JLabel
     */
    public static void showResultHelper(int index, String content, JLabel[] labelsResult, Controller controller) {
        if (index != labelsResult.length - 1 && index != 0 && index != 9 && index != 8)
            setBorder(labelsResult[index + 1], 1, 0, 1, 1);
        setBorder(labelsResult[index], 1, 1, 1, 1);
        labelsResult[index].setVisible(true);
        labelsResult[index].setText(content);
        labelsResult[index].setBackground(controller.getPnImitiate().getBackground());
        if (index == 9) setHidden(labelsResult[9]);
    }

    /**
     * Visualize convert input number into binary
     *
     * @param jLabels array JLabels need to visualize
     * @param color   default color of array JLabels after convert
     * @throws InterruptedException throw error of SLEEP function
     */
    public static void convertBinary(JLabel[] jLabels, Color color) throws InterruptedException {
        for (int i = 0; i < jLabels.length; i++) {
            jLabels[i].setVisible(true);
            if (i == 0) jLabels[i].setBackground(Constant.SIGN);
            else if (i < 9) jLabels[i].setBackground(Constant.EXPONENT);
            else jLabels[i].setBackground(Constant.MANTISSA);
            Thread.sleep(100);
            if (i == 9) setHidden(jLabels[i]);
            else jLabels[i].setBackground(color);
        }
    }

    /**
     * Check mantissa is equals 0 or not
     *
     * @param jLabels arrays mantissa
     * @return true if all mantissa equals 0
     */
    public static boolean isZeroMantissa(JLabel[] jLabels) {
        for (int i = 10; i < jLabels.length; i++) {
            if (i != 11 && !jLabels[i].getText().equals("0")) return false;
        }
        return true;
    }
}
