package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UDWInteractionGraph {

    private List<String> StringEmails = new ArrayList<String>();          //Stores all raw email data
    private List<int []> ArrayEmailData = new ArrayList<int[]>();        //Stores source id and destination id at corresponding indicies for each email
    private List<int []> interactions = new ArrayList<int []>();    //Stores number of interactions between users - number at each index is interactions between user (index of list) and user at index
    private Set<Integer> sendIds = new HashSet<Integer>();          //Stores all ids of people who sent emails
    private Set<Integer> destIds = new HashSet<Integer>();          //Stores all ids of people who received emails
    private Set<Integer> ids = new HashSet<Integer>();
    private Map<Integer, Integer> userIndex = new HashMap<Integer, Integer>();

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
        // TODO: Implement this constructor

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
                newEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                sendIds.add(inputUDWIG.ArrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.ArrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);    }

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
                newEmailData.add(inputUDWIG.ArrayEmailData.get(i));
                sendIds.add(inputUDWIG.ArrayEmailData.get(i)[SENDER]);
                destIds.add(inputUDWIG.ArrayEmailData.get(i)[RECEIVER]);
            }
        }

        MapEmailData(newEmailData);
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

            sendIds.add(inputDWIG.getEmailData().get(i)[SENDER]);
            destIds.add(inputDWIG.getEmailData().get(i)[RECEIVER]);

        }

        MapEmailData(newEmailData);
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
                if ((int) ids.toArray()[i] == emailData.get(j)[SENDER]) {
                    dataForUser[userIndex.get(emailData.get(j)[RECEIVER])]++;
                }

                if ((int) ids.toArray()[i] == emailData.get(j)[RECEIVER]) {
                    dataForUser[userIndex.get(emailData.get(j)[SENDER])]++;
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
        // TODO: Implement this method
        return null;
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
        // TODO: Implement this method
        return null;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @return the User ID for the Nth most active user
     */
    public int NthMostActiveUser(int N) {
        // TODO: Implement this method
        return -1;
    }

    /* ------- Task 3 ------- */

    /**
     * @return the number of completely disjoint graph
     *    components in the UDWInteractionGraph object.
     */
    public int NumberOfComponents() {
        // TODO: Implement this method
        return 0;
    }

    /**
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return whether a path exists between the two users
     */
    public boolean PathExists(int userID1, int userID2) {
        // TODO: Implement this method
        return false;
    }

}
