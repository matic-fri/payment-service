package user.services.beans;


import user.lib.Payment;
import user.models.converters.PaymentConverter;
import user.models.entities.PaymentEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class PaymentBean extends BeanBase {

    private Logger log = Logger.getLogger(PaymentBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Payment> getAllPayments() {
        TypedQuery<PaymentEntity> query = em.createNamedQuery(
                "PaymentEntity.getAll", PaymentEntity.class);

        List<PaymentEntity> resultList = query.getResultList();

        return resultList.stream().map(PaymentConverter::toDto).collect(Collectors.toList());

    }

    public Payment getPayment(Integer id){
        PaymentEntity PaymentEn = em.find(PaymentEntity.class, id);

        if (PaymentEn == null){
            throw new NotFoundException();
        }

        Payment u = PaymentConverter.toDto(PaymentEn);

        return u;

    }

    public Payment createPayment(Payment payment){

        PaymentEntity PaymentEn = PaymentConverter.toEntity(payment);

        try{
            beginTx(em);
            em.persist(PaymentEn);
            commitTx(em);
        }
        catch (Exception e){
            rollbackTx(em);
        }

        if (PaymentEn.getId() == null){
            throw new RuntimeException("Entity payment was not persisted");
        }

        return PaymentConverter.toDto(PaymentEn);

    }

    public Payment updatePayment(Long id, Payment file) {

        PaymentEntity PaymentEn_old = em.find(PaymentEntity.class, id);

        if (PaymentEn_old == null) {
            return null;
        }

        PaymentEntity PaymentEn_new = PaymentConverter.toEntity(file);

        try{
            beginTx(em);
            PaymentEn_new.setId(PaymentEn_old.getId());
            PaymentEn_new = em.merge(PaymentEn_new);
            commitTx(em);
        }
        catch (Exception e){
            rollbackTx(em);
        }

        return PaymentConverter.toDto(PaymentEn_new);
    }

    public boolean deletePayment(int id) {

        PaymentEntity fileEntity = em.find(PaymentEntity.class, id);

        if (fileEntity != null) {
            try {
                beginTx(em);
                em.remove(fileEntity);
                commitTx(em);
            }
            catch (Exception e) {
                rollbackTx(em);
            }
        }
        else {
            return false;
        }

        return true;
    }

}
