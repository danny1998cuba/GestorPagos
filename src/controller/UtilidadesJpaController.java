/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Trabajador;
import model.Utilidades;

/**
 *
 * @author Daniel
 */
public class UtilidadesJpaController implements Serializable {

    public UtilidadesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Utilidades utilidades) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Trabajador idTrabajador = utilidades.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador = em.getReference(idTrabajador.getClass(), idTrabajador.getId());
                utilidades.setIdTrabajador(idTrabajador);
            }
            em.persist(utilidades);
            if (idTrabajador != null) {
                idTrabajador.getUtilidadesList().add(utilidades);
                idTrabajador = em.merge(idTrabajador);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Utilidades utilidades) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Utilidades persistentUtilidades = em.find(Utilidades.class, utilidades.getId());
            Trabajador idTrabajadorOld = persistentUtilidades.getIdTrabajador();
            Trabajador idTrabajadorNew = utilidades.getIdTrabajador();
            if (idTrabajadorNew != null) {
                idTrabajadorNew = em.getReference(idTrabajadorNew.getClass(), idTrabajadorNew.getId());
                utilidades.setIdTrabajador(idTrabajadorNew);
            }
            utilidades = em.merge(utilidades);
            if (idTrabajadorOld != null && !idTrabajadorOld.equals(idTrabajadorNew)) {
                idTrabajadorOld.getUtilidadesList().remove(utilidades);
                idTrabajadorOld = em.merge(idTrabajadorOld);
            }
            if (idTrabajadorNew != null && !idTrabajadorNew.equals(idTrabajadorOld)) {
                idTrabajadorNew.getUtilidadesList().add(utilidades);
                idTrabajadorNew = em.merge(idTrabajadorNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = utilidades.getId();
                if (findUtilidades(id) == null) {
                    throw new NonexistentEntityException("The utilidades with id " + id + " no longer exists.");
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
            Utilidades utilidades;
            try {
                utilidades = em.getReference(Utilidades.class, id);
                utilidades.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The utilidades with id " + id + " no longer exists.", enfe);
            }
            Trabajador idTrabajador = utilidades.getIdTrabajador();
            if (idTrabajador != null) {
                idTrabajador.getUtilidadesList().remove(utilidades);
                idTrabajador = em.merge(idTrabajador);
            }
            em.remove(utilidades);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Utilidades> findUtilidadesEntities() {
        return findUtilidadesEntities(true, -1, -1);
    }

    public List<Utilidades> findUtilidadesEntities(int maxResults, int firstResult) {
        return findUtilidadesEntities(false, maxResults, firstResult);
    }

    private List<Utilidades> findUtilidadesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Utilidades.class));
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

    public Utilidades findUtilidades(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Utilidades.class, id);
        } finally {
            em.close();
        }
    }

    public int getUtilidadesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Utilidades> rt = cq.from(Utilidades.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Utilidades findUtilidadesByTrimestreAndTrab(String trim, int idTrab) {
        EntityManager em = getEntityManager();
        try {
            String sql = "SELECT u FROM Utilidades u WHERE u.trimestre = :trimestre AND u.idTrabajador.id = :idTrab";
            Query q = em.createQuery(sql);
            q.setParameter("trimestre", trim);
            q.setParameter("idTrab", idTrab);

            return (Utilidades) q.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }
    
    public List<Utilidades> findUtilidadesByTrimestre(String trim) {
        EntityManager em = getEntityManager();
        try {
            String sql = "SELECT u FROM Utilidades u WHERE u.trimestre = :trimestre";
            Query q = em.createQuery(sql);
            q.setParameter("trimestre", trim);

            return q.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

    public List<String> listTrimestres() {
        EntityManager em = getEntityManager();
        try {
            String sql = "SELECT DISTINCT u.trimestre FROM Utilidades u";
            Query q = em.createQuery(sql);

            return q.getResultList();
        } catch (NoResultException ex) {
            return null;
        } finally {
            em.close();
        }
    }

}
