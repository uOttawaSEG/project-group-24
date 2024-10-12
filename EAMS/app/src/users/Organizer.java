/**
 * The purpose of this class is to create a template to create an Organizer instance
 *
 */
public class Organizer extends User {
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
     * Method to return the role of the user
     *
     * @return String, the role of the user
     */
    public String getRole(){
        return "Organizer";
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



}