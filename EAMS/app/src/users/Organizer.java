/**
 * The purpose of this class is to create a template to create an Organizer instance
 * 
 * @author Sijun Liu
 */
public class Organizer extends PersonAbstract {
    /**
     * Organizer have unique attribute "Organization name"
     */
    String orgName;

    /**
    * Constructor that creates an Organizer
    * 
    * @param firstName, lastName, email, password, phoneNum, address, organization name
    */
    public Organizer(String firstName, String lastName, String email, String password,
        long phoneNum, String address, String orgName) {
        super(firstName, lastName, email, password, phoneNum, address);
        this.orgName = orgName;
    }

    /**
     * toString method to create a logical representation of the object
     * 
     * @return logical representation of the object
     */
    @Override
    public String toString(){
        return "Organizer" + super.toString() + "\n\tOrganization Name: " + orgName;

    }

    public static void main(String args[]){
        Organizer sijun;
        sijun = new Organizer("Sijun", "Liu", "2049616299@qq.com", "LSJlsj123", "6137902388", "435 albert st", "Nothing");
        System.out.println(sijun);
    }

    
}