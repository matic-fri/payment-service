package org.rso.naloga.zapiski.api.v1.resouces;

import user.lib.Account;
import user.services.beans.AccountBean;

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
@Path("/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountResource {

    private Logger log = Logger.getLogger(AccountResource.class.getName());

    @Inject
    private AccountBean accountBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getAccounts(){
        List<Account> accounts = accountBean.getAllAccounts();

        return Response.status(Response.Status.OK).entity(accounts).build();
    }

    @GET
    @Path("{accountId}")
    public Response getAccountById(@PathParam("accountId") int accountId){

        Account account = accountBean.getAccount(accountId);

        if (account == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        return Response.status(Response.Status.OK).entity(account).build();
    }

    @POST
    public Response createAccount(Account account){

        if (account.getUserId() == null) {

            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        account.setBalance(0);
        account.setReserved(0);
        account = accountBean.createAccount(account);

        return Response.status(Response.Status.OK).entity(account).build();
    }


    @DELETE
    @Path("{accountId}")
    public Response deleteLiterature(@PathParam("accountId") int accountId) {

        boolean deleted = accountBean.deleteAccount(accountId);

        if (deleted) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
