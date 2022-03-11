/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import Utils.constants.GuiConst;
import java.time.LocalDate;
import java.util.Calendar;
import javax.swing.JTextField;
import model.Trabajador;

/**
 *
 * @author Daniel
 */
public abstract class Utils {

    public static String fullName(Trabajador t) {
        return t.getNombre() + " " + (t.getNombre2() != null ? (t.getNombre2() + " ") : "")
                + t.getApellido1() + " " + t.getApellido2();
    }

    public static boolean validateEmptyTF(JTextField tf) {
        return !tf.getText().trim().equals("");
    }

    public static boolean validateLengthTF(JTextField tf, int length) {
        return tf.getText().length() == length;
    }

    public static boolean validateNumbersTF(JTextField tf) {
        String text = tf.getText();
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean validateDoubleTF(JTextField tf) {
        try {
            Double.parseDouble(tf.getText());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String getTrimestre() {
        Calendar c = Calendar.getInstance();
        int current = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, current - 2);
        int init = c.get(Calendar.MONTH);

        return GuiConst.MONTHS[init] + " - " + GuiConst.MONTHS[current];
    }
}
