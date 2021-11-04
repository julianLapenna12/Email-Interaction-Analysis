package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UDWInteractionGraph {

    private List<String> StringEmails = new ArrayList<String>();                 //Stores all raw email data
    private List<int []> ArrayEmailData = new ArrayList<int[]>();           //Stores source id and destination id at corresponding indicies for each email
    private Map<Integer, Integer> userInteractionCounts = new HashMap<>();  //Stores users mapped to their interaction counts
    private ArrayList<Integer> orderUsers = new ArrayList<>();
    private List<int []> interactions = new ArrayList<int []>();            //Stores number of interactions between users - number at each index is interactions between user (index of list) and user at index
    private Set<Integer> sendIds = new HashSet<Integer>();                        //Stores all ids of people who sent emails
    private Set<Integer> destIds = new HashSet<Integer>();                        //Stores all ids of people who received emails
    private Set<Integer> ids = new HashSet<Integer>();
    private Map<Integer, Integer> userIndex = new HashMap<Integer, Integer>();
    private HashSet<Integer>[] arrayOfComponentSets;
    private int nodes;

    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private final int TIME = 2;
    private final int LOWER_TIME = 0;
    private final int UPPER_TIME = 1;

    /* ------- Task 1 ------- */
    /* Building the Constructors */

    /**
     * Creates a new UDWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public UDWInteractionGraph(String fileName) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                StringEmails.add(fileLine);
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        stringDataToArray(StringEmails);
        MapEmailData(ArrayEmailData);
        initializeTask3Data();
        mapUsers();
        activeUsers();
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the input
     *                   UDWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, int[] timeFilter) {
        List<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputUDWIG.ArrayEmailData.size(); i++) {
            if (inputUDWIG.ArrayEmailData.get(i)[TIME] >= timeFilter[LOWER_TIME] && inputUDWIG.ArrayEmailData.get(i)[TIME] <= timeFilter[UPPER_TIME]) {
                ArrayEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                newEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                sendIds.add(inputUDWIG.ArrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.ArrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        mapUsers();
        activeUsers();
    }

    /**
     * Creates a new UDWInteractionGraph from a UDWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputUDWIG a UDWInteractionGraph object
     * @param userFilter a List of User IDs. The created UDWInteractionGraph
     *                   should exclude those emails in the input
     *                   UDWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public UDWInteractionGraph(UDWInteractionGraph inputUDWIG, List<Integer> userFilter) {
        List<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputUDWIG.ArrayEmailData.size(); i++) {
            if (userFilter.contains(inputUDWIG.ArrayEmailData.get(i)[SENDER]) || userFilter.contains(inputUDWIG.ArrayEmailData.get(i)[RECEIVER])) {
                ArrayEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                newEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                sendIds.add(inputUDWIG.ArrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.ArrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        mapUsers();
        activeUsers();
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG a DWInteractionGraph object
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {
        List<int[]> newEmailData = new ArrayList<int[]>();

        for (int i = 0; i < inputDWIG.getEmailData().size(); i++) {
            newEmailData.add(inputDWIG.getEmailData().get(i));
            ArrayEmailData = inputDWIG.getEmailData();
            sendIds.add(inputDWIG.getEmailData().get(i)[SENDER]);
            destIds.add(inputDWIG.getEmailData().get(i)[RECEIVER]);
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        mapUsers();
        activeUsers();
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() { return ids; }

    /**
     * @param sender the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        if (userIndex.containsKey(sender) && userIndex.containsKey(receiver))
            return interactions.get(userIndex.get(sender))[userIndex.get(receiver)];

        return 0;
    }

    private void stringDataToArray(List<String> emails) {
        int sendId = 0;
        int destId = 0;
        int timeId = 0;
        int counter = 0;
        boolean cont = true;

        for(int i = 0; i < emails.size(); i++) {
            int [] srcDstTime = new int[3];
            while(cont) {
                if (emails.get(i).charAt(counter)==' ') {
                    System.out.println(emails.get(i).substring(0, counter));
                    sendId = Integer.parseInt(emails.get(i).substring(0, counter));
                    cont = false;
                }
                counter++;
            }
            cont = true;
            int start = counter;
            while (cont) {
                if (emails.get(i).charAt(counter)==' ') {
                    destId = Integer.parseInt(emails.get(i).substring(start, counter));
                    cont = false;
                }
                counter++;
            }
            cont = true;
            start = counter;
            while (cont) {
                if (counter == emails.get(i).length()) {
                    timeId = Integer.parseInt(emails.get(i).substring(start, counter));
                    cont = false;
                }
                counter++;
            }

            sendIds.add(sendId);
            destIds.add(destId);

            srcDstTime[SENDER] = sendId;
            srcDstTime[RECEIVER] = destId;
            srcDstTime[TIME] = timeId;
            ArrayEmailData.add(srcDstTime);
            counter = 0;
            cont = true;
        }
    }

    private void MapEmailData(List<int[]> emailData) {

        ids.addAll(sendIds);
        ids.addAll(destIds);

        for (int i = 0; i < ids.size(); i++) {
            userIndex.put((int)ids.toArray()[i], i);
        }

        for (int i = 0; i < ids.size(); i++) {
            int[] dataForUser = new int[ids.size()];
            for (int j = 0; j < emailData.size(); j++) {
                if (emailData.get(j)[SENDER] == emailData.get(j)[RECEIVER]) {
                    dataForUser[userIndex.get(emailData.get(j)[RECEIVER])]++;
                } else {
                    if ((int) ids.toArray()[i] == emailData.get(j)[SENDER]) {
                        dataForUser[userIndex.get(emailData.get(j)[RECEIVER])]++;
                    }

                    if ((int) ids.toArray()[i] == emailData.get(j)[RECEIVER]) {
                        dataForUser[userIndex.get(emailData.get(j)[SENDER])]++;
                    }
                }
            }
            interactions.add(dataForUser);
        }
    }

    /* ------- Task 2 ------- */

    /**
     * @param timeWindow is an int array of size 2 [t0, t1]
     *                   where t0<=t1
     * @return an int array of length 2, with the following structure:
     *  [NumberOfUsers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        //create a new UDW object using the time window
        UDWInteractionGraph timeConstrained = new UDWInteractionGraph(this, timeWindow);

        int[] activityReport = new int[2];
        activityReport[0] = timeConstrained.ids.size();
        activityReport[1] = timeConstrained.ArrayEmailData.size();

        return activityReport;
    }

    /**
     * @param userID the User ID of the user for which
     *               the report will be created
     * @return an int array of length 2 with the following structure:
     *  [NumberOfEmails, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        int[] userInformation = new int[2];

        userInformation[0] = getUserInteractionCount(userID);
        userInformation[1] = uniqueUserInteractions(userID);
        return userInformation;
    }

    private int uniqueUserInteractions(int userID) {
        int count = 0;
        if (!userIndex.containsKey(userID)) return count;

        for(int j = 0; j < interactions.size(); j++) { // and if they have an interaction with someone
            if(interactions.get(userIndex.get(userID))[j] != 0) {
                count++;
            }
        }
        return count;
    }

    private int getUserInteractionCount(int userID) { // might wanna store this as a global and then perform lookups
        int count;
        if (!userIndex.containsKey(userID)) return 0;

        /*
        for(int i = 0; i < interactions.size(); i++) {
            count += interactions.get(userIndex.get(userID))[i];
        }
        */

        count = iterateUserInteractions(userIndex.get(userID));
        return count;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user
     */
    public int NthMostActiveUser(int N) {
        int mostActiveUser = 0;
        mostActiveUser = orderUsers.get(userIndex.get(N-1));

        return mostActiveUser;
    }

    private void mapUsers() { // this is kinda the same as getUserInteractionCount, but I haven't thought
                                // through the implications of user IDs vs internal indexing
        for (int i = 0; i < interactions.size(); i++) {
            /*
            int countInteractions = 0;
            for (int j = 0; j < interactions.size(); j++) {
                countInteractions += interactions.get(i)[j];
            }
            */
            userInteractionCounts.put(i,iterateUserInteractions(i));
        }
    }

    private int iterateUserInteractions(int user) {
        int count = 0;
        for(int i = 0; i < interactions.size(); i++) {
            count += interactions.get(user)[i];
        }
        return count;
    }

    private void activeUsers() {
        for (int i = 0; i < userInteractionCounts.size(); i++) {
            int maxVal = 0;
            int maxKey = -1;
            for (Map.Entry<Integer, Integer> entry : userInteractionCounts.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                if (value > maxVal && !orderUsers.contains(key)) {
                    maxVal = value;
                    maxKey = key;
                }
            }
            orderUsers.add(maxKey);
        }
    }

    /* ------- Task 3 ------- */

    /**
     * @return the number of completely disjoint graph
     *    components in the UDWInteractionGraph object.
     */
    public int NumberOfComponents() {
        return nodes;
    }

    private void initializeTask3Data() {
        arrayOfComponentSets = new HashSet[interactions.size()];
        arrayOfComponentSets = componentSets();
        arrayOfComponentSets = groupSets(arrayOfComponentSets);
        nodes = countNodes(arrayOfComponentSets);
    }

    /**
     * Finds the number of components on a graph. Assumes that the graph has already
     * been grouped ( using groupSets() )
     *
     * @param arrayOfSets an array of Hashsets containing a sorted graph
     * @return the number of unique components
     */
    private int countNodes(HashSet<Integer>[] arrayOfSets){
        int nodes = 0;
        for(HashSet<Integer> set : arrayOfSets) {
            if (set.size() > 0) nodes++;
        }
        return nodes;
    }

    private HashSet<Integer>[] groupSets(HashSet<Integer>[] arrayOfSets) {
        HashSet<Integer>[] groupedSets;
        groupedSets = arrayOfSets; // this might be redundant

        for(int i = 0; i < groupedSets.length; i++) { // sort through each set
            if (groupedSets[i].size() > 0) {
                HashSet<Integer> temp = new HashSet<>();
                temp.addAll(groupedSets[i]);
                for (Integer j : temp) { // check each person interacted with the set
                    for (int k = 0; k < groupedSets.length; k++) { // compare if that person is in another interaction set
                        if (k != i) {
                            if (groupedSets[k].contains(j)) { // if they are
                                groupedSets[i].addAll(groupedSets[k]); // add all the elements into the original set
                                groupedSets[k].removeAll(groupedSets[k]); // remove all the elements from the secondary set
                            }
                        }
                    }
                }
            }
        }

        return groupedSets;
    }

    /**
     * Breaks up the interactions into an array of sets where each set contains
     * the IDs of all the people who've interacted with associated person
     *
     * The associated person is the array index of the sets
     *
     * @return an array of sets where each index i of the array holds a set of
     * people that person i has interacted with
     */
    private HashSet<Integer>[] componentSets() {
        HashSet<Integer>[] componentSets = new HashSet[interactions.size()];

        for(int i = 0; i < interactions.size(); i++) { // going through each person
            componentSets[i] = new HashSet<>();

            for(int j = 0; j < interactions.size(); j++) { // and if they have an interaction with someone
                if(interactions.get(i)[j] != 0) {

                    // add both of them to person i's set of people interacted with
                    componentSets[i].add(i);
                    componentSets[i].add(j);
                }
            }
        }

        return componentSets;
    }

    /**
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return whether a path exists between the two users
     */
    public boolean PathExists(int userID1, int userID2) {
        for(HashSet<Integer> graphComponent : arrayOfComponentSets) {
            if(graphComponent.contains(userIndex.get(userID1)) && graphComponent.contains(userIndex.get(userID2))) {
                System.out.println("true");
                return true;
            }
        }
        System.out.println("false");
        return false;
    }

}
