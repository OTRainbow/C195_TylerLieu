package com.tylerlieu.c195.model;

/**
 * Represents a Customer record from the customers table
 */
public class Customer {
    /**
     * The Primary Key
     */
    private int customerID;
    /**
     * This Customer's name
     */
    private String name;
    /**
     * This Customer's phone number
     */
    private String phone;
    /**
     * This Customer's address
     */
    private String address;
    /**
     * A Foreign Key for the Division this Customer lives in
     */
    private int divisionID;
    /**
     * The name of the Division this Customer lives in
     */
    private String divisionName;
    /**
     * A Foreign Key for the Country this Customer lives in
     */
    private int countryID;
    /**
     * The name of the Country this Customer lives in
     */
    private String countryName;
    /**
     * The Customer's postal/zip code
     */
    private String postal;
    /**
     * The Division this Customer lives in.
     *
     * DERIVED from divisionID, divisionName, and countryID
     */
    private Division division;
    /**
     * The Country this Customer lives in.
     *
     * DERIVED from countryID and countryName
     */
    private Country country;

    /**
     * Fully constructs a Customer object with all fields populated.
     * Used when calling <code>CustomersQuery.getAllCustomerRecords()</code>.
     * @param customerID Primary Key
     * @param name Customer name
     * @param phone Phone number
     * @param address Street Address
     * @param divisionID Foreign Key for Division
     * @param divisionName Division name
     * @param countryID Foreign Key for Country
     * @param countryName Country name
     * @param postal Postal/Zip code
     */
    public Customer(int customerID, String name, String phone, String address, int divisionID, String divisionName, int countryID, String countryName, String postal) {
        this.customerID = customerID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
        this.countryName = countryName;
        this.postal = postal;
        this.division = new Division(divisionID, divisionName, countryID);
        this.country = new Country(countryID, countryName);
    }

    /**
     * Constructs a Customer object using only the fields necessary for adding a Customer to the database.
     * Used when calling <code>CustomersQuery.addCustomer()</code>
     * @param name Customer name
     * @param address Street Address
     * @param postal Postal/Zip code
     * @param phone Phone number
     * @param divisionID Foreign Key for Division
     */
    public Customer(String name, String address, String postal, String phone, int divisionID) {
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**
     * Constructs a Customer object using only the fields necessary for updating a Customer in the database.
     * Used when calling <code>CustomersQuery.updateCustomer(Customer)</code>.
     * @param customerID Primary Key
     * @param name Customer name
     * @param address Street Address
     * @param postal Postal/Zip code
     * @param phone Phone number
     * @param divisionID Foreign Key for Division
     */
    public Customer(int customerID, String name, String address, String postal, String phone, int divisionID) {
        this.customerID = customerID;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionID = divisionID;
    }

    /**
     * Constructs a Customer object using only the name and primary key.
     * Used when creating an Appointment object
     * @param customerID Primary Key
     * @param name Customer name
     */
    public Customer(int customerID, String name) {
        this.customerID = customerID;
        this.name = name;
    }
    /**
     * Overrides the toString() method in order to properly display in ComboBoxes
     * @return A String representation of the Customer instance
     */
    @Override
    public String toString() {
        return (customerID + " " + name);
    }

    /**
     * Returns this Customer's customerID
     * @return Primary Key
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Returns this Customer's name
     * @return Customer's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns this Customer's phone number
     * @return Phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Returns this Customer's address
     * @return Street Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Returns this Customer's Division's divisionID
     * @return Foreign Key for Division
     */
    public int getDivisionID() {
        return divisionID;
    }

    /**
     * Returns this Customer's Division's divisionName
     * @return Division name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Returns this Customer's Country's countryID
     * @return Foreign Key for Country
     */
    public int getCountryID() {
        return countryID;
    }

    /**
     * Returns this Customer's Country's countryName
     * @return Country name
     */
    public String getCountryName() {
        return countryName;
    }

    /**
     * Returns this Customer's postal code
     * @return Postal/Zip code
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Returns this Customer's Division
     * @return Division object
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Returns this Customer's Country
     * @return Country object
     */
    public Country getCountry() {
        return country;
    }
}
