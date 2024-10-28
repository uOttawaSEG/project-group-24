import java.util.ArrayList;

/**
 * The purpose of this class is to create a template to create an Administrator instance
 * Inheritance is used to create this class, as it is a type of User
 *
 */
public class Administrator extends User{
    /**
     * Class constructor
     * @param firstName  the first name of the Administrator
     * @param lastName   the last name of the Administrator
     * @param email      the email of the Administrator
     * @param password   the password of the Administrator
     * @param phoneNum   the phone number of the Administrator
     * @param address    the address of the Administrator
     * @param inbox
     * @para rejectedRequests
     */
    private ArrayList<User> inbox;
    private ArrayList<User> rejectedRequest;

    Administrator(String firstName, String lastName, String email, String password,
                  long phoneNum, String address) {
        super(firstName, lastName, email, password, phoneNum, address);

        inbox = new ArrayList<>();
        rejectedRequest = new Arraylist<>();
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

    /**
     * Method to accept registrations
     */

    public void acceptRegistration(User request){
        // TODO
        if (!request.isRejected()) {
            request.approve();
            inbox.remove(request);  // Remove from inbox after approval
        }
    }
    /**
     * Method to get registrations
     *
     */
    public void receiveRegistrationRequest(User request) {
        inbox.add(request);
    }
    /**
     * Method to view inbox
     */

    public ArrayList<User> viewInbox() {
        return inbox;
    }

    /**
     * Method to reject registations
     * @retrun adds removes rejections from inbox and sets them in
     */
    public void rejectRegistration(){
        // TODO
        if(!request.isApproved()) {
            request.reject();
            inbox.remove(request);  // Remove from inbox after rejection
            rejectedRequests.add(request);  // Add to rejected list
        }
    }

    /**
     * Method to access rejected registration requests
     * @return list of rejected requests
     */
    public ArrayList<User> getRejectedRequest() {
        return rejectedRequest;
    }

    /**
     * Method to access all registration requests
     *
     * @retrun list of registration requests
     */
    public ArrayList<User> getInbox() {
        return inbox;
    }
}
