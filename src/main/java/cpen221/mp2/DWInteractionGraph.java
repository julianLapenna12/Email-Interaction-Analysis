package cpen221.mp2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DWInteractionGraph {

    private List<String> emails = new ArrayList<String>();          //Stores all raw email data
    private List<int []> emailData = new ArrayList<int[]>();        //Stores source id and destination id at corresponding indicies for each email
    private List<int []> interactions = new ArrayList<int []>();    //Stores number of interactions between users - number at each index is interactions between user (index of list) and user at index
    private Set<Integer> sendIds = new HashSet<Integer>();          //Stores all ids of people who sent emails
    private Set<Integer> destIds = new HashSet<Integer>();          //Stores all ids of people who received emails
    private Set<Integer> ids = new HashSet<Integer>();
    private Map<Integer, Integer> userIndex = new HashMap<Integer, Integer>();

    //Joels Experimental New Implementation, creates a map, mapping each ID to a history of interactions, which is a map of the receiver IDs to the time sent
    private Map<Integer, Map<Integer, List<Integer>>> emailGraph;
    private TreeSet<int[]> receiverMetric = new TreeSet<>(Comparator.comparingInt(o -> o[1]));
    private TreeSet<int[]> senderMetric = new TreeSet<>(Comparator.comparingInt(o -> o[1]));


    private final int SENDER = 0;
    private final int RECEIVER = 1;
    private final int TIME = 2;
    private final int LOWER_TIME = 0;
    private final int UPPER_TIME = 1;

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
        emailGraph = categorizeEmails(emailData);

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
        List<int[]> newEmailData = new ArrayList<>();
        List<int[]> otherEmailData = inputDWIG.getEmailData();
        int lowBound = timeFilter[LOWER_TIME];
        int highBound = timeFilter[UPPER_TIME];
        for(int[] email : otherEmailData){
            if(lowBound <= email[TIME] && email[TIME] <= highBound){
                newEmailData.add(email);
            }
        }
        emailGraph = categorizeEmails(newEmailData);
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
        List<int[]> otherEmailData = inputDWIG.getEmailData();
        otherEmailData.stream().filter(
                e -> userFilter.contains(e[SENDER]) || userFilter.contains(e[RECEIVER])
        );
        emailGraph = categorizeEmails(otherEmailData);
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

    /**
     * Turns email data from array into interactions data in array
     *
     * @param emailData
     */
    private Map<Integer, Map<Integer, List<Integer>>> categorizeEmails(List<int[]> emailData) {
        Map<Integer, Map<Integer, List<Integer>>> graph = new HashMap<>();
        ids.addAll(sendIds);
        ids.addAll(destIds);
        for(int[] email : emailData){
            int sender = email[SENDER];
            int receiver = email[RECEIVER];
            int time = email[TIME];
            if(graph.containsKey(sender)){
                if(graph.get(sender).containsKey(receiver)){
                    graph.get(sender).get(receiver).add(time);
                }
                else {
                    ArrayList<Integer> timeList = new ArrayList<>();
                    timeList.add(time);
                    graph.get(sender).put(receiver,timeList);
                }
            }
            else{
                ArrayList<Integer> timeList = new ArrayList<>();
                HashMap<Integer, List<Integer>> receiverData = new HashMap<>();
                timeList.add(time);
                receiverData.put(receiver, timeList);
                graph.put(sender, receiverData);
            }

        }
        return graph;
    }

    /**
     * Turns email string info into array info
     *
     * @param emails
     */
    private void getData(List<String> emails) {
        int sendId, destId, timeId;
        for(String email : emails) {
            String[] srcDstTimeStr  = email.split(" ");
            sendId = Integer.parseInt(srcDstTimeStr[SENDER]);
            destId = Integer.parseInt(srcDstTimeStr[RECEIVER]);
            timeId = Integer.parseInt(srcDstTimeStr[TIME]);
            sendIds.add(sendId);
            destIds.add(destId);
            emailData.add(new int[]{sendId, destId, timeId});
        }
    }

    /**
     * A protected Getter method for the Email Data
     * @return a List of ints containing sender ID, receiver ID, and the time the email was sent.
     */
    List<int[]> getEmailData(){
        return  new ArrayList<>(emailData);
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
        int lowBound = timeWindow[0];
        int highBound = timeWindow[1];
        int[] timeMetrics = new int[3];
        Set<Integer> senderIds = new HashSet<>();
        Set<Integer> receiverIds = new HashSet<>();
        for(int[] email : emailData) {
            if (email[TIME] >= lowBound && email[2] <= highBound) {
                timeMetrics[2]++;
                senderIds.add(email[SENDER]);
                receiverIds.add(email[RECEIVER]);
            }
            else if(email[2] > highBound) break;
        }
        timeMetrics[0] = senderIds.size();
        timeMetrics[1] = receiverIds.size();
        return timeMetrics;
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
        Map<Integer, List<Integer>> history = emailGraph.get(userID);
        int[] userMetric = new int[3];
        AtomicInteger numSent = new AtomicInteger();
        history.forEach((user, times) -> numSent.addAndGet(times.size()));
        userMetric[2] = history.size();
        userMetric[0] = numSent.get();
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
