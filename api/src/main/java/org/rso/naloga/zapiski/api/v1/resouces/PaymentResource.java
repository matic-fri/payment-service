package org.rso.naloga.zapiski.api.v1.resouces;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
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
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, OPTIONS")
public class PaymentResource {

    private Logger log = Logger.getLogger(PaymentResource.class.getName());

    @Inject
    private PaymentBean paymentBean;
    @Inject
    private AccountBean accountBean;

    @Context
    protected UriInfo uriInfo;


    @Operation(description = "Get all payments.", summary = "Get payments.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "All payments",
                    content = @Content(schema = @Schema(implementation = Payment.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Payments")}
            )})
    @GET
    public Response getPayments(){
        List<Payment> payments = paymentBean.getAllPayments();

        return Response.status(Response.Status.OK).entity(payments).build();
    }


    @Operation(description = "Get specified payment.", summary = "Get payment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Specified payment",
                    content = @Content(schema = @Schema(implementation = Payment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Payment")}
            )})
    @GET
    @Path("{paymentId}")
    public Response getPaymentById(@PathParam("paymentId") int paymentId){

        Payment payment = paymentBean.getPayment(paymentId);

        if (payment == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).entity(payment).build();
    }


    @Operation(description = "Create new payment", summary = "Create payment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "New payment",
                    content = @Content(schema = @Schema(implementation = Payment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Payment")}
            )})
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


    @Operation(description = "Update specified payment.", summary = "Update payment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Updated payment",
                    content = @Content(schema = @Schema(implementation = Payment.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Payment")}
            )})
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


    @Operation(description = "Delete specified payment.", summary = "Delete payment.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Status",
                    content = @Content(schema = @Schema(implementation = Payment.class, type = SchemaType.BOOLEAN)),
                    headers = {@Header(name = "X-Total-Count", description = "Status")}
            )})
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
