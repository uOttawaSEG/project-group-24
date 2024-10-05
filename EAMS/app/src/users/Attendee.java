/**
 * The purpose of this class is to create a template to create an Attendee instance
 * 
 * @author Sijun Liu
 */
public class Attendee extends PersonAbstract{
    
   /**
    * Constructor that creates an Attendee
    * 
    * @param firstName, lastName, email, password, phoneNum, address
    */
    public Attendee(String firstName, String lastName, String email, 
        String password, long phoneNum, String address){
            super(firstName, lastName, email, password, phoneNum, address);
    }

    /**
     * toString method to create a logical representation of the object
     * 
     * @return logical representation of the object
     */
    @Override
    public String toString(){
        return "Attendee" + super.toString();

    }
}
