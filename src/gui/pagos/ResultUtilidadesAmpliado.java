/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.pagos;

import Utils.Utils;
import controller.Control;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Pago;
import model.Trabajador;
import model.Utilidades;

/**
 *
 * @author Daniel
 */
public class ResultUtilidadesAmpliado extends javax.swing.JInternalFrame {

    double utilidades;
    double montoA, montoB, montoC, montoD, montoE;
    double coefA = 0, coefB = 0, coefC = 0, coefD = 0, coefE = 0;

    /**
     * Creates new form ResultUtilidades
     *
     * @param utilidades
     */
    public ResultUtilidadesAmpliado(double utilidades) {
        initComponents();

        this.utilidades = utilidades;
        montoA = utilidades * 0.6;
        montoB = utilidades * 0.1;
        montoC = utilidades * 0.1;
        montoD = utilidades * 0.1;
        montoE = utilidades * 0.1;

        calcUtilidades();
    }

    private void calcUtilidades() {
        List<Trabajador> trabaj = Control.trabajJPA.findTrabajadorEntities();
        List<Pago> pagos = Control.pagosJPA.findByTrimestre();
        DefaultTableModel model = ((DefaultTableModel) jTable1.getModel());

        List<Double> appstrl = new ArrayList<>();

        for (int i = 0; i < trabaj.size(); i++) {
            model.addRow(new Object[]{});

            Trabajador t = trabaj.get(i);

            jTable1.setValueAt(t, i, 0);
            jTable1.setValueAt(t.getIdEscala().getDescripcion(), i, 1);
            jTable1.setValueAt(t.getIdEscala().getSalario(), i, 2);

            List<Pago> pagosTrabajadorTrim = new ArrayList<>();
            pagos.forEach(p -> {
                if (p.getIdTrabajador().equals(t)) {
                    pagosTrabajadorTrim.add(p);
                }
            });

            double salarioTrim = 0;
            double evalProm = 0;
            double diasTrabajo = 0;
            for (Pago p : pagosTrabajadorTrim) {
                double pagoAdic = p.getPagoAdicional() != null ? p.getPagoAdicional() : 0d;

                salarioTrim += p.getIdTrabajador().getIdEscala().getSalario() + pagoAdic;
                evalProm += p.getEvaluacion();
                diasTrabajo += p.getDiasTrabajados();
            }
            evalProm = evalProm / pagosTrabajadorTrim.size();

            jTable1.setValueAt(salarioTrim, i, 3);
            jTable1.setValueAt(diasTrabajo, i, 4);

            double salarioMedio = (salarioTrim / diasTrabajo) * 24;
            jTable1.setValueAt(salarioMedio, i, 5);

            double strl = (salarioMedio / 70.375) * diasTrabajo;
            jTable1.setValueAt(strl, i, 6);
            jTable1.setValueAt(evalProm, i, 7);

            double strlEval = (strl / 100) * evalProm;
            appstrl.add(strlEval);
            jTable1.setValueAt(strlEval, i, 8);
        }

        calculoCoef(model);

        for (int i = 0; i < trabaj.size(); i++) {
            Trabajador t = trabaj.get(i);
            String classs = t.getIdClasificacion().getDescripcion();
            Double pagoA = null, pagoB = null, pagoC = null, pagoD = null, pagoE = null;

            switch (classs) {
                case "A":
                    pagoA = (appstrl.get(i) / coefA) * montoA;
                    break;
                case "B":
                    pagoA = (appstrl.get(i) / coefA) * montoA;
                    pagoB = (appstrl.get(i) / coefB) * montoB;
                    break;
                case "C":
                    pagoA = (appstrl.get(i) / coefA) * montoA;
                    pagoB = (appstrl.get(i) / coefB) * montoB;
                    pagoC = (appstrl.get(i) / coefC) * montoC;
                    break;
                case "D":
                    pagoA = (appstrl.get(i) / coefA) * montoA;
                    pagoB = (appstrl.get(i) / coefB) * montoB;
                    pagoC = (appstrl.get(i) / coefC) * montoC;
                    pagoD = (appstrl.get(i) / coefD) * montoD;
                    break;
                case "E":
                    pagoA = (appstrl.get(i) / coefA) * montoA;
                    pagoB = (appstrl.get(i) / coefB) * montoB;
                    pagoC = (appstrl.get(i) / coefC) * montoC;
                    pagoD = (appstrl.get(i) / coefD) * montoD;
                    pagoE = (appstrl.get(i) / coefE) * montoE;
                    break;
            }

            double total = 0;

            if (pagoA != null) {
                jTable1.setValueAt(pagoA, i, 9);
                total += pagoA;
            }
            if (pagoB != null) {
                jTable1.setValueAt(pagoB, i, 10);
                total += pagoB;
            }
            if (pagoC != null) {
                jTable1.setValueAt(pagoC, i, 11);
                total += pagoC;
            }
            if (pagoD != null) {
                jTable1.setValueAt(pagoD, i, 12);
                total += pagoD;
            }
            if (pagoE != null) {
                jTable1.setValueAt(pagoE, i, 13);
                total += pagoE;
            }

            jTable1.setValueAt(total, i, 14);

            String trimestre = Utils.getTrimestre();
            Utilidades ut = Control.utilidJPA.findUtilidadesByTrimestreAndTrab(trimestre, t.getId());
            Utilidades u;

            if (ut != null) {
                u = ut;
            } else {
                u = new Utilidades();
            }

            u.setIdTrabajador(t);
            u.setPagoa(pagoA);
            u.setPagob(pagoB);
            u.setPagoc(pagoC);
            u.setPagod(pagoD);
            u.setPagoe(pagoE);
            u.setTrimestre(trimestre);

            if (ut != null) {
                try {
                    Control.utilidJPA.edit(u);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Excep " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Control.utilidJPA.create(u);
            }
        }
    }

    private void calculoCoef(DefaultTableModel model) {
        for (int i = 0; i < model.getRowCount(); i++) {
            Trabajador t = (Trabajador) model.getValueAt(i, 0);
            String classs = t.getIdClasificacion().getDescripcion();

            switch (classs) {
                case "E":
                    coefA += (Double) model.getValueAt(i, 8);
                    coefB += (Double) model.getValueAt(i, 8);
                    coefC += (Double) model.getValueAt(i, 8);
                    coefD += (Double) model.getValueAt(i, 8);
                    coefE += (Double) model.getValueAt(i, 8);
                    break;
                case "D":
                    coefA += (Double) model.getValueAt(i, 8);
                    coefB += (Double) model.getValueAt(i, 8);
                    coefC += (Double) model.getValueAt(i, 8);
                    coefD += (Double) model.getValueAt(i, 8);
                    break;
                case "C":
                    coefA += (Double) model.getValueAt(i, 8);
                    coefB += (Double) model.getValueAt(i, 8);
                    coefC += (Double) model.getValueAt(i, 8);
                    break;
                case "B":
                    coefA += (Double) model.getValueAt(i, 8);
                    coefB += (Double) model.getValueAt(i, 8);
                    break;
                case "A":
                    coefA += (Double) model.getValueAt(i, 8);
                    break;
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Resultado del pago de utilidades");
        setMinimumSize(new java.awt.Dimension(750, 450));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Trabajador (Clasificación)", "GE", "Salario escala", "Salario trimestre", "Días Trabajados", "Salario medio", "STRL", "Evaluación trimestral", "Aplicación Evaluación al STRL", "PagoA", "PagoB", "PagoC", "PagoD", "PagoE", "Monto Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
