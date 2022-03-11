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
import model.GrupoEscala;

/**
 *
 * @author Daniel
 */
public class GrupoEscalaJpaController implements Serializable {

    public GrupoEscalaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(GrupoEscala grupoEscala) {
        if (grupoEscala.getTrabajadorList() == null) {
            grupoEscala.setTrabajadorList(new ArrayList<Trabajador>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Trabajador> attachedTrabajadorList = new ArrayList<Trabajador>();
            for (Trabajador trabajadorListTrabajadorToAttach : grupoEscala.getTrabajadorList()) {
                trabajadorListTrabajadorToAttach = em.getReference(trabajadorListTrabajadorToAttach.getClass(), trabajadorListTrabajadorToAttach.getId());
                attachedTrabajadorList.add(trabajadorListTrabajadorToAttach);
            }
            grupoEscala.setTrabajadorList(attachedTrabajadorList);
            em.persist(grupoEscala);
            for (Trabajador trabajadorListTrabajador : grupoEscala.getTrabajadorList()) {
                GrupoEscala oldIdEscalaOfTrabajadorListTrabajador = trabajadorListTrabajador.getIdEscala();
                trabajadorListTrabajador.setIdEscala(grupoEscala);
                trabajadorListTrabajador = em.merge(trabajadorListTrabajador);
                if (oldIdEscalaOfTrabajadorListTrabajador != null) {
                    oldIdEscalaOfTrabajadorListTrabajador.getTrabajadorList().remove(trabajadorListTrabajador);
                    oldIdEscalaOfTrabajadorListTrabajador = em.merge(oldIdEscalaOfTrabajadorListTrabajador);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(GrupoEscala grupoEscala) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            GrupoEscala persistentGrupoEscala = em.find(GrupoEscala.class, grupoEscala.getId());
            List<Trabajador> trabajadorListOld = persistentGrupoEscala.getTrabajadorList();
            List<Trabajador> trabajadorListNew = grupoEscala.getTrabajadorList();
            List<String> illegalOrphanMessages = null;
            for (Trabajador trabajadorListOldTrabajador : trabajadorListOld) {
                if (!trabajadorListNew.contains(trabajadorListOldTrabajador)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Trabajador " + trabajadorListOldTrabajador + " since its idEscala field is not nullable.");
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
            grupoEscala.setTrabajadorList(trabajadorListNew);
            grupoEscala = em.merge(grupoEscala);
            for (Trabajador trabajadorListNewTrabajador : trabajadorListNew) {
                if (!trabajadorListOld.contains(trabajadorListNewTrabajador)) {
                    GrupoEscala oldIdEscalaOfTrabajadorListNewTrabajador = trabajadorListNewTrabajador.getIdEscala();
                    trabajadorListNewTrabajador.setIdEscala(grupoEscala);
                    trabajadorListNewTrabajador = em.merge(trabajadorListNewTrabajador);
                    if (oldIdEscalaOfTrabajadorListNewTrabajador != null && !oldIdEscalaOfTrabajadorListNewTrabajador.equals(grupoEscala)) {
                        oldIdEscalaOfTrabajadorListNewTrabajador.getTrabajadorList().remove(trabajadorListNewTrabajador);
                        oldIdEscalaOfTrabajadorListNewTrabajador = em.merge(oldIdEscalaOfTrabajadorListNewTrabajador);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = grupoEscala.getId();
                if (findGrupoEscala(id) == null) {
                    throw new NonexistentEntityException("The grupoEscala with id " + id + " no longer exists.");
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
            GrupoEscala grupoEscala;
            try {
                grupoEscala = em.getReference(GrupoEscala.class, id);
                grupoEscala.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The grupoEscala with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Trabajador> trabajadorListOrphanCheck = grupoEscala.getTrabajadorList();
            for (Trabajador trabajadorListOrphanCheckTrabajador : trabajadorListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This GrupoEscala (" + grupoEscala + ") cannot be destroyed since the Trabajador " + trabajadorListOrphanCheckTrabajador + " in its trabajadorList field has a non-nullable idEscala field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(grupoEscala);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<GrupoEscala> findGrupoEscalaEntities() {
        return findGrupoEscalaEntities(true, -1, -1);
    }

    public List<GrupoEscala> findGrupoEscalaEntities(int maxResults, int firstResult) {
        return findGrupoEscalaEntities(false, maxResults, firstResult);
    }

    private List<GrupoEscala> findGrupoEscalaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(GrupoEscala.class));
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

    public GrupoEscala findGrupoEscala(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(GrupoEscala.class, id);
        } finally {
            em.close();
        }
    }

    public int getGrupoEscalaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<GrupoEscala> rt = cq.from(GrupoEscala.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
