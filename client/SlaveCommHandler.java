package client;

import server.ServerCommInterface;

import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.HashMap;

public class SlaveCommHandler extends UnicastRemoteObject implements SlaveCommInterface {

    private final HashMap<String, Integer> rainbowTable = new HashMap<>();
    private final String teamName;

    private int maxInt = 0;

    private boolean isBusy;

    protected SlaveCommHandler(String teamName) throws Exception {
        super();
        this.teamName = teamName;
    }

    @Override
    public void publishProblem(ServerCommInterface sci, byte[] problem, int start, int end) throws Exception {
        isBusy = true;
        MessageDigest md = MessageDigest.getInstance("MD5");
        if (rainbowTable.get(Arrays.toString(problem)) != null && problem != null) {
            int i = rainbowTable.get(Arrays.toString(problem));
            sci.submitSolution(teamName, String.valueOf(i));
            isBusy = false;
            return;
        }
        for (int i = (Math.max(maxInt, start)); i < end && isBusy; i++) {
            byte[] hash = md.digest(String.valueOf(i).getBytes());
            rainbowTable.put(Arrays.toString(hash), i);
            if (Arrays.equals(problem, hash)) {
                sci.submitSolution(teamName, String.valueOf(i));
                isBusy = false;
                return;
            }
            maxInt = i;
        }
    }

    @Override
    public void interrupt() throws Exception {
        isBusy = false;
    }

    @Override
    public boolean isBusy() throws Exception {
        return isBusy;
    }

}
