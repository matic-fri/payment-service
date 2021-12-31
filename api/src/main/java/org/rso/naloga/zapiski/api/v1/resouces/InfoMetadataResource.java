package org.rso.naloga.zapiski.api.v1.resouces;

import user.lib.InfoMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class InfoMetadataResource {

    private Logger log = Logger.getLogger(InfoMetadataResource.class.getName());


    @GET
    public Response getInfoMetadata() {


        InfoMetadata infoMetadata = new InfoMetadata();


        //List<ImageMetadata> imageMetadata = imageMetadataBean.getImageMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(infoMetadata).build();
    }




}
