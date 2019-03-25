package dialog;

import extension.Controller;
import helpers.Constant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class FloatingPointNumber extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JRadioButton rdAddition;
    private JRadioButton rdSubtraction;
    private JRadioButton rdMultiplication;
    private JRadioButton rdDivision;
    private JFormattedTextField num1;
    private JFormattedTextField num2;

    private Controller controller;
    private String calculationType;

    public FloatingPointNumber(Controller controller) {
        this.controller = controller;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        // default calculation type
        calculationType = Constant.ADDITION_TYPE;
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(rdAddition);
        buttonGroup.add(rdSubtraction);
        buttonGroup.add(rdMultiplication);
        buttonGroup.add(rdDivision);

//        num1.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                Runnable format = new Runnable() {
//                    @Override
//                    public void run() {
//                        String text = num1.getText();
//                        if (!text.matches("(\\+|-)?([0-9]+(\\.[0-9]+))")) {
//                            num1.setText(text.substring(0, text.length() - 1));
//                        }
//                    }
//                };
//                SwingUtilities.invokeLater(format);
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//            }
//        });
//        num2.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                Runnable format = new Runnable() {
//                    @Override
//                    public void run() {
//                        String text = num2.getText();
//                        if (!text.matches("(\\+|-)?([0-9]+(\\.[0-9]+))")) {
//                            num2.setText(text.substring(0, text.length() - 1));
//                        }
//                    }
//                };
//                SwingUtilities.invokeLater(format);
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//            }
//        });


        rdAddition.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCalculationType(Constant.ADDITION_TYPE);
            }
        });
        rdSubtraction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCalculationType(Constant.SUBTRACTION_TYPE);
            }
        });
        rdMultiplication.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setCalculationType(Constant.MULTIPLICATION_TYPE);
            }
        });
        rdDivision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(new Frame(), "Oops ! We do not have this feature yet.");
                switch (calculationType) {
                    case Constant.ADDITION_TYPE:
                        rdAddition.setSelected(true);
                        break;
                    case Constant.SUBTRACTION_TYPE:
                        rdSubtraction.setSelected(true);
                        break;
                    case Constant.MULTIPLICATION_TYPE:
                        rdMultiplication.setSelected(true);
                        break;
                }
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

    private void setCalculationType(String calculationType) {
        this.calculationType = calculationType;
    }

    private void onOK() {
        // add your code here
        BigDecimal number1 = new BigDecimal(num1.getText());
        BigDecimal number2 = new BigDecimal(num2.getText());
        controller.setDataForFPNSimulation(calculationType, number1, number2);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
