package client;

import server.ServerCommInterface;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class Master {

    public static void main(String[] args) throws Exception {
        String teamName = args[2];
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        // Create the connection to the server
        System.setProperty("java.rmi.server.hostname", args[1]);
        ServerCommInterface sci = (ServerCommInterface) Naming.lookup("rmi://" + args[0] + "/server");
        ClientCommHandler cch = new ClientCommHandler();
        sci.register(teamName, cch);

        // Create a new Master object to be accessible for the slaves
        System.setProperty("java.rmi.master.hostname", args[1]);
        MasterCommHandler mch = new MasterCommHandler();
        LocateRegistry.createRegistry(5000);
        Naming.rebind("rmi://" + args[1] + ":5000/master", mch);

        while (true) {
            while (cch.currProblem == null) {
                Thread.sleep(1);
                mch.interrupt();
            }

            if (!mch.bothSlavesBusy()) {
                mch.sendProblem(sci, cch);
                Thread.sleep(2);
            } else if(cch.currProblem == null) {
                mch.interrupt();
                cch.currProblem = null;
            }
        }
    }
}
