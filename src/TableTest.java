package src;
import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
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
    private JButton stopMotors;

    private static JFrame frame;
    private JTextPane textPane;
    private SimpleAttributeSet keyWord = new SimpleAttributeSet();

    private NetworkTableInstance inst;
    private NetworkTable dataTable; 

    private StringArrayPublisher dArrayPublisher;
    private BooleanPublisher rBooleanPublisher;

    private String[] errorStrings;
    private String[] temp;
    private String[] messages = {"all is well!"};
    private String[] testingOut = {"sub solenoid 12 13 0.0 0 0 0 0.0 -2.0 0.0 0 0", 
                                      "sub motor 12 13 0.0 0 0 0 0.0 -2.0 0.0 0 0",
                                      "sub motor 15 23 0.0 0 0 0 0.0 -2.0 0.0 0 0"}; //only to test gui

    private StringArraySubscriber registeredMotors;
    private StringArraySubscriber allErrors;

    public TableTest() {
        super();
        inst = NetworkTableInstance.getDefault();
        dataTable = inst.getTable("dataTable");

        dArrayPublisher = dataTable.getStringArrayTopic("tableValues").publish();
        rBooleanPublisher = dataTable.getBooleanTopic("Running?").publish();
        this.registeredMotors = dataTable.getStringArrayTopic("motors").subscribe(messages);
        this.allErrors = dataTable.getStringArrayTopic("errors").subscribe(messages);

        try { Thread.sleep(1000); } catch (Exception e) {}

        String[] motors = registeredMotors.get();
        System.out.println(Arrays.toString(motors));

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        table = new JTable(new RowInputOutput(motors)); //change to motors when testing with robot
        table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        table.setFillsViewportHeight(true);
        table.setMinimumSize(getMinimumSize());
        table.setFont(new Font("Sushi Sans", Font.BOLD, 18));
        table.getTableHeader().setFont(new Font("Sushi Sans", Font.PLAIN, 17));
        table.setRowHeight(27);

        add(new JScrollPane(table));
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);  
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        int rowCount = table.getModel().getRowCount();
        pushAll = new Buttons("run all", rowCount);
        pushAll.addActionListener(this);
        add(pushAll);

        defaultButton = new Buttons("load original defaults", rowCount);
        defaultButton.addActionListener(this);
        add(defaultButton);

        defaultButton.setBackground(new ColorUIResource(209, 237, 245));

        stopMotors = new Buttons("stop all", rowCount);
        stopMotors.addActionListener(this);
        add(stopMotors);

        textPane = new JTextPane();
        StyleConstants.setForeground(keyWord, Color.RED);
        add(new JScrollPane(textPane));

        JLabel picLabel = new JLabel("");
        ImageIcon image = new ImageIcon(new ImageIcon("assets\\images\\img2.png").getImage().getScaledInstance(2000, 50, Image.SCALE_SMOOTH));
        picLabel.setIcon(image);
        table.setBackground(Color.WHITE);
        table.setSelectionBackground(new ColorUIResource(209, 237, 250));
        add(picLabel);

        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setBackground(attributeSet, Color.CYAN);
        textPane.setBackground(new ColorUIResource(209, 237, 250));

        Timer timer = new Timer(300, this);
        timer.setInitialDelay(1000);
        timer.start();

    }
 
    public void actionPerformed(ActionEvent event) {
        Object buttonName = event.getSource();
        printOutput();
        if (buttonName.equals(pushAll)){
            String[] output = new String[table.getRowCount()];
            
            dArrayPublisher.set(RowInputOutput.sendValues(output, table));
            dArrayPublisher.setDefault(output);

            rBooleanPublisher.set(true);
            rBooleanPublisher.setDefault(false);

        } else if (buttonName.equals(defaultButton)){ //use create and show gui to load new window instead, or do this
            TableTest newContentPane = new TableTest();
            newContentPane.setOpaque(true); 
            frame.setContentPane(newContentPane);
            frame.pack();
            frame.setVisible(true);
        } else if (buttonName.equals(stopMotors)){
            rBooleanPublisher.set(false);
        }
    }

    private void printOutput(){ 
        String[] messages = allErrors.get();
        System.out.print(Arrays.toString(messages));
        StyledDocument doc = textPane.getStyledDocument();
        doc.setCharacterAttributes(ALLBITS, ABORT, keyWord, getFocusTraversalKeysEnabled());
        int totalCount = 0;
        int count = 0;
        int printCount = 0;
        try {
            for (String message : messages) {
                totalCount = messages.length;
                if (!message.equals("")){
                    if (message.contains("Error")) {
                        doc.insertString(doc.getLength(), message + "\n", keyWord);
                        count++;
                    } else {
                        doc.insertString(doc.getLength(), message + "\n", null);
                    }
                    printCount++;
                }
            }

            if (printCount >= 30) {
                doc.remove(0, doc.getLength());
            }
            doc.insertString(doc.getLength(), count + " motor has errors of " + totalCount + "\n", keyWord);
        } catch(BadLocationException e){
            e.printStackTrace();
        }
    }


    // Create the GUI and show it. This is my window method
    public static void createAndShowGUI() {
        //Create and set up the window.
        frame = new JFrame("tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.

        TableTest newContentPane = new TableTest();
        newContentPane.setBackground(Color.white);
        newContentPane.setOpaque(true); //content panes must be opaque.
        frame.add(newContentPane);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
}

