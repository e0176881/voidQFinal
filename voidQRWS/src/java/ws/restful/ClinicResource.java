package ws.restful;

import datamodel.ws.rest.CreatePatientReq;
import datamodel.ws.rest.CreatePatientRsp;
import datamodel.ws.rest.ErrorRsp;
import datamodel.ws.rest.PatientLoginRsp;
import datamodel.ws.rest.RetrieveAllActivatedClinicsRsp;
import datamodel.ws.rest.UpdatePatientReq;

import ejb.entity.BookingEntity;
import ejb.entity.ClinicEntity;
import ejb.entity.PatientEntity;
import ejb.session.stateless.ClinicSessionBeanLocal;
import ejb.session.stateless.PatientSessionBeanLocal;
import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.PatientNotFoundException;
import util.exception.UpdatePatientException;

@Path("Clinic")
public class ClinicResource {

    ClinicSessionBeanLocal clinicSessionBean = lookupClinicSessionBeanLocal();

   
    
    @Context
    private UriInfo context;

    
    private final ClinicSessionBeanLocal clinicSessionBeanLocal;

    public ClinicResource() {
       
        clinicSessionBeanLocal = lookupClinicSessionBeanLocal();
    }

   
    @Path("retrieveAllActivatedClinics")
    @GET
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllClinics()
    {
        try
        {
               
//            PatientEntity patientEntity = patientSessionBeanLocal.patientLogin(username, password);
//            System.out.println("********** PatientResource.retrieveAllClinics(): Patient " + patientEntity.getFirstName()+ " login remotely via web service");

            List<ClinicEntity> clinicEntities = clinicSessionBeanLocal.retrieveAllActivatedClinics();
             System.out.println("******ran retrieveAllActivatedClinic****");
            for(ClinicEntity clinicEntity:clinicEntities)
            {
//                if(clinicEntity.getApplicationStatus() == ACTIVATED)
//                {
                    clinicEntity.getBookingEntities().clear();
                    clinicEntity.getDoctorEntities().clear();
                    clinicEntity.getNurseEntities().clear();
                    clinicEntity.getApplicationStatus();
                //}
                
            }
            
            return Response.status(Status.OK).entity(new RetrieveAllActivatedClinicsRsp(clinicEntities)).build();
        }
        catch(Exception ex)
        {
            System.out.println("*****ERROR LA****");
            ErrorRsp errorRsp = new ErrorRsp(ex.getMessage());
            
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(errorRsp).build();
        }
    }

   

    private ClinicSessionBeanLocal lookupClinicSessionBeanLocal() {
        try {
            javax.naming.Context c = new InitialContext();
            return (ClinicSessionBeanLocal) c.lookup("java:global/voidQ/voidQ-ejb/ClinicSessionBean!ejb.session.stateless.ClinicSessionBeanLocal");
        } catch (NamingException ne) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "exception caught", ne);
            throw new RuntimeException(ne);
        }
    }
    
    

}
