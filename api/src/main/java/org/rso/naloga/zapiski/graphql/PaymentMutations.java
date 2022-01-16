package org.rso.naloga.zapiski.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import user.lib.Account;
import user.lib.Payment;
import user.services.beans.AccountBean;
import user.services.beans.PaymentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class PaymentMutations {

    @Inject
    private AccountBean accountBean;

    @Inject
    private PaymentBean paymentBean;

    @GraphQLMutation
    public PaymentResponse createPayment(@GraphQLArgument(name = "payment") Payment payment){

        Account sender_account = accountBean.getAccountByUser(payment.getSenderId().intValue());
        Account receiver_account = accountBean.getAccountByUser(payment.getReceiverId().intValue());

        if (sender_account == null || receiver_account == null ||
                sender_account.getBalance() < payment.getAmount() ||
                payment.getFileId() == null || payment.getReceiverId() == null ||
                payment.getSenderId() == null || payment.getAmount() == null) {

            return new PaymentResponse(null, false);
        }

        // make transaction
        sender_account.setBalance(sender_account.getBalance() - payment.getAmount());
        sender_account.setReserved(sender_account.getReserved() + payment.getAmount());
        sender_account = accountBean.updateAccount(sender_account.getId(), sender_account);

        payment = paymentBean.createPayment(payment);

        if (payment == null) { // rollback

            sender_account.setReserved(sender_account.getReserved() - payment.getAmount());
            sender_account.setBalance(sender_account.getBalance() + payment.getAmount());
            sender_account = accountBean.updateAccount(sender_account.getId(), sender_account);

            return new PaymentResponse(null, false);
        }

        // commit
        sender_account.setReserved(sender_account.getReserved() - payment.getAmount());
        sender_account = accountBean.updateAccount(sender_account.getId(), sender_account);
        receiver_account.setBalance(receiver_account.getBalance() + payment.getAmount());
        receiver_account = accountBean.updateAccount(receiver_account.getId(), receiver_account);

        return  new PaymentResponse(payment, false);
    }

    @GraphQLMutation
    public PaymentResponse putPayment(@GraphQLArgument(name = "paymentId") long paymentId,
                                      @GraphQLArgument(name = "payment") Payment payment){

        if (payment.getFileId() == null || payment.getReceiverId() == null ||
                payment.getSenderId() == null || payment.getAmount() == null) {

            return new PaymentResponse(null, false);
        }

        payment = paymentBean.updatePayment(paymentId, payment);

        if (payment == null) {
            return new PaymentResponse(null, false);
        }

        return new PaymentResponse(payment, false);
    }

    @GraphQLMutation
    public DeleteResponse deletePayment(@GraphQLArgument(name = "paymentId") long paymentId){
        return new DeleteResponse(paymentBean.deletePayment(paymentId));
    }

}
