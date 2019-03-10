package jsf.managedbean;

import ejb.entity.MessageOfTheDayEntity;
import ejb.session.stateless.MessageOfTheDayControllerLocal;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;



@Named(value = "partnerIndexManagedBean")
@RequestScoped

public class IndexManagedBean 
{
    @EJB
    private MessageOfTheDayControllerLocal messageOfTheDayControllerLocal;
    
    private List<MessageOfTheDayEntity> messageOfTheDayEntities;
    
    
    
    public IndexManagedBean() 
    {
    }



    @PostConstruct
    public void postConstruct()
    {
        messageOfTheDayEntities = messageOfTheDayControllerLocal.retrieveAllMessagesOfTheDay();
    }

    
    
    public List<MessageOfTheDayEntity> getMessageOfTheDayEntities() {
        return messageOfTheDayEntities;
    }

    public void setMessageOfTheDayEntities(List<MessageOfTheDayEntity> messageOfTheDayEntities) {
        this.messageOfTheDayEntities = messageOfTheDayEntities;
    }
}
