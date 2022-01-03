package org.rso.naloga.zapiski.api.v1.resouces;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import user.lib.InfoMetadata;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Log
@ApplicationScoped
@Path("/info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, OPTIONS")
public class InfoMetadataResource {

    private Logger log = Logger.getLogger(InfoMetadataResource.class.getName());


    @Operation(description = "Get info metadata.", summary = "Get info.")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Info metadata",
                    content = @Content(schema = @Schema(implementation = InfoMetadata.class, type = SchemaType.OBJECT)),
                    headers = {@Header(name = "X-Total-Count", description = "Info")}
            )})
    @GET
    public Response getInfoMetadata() {


        InfoMetadata infoMetadata = new InfoMetadata();


        //List<ImageMetadata> imageMetadata = imageMetadataBean.getImageMetadataFilter(uriInfo);

        return Response.status(Response.Status.OK).entity(infoMetadata).build();
    }




}
