package com.ibm.cdi.api;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("v1/")
public class CDIDataServices {
	@Path("info") 
	@GET
	@Produces(MediaType.TEXT_PLAIN)
    public String getInfo() {		
		return "This service is a CDI data service.";
    }
	
	
	@Path("getMetrics") 
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
    public Map getMetrics(String poststr) {		
				Map map=new HashMap();				
				map.put("a", "100");
				return map;
				
    }
}
