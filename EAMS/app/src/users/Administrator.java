/**
 * The purpose of this class is to create a template to create an Administrator instance
 * Inheritance is used to create this class, as it is a type of User
 *
 */
public class Administrator extends User{

    /**
     * Class constructor
     *
     * @param firstName  the first name of the Administrator
     * @param lastName   the last name of the Administrator
     * @param email      the email of the Administrator
     * @param password   the password of the Administrator
     * @param phoneNum   the phone number of the Administrator
     * @param address    the address of the Administrator
     */
    Administrator(String firstName, String lastName, String email, String password,
                  long phoneNum, String address) {
        super(firstName, lastName, email, password, phoneNum, address);
    }

    /**
     * Method to return the role of the Administrator
     *
     * @return the role of the Administrator
     */
    public String getRole() {
        return "Administrator";
    }

    /**
     * toString method to create a logical representation of the object
     *
     * @return logical representation of the object
     */
    @Override
    public String toString(){
        return "Administrator" + super.toString();

    }
}
