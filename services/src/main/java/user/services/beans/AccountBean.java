package user.services.beans;

import user.lib.Account;
import user.models.converters.AccountConverter;
import user.models.entities.AccountEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequestScoped
public class AccountBean extends BeanBase {

    private Logger log = Logger.getLogger(AccountBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Account> getAllAccounts() {
        TypedQuery<AccountEntity> query = em.createNamedQuery(
                "AccountEntity.getAll", AccountEntity.class);

        List<AccountEntity> resultList = query.getResultList();

        return resultList.stream().map(AccountConverter::toDto).collect(Collectors.toList());

    }

    public Account getAccount(Integer id){
        AccountEntity AccountEn = em.find(AccountEntity.class, id);

        if (AccountEn == null){
            throw new NotFoundException();
        }

        Account u = AccountConverter.toDto(AccountEn);

        return u;

    }

    public Account getAccountByUser(Integer userId){
        AccountEntity AccountEn = em.createQuery("SELECT a FROM AccountEntity a WHERE a.userId = :usrId", AccountEntity.class)
                .setParameter("usrId", userId).getSingleResult();

        if (AccountEn == null){
            throw new NotFoundException();
        }

        Account u = AccountConverter.toDto(AccountEn);

        return u;

    }

    public Account createAccount(Account account){

        AccountEntity AccountEn = AccountConverter.toEntity(account);

        try{
            beginTx(em);
            em.persist(AccountEn);
            commitTx(em);
        }
        catch (Exception e){
            rollbackTx(em);
        }

        if (AccountEn.getId() == null){
            throw new RuntimeException("Entity account was not persisted");
        }

        return AccountConverter.toDto(AccountEn);

    }

    public Account updateAccount(Long id, Account account) {

        AccountEntity AccountEn_old = em.find(AccountEntity.class, id);

        if (AccountEn_old == null) {
            return null;
        }

        AccountEntity AccountEn_new = AccountConverter.toEntity(account);

        try{
            beginTx(em);
            AccountEn_new.setId(AccountEn_old.getId());
            AccountEn_new = em.merge(AccountEn_new);
            commitTx(em);
        }
        catch (Exception e){
            rollbackTx(em);
        }

        return AccountConverter.toDto(AccountEn_new);
    }

    public boolean deleteAccount(int id) {

        AccountEntity accountEntity = em.find(AccountEntity.class, id);

        if (accountEntity != null) {
            try {
                beginTx(em);
                em.remove(accountEntity);
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
