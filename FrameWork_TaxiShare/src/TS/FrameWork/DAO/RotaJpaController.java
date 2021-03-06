/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package TS.FrameWork.DAO;

import TS.FrameWork.DAO.exceptions.NonexistentEntityException;
import TS.FrameWork.TO.Perimetro;
import TS.FrameWork.TO.Rota;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;


public class RotaJpaController implements Serializable {

    public RotaJpaController(EntityManager emf) {
        this.emf = emf;
    }
    private EntityManager emf = null;

    public EntityManager getEntityManager() {
        return emf;
    }

    public void create(Rota rota) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.persist(rota);
        } catch(Exception ex){
            throw ex;
        }
    }

    public void edit(Rota rota) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.merge(rota);
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = rota.getId();
                if (findRota(id) == null) {
                    throw new NonexistentEntityException("The rota with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } 
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try 
        {
            em = getEntityManager();
            Rota rota;
            try {
                rota = em.getReference(Rota.class, id);
                rota.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The rota with id " + id + " no longer exists.", enfe);
            }
            em.remove(rota);
        } 
        catch(Exception ex){
            throw ex;
        }
    }

    public Rota findRota(int id) {
        EntityManager em = getEntityManager();
        try {
            Rota rota = em.find(Rota.class, id);
            return rota;
        } catch(Exception ex){
            throw ex;
        }
    }
    
    public List<Rota> findRotaEntities() {
        return findRotaEntities(true, -1, -1);
    }

    public List<Rota> findRotaEntities(int maxResults, int firstResult) {
        return findRotaEntities(false, maxResults, firstResult);
    }

    private List<Rota> findRotaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            List<Rota> rotas;
            Query q = em.createQuery("select object(o) from Rota as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            rotas = q.getResultList();
            
            return rotas;
            
        } catch(Exception ex) {
            throw ex;
        }
    }
    
    public List<Rota> findByPerimeter(Perimetro origem, Perimetro destino) {
        EntityManager em = getEntityManager();
        try {
            List<Rota> rotas;
            List<Rota> rotas2;
            List<Rota> result = new ArrayList();
            Query q = em.createQuery("SELECT distinct r "
                                    + "FROM Rota as r "
                                    + "JOIN r.enderecos as e "     
                                    + "WHERE ((e.latitude between "+origem.getBaixo()+" and "+origem.getCima()+") "
                                    + "AND (e.longitude between "+origem.getEsquerda()+" and "+origem.getDireita()+")) and e.tipo = 'O') "    
                                    + "AND r.flagAberta = 1");
            Query q2 = em.createQuery("SELECT distinct r "
                                    + "FROM Rota as r "
                                    + "JOIN r.enderecos as e "     
                                    + "WHERE ((e.latitude between "+destino.getBaixo()+" and "+destino.getCima()+") "
                                    + "AND (e.longitude between "+destino.getEsquerda()+" and "+destino.getDireita()+")) and e.tipo = 'D') "    
                                    + "AND r.flagAberta = 1");
//                    .setParameter("lat1", origem.getCima())
//                    .setParameter("lat2", origem.getBaixo())
//                    .setParameter("lat3", destino.getCima())
//                    .setParameter("lat4", destino.getBaixo())
//                    .setParameter("long1", origem.getEsquerda())
//                    .setParameter("long2", origem.getDireita())
//                    .setParameter("long3", destino.getEsquerda())
//                    .setParameter("long4", destino.getDireita());
                        
            
            
            
            rotas = q.getResultList();
            rotas2 = q2.getResultList();
            if(rotas.size() > 0 && rotas2.size()> 0)
            {
                for(int j=0; j<rotas.size();j++)
                {
                
                    for(int i=0; i<rotas2.size();i++)
                    {
                        if(rotas.get(j).getId() == rotas2.get(i).getId())
                        {
                            result.add(rotas2.get(i));
                        }
                    }
                }
            }
            return result;
            
        } catch(Exception ex) {
            throw ex;
        }
    }
    

    public int getRotaCount() {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select count(o) from Rota as o");
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
