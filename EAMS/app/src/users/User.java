/**
 * The purpose of this class is to allow users to be created with a given template.
 * Making this class abstract so no instances of this class should be created.
 * Class Attendee and class class Organizer will extend this class.
 *
 */
import java.util.regex.Pattern;
import java.util.regex.Matcher;
public abstract class User {

    /**
     * Attributes for a user that will created as a person.
     * The email also refers to the email address of the user.
     */
    private String firstName, lastName, email, password, address;
    private long phoneNum;

    /**
     * Constructor takes in all user detail
     * This is because no person should be registered if their information isn't complete
     *
     * @param firstName, lastName, email, password, phoneNum, address
     * @throws IllegalArugumentException if either phoneNum or email is not valid
     */
    public User(String firstName, String lastName, String email, String password,
                long phoneNum, String address){
        if(!isEmailValid(email) || !isPhoneNumValid(phoneNum)){
            throw new IllegalArgumentException("Invalid phone number or email address");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phoneNum = phoneNum;
        this.address = address;
    }

    /**
     * Get the first name of the user
     *
     * @return String, the first name of the user
     */
    public String getFirstName(){
        return this.firstName;
    }

    /**
     * Get the first name of the user
     *
     * @return String, the first name of the user
     */
    public String getLastName(){
        return this.LastName;
    }
    /**
     * Get the ast name of the user
     *
     * @return String, the last name of the user
     */
    public String getEmail(){
        return this.email;
    }
    /**
     * Get the password of the user
     *
     * @return String, the password of the user
     */
    public String getPassword(){
        return this.password;
    }
    /**
     * Get the address of the user
     *
     * @return String, the address of the user
     */
    public String getAddress(){
        return this.address;}
    /**
     * Get the phone number of the user
     *
     * @return integer, the phone number of the user
     */
    public String getPhoneNumber(){
        return this.phoneNum;


        /**
         * toString method so it can represent the object in a logical format
         * Not showing sensitive information such as password and address
         */
    public String toString(){
        return "\n\tFirst Name: "+this.firstName+
                "\n\tLast Name: "+this.lastName+
                "\n\tEmail: "+this.email+
                "\n\tPhone Number: "+this.phoneNum;

    }

    /**
     * Method to verify if the email entered is in valid form
     * Note: the email have to be verified for validity before constructing a user.
     *
     * @param a string representing email
     * @return boolean
     */
    public boolean isEmailValid(String email){
        //code from https://howtodoinjava.com/java/regex/java-regex-validate-email-address/
        String regex = "^[a-zA-Z0-9_!#$%&amp;'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Method to verify if the phone number entered is in valid form
     * The method checks if user's phone number is exactly ten digits (Canadian standard)
     * Note: the phone number have to be verified for validity before constructing a user.
     *
     *
     * @param number long representing the phone number
     * @return boolean
     */
    public boolean isPhoneNumValid(long number){
        return number >= 1000000000L && number <= 9999999999L;
    }


}