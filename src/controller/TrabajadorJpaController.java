/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Clasificacion;
import model.GrupoEscala;
import model.Utilidades;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Pago;
import model.Trabajador;

/**
 *
 * @author Daniel
 */
public class TrabajadorJpaController implements Serializable {

    public TrabajadorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Trabajador trabajador) {
        if (trabajador.getUtilidadesList() == null) {
            trabajador.setUtilidadesList(new ArrayList<Utilidades>());
        }
        if (trabajador.getPagoList() == null) {
            trabajador.setPagoList(new ArrayList<Pago>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificacion idClasificacion = trabajador.getIdClasificacion();
            if (idClasificacion != null) {
                idClasificacion = em.getReference(idClasificacion.getClass(), idClasificacion.getId());
                trabajador.setIdClasificacion(idClasificacion);
            }
            GrupoEscala idEscala = trabajador.getIdEscala();
            if (idEscala != null) {
                idEscala = em.getReference(idEscala.getClass(), idEscala.getId());
                trabajador.setIdEscala(idEscala);
            }
            List<Utilidades> attachedUtilidadesList = new ArrayList<Utilidades>();
            for (Utilidades utilidadesListUtilidadesToAttach : trabajador.getUtilidadesList()) {
                utilidadesListUtilidadesToAttach = em.getReference(utilidadesListUtilidadesToAttach.getClass(), utilidadesListUtilidadesToAttach.getId());
                attachedUtilidadesList.add(utilidadesListUtilidadesToAttach);
            }
            trabajador.setUtilidadesList(attachedUtilidadesList);
            List<Pago> attachedPagoList = new ArrayList<Pago>();
            for (Pago pagoListPagoToAttach : trabajador.getPagoList()) {
                pagoListPagoToAttach = em.getReference(pagoListPagoToAttach.getClass(), pagoListPagoToAttach.getId());
                attachedPagoList.add(pagoListPagoToAttach);
            }
            trabajador.setPagoList(attachedPagoList);
            em.persist(trabajador);
            if (idClasificacion != null) {
                idClasificacion.getTrabajadorList().add(trabajador);
                idClasificacion = em.merge(idClasificacion);
            }
            if (idEscala != null) {
                idEscala.getTrabajadorList().add(trabajador);
                idEscala = em.merge(idEscala);
            }
            for (Utilidades utilidadesListUtilidades : trabajador.getUtilidadesList()) {
                Trabajador oldIdTrabajadorOfUtilidadesListUtilidades = utilidadesListUtilidades.getIdTrabajador();
                utilidadesListUtilidades.setIdTrabajador(trabajador);
                utilidadesListUtilidades = em.merge(utilidadesListUtilidades);
                if (oldIdTrabajadorOfUtilidadesListUtilidades != null) {
                    oldIdTrabajadorOfUtilidadesListUtilidades.getUtilidadesList().remove(utilidadesListUtilidades);
                    oldIdTrabajadorOfUtilidadesListUtilidades = em.merge(oldIdTrabajadorOfUtilidadesListUtilidades);
                }
            }
            for (Pago pagoListPago : trabajador.getPagoList()) {
                Trabajador oldIdTrabajadorOfPagoListPago = pagoListPago.getIdTrabajador();
                pagoListPago.setIdTrabajador(trabajador);
                pagoListPago = em.merge(pagoListPago);
                if (oldIdTrabajadorOfPagoListPago != null) {
                    oldIdTrabajadorOfPagoListPago.getPagoList().remove(pagoListPago);
                    oldIdTrabajadorOfPagoListPago = em.merge(oldIdTrabajadorOfPagoListPago);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Trabajador trabajador) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador persistentTrabajador = em.find(Trabajador.class, trabajador.getId());
            Clasificacion idClasificacionOld = persistentTrabajador.getIdClasificacion();
            Clasificacion idClasificacionNew = trabajador.getIdClasificacion();
            GrupoEscala idEscalaOld = persistentTrabajador.getIdEscala();
            GrupoEscala idEscalaNew = trabajador.getIdEscala();
            List<Utilidades> utilidadesListOld = persistentTrabajador.getUtilidadesList();
            List<Utilidades> utilidadesListNew = trabajador.getUtilidadesList();
            List<Pago> pagoListOld = persistentTrabajador.getPagoList();
            List<Pago> pagoListNew = trabajador.getPagoList();
            List<String> illegalOrphanMessages = null;
            for (Utilidades utilidadesListOldUtilidades : utilidadesListOld) {
                if (!utilidadesListNew.contains(utilidadesListOldUtilidades)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Utilidades " + utilidadesListOldUtilidades + " since its idTrabajador field is not nullable.");
                }
            }
            for (Pago pagoListOldPago : pagoListOld) {
                if (!pagoListNew.contains(pagoListOldPago)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pago " + pagoListOldPago + " since its idTrabajador field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idClasificacionNew != null) {
                idClasificacionNew = em.getReference(idClasificacionNew.getClass(), idClasificacionNew.getId());
                trabajador.setIdClasificacion(idClasificacionNew);
            }
            if (idEscalaNew != null) {
                idEscalaNew = em.getReference(idEscalaNew.getClass(), idEscalaNew.getId());
                trabajador.setIdEscala(idEscalaNew);
            }
            List<Utilidades> attachedUtilidadesListNew = new ArrayList<Utilidades>();
            for (Utilidades utilidadesListNewUtilidadesToAttach : utilidadesListNew) {
                utilidadesListNewUtilidadesToAttach = em.getReference(utilidadesListNewUtilidadesToAttach.getClass(), utilidadesListNewUtilidadesToAttach.getId());
                attachedUtilidadesListNew.add(utilidadesListNewUtilidadesToAttach);
            }
            utilidadesListNew = attachedUtilidadesListNew;
            trabajador.setUtilidadesList(utilidadesListNew);
            List<Pago> attachedPagoListNew = new ArrayList<Pago>();
            for (Pago pagoListNewPagoToAttach : pagoListNew) {
                pagoListNewPagoToAttach = em.getReference(pagoListNewPagoToAttach.getClass(), pagoListNewPagoToAttach.getId());
                attachedPagoListNew.add(pagoListNewPagoToAttach);
            }
            pagoListNew = attachedPagoListNew;
            trabajador.setPagoList(pagoListNew);
            trabajador = em.merge(trabajador);
            if (idClasificacionOld != null && !idClasificacionOld.equals(idClasificacionNew)) {
                idClasificacionOld.getTrabajadorList().remove(trabajador);
                idClasificacionOld = em.merge(idClasificacionOld);
            }
            if (idClasificacionNew != null && !idClasificacionNew.equals(idClasificacionOld)) {
                idClasificacionNew.getTrabajadorList().add(trabajador);
                idClasificacionNew = em.merge(idClasificacionNew);
            }
            if (idEscalaOld != null && !idEscalaOld.equals(idEscalaNew)) {
                idEscalaOld.getTrabajadorList().remove(trabajador);
                idEscalaOld = em.merge(idEscalaOld);
            }
            if (idEscalaNew != null && !idEscalaNew.equals(idEscalaOld)) {
                idEscalaNew.getTrabajadorList().add(trabajador);
                idEscalaNew = em.merge(idEscalaNew);
            }
            for (Utilidades utilidadesListNewUtilidades : utilidadesListNew) {
                if (!utilidadesListOld.contains(utilidadesListNewUtilidades)) {
                    Trabajador oldIdTrabajadorOfUtilidadesListNewUtilidades = utilidadesListNewUtilidades.getIdTrabajador();
                    utilidadesListNewUtilidades.setIdTrabajador(trabajador);
                    utilidadesListNewUtilidades = em.merge(utilidadesListNewUtilidades);
                    if (oldIdTrabajadorOfUtilidadesListNewUtilidades != null && !oldIdTrabajadorOfUtilidadesListNewUtilidades.equals(trabajador)) {
                        oldIdTrabajadorOfUtilidadesListNewUtilidades.getUtilidadesList().remove(utilidadesListNewUtilidades);
                        oldIdTrabajadorOfUtilidadesListNewUtilidades = em.merge(oldIdTrabajadorOfUtilidadesListNewUtilidades);
                    }
                }
            }
            for (Pago pagoListNewPago : pagoListNew) {
                if (!pagoListOld.contains(pagoListNewPago)) {
                    Trabajador oldIdTrabajadorOfPagoListNewPago = pagoListNewPago.getIdTrabajador();
                    pagoListNewPago.setIdTrabajador(trabajador);
                    pagoListNewPago = em.merge(pagoListNewPago);
                    if (oldIdTrabajadorOfPagoListNewPago != null && !oldIdTrabajadorOfPagoListNewPago.equals(trabajador)) {
                        oldIdTrabajadorOfPagoListNewPago.getPagoList().remove(pagoListNewPago);
                        oldIdTrabajadorOfPagoListNewPago = em.merge(oldIdTrabajadorOfPagoListNewPago);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = trabajador.getId();
                if (findTrabajador(id) == null) {
                    throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador trabajador;
            try {
                trabajador = em.getReference(Trabajador.class, id);
                trabajador.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The trabajador with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Utilidades> utilidadesListOrphanCheck = trabajador.getUtilidadesList();
            for (Utilidades utilidadesListOrphanCheckUtilidades : utilidadesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Utilidades " + utilidadesListOrphanCheckUtilidades + " in its utilidadesList field has a non-nullable idTrabajador field.");
            }
            List<Pago> pagoListOrphanCheck = trabajador.getPagoList();
            for (Pago pagoListOrphanCheckPago : pagoListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Trabajador (" + trabajador + ") cannot be destroyed since the Pago " + pagoListOrphanCheckPago + " in its pagoList field has a non-nullable idTrabajador field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Clasificacion idClasificacion = trabajador.getIdClasificacion();
            if (idClasificacion != null) {
                idClasificacion.getTrabajadorList().remove(trabajador);
                idClasificacion = em.merge(idClasificacion);
            }
            GrupoEscala idEscala = trabajador.getIdEscala();
            if (idEscala != null) {
                idEscala.getTrabajadorList().remove(trabajador);
                idEscala = em.merge(idEscala);
            }
            em.remove(trabajador);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Trabajador> findTrabajadorEntities() {
        return findTrabajadorEntities(true, -1, -1);
    }

    public List<Trabajador> findTrabajadorEntities(int maxResults, int firstResult) {
        return findTrabajadorEntities(false, maxResults, firstResult);
    }

    private List<Trabajador> findTrabajadorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Trabajador.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Trabajador findTrabajador(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Trabajador.class, id);
        } finally {
            em.close();
        }
    }

    public int getTrabajadorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Trabajador> rt = cq.from(Trabajador.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
