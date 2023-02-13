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
    private JButton stopMotors;

    private static JFrame frame;
    private JTextPane textPane;
    private SimpleAttributeSet keyWord = new SimpleAttributeSet();

    static NetworkTableInstance inst = NetworkTableInstance.getDefault();
    NetworkTable dataTable = inst.getTable("dataTable"); 

    StringArrayPublisher dArrayPublisher = dataTable.getStringArrayTopic("tableValues").publish();
    BooleanPublisher cBooleanPublisher = dataTable.getBooleanTopic("Changed?").publish();
    BooleanPublisher rBooleanPublisher = dataTable.getBooleanTopic("Running?").publish();

    private String[] defaultInfo = {
        "subsystem0 motor1 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0", "subsystem0 motor2 12 13 0.0 0.0 0 0 0.0 0.0 0.0 0"
    };
    private String[] messages = {"System is working", "Error: not working", "System is back to normal", "Error: not working"};

    public TableTest() {
        super();

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        table = new JTable(new RowInputOutput(defaultInfo));
        table.setPreferredScrollableViewportSize(new Dimension(400, 200));
        table.setFillsViewportHeight(true);
        table.setFont(new Font("Sushi Sans", Font.BOLD, 18));
        table.getTableHeader().setFont(new Font("Sushi Sans", Font.PLAIN, 17));
        table.setRowHeight(27);

        add(new JScrollPane(table));
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);  
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        int rowCount = table.getModel().getRowCount();
        pushAll = new Buttons("run motors", rowCount);
        pushAll.addActionListener(this);
        add(pushAll);

        defaultButton = new Buttons("load original defaults", rowCount);
        defaultButton.addActionListener(this);
        add(defaultButton);

        stopMotors = new Buttons("stop included motors", rowCount);
        stopMotors.addActionListener(this);
        add(stopMotors);

        textPane = new JTextPane();
        StyleConstants.setForeground(keyWord, Color.RED);
        add(new JScrollPane(textPane));

        JLabel picLabel = new JLabel("");
        ImageIcon image = new ImageIcon(new ImageIcon("/Users/nitya/java/tables_copy/assets/images/img2.png").getImage().getScaledInstance(2000, 50, Image.SCALE_SMOOTH));
        picLabel.setIcon(image);
        table.setBackground(Color.WHITE);
        add(picLabel);


    }
 
    public void actionPerformed(ActionEvent event) {
        Object buttonName = event.getSource();
        if (buttonName.equals(pushAll)){
            String[] output = new String[table.getRowCount()]; //could get row count for included values
            System.out.println(Arrays.toString(RowInputOutput.sendValues(output, table)));
            
            dArrayPublisher.set(RowInputOutput.sendValues(output, table));
            dArrayPublisher.setDefault(output);

            cBooleanPublisher.set(true);
            cBooleanPublisher.setDefault(false);

            rBooleanPublisher.set(true);
            rBooleanPublisher.setDefault(false);

            printOutput(messages); //subscriber needed to receive error messages 
        } else if (buttonName.equals(defaultButton)){ //use create and show gui to load new window instead, or do this
            TableTest newContentPane = new TableTest();
            newContentPane.setOpaque(true); 
            frame.setContentPane(newContentPane);
            frame.pack();
            frame.setVisible(true);
        } else {
            String[] output = new String[table.getRowCount()];
            System.out.println(Arrays.toString(RowInputOutput.stopMotors(output, table)));
            rBooleanPublisher.set(false);
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
    public static void createAndShowGUI() {
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

}

