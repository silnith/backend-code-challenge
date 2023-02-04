package com.midwesttape.project.challengeapplication.model;

import java.util.Objects;

/**
 * An address for a user.
 * 
 * <p>This corresponds to the database table "Address".
 */
public class Address {
    
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String postal;

    /**
     * Creates a new address.
     */
    public Address() {
        super();
    }

    /**
     * Returns the first line of the address.  Will not be null.  May be empty.
     * 
     * @return the first line of the address
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * Sets the first line of the address.  Cannot be null.  May be empty.
     * 
     * @param address1 the first line of the address to set
     * @throws IllegalArgumentException if the input is null
     */
    public void setAddress1(final String address1) {
        this.address1 = address1;
    }

    /**
     * Returns the second line of the address.  May be null.
     * 
     * @return the second line of the address
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * Sets the second line of the address.  May be null.
     * 
     * @param address2 the second line of the address to set
     */
    public void setAddress2(final String address2) {
        this.address2 = address2;
    }

    /**
     * Returns the city.  Will not be null.  May be empty.
     * 
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city.  Cannot be null.  May be empty.
     * 
     * @param city the city to set
     * @throws IllegalArgumentException if the city is null
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * Returns the state.  Will not be null.  May be empty.  Format is unspecified.
     * 
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state.  Cannot be null.  May be empty.  Format is unspecified.
     * 
     * @param state the state to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * Returns the postal code.  Will not be null.  May be empty.  Is not large enough for internationalization.
     * 
     * @return the postal code
     */
    public String getPostal() {
        return postal;
    }

    /**
     * Sets the postal code.  Cannot be null.  May be empty.  Limited to 10 characters.
     * 
     * @param postal the postal code to set
     */
    public void setPostal(final String postal) {
        this.postal = postal;
    }

    @Override
    public int hashCode() {
        return Objects.hash(address1, address2, city, postal, state);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Address)) {
            return false;
        }
        final Address other = (Address) obj;
        return Objects.equals(address1, other.address1) && Objects.equals(address2, other.address2)
                && Objects.equals(city, other.city) && Objects.equals(postal, other.postal)
                && Objects.equals(state, other.state);
    }

    @Override
    public String toString() {
        return "Address [address1=" + address1 + ", address2=" + address2 + ", city=" + city + ", state=" + state
                + ", postal=" + postal + "]";
    }

}
