package src;
import java.awt.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

import src.view.Buttons;
import src.view.CustomTableCellRenderer;

import edu.wpi.first.networktables.*;
import src.view.RowInputOutput;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class TableTest extends JPanel 
                        implements ActionListener { 
    
    private JTable table;
    private JButton pushAll;
    private JButton defaultButton;
    private JButton runMotors;
    private JButton stopMotors;

    private static JFrame frame;
    private JTextPane textPane;
    private SimpleAttributeSet keyWord = new SimpleAttributeSet();

    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    static NetworkTable dataTable = inst.getTable("dataTable"); //makes data table
    static NetworkTableEntry dataTableEntry = inst.getEntry("data"); //makes string array
    static NetworkTableEntry runningEntry = inst.getEntry("running?"); //is running? entry


    private String[] defaultInfo = {
        "subsystem0 motor1 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0", "subsystem0 motor2 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0",
        "subsystem0 motor3 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0", "subsystem0 motor4 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0",
        "subsystem0 motor5 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0", "subsystem0 motor6 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0"
    };
    private String[] messages = {"System is working", "Error: not working", "System is back to normal", "Error: not working"};

    public TableTest() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        table = new JTable(new RowInputOutput(defaultInfo));
        table.setPreferredScrollableViewportSize(new Dimension(300, 50));
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table));
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);  
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        int rowCount = table.getModel().getRowCount();
        pushAll = new Buttons("download all updates", rowCount);
        pushAll.addActionListener(this);
        add(pushAll);

        defaultButton = new Buttons("load original defaults", rowCount);
        defaultButton.addActionListener(this);
        add(defaultButton);

        runMotors = new Buttons("run included motors", rowCount);
        runMotors.addActionListener(this);
        add(runMotors);

        stopMotors = new Buttons("stop included motors", rowCount);
        stopMotors.addActionListener(this);
        add(stopMotors);

        textPane = new JTextPane();
        StyleConstants.setForeground(keyWord, Color.RED);
        add(new JScrollPane(textPane));

    }
 
    public void actionPerformed(ActionEvent event) {
        Object buttonName = event.getSource();
        if (buttonName.equals(pushAll)){
            String[] output = new String[table.getRowCount()]; //could get row count for included values
            System.out.println(Arrays.toString(RowInputOutput.sendValues(output, table)));

            dataTableEntry.setDefaultStringArray(null);
            dataTableEntry.setStringArray(RowInputOutput.sendValues(output, table));
        
            printOutput(messages); //subscriber needed to receive error messages 
        } else if (buttonName.equals(defaultButton)){ //use create and show gui to load new window instead, or do this
            TableTest newContentPane = new TableTest();
            newContentPane.setOpaque(true); 
            frame.setContentPane(newContentPane);
            frame.pack();
            frame.setVisible(true);
        } else if (buttonName.equals(runMotors)){
            String[] output = new String[table.getRowCount()];
            System.out.println(Arrays.toString(RowInputOutput.sendIncludedValues(output, table)));

            runningEntry.setDefaultBoolean(false);
            runningEntry.setBoolean(true);

            printOutput(messages);
        } else {
            String[] output = new String[table.getRowCount()];
            System.out.println(Arrays.toString(RowInputOutput.stopMotors(output, table)));

            runningEntry.setDefaultBoolean(false);
            runningEntry.setBoolean(false);
        }
    }

    private void printOutput(String[] messages){ 
        StyledDocument doc = textPane.getStyledDocument();
        int totalCount = 0;
        int count = 0;
        try {
            for (String message : messages) {
                totalCount++;
                if (message.contains("not working")) {
                    doc.insertString(doc.getLength(), message + "\n", keyWord);
                    count++;
                } else {
                    doc.insertString(doc.getLength(), message + "\n", null);
                }
            }
            doc.insertString(doc.getLength(), count + " errors of " + totalCount + "\n", keyWord);
        } catch(BadLocationException e){
            e.printStackTrace();
        }
      
    }

    // Create the GUI and show it. This is my window method
    private static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.

        // ImageIcon icon = new ImageIcon("/Users/nitya/java/tables_copy/assets/images/img1.png");
        // JLabel background = new JLabel(icon);
        // background.setBounds(0, 50, 500, 200);
        // frame.add(background);

        TableTest newContentPane = new TableTest();
        newContentPane.setOpaque(true); //content panes must be opaque.
        frame.add(newContentPane);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //creating and showing GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();

                inst.startClient4("systems-check");
                inst.setServerTeam(7461);
                inst.setServer("systems-check", NetworkTableInstance.kDefaultPort4);
                inst.startDSClient();

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        System.out.println("interrupted");
                        return;
                    }
                }

            }
        });
    }
}
