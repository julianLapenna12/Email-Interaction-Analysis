package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UDWInteractionGraph {

    private List<String> stringEmails = new ArrayList<String>();                 //Stores all raw email data
    private List<int[]> arrayEmailData = new ArrayList<int[]>();           //Stores source id and destination id at corresponding indicies for each email
    private Map<Integer, Integer> userInteractionCounts = new HashMap<>();  //Stores users mapped to their interaction counts
    private ArrayList<Integer> orderUsers = new ArrayList<>();
    private List<int[]> interactions = new ArrayList<int[]>();            //Stores number of interactions between users - number at each index is interactions between user (index of list) and user at index
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
                stringEmails.add(fileLine);
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        stringDataToArray(stringEmails);
        MapEmailData(arrayEmailData);
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
        for (int i = 0; i < inputUDWIG.arrayEmailData.size(); i++) {
            if (inputUDWIG.arrayEmailData.get(i)[TIME] >= timeFilter[LOWER_TIME] && inputUDWIG.arrayEmailData.get(i)[TIME] <= timeFilter[UPPER_TIME]) {
                arrayEmailData.add(inputUDWIG.arrayEmailData.get(i));
                newEmailData.add(inputUDWIG.arrayEmailData.get(i));
                sendIds.add(inputUDWIG.arrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.arrayEmailData.get(i)[RECEIVER]);
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
        for (int i = 0; i < inputUDWIG.arrayEmailData.size(); i++) {
            if (userFilter.contains(inputUDWIG.arrayEmailData.get(i)[SENDER]) || userFilter.contains(inputUDWIG.arrayEmailData.get(i)[RECEIVER])) {
                arrayEmailData.add(inputUDWIG.arrayEmailData.get(i));
                newEmailData.add(inputUDWIG.arrayEmailData.get(i));
                sendIds.add(inputUDWIG.arrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.arrayEmailData.get(i)[RECEIVER]);
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
            arrayEmailData = inputDWIG.getEmailData();
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
     * in this UDWInteractionGraph.
     */
    public Set<Integer> getUserIDs() { return ids; }

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
     */
    private void stringDataToArray(List<String> emails) {
        int sendId = 0;
        int destId = 0;
        int timeId = 0;
        int counter = 0;
        boolean cont = true;

        for (String email : emails) {
            int[] srcDstTime = new int[3];

            // getting the sender
            while (cont) {
                if (email.charAt(counter) == ' ') {
                    sendId = Integer.parseInt(email.substring(0, counter));
                    cont = false;
                }
                counter++;
            }
            cont = true;
            int start = counter;

            // getting the receiver
            while (cont) {
                if (email.charAt(counter) == ' ') {
                    destId = Integer.parseInt(email.substring(start, counter));
                    cont = false;
                }
                counter++;
            }
            cont = true;
            start = counter;

            // getting the time signature
            while (cont) {
                if (counter == email.length()) {
                    timeId = Integer.parseInt(email.substring(start, counter));
                    cont = false;
                }
                counter++;
            }

            sendIds.add(sendId);
            destIds.add(destId);

            srcDstTime[SENDER] = sendId;
            srcDstTime[RECEIVER] = destId;
            srcDstTime[TIME] = timeId;
            arrayEmailData.add(srcDstTime);
            counter = 0;
            cont = true;
        }
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
    private void MapEmailData(List<int[]> emailData) {

        ids.addAll(sendIds);
        ids.addAll(destIds);

        for (int i = 0; i < ids.size(); i++) {
            // mapping the user IDs to their internal indexes
            userIndex.put((int) ids.toArray()[i], i);
        }

        for (int i = 0; i < ids.size(); i++) { // iterating through the users

            // initializing the int[] for a single user's data
            int[] dataForUser = new int[ids.size()];

            // iterating through the other users to find who interacted with who
            for (int j = 0; j < emailData.size(); j++) {

                // for user interactions with themselves
                if (emailData.get(j)[SENDER] == emailData.get(j)[RECEIVER]
                        && (int) ids.toArray()[i] == emailData.get(j)[SENDER]) {
                    dataForUser[userIndex.get(emailData.get(j)[RECEIVER])]++;

                } else { // for interactions with other users

                    if ((int) ids.toArray()[i] == emailData.get(j)[SENDER]) {
                        dataForUser[userIndex.get(emailData.get(j)[RECEIVER])]++;
                    }
                    if ((int) ids.toArray()[i] == emailData.get(j)[RECEIVER]) {
                        dataForUser[userIndex.get(emailData.get(j)[SENDER])]++;
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
     * [NumberOfUsers, NumberOfEmailTransactions]
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
     * [NumberOfEmails, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0].
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
     * a given user has interacted with
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
     * any other user
     */
    private int getUserInteractionCount(int userID) {
        int count;
        if (!userIndex.containsKey(userID)) return 0;

        count = iterateUserInteractions(userIndex.get(userID));
        return count;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user
     */
    public int NthMostActiveUser(int N) {
        int mostActiveUser = 0;
        // N-1 to account for array indexing
        mostActiveUser = orderUsers.get(userIndex.get(N - 1));

        return mostActiveUser;
    }

    /**
     * Maps each user to their interaction count. Helper method
     * that helps when computing the listing of users ranked
     * by activity
     */
    private void mapUsers() {
        for (int i = 0; i < interactions.size(); i++) {
            userInteractionCounts.put(i, iterateUserInteractions(i));
        }
    }

    /**
     * @param user the index of the specific user
     * @return the total number of interactions between the user and
     * any other user
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
     * appearing first and the least active user appearing last.
     * <p>
     * When two users have the same activity, the user with
     * the lower user ID comes first in the listing.
     */
    private void activeUsers() {
        for (int i = 0; i < userInteractionCounts.size(); i++) {
            int maxVal = 0;
            int maxKey = -1;
            for (Map.Entry<Integer, Integer> entry : userInteractionCounts.entrySet()) {
                Integer key = entry.getKey();
                Integer value = entry.getValue();

                // when two users have the same interaction count, the first will be added,
                // then the second time around they will appear in orderUsers
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
     * components in the UDWInteractionGraph object.
     */
    public int NumberOfComponents() {
        return nodes;
    }

    /**
     * Helper method that initializes the component graph, nodes and paths
     * for task 3. Included in a single method to make constructors
     * more concise. Given the immutability, these should never be
     * changed after initialization.
     */
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
     * @param arrayOfSets an array of non-null HashSets containing a sorted graph
     *                    where each HashSet contains a unique graph node or is empty
     * @return the number of unique components
     */
    private int countNodes(HashSet<Integer>[] arrayOfSets) {
        int nodes = 0;
        for (HashSet<Integer> set : arrayOfSets) {
            if (set.size() > 0) nodes++;
        }
        return nodes;
    }

    /**
     * Converts an array of HashSets representing interactions into
     * UDWInteractionGraph components
     *
     * @param arrayOfSets an array of non-null HashSets that represents the
     *                    interaction graph of all users where each array index is a node
     *                    and the HashSet contained holds the edges to the connecting
     *                    nodes
     * @return a collapsed array of HashSets where each non-empty HashSet is a
     * unique graph component. Contains a minimum of 1 non-empty HashSet signifying
     * a graph where every user is connected with a single component, and a max
     * of n non-empty HashSets where every user interacted only with themselves.
     * Empty HashSets are insignificant and ordering of HashSets is insignificant.
     */
    private HashSet<Integer>[] groupSets(HashSet<Integer>[] arrayOfSets) {
        HashSet<Integer>[] groupedSets;
        groupedSets = arrayOfSets.clone(); // to prevent mutation

        for (int i = 0; i < groupedSets.length; i++) { // sort through each set
            if (groupedSets[i].size() > 0) {
                HashSet<Integer> temp = new HashSet<>();
                temp.addAll(groupedSets[i]);

                // check each person interacted with the set
                for (Integer j : temp) {

                    // compare if that person is in another interaction set
                    for (int k = 0; k < groupedSets.length; k++) {
                        if (k != i) {

                            // if person j interacted with someone else (they appear in another interaction set)
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
     * the IDs of all the people who've interacted with associated person
     * <p>
     * The associated person is the index of the set's position in the array
     *
     * @return an array of sets where each index i of the array holds a set of
     * people that person i has interacted with
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
