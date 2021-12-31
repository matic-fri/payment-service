package org.rso.naloga.zapiski.api.v1.resouces;

import user.lib.Account;
import user.lib.Payment;
import user.services.beans.AccountBean;
import user.services.beans.PaymentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/payments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PaymentResource {

    private Logger log = Logger.getLogger(PaymentResource.class.getName());

    @Inject
    private PaymentBean paymentBean;
    @Inject
    private AccountBean accountBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getPayments(){
        List<Payment> payments = paymentBean.getAllPayments();

        return Response.status(Response.Status.OK).entity(payments).build();
    }

    @GET
    @Path("{paymentId}")
    public Response getPaymentById(@PathParam("paymentId") int paymentId){

        Payment payment = paymentBean.getPayment(paymentId);

        if (payment == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).entity(payment).build();
    }

    @POST
    public Response createPayment(Payment payment){

        Account sender_account = accountBean.getAccountByUser(payment.getSenderId().intValue());
        Account receiver_account = accountBean.getAccountByUser(payment.getReceiverId().intValue());

        if (sender_account == null || receiver_account == null ||
                sender_account.getBalance() < payment.getAmount() ||
                payment.getFileId() == null || payment.getReceiverId() == null ||
                payment.getSenderId() == null || payment.getAmount() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
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

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // commit
        sender_account.setReserved(sender_account.getReserved() - payment.getAmount());
        sender_account = accountBean.updateAccount(sender_account.getId(), sender_account);
        receiver_account.setBalance(receiver_account.getBalance() + payment.getAmount());
        receiver_account = accountBean.updateAccount(receiver_account.getId(), receiver_account);

        return Response.status(Response.Status.OK).entity(payment).build();
    }

    @PUT
    @Path("{paymentId}")
    public Response putPayment(@PathParam("paymentId") Long paymentId, Payment payment) {

        if (payment.getFileId() == null || payment.getReceiverId() == null ||
                payment.getSenderId() == null || payment.getAmount() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        payment = paymentBean.updatePayment(paymentId, payment);

        if (payment == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(payment).build();
    }

    @DELETE
    @Path("{paymentId}")
    public Response deleteLiterature(@PathParam("paymentId") int paymentId) {

        boolean deleted = paymentBean.deletePayment(paymentId);

        if (deleted) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }


}
