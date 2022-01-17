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
import user.lib.NewAccount;
import user.lib.UpdateBalance;
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
@CrossOrigin(supportedMethods = "GET, POST, PUT, DELETE, OPTIONS")
public class AccountResource {

    private Logger log = Logger.getLogger(AccountResource.class.getName());

    @Inject
    private AccountBean accountBean;

    @Context
    protected UriInfo uriInfo;


    @Operation(description = "Get all accounts.", summary = "Get accounts.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "All accounts",
                    content = @Content(schema = @Schema(implementation = Account.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Accounts")}
            )})
    @GET
    public Response getAccounts(){
        List<Account> accounts = accountBean.getAllAccounts();

        return Response.status(Response.Status.OK).entity(accounts).build();
    }


    @Operation(description = "Get specified account.", summary = "Get account.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Specified account",
                    content = @Content(schema = @Schema(implementation = Account.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Account")}
            )})
    @GET
    @Path("{accountId}")
    public Response getAccountById(@PathParam("accountId") long accountId){

        Account account = accountBean.getAccount(accountId);

        if (account == null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        if(account.getId()==-1){
            return Response.status(500, "Fallback. Account does not exist or circuit open.").build();
        }

        return Response.status(Response.Status.OK).entity(account).build();
    }


    @Operation(description = "Create new account for user", summary = "Create account.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "New account for user",
                    content = @Content(schema = @Schema(implementation = NewAccount.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Create account")}
            )})
    @POST
    public Response createAccount(NewAccount accountInput){


        Account account = new Account();
        account.setUserId(accountInput.getUserId());

        account.setBalance(0);
        account.setReserved(0);
        account = accountBean.createAccount(account);

        return Response.status(Response.Status.OK).entity(account).build();
    }

    @Operation(description = "Add balance and reserved to current account (can be negative)", summary = "Update balance and reserved.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Update balance and reserved for an account",
                    content = @Content(schema = @Schema(implementation = UpdateBalance.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Update balance for account")}
            )})
    @PUT
    @Path("{accountId}")
    public Response updateBalance(@PathParam("accountId") long accountId, UpdateBalance ub){
        Account a = accountBean.updateBalance(accountId, ub);
        if(a==null){
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return Response.status(Response.Status.OK).entity(a).build();
    }


    @Operation(description = "Delete specified account.", summary = "Delete account.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Status",
                    content = @Content(schema = @Schema(implementation = Account.class, type = SchemaType.BOOLEAN)),
                    headers = {@Header(name = "X-Total-Count", description = "Status")}
            )})
    @DELETE
    @Path("{accountId}")
    public Response deleteLiterature(@PathParam("accountId") long accountId) {

        boolean deleted = accountBean.deleteAccount(accountId);

        if (deleted) {
            return Response.status(Response.Status.OK).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
