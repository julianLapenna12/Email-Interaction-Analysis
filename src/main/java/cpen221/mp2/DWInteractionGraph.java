package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DWInteractionGraph {

    private List<String> emails = new ArrayList<String>();          //Stores all raw email data
    private List<int []> emailData = new ArrayList<int[]>();        //Stores source id and destination id at corresponding indecies for each email
    private List<int []> interactions = new ArrayList<int []>();    //Stores number of interactions between users - number at each index is interactions between user (index of list) and user at index
    private Set<Integer> sendIds = new HashSet<Integer>();          //Stores all ids of people who sent emails
    private Set<Integer> destIds = new HashSet<Integer>();          //Stores all ids of people who recieved emails
    private Set<Integer> ids = new HashSet<Integer>();
    private Map<Integer, Integer> userIndex = new HashMap<Integer, Integer>();

    //NOTE- Format of data in text files is SourceID DestinationID TimestampFrom0

    /* ------- Task 1 ------- */
    /* Building the Constructors */

    /**
     * Creates a new DWInteractionGraph using an email interaction file.
     * The email interaction file will be in the resources directory.
     *
     * @param fileName the name of the file in the resources
     *                 directory containing email interactions
     */
    public DWInteractionGraph(String fileName) {
        // TODO: Implement this constructor


        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                emails.add(fileLine);
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        getData(emails);
        categorizeEmails(emailData);

    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG a DWInteractionGraph object
     * @param timeFilter an integer array of length 2: [t0, t1]
     *                   where t0 <= t1. The created DWInteractionGraph
     *                   should only include those emails in the input
     *                   DWInteractionGraph with send time t in the
     *                   t0 <= t <= t1 range.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, int[] timeFilter) {
        List<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputDWIG.emailData.size(); i++) {
            if (inputDWIG.emailData.get(i)[2]> timeFilter[0] && inputDWIG.emailData.get(i)[2] < timeFilter[1]) {
                newEmailData.add(inputDWIG.emailData.get(i));
            }
        }

        categorizeEmails(newEmailData);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        List<int[]> newEmailData = new ArrayList<int[]>();
        for (int i = 0; i < inputDWIG.emailData.size(); i++) {
            if (!(userFilter.contains(inputDWIG.emailData.get(i)[0]) || userFilter.contains(inputDWIG.emailData.get(i)[0]))) {
                newEmailData.add(inputDWIG.emailData.get(i));
            }
        }

        categorizeEmails(newEmailData);
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        return ids;
    }

    /**
     * @param sender the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        return interactions.get(userIndex.get(sender))[userIndex.get(receiver)];
    }


    private void categorizeEmails(List<int[]> emailData) {

        ids.addAll(sendIds);
        ids.addAll(destIds);

        for (int i = 0; i < ids.size(); i++) {
            userIndex.put((int)ids.toArray()[i], i);
        }

        for(int i = 0; i < ids.size(); i++) {
            int [] dataForUser = new int[ids.size()];
            for (int j = 0; j < emailData.size(); j++) {
                if ((int)ids.toArray()[i]==emailData.get(j)[0]) {
                    dataForUser[userIndex.get(emailData.get(j)[1])]++;
                }
            }
            interactions.add(dataForUser);
        }
    }

    private void getData(List<String> emails) {
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

            sendIds.add(sendId);
            destIds.add(destId);

            srcDstTime[0] = sendId;
            srcDstTime[1] = destId;
            srcDstTime[2] = timeId;
            emailData.add(srcDstTime);
            counter = 0;
            cont = true;
        }
    }

    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     * @param timeWindow is an int array of size 2 [t0, t1] where t0<=t1.
     * @return an int array of length 3, with the following structure:
     * [NumberOfSenders, NumberOfReceivers, NumberOfEmailTransactions]
     */
    public int[] ReportActivityInTimeWindow(int[] timeWindow) {
        // TODO: Implement this method
        return null;
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {
        // TODO: Implement this method
        return null;
    }

    /**
     * @param N a positive number representing rank. N=1 means the most active.
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order.
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        // TODO: Implement this method
        return -1;
    }

    /* ------- Task 3 ------- */

    /**
     * performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {
        // TODO: Implement this method
        return null;
    }

    /**
     * performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        // TODO: Implement this method
        return null;
    }

    /* ------- Task 4 ------- */

    /**
     * Read the MP README file carefully to understand
     * what is required from this method.
     * @param hours
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {
        // TODO: Implement this method
        return 0;
    }

}
