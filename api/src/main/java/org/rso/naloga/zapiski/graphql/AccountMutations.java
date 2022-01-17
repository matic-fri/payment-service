package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import user.lib.Account;
import user.lib.UpdateBalance;
import user.services.beans.AccountBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@GraphQLClass
@ApplicationScoped
public class AccountMutations {

    @Inject
    private AccountBean accountBean;

    @GraphQLMutation
    public Account addAccount(@GraphQLArgument(name = "userId") long userId){
        Account a = new Account();
        a.setUserId(userId);
        a.setBalance(0);
        a.setReserved(0);
        return accountBean.createAccount(a);
    }

    @GraphQLMutation
    public Account updateBalance(@GraphQLArgument(name = "accountId") long accountId,
                                 @GraphQLArgument(name = "updateBalance") UpdateBalance ub){

        return accountBean.updateBalance(accountId, ub);

    }

    @GraphQLMutation
    public DeleteResponse deleteAccount(@GraphQLArgument(name = "accountId") long accountId){
        return new DeleteResponse(accountBean.deleteAccount(accountId));
    }
}
