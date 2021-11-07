package cpen221.mp2;

import com.sun.source.tree.Tree;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.HashMap;
import java.util.Comparator;
import java.util.HashSet;

/**
 * This class represents a graph holding all email interactions between users where each node is
 * a user who sent or received an email and each edge has a direction pointing from the sender to
 * the receiver of an email sent between the two users with the weight of the edge representing
 * how many emails were sent between the two users.
 *
 * Abstraction Invariant:
 * Each node must be distinct and have at least one common edge with any node (including itself)
 */
public class DWInteractionGraph {
    /*Stores all raw email data*/
    private List<String> emails = new ArrayList<String>();
    /*Stores sender id, receiver id, and time sent at indices 0, 1, and 2 respectively for an
      integer array representing an email*/
    private List<int[]> emailData;
    /*Stores all ids of users who have send or recieved an email*/
    private Set<Integer> ids;
    /*Stores number of emails sent from one ID to another ID*/
    private Map<Integer, TreeMap<Integer, List<Integer>>> emailGraph;
    /*?UNSURE?*/
    private TreeSet<EmailUser> senderMetric = new TreeSet<>((u1, u2) -> {
        if (u1.getNumSends() == u2.getNumSends()) {
            return u2.getId() - u1.getId();
        } else return u1.getNumSends() - u2.getNumSends();
    });
    /*?UNSURE?*/
    private TreeSet<EmailUser> receiverMetric = new TreeSet<>((u1, u2) -> {
        if (u1.getNumReceives() == u2.getNumReceives()) {
            return u2.getId() - u1.getId();
        } else return u1.getNumReceives() - u2.getNumReceives();
    });

    /*Where send ID is stored in any integer array representing an email*/
    private final int SENDER = 0;
    /*Where receiver ID is stored in any integer array representing an email*/
    private final int RECEIVER = 1;
    /*Where reference time is stored in any integer array representing an email*/
    private final int TIME = 2;
    /*Where lower bound on time filter is stored within time filter array*/
    private final int LOWER_TIME = 0;
    /*Where upper bound on time filter is stored within time filter array*/
    private final int UPPER_TIME = 1;

    // Representation Invariant
    // For all list elements of emailData, each integer array size is 3
    // For all list elements of emails, no element is null
    // All ids in emailData must also be contained in ids


    // Abstraction Function
    // For any graph G
    //


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

        try {
            BufferedReader reader = new BufferedReader(new FileReader(fileName));
            for (String fileLine = reader.readLine(); fileLine != null; fileLine = reader.readLine()) {
                emails.add(fileLine);
            }
            reader.close();
        } catch (IOException ioe) {
            System.out.println("Problem reading file!");
        }

        emailData = getData(emails);
        ids = createIDSet(emailData);
        emailGraph = categorizeEmails(emailData);
        createMetrics(emailData);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a time window filter.
     *
     * @param inputDWIG  a DWInteractionGraph object
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
        for (int[] email : otherEmailData) {
            if (lowBound <= email[TIME] && email[TIME] <= highBound) {
                newEmailData.add(email);
            }
        }

        emailData = newEmailData;
        ids = createIDSet(emailData);
        emailGraph = categorizeEmails(newEmailData);
        createMetrics(emailData);
    }

    /**
     * Creates a new DWInteractionGraph from a DWInteractionGraph object
     * and considering a list of User IDs.
     *
     * @param inputDWIG  a DWInteractionGraph object
     * @param userFilter a List of User IDs. The created DWInteractionGraph
     *                   should exclude those emails in the input
     *                   DWInteractionGraph for which neither the sender
     *                   nor the receiver exist in userFilter.
     */
    public DWInteractionGraph(DWInteractionGraph inputDWIG, List<Integer> userFilter) {
        List<int[]> newEmailData = new ArrayList<>();
        List<int[]> otherEmailData = inputDWIG.getEmailData();

        for (int[] email : otherEmailData) {
            if ((userFilter.contains(email[SENDER]) || userFilter.contains(email[RECEIVER]))) {
                newEmailData.add(email);
            }
        }

        emailData = newEmailData;
        ids = createIDSet(emailData);
        emailGraph = categorizeEmails(otherEmailData);
        createMetrics(emailData);
    }

    /**
     * @return a Set of Integers, where every element in the set is a User ID
     * in this DWInteractionGraph.
     */
    public Set<Integer> getUserIDs() {
        return ids;
    }

    /**
     * @param sender   the User ID of the sender in the email transaction.
     * @param receiver the User ID of the receiver in the email transaction.
     * @return the number of emails sent from the specified sender to the specified
     * receiver in this DWInteractionGraph.
     */
    public int getEmailCount(int sender, int receiver) {
        if (!emailGraph.containsKey(sender) || !emailGraph.get(sender).containsKey(receiver)) {
            return 0;
        }
        return emailGraph.get(sender).get(receiver).size();
    }

    /**
     * Turns email data from array into interactions data in array
     *
     * @param emailData
     */
    private Map<Integer, TreeMap<Integer,
                List<Integer>>> categorizeEmails(List<int[]> emailData) {
        Map<Integer, TreeMap<Integer, List<Integer>>> graph = new HashMap<>();
        for (int[] email : emailData) {
            graph = addEmail(email, graph);
        }
        return graph;
    }


    /**
     * Adds information from a single Email interaction to the interaction Graph
     *
     * @param email an int array representing a single email, where the first element is sender
     *              ID, second is receiver, and third is the time.
     * @param graph a Map mapping each sender to a map of Receivers and time list of time stamps
     * @return the edited copy of the graph
     */
    private Map<Integer, TreeMap<Integer,
                List<Integer>>> addEmail(int[] email, Map<Integer,
                                         TreeMap<Integer, List<Integer>>> graph) {
        int sender = email[SENDER];
        int receiver = email[RECEIVER];
        int time = email[TIME];

        if (graph.containsKey(sender)) {
            if (graph.get(sender).containsKey(receiver)) {
                graph.get(sender).get(receiver).add(time);
            } else {
                ArrayList<Integer> timeList = new ArrayList<>();

                timeList.add(time);
                graph.get(sender).put(receiver, timeList);
            }
        } else {
            ArrayList<Integer> timeList = new ArrayList<>();
            TreeMap<Integer, List<Integer>> receiverData =
                    new TreeMap<Integer, List<Integer>>(Comparator.comparingInt(u -> -u));

            timeList.add(time);
            receiverData.put(receiver, timeList);
            graph.put(sender, receiverData);
        }
        return graph;
    }

    /**
     * Turns email string info into array info
     *
     * @param emails is a list of strings representing the raw data of the email information,
     *               where each line is a string containing 3 integer values separated by spaces,
     *               representing sender ID, receiver ID, and the Time the email was sent.
     * @return an arrayList of int triples, where each int array represents the sender ID
     *         receiver ID, and time sent of each email.
     * Effects: adds all the IDS of senders and receivers to sets of user IDs.
     */
    private ArrayList<int[]> getData(List<String> emails) {
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

            data.add(new int[]{sendId, destId, timeId});
        }
        return data;
    }

    private Set<Integer> createIDSet(List<int[]> emails) {
        HashSet<Integer> allIDs = new HashSet<>();
        for (int[] email : emails) {
            allIDs.add(email[SENDER]);
            allIDs.add(email[RECEIVER]);
        }
        return allIDs;
    }

    /**
     * Takes the list of all emails sents and uses it to create orderings of all users based on
     * number of emails sent and received.
     *
     * @param emails a 3 valued integer list of all email data, containing sender and receiver
     *               ids and the time sent
     */
    private void createMetrics(List<int[]> emails) {
        for (int[] email : emails) {
            if (receiverMetric.contains(new EmailUser(email[RECEIVER]))) {
                receiverMetric.stream().forEach(u -> {
                    if (u.getId() == email[RECEIVER]) {
                        u.receiveEmail(email[SENDER]);
                    }
                });
            } else receiverMetric.add(new EmailUser(email[RECEIVER], SendOrReceive.RECEIVE,
                                      email[SENDER]));

            if (senderMetric.contains(new EmailUser(email[SENDER]))) {
                senderMetric.stream().forEach(u -> {
                    if (u.getId() == email[SENDER]) {
                        u.sendEmail(email[RECEIVER]);
                    }
                });
            } else senderMetric.add(new EmailUser(email[SENDER], SendOrReceive.SEND,
                                                  email[RECEIVER]));
        }
    }

    /**
     * A protected Getter method for the Email Data
     *
     * @return a List of ints containing sender ID, receiver ID, and the time the email was sent.
     */
    ArrayList<int[]> getEmailData() {
        return new ArrayList<>(emailData);
    }

    /* ------- Task 2 ------- */

    /**
     * Given an int array, [t0, t1], reports email transaction details.
     * Suppose an email in this graph is sent at time t, then all emails
     * sent where t0 <= t <= t1 are included in this report.
     *
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
        for (int[] email : emailData) {
            if (email[TIME] >= lowBound && email[2] <= highBound) {
                timeMetrics[2]++;
                senderIds.add(email[SENDER]);
                receiverIds.add(email[RECEIVER]);
            } else if (email[2] > highBound) break;
        }
        timeMetrics[0] = senderIds.size();
        timeMetrics[1] = receiverIds.size();
        return timeMetrics;
    }

    /**
     * Given a User ID, reports the specified User's email transaction history.
     *
     * @param userID the User ID of the user for which the report will be
     *               created.
     * @return an int array of length 3 with the following structure:
     * [NumberOfEmailsSent, NumberOfEmailsReceived, UniqueUsersInteractedWith]
     * If the specified User ID does not exist in this instance of a graph,
     * returns [0, 0, 0].
     */
    public int[] ReportOnUser(int userID) {
        Set<Integer> interactions = new HashSet<>();
        int[] userMetric = new int[]{0, 0, 0};

        if (!ids.contains(userID)) {
            return new int[]{0, 0, 0};
        }
        interactions.addAll(getRecipientSet(userID));
        interactions.addAll(getSenderSet(userID));

        userMetric[0] = getNumEmailSent(userID);

        userMetric[1] = getNumEmailReceived(userID);

        userMetric[2] = interactions.size();
        return userMetric;
    }

    /**
     * Given a user ID reports on the number of emails they sent
     *
     * @param userID The user id for which the report will be created.
     * @return an integer number of emails sent, if UserID did not send any emails then returns 0
     */
    private int getNumEmailSent(int userID) {
        if (!emailGraph.containsKey(userID)) {
            return 0;
        }
        Map<Integer, List<Integer>> history = emailGraph.get(userID);
        int numSent = 0;
        for (int recipient : history.keySet()) {
            numSent += history.get(recipient).size();
        }
        return numSent;
    }

    /**
     * Given a user ID reports on the number of emails they received.
     *
     * @param userID The user id for which the report will be created.
     * @return an integer number of emails received, if UserID did not receive any emails then
     *         returns 0
     */
    private int getNumEmailReceived(int userID) {
        Map<Integer, TreeMap<Integer, List<Integer>>> receiverGraph =
                                            createReceiverGraph(emailGraph, userID);
        if (receiverGraph.isEmpty()) {
            return 0;
        }
        int numReceived = 0;
        for (int sender : receiverGraph.keySet()) {
            numReceived += receiverGraph.get(sender).get(userID).size();
        }
        return numReceived;
    }

    /**
     * Given a user id reports all the users who received emails from them
     *
     * @param userID The user id for which the report will be created.
     * @return a set of user Id's corresponding to all the users who received emails from the
     *         specified user,
     * returns an empty set if the user sent no emails.
     */
    private Set<Integer> getRecipientSet(int userID) {
        Set<Integer> recipients = new HashSet<>();
        Map<Integer, List<Integer>> history = emailGraph.get(userID);
        if (!emailGraph.containsKey(userID)) {
            return recipients;
        }
        for (int recipient : history.keySet()) {
            recipients.add(recipient);
        }
        return recipients;
    }

    /**
     * Given a user id reports all users who sent emails to them.
     *
     * @param userID The user id for which the report will be created.
     * @return a set of user Id's corresponding to all the users who sent emails to the
     *         specified user,
     * returns an empty set if the user received no emails.
     */
    private Set<Integer> getSenderSet(int userID) {
        Set<Integer> senders = new HashSet<>();
        Map<Integer, TreeMap<Integer, List<Integer>>> receiverGraph =
                                            createReceiverGraph(emailGraph, userID);
        if (receiverGraph.isEmpty()) {
            return senders;
        }
        for (int sender : receiverGraph.keySet()) {
            senders.add(sender);
        }
        return senders;

    }

    /**
     * Creates a sub-graph of the total email graph, representing all the emails sent to a
     * specified ID
     *
     * @param graph the total email graph from which the sub-graph will be constructed
     * @param id    is the id of the recipient from whose senders and emails the graph will be
     *              made
     * @return a directed graph representing all emails sent to a user.
     */
    private Map<Integer, TreeMap<Integer, List<Integer>>>createReceiverGraph(Map<Integer,
                                                                            TreeMap<Integer,
                                                                            List<Integer>>> graph,
                                                                            int id) {
        Map<Integer, TreeMap<Integer, List<Integer>>> receiverGraph = new HashMap<>();
        for (int sender : graph.keySet()) {
            if (graph.get(sender).containsKey(id)) {
                TreeMap<Integer, List<Integer>> received = new TreeMap<>();
                received.put(id, new ArrayList<>(graph.get(sender).get(id)));
                receiverGraph.put(sender, received);
            }
        }
        return receiverGraph;
    }

    /**
     * @param N               a positive number representing rank. N=1 means the most active.
     *                        Requires n is a natural number
     * @param interactionType Represent the type of interaction to calculate the rank for
     *                        Can be SendOrReceive.Send or SendOrReceive.RECEIVE
     * @return the User ID for the Nth most active user in specified interaction type.
     * Sorts User IDs by their number of sent or received emails first. In the case of a
     * tie, secondarily sorts the tied User IDs in ascending order.
     * Returns -1 if N is greater than the number of users
     */
    public int NthMostActiveUser(int N, SendOrReceive interactionType) {
        TreeSet<EmailUser> ordering;
        EmailUser user;
        if (interactionType.equals(SendOrReceive.SEND)) {
            ordering = senderMetric;
        } else ordering = receiverMetric;
        if (ordering.size() < N) {
            return -1;
        }
        user = ordering.last();
        for (int i = 1; i < N; i++) {
            user = ordering.lower(user);
        }
        return user.getId();
    }

    /* ------- Task 3 ------- */

    /**
     * performs breadth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> BFS(int userID1, int userID2) {
        ArrayList<Integer> queue = new ArrayList<>();
        ArrayList<Integer> searched = new ArrayList<>();
        int user;
        queue.add(userID1);
        for (int i = 0; i < queue.size(); i++) {
            user = queue.get(i);
            if (user == userID2) {
                searched.add(user);
                break;
            } else if (!searched.contains(user)) {
                searched.add(user);
                if (emailGraph.containsKey(user)) {
                    queue.addAll(emailGraph.get(user).descendingKeySet());
                }
            }
        }
        return searched;
    }

    /**
     * performs depth first search on the DWInteractionGraph object
     * to check path between user with userID1 and user with userID2.
     *
     * @param userID1 the user ID for the first user
     * @param userID2 the user ID for the second user
     * @return if a path exists, returns aa list of user IDs
     * in the order encountered in the search.
     * if no path exists, should return null.
     */
    public List<Integer> DFS(int userID1, int userID2) {
        ArrayList<Integer> nodesVisited = new ArrayList<>();
        if (recursiveSearch(userID1, userID2, nodesVisited)) {
            nodesVisited.add(userID2);
            return nodesVisited;
        }
        return null;
    }

    /**
     * Recursive Function which searchs through the User Email graph using depth first search
     *
     * @param userID1      the user id from which the search is starting, is a valid user ID
     *                     contained in the graph with non-null value
     * @param userID2      the user id for which the search is finding the path to, must be a
     *                     non-null integer which is contained in the graph.
     * @param nodesVisited a list of valid user ids in the order which the function has visited
     *                     the nodes, contains only valid integers corresponding to user IDs.
     * @return a boolean determining whether or not the search has found user 2.
     */
    private boolean recursiveSearch(int userID1, int userID2, List<Integer> nodesVisited) {
        TreeMap<Integer, List<Integer>> searchMap = emailGraph.get(userID1);
        nodesVisited.add(userID1);
        if (searchMap != null) {
            for (int user2 : searchMap.descendingKeySet()) {
                if (!nodesVisited.contains(user2)) {
                    if (user2 == userID2 || recursiveSearch(user2, userID2, nodesVisited)) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }


    /* ------- Task 4 ------- */

    /**
     * Read the MP README file carefully to understand
     * what is required from this method.
     *
     * @param hours
     * @return the maximum number of users that can be polluted in N hours
     */
    public int MaxBreachedUserCount(int hours) {
        int max = 0;
        for (int user : emailGraph.keySet()) {
            for (int receiver : emailGraph.get(user).keySet()) {
                for (int time : emailGraph.get(user).get(receiver)) {
                    Set<Integer> pollutedUsers = new HashSet<>();
                    pollutedUsers = findPolluted(user, time, time + 60 * 60 * hours,
                                                 pollutedUsers);
                    if (pollutedUsers.size() > max) {
                        max = pollutedUsers.size();
                    }

                }
            }
        }
        return max;
    }

    private Set<Integer> findPolluted(int infectedUser, int startTime, int endTime,
                                      Set<Integer> polluted) {
        TreeMap<Integer, List<Integer>> searchMap = emailGraph.get(infectedUser);
        if (startTime <= endTime && !polluted.contains(infectedUser)) {
            polluted.add(infectedUser);
            if (searchMap != null) {
                for (int userSpread : searchMap.descendingKeySet()) {
                    List<Integer> emailTimes = searchMap.get(userSpread);
                    for (int email : emailTimes) {
                        if (email >= startTime) {
                            polluted.addAll(findPolluted(userSpread, email, endTime, polluted));
                        }
                    }
                }

            }
        }
        return polluted;
    }

}
