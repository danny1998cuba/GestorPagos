/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pago;
import model.Trabajador;

/**
 *
 * @author Daniel
 */
public class PagoJpaController implements Serializable {

    public PagoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pago pago) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador idTrabajador = pago.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador = em.getReference(idTrabajador.getClass(), idTrabajador.getId());
                pago.setIdTrabajador(idTrabajador);
            }
            em.persist(pago);
            if (idTrabajador != null) {
                idTrabajador.getPagoList().add(pago);
                idTrabajador = em.merge(idTrabajador);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pago pago) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago persistentPago = em.find(Pago.class, pago.getId());
            Trabajador idTrabajadorOld = persistentPago.getIdTrabajador();
            Trabajador idTrabajadorNew = pago.getIdTrabajador();
            if (idTrabajadorNew != null) {
                idTrabajadorNew = em.getReference(idTrabajadorNew.getClass(), idTrabajadorNew.getId());
                pago.setIdTrabajador(idTrabajadorNew);
            }
            pago = em.merge(pago);
            if (idTrabajadorOld != null && !idTrabajadorOld.equals(idTrabajadorNew)) {
                idTrabajadorOld.getPagoList().remove(pago);
                idTrabajadorOld = em.merge(idTrabajadorOld);
            }
            if (idTrabajadorNew != null && !idTrabajadorNew.equals(idTrabajadorOld)) {
                idTrabajadorNew.getPagoList().add(pago);
                idTrabajadorNew = em.merge(idTrabajadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pago.getId();
                if (findPago(id) == null) {
                    throw new NonexistentEntityException("The pago with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pago pago;
            try {
                pago = em.getReference(Pago.class, id);
                pago.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pago with id " + id + " no longer exists.", enfe);
            }
            Trabajador idTrabajador = pago.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador.getPagoList().remove(pago);
                idTrabajador = em.merge(idTrabajador);
            }
            em.remove(pago);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pago> findPagoEntities() {
        return findPagoEntities(true, -1, -1);
    }

    public List<Pago> findPagoEntities(int maxResults, int firstResult) {
        return findPagoEntities(false, maxResults, firstResult);
    }

    private List<Pago> findPagoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pago.class));
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

    public Pago findPago(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pago.class, id);
        } finally {
            em.close();
        }
    }

    public int getPagoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pago> rt = cq.from(Pago.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<Pago> findByMonthAndYear(int month, int year) {
        EntityManager em = getEntityManager();

        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);
        Date ini = c.getTime();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        Date fin = c.getTime();

        try {
            String sql = "SELECT p FROM Pago p WHERE p.fecha >= :ini AND p.fecha < :fin";
            Query q = em.createQuery(sql);
            q.setParameter("ini", ini);
            q.setParameter("fin", fin);

            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Pago> findByTrimestre() {
        EntityManager em = getEntityManager();

        Calendar c = Calendar.getInstance();
        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1);
        c.set(Calendar.DATE, c.get(Calendar.DATE) - 1);
        Date fin = c.getTime();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 2);
        c.set(Calendar.DATE, 1);
        Date ini = c.getTime();
        
        try {
            String sql = "SELECT p FROM Pago p WHERE p.fecha >= :ini AND p.fecha < :fin";
            Query q = em.createQuery(sql);
            q.setParameter("ini", ini);
            q.setParameter("fin", fin);

            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
