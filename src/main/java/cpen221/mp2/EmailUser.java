package cpen221.mp2;

import java.util.HashSet;
import java.util.Objects;

class EmailUser {
    private int id;
    private int sends = 0;
    private int receives = 0;
    private HashSet<Integer> interactions = new HashSet<>();

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
        if (o == null || getClass() != o.getClass()) return false;
        EmailUser emailUser = (EmailUser) o;
        return id == emailUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
