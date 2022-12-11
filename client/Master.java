package client;

import server.ServerCommInterface;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Master {

    public static void main(String[] args) throws Exception {

        String teamName = args[2];

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

            if (!mch.isBusy()) {
                mch.sendProblem(cch.currProblem, cch.currProblemSize, sci, cch);
            } else if(cch.currProblem == null) {
                mch.interrupt();
            }
        }
    }

    /* protected Master() throws RemoteException {
    }

    @Override
    public void resultFound(int password) throws Exception {

    }

    public static void main(String[] args) throws Exception {

        // Register the master on the server

        String teamName = args[2];
        int numberOfSlaves = Integer.parseInt(args[3]);
        System.setProperty("java.rmi.server.hostname", args[1]);
        ServerCommInterface sci = (ServerCommInterface) Naming.lookup("rmi://" + args[0] + "/server");
        ClientCommHandler cch = new ClientCommHandler();
        sci.register(teamName, cch);

        HashMap<String, Slave> slaves = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < numberOfSlaves; i++) {
            System.out.println("Enter slave ip");
            String slaveIp = scanner.nextLine();
            Slave slave = new Slave(teamName, slaveIp);
            slaves.put(slaveIp + i, slave);
        }

        ArrayList<Thread> threads = new ArrayList<>();

        while(true) {

            if (cch.currProblem==null) {
                Thread.sleep(1);
                threads.forEach(Thread::interrupt);
                threads.removeAll(threads);
                slaves.forEach((s, slave) -> {
                    slave.setSolving(false);
                });
            } else {
                if (slaves.values().stream().noneMatch(Slave::isSolving)) {
                    for (int i = 0; i < numberOfSlaves; i++) {
                        Slave slave = slaves.values().toArray(new Slave[0])[i];
                        int start = i * (cch.currProblemSize / numberOfSlaves);
                        int end = (i + 1) * (cch.currProblemSize / numberOfSlaves);
                        System.out.println("Start: " + start + " End: " + end + " id: " + i);
                        Thread thread = new Thread(() -> {
                            try {
                                slave.solveProblem(start, end, sci, cch);
                            } catch (Exception ignored) {}
                        });
                        threads.add(thread);
                        thread.start();
                    }
                }
            }


        }
    }*/
}
