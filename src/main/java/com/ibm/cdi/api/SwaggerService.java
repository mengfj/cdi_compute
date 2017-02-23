package com.ibm.cdi.api;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import io.swagger.annotations.Api;

@Path("/")
@Api(hidden = true)
public class SwaggerService {	
	@GET 
	public Response swaggerRoot(String poststr){		
		URI uri=UriBuilder.fromUri("swagger/").build();
		return Response.seeOther(uri).build();	 
	}
	
	
}
