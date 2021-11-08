package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.Set;
import java.util.List;

/**
 * This class represents a graph holding all email interactions between users where each node is
 *   a user who sent or received an email and each edge means the starting node and ending node (could
 *   be the same node) have interacted at least once. The weight of the edge represents the frequency
 *   of connection between nodes (or in other words is how many times the respective nodes have
 *   interacted).
 *
 * Abstraction Invariant:
 *   Each distinct user that has received or sent an email is a unique node and must have at least one common
 *   edge with another node in the graph (including itself)
 */
public class UDWInteractionGraph {

    /** Graph data separated by connection where each element of arrayEmailData
     *    holds an interaction of weight 1 between two nodes */
    private ArrayList<int[]> arrayEmailData = new ArrayList<int[]>();

    /** A list of nodes in order of number of distinct connections to other nodes.
     *    The node connected to the most other nodes is at the front, and
     *    nodes with the least amount of connections are at the end.
     *
     *    Two nodes with the same number of connections are ordered by node number
     *
     *    A node only connected to itself has exactly one connection. */
    private ArrayList<Integer> orderedNodes = new ArrayList<>();

    /** A grid (or matrix) that holds interaction data such that each element (i,j)
     *    represents the weight between nodes i and j. (By symmetry, the elements
     *    (i,j) and (j,i) have the same value) */
    private ArrayList<int[]> interactions = new ArrayList<int[]>();

    /** The ID of every node (each user who has sent or received an email). */
    private HashSet<Integer> ids = new HashSet<>();

    /** A mapping that stores the node number on the graph, to the ID of the
     *    user represented by that node. */
    private HashMap<Integer, Integer> userIndex = new HashMap<Integer, Integer>();

    /** The components of the graph separated into array elements. Each non
     *    empty set is a unique graph component that appears only once. Empty
     *    sets do not matter. */
    private HashSet<Integer>[] arrayOfComponentSets;

    /** The number of components on the graph. */
    private int components;

    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private final int TIME = 2;

    // Representation Invariant
    //   For all elements of arrayEmailData, each integer array size is 3
    //   All ids in emailData must also be contained in ids
    //   For every graph G, (# of edges) >= (# of components) - (# of nodes),
    //     components = (# of components), ids.size() = (# of nodes)

    private void checkRep() {

        for (int i = 0; i < arrayEmailData.size(); i++) {
            if (arrayEmailData.get(i).length != 3) throw new RuntimeException("invalid data storage");
        }

        int edges = 0;
        for (int i = 0; i < interactions.size(); i++) {
            for (int j = i; j < interactions.size(); j++) {
                if (interactions.get(i)[j] != 0) edges++;
            }
        }

        if (edges < components - ids.size()) throw new RuntimeException("All present users should " +
                "have at least one interaction");
    }


    // Abstraction Function
    //   For any graph G with nodes m1, m2, m3, ... mN
    //     Edge between mX and mY = G.getEmailCount(mX, mY)
    //   Nodes in G = G.ids
    //

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
        //Stores all raw email data
        ArrayList<String> stringEmails = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                stringEmails.add(fileLine);
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }
        arrayEmailData = stringDataToArray(stringEmails);
        MapEmailData(arrayEmailData);
        initializeTask3Data();
        activeUsers(mapUsers());
        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph using an email interaction file.
     *          and considering a time window filter.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     * @param timeWindow an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created UDWInteractionGraph
     *                   should only include those emails in the input
     *                   UDWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public UDWInteractionGraph(String fileName, int[] timeWindow) {
        this(new UDWInteractionGraph(fileName), timeWindow);
        checkRep();
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
        ArrayList<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputUDWIG.arrayEmailData.size(); i++) {
            int LOWER_TIME = 0;
            int UPPER_TIME = 1;
            if (inputUDWIG.arrayEmailData.get(i)[TIME] >= timeFilter[LOWER_TIME]
                    && inputUDWIG.arrayEmailData.get(i)[TIME] <= timeFilter[UPPER_TIME]) {
                arrayEmailData.add(inputUDWIG.arrayEmailData.get(i));
                newEmailData.add(inputUDWIG.arrayEmailData.get(i));
                ids.add(inputUDWIG.arrayEmailData.get(i)[SENDER]);
                ids.add(inputUDWIG.arrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        activeUsers(mapUsers());
        checkRep();
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
        ArrayList<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputUDWIG.arrayEmailData.size(); i++) {
            if (userFilter.contains(inputUDWIG.arrayEmailData.get(i)[SENDER]) ||
                    userFilter.contains(inputUDWIG.arrayEmailData.get(i)[RECEIVER])) {
                arrayEmailData.add(inputUDWIG.arrayEmailData.get(i));
                newEmailData.add(inputUDWIG.arrayEmailData.get(i));
                ids.add(inputUDWIG.arrayEmailData.get(i)[SENDER]);
                ids.add(inputUDWIG.arrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        activeUsers(mapUsers());
        checkRep();
    }

    /**
     * Creates a new UDWInteractionGraph from a DWInteractionGraph object.
     *
     * @param inputDWIG a DWInteractionGraph object
     */
    public UDWInteractionGraph(DWInteractionGraph inputDWIG) {
        ArrayList<int[]> newEmailData = new ArrayList<int[]>();

        for (int i = 0; i < inputDWIG.getEmailData().size(); i++) {
            newEmailData.add(inputDWIG.getEmailData().get(i));
            arrayEmailData = inputDWIG.getEmailData();
            ids.add(inputDWIG.getEmailData().get(i)[SENDER]);
            ids.add(inputDWIG.getEmailData().get(i)[RECEIVER]);
        }

        MapEmailData(newEmailData);
        initializeTask3Data();
        activeUsers(mapUsers());
        checkRep();
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this UDWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        return ids;
    }

    /**
     * @param user1 the User ID of the first user.
     * @param user2 the User ID of the second user.
     * @return the number of email interactions (send/receive) between user1 and user2
     */
    public int getEmailCount(int user1, int user2) {
        if (userIndex.containsKey(user1) && userIndex.containsKey(user2))
            return interactions.get(userIndex.get(user1))[userIndex.get(user2)];
        return 0;
    }

    /**
     * @param emails a non-null list of Strings representing emails between
     *               users. Each String contains three integers separated by spaces
     *               where the first integer is the sender's user ID, the second
     *               integer is the receiver's user ID and the third integer is the
     *               time signature of the email.
     * @return emails where the String values have been converted to integers
     *         and each respective integer represents the same thing it did
     *         as a String
     */
    private ArrayList<int[]> stringDataToArray(ArrayList<String> emails) {
        ArrayList<int[]> data = new ArrayList<>();
        int sendId, destId, timeId;

        for (String email : emails) {
            String[] srcDstTimeStr = new String[3];
            int counter = 0;

            for (int j = 0; j < 3; j++) { //Iterates through three datapoints in email
                while (email.charAt(0) == ' ') {
                    email = email.substring(1);//Appends email so it doesn't start with a space
                }
                while (srcDstTimeStr[j] == null) {
                    if (email.length() <= counter || email.charAt(counter) == ' ') {
                        srcDstTimeStr[j] = email.substring(0, counter);
                        email = email.substring(counter);
                    }
                    counter++;
                }
                counter = 0;
            }

            sendId = Integer.parseInt(srcDstTimeStr[SENDER]);
            destId = Integer.parseInt(srcDstTimeStr[RECEIVER]);
            timeId = Integer.parseInt(srcDstTimeStr[TIME]);

            ids.add(sendId);
            ids.add(destId);


            data.add(new int[]{sendId, destId, timeId});
        }
        return data;
    }

    /**
     * Builds an interaction chart from email data to record the number
     * of interactions between every two users
     *
     * @param emailData a list of integer arrays containing the email data
     *                  between users where each int[].length = 3 and
     *                  int[0] = sender's user ID
     *                  int[1] = receiver's user ID
     *                  int[2] = time signature of email
     */
    private void MapEmailData(ArrayList<int[]> emailData) {

        for (int i = 0; i < ids.size(); i++) {
            // mapping the user IDs to their internal indexes
            userIndex.put((int) ids.toArray()[i], i);
        }

        for (int i = 0; i < ids.size(); i++) { // iterating through the users

            // initializing the int[] for a single user's data
            int[] dataForUser = new int[ids.size()];

            // iterating through the other users to find who interacted with who
            for (int[] emailDatum : emailData) {

                // for user interactions with themselves
                if (emailDatum[SENDER] == emailDatum[RECEIVER]
                        && (int) ids.toArray()[i] == emailDatum[SENDER]) {
                    dataForUser[userIndex.get(emailDatum[RECEIVER])]++;

                } else { // for interactions with other users

                    if ((int) ids.toArray()[i] == emailDatum[SENDER]) {
                        dataForUser[userIndex.get(emailDatum[RECEIVER])]++;
                    }
                    if ((int) ids.toArray()[i] == emailDatum[RECEIVER]) {
                        dataForUser[userIndex.get(emailDatum[SENDER])]++;
                    }
                }
            }
            // add the user to the master interactions list
            interactions.add(dataForUser);
        }
    }

    /* ------- Task 2 ------- */

    /**
     * @param timeWindow is an int array of size 2 [t0, t1]
     *                   where t0<=t1
     * @return an int array of length 2, with the following structure:
     *   [NumberOfUsers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        //create a new UDW object using the time window
        UDWInteractionGraph timeConstrained = new UDWInteractionGraph(this, timeWindow);

        int[] activityReport = new int[2];
        activityReport[0] = timeConstrained.ids.size();
        activityReport[1] = timeConstrained.arrayEmailData.size();

        return activityReport;
    }

    /**
     * @param userID the User ID of the user for which
     *               the report will be created
     * @return an int array of length 2 with the following structure:
     *   [NumberOfEmails, UniqueUsersInteractedWith]
     *   If the specified User ID does not exist in this instance of a graph,
     *   returns [0, 0].
     */
    public int[] ReportOnUser(int userID) {
        int[] userInformation = new int[2];

        userInformation[0] = getUserInteractionCount(userID);
        userInformation[1] = uniqueUserInteractions(userID);
        return userInformation;
    }

    /**
     * @param userID the user ID of the user to lookup
     * @return the number of distinct users (which can include themselves) that
     *   a given user has interacted with
     */
    private int uniqueUserInteractions(int userID) {
        int count = 0;
        if (!userIndex.containsKey(userID)) return count;

        for (int j = 0; j < interactions.size(); j++) {
            // and if they have an interaction with someone
            if (interactions.get(userIndex.get(userID))[j] != 0) {
                count++;
            }
        }
        return count;
    }

    /**
     * Helper method to check user ID and switch between user ID
     * and user index before getting the action count
     *
     * @param userID the user ID of the user to lookup
     * @return the total number of interactions between userID and
     *   any other user
     */
    private int getUserInteractionCount(int userID) {
        int count;
        if (!userIndex.containsKey(userID)) return 0;

        count = iterateUserInteractions(userIndex.get(userID));
        return count;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user, or -1 if either N < 1
     *   or if N > number of users
     */
    public int NthMostActiveUser(int N) {
        // only valid for 0 < N <= number of users
        if (N < 1) return -1;

        if (ids.size() >= N) {
            return reverseLookup(orderedNodes.get(N - 1), userIndex);
        }
        return -1;
    }

    /**
     *
     * @param value a value that is contained at least once in the values of
     *              a Map
     * @param map a non-null Map such that value appears in the Map's values
     *            at least once
     * @return the first key in the map that is mapped to the given value
     *
     */
    private int reverseLookup(int value, HashMap<Integer,Integer> map) {
        for (HashMap.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() == value) return entry.getKey();
        }

        return 0;
    }

    /**
     *
     * @return a map of each user and their interaction count. Helper method
     *        that helps when computing the listing of users ranked
     *        by activity
     */
    private HashMap<Integer, Integer> mapUsers() {
        HashMap<Integer, Integer> userInteractionCounts = new HashMap<>();
        for (int i = 0; i < interactions.size(); i++) {
            userInteractionCounts.put(i, iterateUserInteractions(i));
        }
        return userInteractionCounts;
    }

    /**
     * @param user the index of the specific user
     * @return the total number of interactions between the user and
     *   any other user
     */
    private int iterateUserInteractions(int user) {
        int count = 0;
        for (int i = 0; i < interactions.size(); i++) {
            count += interactions.get(user)[i];
        }
        return count;
    }

    /**
     * Generates a listing of users ordered by the most active user
     *   appearing first and the least active user appearing last.
     * <p>
     * When two users have the same activity, the user with
     *   the lower user ID comes first in the listing.
     */
    private void activeUsers(HashMap<Integer, Integer> interactionCounts) {
        for (int i = 0; i < interactionCounts.size(); i++) {
            int maxVal = 0;
            int maxKey = -1;
            for (HashMap.Entry<Integer, Integer> entry : interactionCounts.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                // when two users have the same interaction count, the first will be added,
                // then the second time around they will appear in orderedNodes
                if (value > maxVal && !orderedNodes.contains(key)) {
                    maxVal = value;
                    maxKey = key;
                }
            }
            orderedNodes.add(maxKey);
        }
    }

    /* ------- Task 3 ------- */

    /**
     * @return the number of completely disjoint graph
     *   components in the UDWInteractionGraph object.
     */
    public int NumberOfComponents() {
        return components;
    }

    /**
     * Helper method that initializes the component graph, nodes and paths
     *   for task 3. Included in a single method to make constructors
     *   more concise. Given the immutability, these should never be
     *   changed after initialization.
     */
    private void initializeTask3Data() {
        arrayOfComponentSets = new HashSet[interactions.size()];
        arrayOfComponentSets = componentSets();
        arrayOfComponentSets = groupSets(arrayOfComponentSets);
        components = countNodes(arrayOfComponentSets);
    }

    /**
     * Finds the number of components on a graph. Assumes that the graph has already
     *   been grouped ( using groupSets() )
     *
     * @param arrayOfSets an array of non-null HashSets containing a sorted graph
     *                    where each HashSet contains a unique graph node or is empty
     * @return the number of unique components
     */
    private int countNodes(HashSet<Integer>[] arrayOfSets) {
        int nodes = 0;
        for (HashSet<Integer> set : arrayOfSets) if (set.size() > 0) nodes++;
        return nodes;
    }

    /**
     * Converts an array of HashSets representing interactions into
     *   UDWInteractionGraph components
     *
     * @param arrayOfSets an array of non-null HashSets that represents the
     *                    interaction graph of all users where each array index is a node
     *                    and the HashSet contained holds the edges to the connecting
     *                    nodes
     * @return a collapsed array of HashSets where each non-empty HashSet is a
     *   unique graph component. Contains a minimum of 1 non-empty HashSet signifying
     *   a graph where every user is connected with a single component, and a max
     *   of n non-empty HashSets where every user interacted only with themselves.
     *   Empty HashSets are insignificant.
     */
    private HashSet<Integer>[] groupSets(HashSet<Integer>[] arrayOfSets) {
        HashSet<Integer>[] groupedSets;
        groupedSets = arrayOfSets.clone(); // to prevent mutation

        for (int i = 0; i < groupedSets.length; i++) { // sort through each set
            if (groupedSets[i].size() > 0) {
                HashSet<Integer> temp = new HashSet<>(groupedSets[i]);

                // check each person interacted with the set
                for (Integer j : temp) {

                    // compare if that person is in another interaction set
                    for (int k = 0; k < groupedSets.length; k++) {
                        if (k != i) {

                            // if person j interacted with someone else
                            // (they appear in another interaction set)
                            if (groupedSets[k].contains(j)) {

                                // add all the elements into the original set
                                groupedSets[i].addAll(groupedSets[k]);
                                // remove all the elements from the secondary set
                                groupedSets[k].removeAll(groupedSets[k]);
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
     *   the IDs of all the people who've interacted with associated person
     * <p>
     * The associated person is the index of the set's position in the array
     *
     * @return an array of sets where each index i of the array holds a set of
     *   people that person i has interacted with
     */
    private HashSet<Integer>[] componentSets() {
        HashSet<Integer>[] componentSets = new HashSet[interactions.size()];

        for (int i = 0; i < interactions.size(); i++) { // going through each person
            componentSets[i] = new HashSet<>();

            for (int j = 0; j < interactions.size(); j++) {

                // and if they have an interaction with someone
                if (interactions.get(i)[j] != 0) {

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
        for (HashSet<Integer> graphComponent : arrayOfComponentSets) {
            if (graphComponent.contains(userIndex.get(userID1)) && graphComponent.contains(userIndex.get(userID2))) {
                System.out.println("true");
                return true;
            }
        }
        System.out.println("false");
        return false;
    }
}
