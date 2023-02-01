package src;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.*;

//import edu.wpi.first.networktables.NetworkTableInstance;
import src.view.RowInputOutput;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.ImageObserver;
import java.util.Arrays;
import java.awt.Dimension;

public class TableTest extends JPanel 
                        implements ActionListener { 

    private JTable table;
    private JButton pushAll;
    private JTextPane textPane;
    private SimpleAttributeSet keyWord = new SimpleAttributeSet();

    private String[] info = {"subsystem0 motor1 0.0 0 0 0 0.0 0.0", "subsystem0 motor2 0.0 0 0 0 0.0 0.0", "subsystem1 motor3 0.0 0 0 0 0.0 0.0"};
    private String[] messages = {"System is working", "Error: not working", "System is back to normal", "Error: not working"};

    public TableTest() {
        super();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        table = new JTable(new RowInputOutput(info));
        table.setPreferredScrollableViewportSize(new Dimension(300, 50));
        table.setFillsViewportHeight(true);
        add(new JScrollPane(table));
        ((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);  
        table.setDefaultRenderer(Object.class, new CustomTableCellRenderer());

        pushAll = new JButton("push all rows");
        pushAll.setVerticalTextPosition(AbstractButton.CENTER);
        pushAll.setBounds(200, table.getModel().getRowCount() * 20 + 20,100,60);

        pushAll.addActionListener(this);
        table.add(pushAll);

        textPane = new JTextPane();
        StyleConstants.setForeground(keyWord, Color.RED);
        add(new JScrollPane(textPane));

        printOutput(messages);
    }
 
    public void actionPerformed(ActionEvent event) {
        String[] output = new String[table.getRowCount()];
        System.out.println(Arrays.toString(RowInputOutput.sendValues(output, table)));
        printOutput(messages);
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

    static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
          Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
          if (row % 2 == 1) {
            c.setBackground(Color.CYAN);
          } else {
            c.setBackground(Color.PINK);
          }
          return c;
        }
    }
    
    // Create the GUI and show it. This is my window method
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("tables");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
           //Create and set up the content pane.
        TableTest newContentPane = new TableTest();
        newContentPane.setOpaque(true); //content panes must be opaque.
        frame.setContentPane(newContentPane);

        ImageIcon icon = new ImageIcon(".assets/images/Screen Shot 2022-12-03 at 9.45.27 PM.png");
        JLabel background = new JLabel(icon);
        background.setSize(ImageObserver.WIDTH, ImageObserver.HEIGHT);
    
        frame.getContentPane().add(background);    

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //creating and showing GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //NetworkTableInstance.getDefault();
                createAndShowGUI();
            }
        });
    }
}

