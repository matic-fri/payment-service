package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import user.lib.Payment;
import user.services.beans.PaymentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class PaymentQueries {

    @Inject
    private PaymentBean paymentBean;

    @GraphQLQuery
    public Payment getPayment(@GraphQLArgument(name = "id") Long id){
        return paymentBean.getPayment(id);
    }

    @GraphQLQuery
    public PaginationWrapper<Payment> getAllPayments(@GraphQLArgument(name = "pagination") Pagination pagination,
                                               @GraphQLArgument(name = "sort") Sort sort,
                                               @GraphQLArgument(name = "filter") Filter filter){

        return GraphQLUtils.process(paymentBean.getAllPayments(), pagination, sort, filter);

    }
}
