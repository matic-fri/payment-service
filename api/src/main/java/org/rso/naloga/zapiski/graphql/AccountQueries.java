package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import user.lib.Account;
import user.lib.Payment;
import user.services.beans.AccountBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class AccountQueries {

    @Inject
    private AccountBean accountBean;

    @GraphQLQuery
    public Account getAccount(@GraphQLArgument(name = "id") Long id){
        return accountBean.getAccount(id);
    }

    @GraphQLQuery
    public PaginationWrapper<Account> getAllAccounts(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                  @GraphQLArgument(name = "sort") Sort sort,
                                                  @GraphQLArgument(name = "filter") Filter filter){

        return GraphQLUtils.process(accountBean.getAllAccounts(), pagination, sort, filter);

    }
}
