/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Daniel
 */
public class Control {

    private static final EntityManagerFactory EMF = Persistence.createEntityManagerFactory("GestorPagosPU");

    public static ClasificacionJpaController clasiffJPA = new ClasificacionJpaController(EMF);
    public static GrupoEscalaJpaController grupoEscalaJPA = new GrupoEscalaJpaController(EMF);
    public static PagoJpaController pagosJPA = new PagoJpaController(EMF);
    public static TrabajadorJpaController trabajJPA = new TrabajadorJpaController(EMF);
    public static UtilidadesJpaController utilidJPA = new UtilidadesJpaController(EMF);

}
