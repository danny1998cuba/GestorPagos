/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.pagos;

import controller.Control;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import model.Utilidades;

/**
 *
 * @author Daniel
 */
public class ResultUtilidades extends javax.swing.JInternalFrame {

    /**
     * Creates new form ResultUtilidades
     *
     * @param t
     */
    public ResultUtilidades(String t) {
        initComponents();

        this.setTitle(this.getTitle() + " trimestre " + t);

        List<Utilidades> uts = Control.utilidJPA.findUtilidadesByTrimestre(t);

        loadData(uts);
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
                "Trabajador", "PagoA", "PagoB", "PagoC", "PagoD", "PagoE", "Monto Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
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

    private void loadData(List<Utilidades> uts) {
        DefaultTableModel model = ((DefaultTableModel) jTable1.getModel());

        for (int i = 0; i < uts.size(); i++) {
            model.addRow(new Object[]{});

            Utilidades u = uts.get(i);

            jTable1.setValueAt(u.getIdTrabajador(), i, 0);
            jTable1.setValueAt(u.getPagoa(), i, 1);
            jTable1.setValueAt(u.getPagob(), i, 2);
            jTable1.setValueAt(u.getPagoc(), i, 3);
            jTable1.setValueAt(u.getPagod(), i, 4);
            jTable1.setValueAt(u.getPagoe(), i, 5);

            double total = ((Double) jTable1.getValueAt(i, 1) != null? (Double) jTable1.getValueAt(i, 1) : 0)
                    + ((Double) jTable1.getValueAt(i, 2) != null? (Double) jTable1.getValueAt(i, 2) : 0)
                    + ((Double) jTable1.getValueAt(i, 3) != null? (Double) jTable1.getValueAt(i, 3) : 0)
                    + ((Double) jTable1.getValueAt(i, 4)!= null? (Double) jTable1.getValueAt(i, 4) : 0)
                    + ((Double) jTable1.getValueAt(i, 5) != null? (Double) jTable1.getValueAt(i, 5) : 0);

            jTable1.setValueAt(total, i, 6);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

}