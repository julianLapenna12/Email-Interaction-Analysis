package cpen221.mp2;

import java.util.HashSet;
import java.util.Objects;

/**
 * A class for storing information about a single user in an Email network for getting metrics about them
 * Stores value of how many emails they have sent and received and the number of emails you interacted with.
 */
class EmailUser {
    private final int id;
    private int sends = 0;
    private int receives = 0;
    private final HashSet<Integer> interactions = new HashSet<>();

    public EmailUser(int id){
        this.id = id;
    }

    public EmailUser(int id, SendOrReceive sR, int otherID){
        this.id = id;
        if(sR.equals( SendOrReceive.RECEIVE)){
            receives++;
            interactions.add(otherID);
        }
        else{
            sends++;
            interactions.add(otherID);
        }
    }

    public void receiveEmail(int sender){
        receives++;
        interactions.add(sender);
    }

    public void sendEmail(int recipient){
        sends++;
        interactions.add(recipient);
    }

    public int getNumSends(){
        return sends;
    }

    public int getNumReceives(){
        return receives;
    }

    public int getNumInteractions(){
        return interactions.size();
    }

    public int getId(){
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if(o instanceof EmailUser) {
            EmailUser emailUser = (EmailUser) o;
            return id == emailUser.id;
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
