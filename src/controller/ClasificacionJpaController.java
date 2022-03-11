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
import model.Trabajador;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Clasificacion;

/**
 *
 * @author Daniel
 */
public class ClasificacionJpaController implements Serializable {

    public ClasificacionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Clasificacion clasificacion) {
        if (clasificacion.getTrabajadorList() == null) {
            clasificacion.setTrabajadorList(new ArrayList<Trabajador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Trabajador> attachedTrabajadorList = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListTrabajadorToAttach : clasificacion.getTrabajadorList()) {
                trabajadorListTrabajadorToAttach = em.getReference(trabajadorListTrabajadorToAttach.getClass(), trabajadorListTrabajadorToAttach.getId());
                attachedTrabajadorList.add(trabajadorListTrabajadorToAttach);
            }
            clasificacion.setTrabajadorList(attachedTrabajadorList);
            em.persist(clasificacion);
            for (Trabajador trabajadorListTrabajador : clasificacion.getTrabajadorList()) {
                Clasificacion oldIdClasificacionOfTrabajadorListTrabajador = trabajadorListTrabajador.getIdClasificacion();
                trabajadorListTrabajador.setIdClasificacion(clasificacion);
                trabajadorListTrabajador = em.merge(trabajadorListTrabajador);
                if (oldIdClasificacionOfTrabajadorListTrabajador != null) {
                    oldIdClasificacionOfTrabajadorListTrabajador.getTrabajadorList().remove(trabajadorListTrabajador);
                    oldIdClasificacionOfTrabajadorListTrabajador = em.merge(oldIdClasificacionOfTrabajadorListTrabajador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Clasificacion clasificacion) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Clasificacion persistentClasificacion = em.find(Clasificacion.class, clasificacion.getId());
            List<Trabajador> trabajadorListOld = persistentClasificacion.getTrabajadorList();
            List<Trabajador> trabajadorListNew = clasificacion.getTrabajadorList();
            List<String> illegalOrphanMessages = null;
            for (Trabajador trabajadorListOldTrabajador : trabajadorListOld) {
                if (!trabajadorListNew.contains(trabajadorListOldTrabajador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trabajador " + trabajadorListOldTrabajador + " since its idClasificacion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Trabajador> attachedTrabajadorListNew = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListNewTrabajadorToAttach : trabajadorListNew) {
                trabajadorListNewTrabajadorToAttach = em.getReference(trabajadorListNewTrabajadorToAttach.getClass(), trabajadorListNewTrabajadorToAttach.getId());
                attachedTrabajadorListNew.add(trabajadorListNewTrabajadorToAttach);
            }
            trabajadorListNew = attachedTrabajadorListNew;
            clasificacion.setTrabajadorList(trabajadorListNew);
            clasificacion = em.merge(clasificacion);
            for (Trabajador trabajadorListNewTrabajador : trabajadorListNew) {
                if (!trabajadorListOld.contains(trabajadorListNewTrabajador)) {
                    Clasificacion oldIdClasificacionOfTrabajadorListNewTrabajador = trabajadorListNewTrabajador.getIdClasificacion();
                    trabajadorListNewTrabajador.setIdClasificacion(clasificacion);
                    trabajadorListNewTrabajador = em.merge(trabajadorListNewTrabajador);
                    if (oldIdClasificacionOfTrabajadorListNewTrabajador != null && !oldIdClasificacionOfTrabajadorListNewTrabajador.equals(clasificacion)) {
                        oldIdClasificacionOfTrabajadorListNewTrabajador.getTrabajadorList().remove(trabajadorListNewTrabajador);
                        oldIdClasificacionOfTrabajadorListNewTrabajador = em.merge(oldIdClasificacionOfTrabajadorListNewTrabajador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = clasificacion.getId();
                if (findClasificacion(id) == null) {
                    throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.");
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
            Clasificacion clasificacion;
            try {
                clasificacion = em.getReference(Clasificacion.class, id);
                clasificacion.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The clasificacion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Trabajador> trabajadorListOrphanCheck = clasificacion.getTrabajadorList();
            for (Trabajador trabajadorListOrphanCheckTrabajador : trabajadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Clasificacion (" + clasificacion + ") cannot be destroyed since the Trabajador " + trabajadorListOrphanCheckTrabajador + " in its trabajadorList field has a non-nullable idClasificacion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(clasificacion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Clasificacion> findClasificacionEntities() {
        return findClasificacionEntities(true, -1, -1);
    }

    public List<Clasificacion> findClasificacionEntities(int maxResults, int firstResult) {
        return findClasificacionEntities(false, maxResults, firstResult);
    }

    private List<Clasificacion> findClasificacionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Clasificacion.class));
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

    public Clasificacion findClasificacion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Clasificacion.class, id);
        } finally {
            em.close();
        }
    }

    public int getClasificacionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Clasificacion> rt = cq.from(Clasificacion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
