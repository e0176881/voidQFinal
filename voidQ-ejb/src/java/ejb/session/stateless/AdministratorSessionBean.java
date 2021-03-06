/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;


import ejb.entity.AdminEntity;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import util.exception.AdministratorNotFoundException;
import util.exception.DeleteAdminException;
import util.exception.InputDataValidationException;
import util.exception.InvalidLoginCredentialException;
import util.exception.UpdateAdminException;
import util.security.CryptographicHelper;

/**
 *
 * @author mingxuan
 */
@Stateless
@Local(AdministratorSessionBeanLocal.class)
public class AdministratorSessionBean implements AdministratorSessionBeanLocal {

    @EJB(name = "EmailControllerLocal")
    private EmailControllerLocal emailControllerLocal;

    @PersistenceContext(unitName = "voidQ-ejbPU")
    private EntityManager em;
    private final ValidatorFactory validatorFactory;
    private final Validator validator;

    public AdministratorSessionBean() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Override
    public AdminEntity createNewAdmin(AdminEntity newAdmin) throws InputDataValidationException {
        Set<ConstraintViolation<AdminEntity>> constraintViolations = validator.validate(newAdmin);

        if (constraintViolations.isEmpty()) {
            em.persist(newAdmin);
            em.flush();

            return newAdmin;
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public AdminEntity retrieveAdminByUsername(String username) throws AdministratorNotFoundException {
        Query query = em.createQuery("SELECT a FROM AdminEntity a WHERE a.email = :inUsername");
        query.setParameter("inUsername", username);

        try {
            return (AdminEntity) query.getSingleResult();
        } catch (NoResultException | NonUniqueResultException ex) {
            throw new AdministratorNotFoundException("Administrator " + username + " does not exist!");
        }
    }

      @Override
    public AdminEntity resetPassword(Long adminId)
    {
         String password = new Random().ints(10, 33, 122).collect(StringBuilder::new,
                StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        AdminEntity admin= null;
        try {
             admin = retrieveAdminByAdminId(adminId);
          emailControllerLocal.emailResetPassword(admin, password);
        } catch (AdministratorNotFoundException ex) {
           
        }
        
       return admin;
      
    }
    
    @Override
    public List<AdminEntity> retrieveAllAdministrators() {
        Query query = em.createQuery("SELECT a FROM AdminEntity a");

        return query.getResultList();
    }

    @Override
    public AdminEntity retrieveAdminByAdminId(Long adminId) throws AdministratorNotFoundException {
        AdminEntity admin = em.find(AdminEntity.class, adminId);

        if (admin != null) {
            return admin;
        } else {
            throw new AdministratorNotFoundException("Admin ID " + adminId + " does not exist!");
        }
    }

    @Override
    public void updateAdmin(AdminEntity admin) throws InputDataValidationException, AdministratorNotFoundException, UpdateAdminException {
        // Updated in v4.1 to update selective attributes instead of merging the entire state passed in from the client
        // Also check for existing staff before proceeding with the update

        // Updated in v4.2 with bean validation
        Set<ConstraintViolation<AdminEntity>> constraintViolations = validator.validate(admin);

        if (constraintViolations.isEmpty()) {
            if (admin.getUserId()!= null) {
                AdminEntity adminToUpdate = retrieveAdminByAdminId(admin.getUserId());

                if (adminToUpdate.getEmail().equals(admin.getEmail())) {
                    adminToUpdate.setFirstName(admin.getFirstName());
                    adminToUpdate.setLastName(admin.getLastName());
                   
                } else {
                    throw new UpdateAdminException("Username of admin record to be updated does not match the existing record");
                }
            } else {
                throw new AdministratorNotFoundException("Administator ID not provided for admin to be updated");
            }
        } else {
            throw new InputDataValidationException(prepareInputDataValidationErrorsMessage(constraintViolations));
        }
    }

    @Override
    public AdminEntity userNameLogin(String email, String password) throws InvalidLoginCredentialException {
        try {
            AdminEntity admin = retrieveAdminByUsername(email);
            String passwordHash = CryptographicHelper.getInstance().byteArrayToHexString(CryptographicHelper.getInstance().doMD5Hashing(password + admin.getSalt()));

            if (admin.getPassword().equals(passwordHash)) {
                //load partner stuff .size

                return admin;
            } else {
                throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
            }
        } catch (AdministratorNotFoundException ex) {
            throw new InvalidLoginCredentialException("Username does not exist or invalid password!");
        }
    }

    @Override
    public void deleteAdmin(Long adminId) throws AdministratorNotFoundException, DeleteAdminException {
        AdminEntity admin = retrieveAdminByAdminId(adminId);

        /*if(partner.getSaleTransactionEntities().isEmpty())
        {
            em.remove(partner);
        } 
        else
        {
            // New in v4.1 to prevent deleting staff with existing sale transaction(s)
            throw new DeletePartnerException("Partner ID " + partnerId + " is associated with existing sale transaction(s) and cannot be deleted!");
        }*/
        // have to check if partner is associated with something 
        em.remove(admin);
    }
    
    @Override
    public void updateAdminPassword(Long adminId, String password) {
        AdminEntity admin = em.find(AdminEntity.class, adminId);
        admin.setPassword(password);
        em.merge(admin);
        em.flush();
    }

    private String prepareInputDataValidationErrorsMessage(Set<ConstraintViolation<AdminEntity>> constraintViolations) {
        String msg = "Input data validation error!:";

        for (ConstraintViolation constraintViolation : constraintViolations) {
            msg += "\n\t" + constraintViolation.getPropertyPath() + " - " + constraintViolation.getInvalidValue() + "; " + constraintViolation.getMessage();
        }

        return msg;
    }

}
