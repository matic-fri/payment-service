package user.services.beans;

import javax.persistence.EntityManager;

public class BeanBase {

    protected void beginTx(EntityManager em){
        if(!em.getTransaction().isActive()){
            em.getTransaction().begin();
        }
    }

    protected void commitTx(EntityManager em){
        if (em.getTransaction().isActive()){
            em.getTransaction().commit();
        }
    }

    protected void rollbackTx(EntityManager em){
        if(em.getTransaction().isActive()){
            em.getTransaction().rollback();
        }
    }
}
