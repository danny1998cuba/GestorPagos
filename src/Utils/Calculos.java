/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import model.Trabajador;

/**
 *
 * @author Daniel
 */
public abstract class Calculos {

    public static Double calcAdic(Trabajador t, double diasTrabajados, int evaluacion) {
        if (evaluacion >= 80) {
            return (t.getIdEscala().getSalario() * diasTrabajados) / 190.6;
        }
        return null;
    }
}
