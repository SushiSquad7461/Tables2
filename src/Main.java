package src;

import edu.wpi.first.networktables.NetworkTableInstance;

public class Main {
    public static void main(String[] args) {
        //creating and showing GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                NetworkTableInstance inst = NetworkTableInstance.getDefault();
                inst.startClient4("systems-check");
                inst.setServerTeam(7461);
                inst.setServer("systems-check", NetworkTableInstance.kDefaultPort4);
                inst.startDSClient();
                TableTest.createAndShowGUI();

                // while (true) {
                //     try {
                //         Thread.sleep(1000);
                //         //receive error messages to display
                //     } catch (InterruptedException ex) {
                //         System.out.println("interrupted");
                //         return;
                //     }
                // }
            }
        });
    }
}
