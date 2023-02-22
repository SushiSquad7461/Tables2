package src.view;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import src.logic.MotorInfo;

public class RowInputOutput extends AbstractTableModel{

    private String[] columnNames = {
    "Subsystem", "MotorNum", "CanID", 
    "PdhPort", "Const Speed", "Joystick Scale Factor", //preset
    "Is Coast?", "Motor Inversed?", "Current Limit",
    "Encoder Min", "Encoder Max", "Is Included?"};
    
    private int rowCount;
    private Object[][] data;

    public RowInputOutput(String[] inputs) {
        rowCount = inputs.length;
        data = new Object[rowCount][columnNames.length];
        for (int i = 0; i < inputs.length; i++){//read network table string array
            MotorInfo motorInfo = new MotorInfo(inputs[i]);
            data[i] = new Object[]{
            motorInfo.subSystem, motorInfo.motorName, motorInfo.pdhPort, 
            motorInfo.canID, motorInfo.speed, motorInfo.joystickScale,
            motorInfo.coast, motorInfo.motorInversed, motorInfo.currLimit, 
            motorInfo.encoderMin, motorInfo.encoderMax, motorInfo.isIncluded
            };
        }
    }

    public static String[] sendValues(String[] output, JTable table){
        System.out.println("called");
        for (int rows = 0; rows < table.getRowCount(); rows++){
            if ((Boolean)table.getModel().getValueAt(rows, 11) != Boolean.FALSE){
                for (int cols = 0; cols < table.getColumnCount(); cols++){
                    Object value = table.getModel().getValueAt(rows, cols).toString();
                    output[rows] += (value) + " ";
                }
            } else {
                output[rows] = "containsNull";
            } 
            
            if (output[rows] != null && !output[rows].equals("containsNull")){
                output[rows] = output[rows].substring(4);
            }
        }
        return output;
    }

    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public int getRowCount() {
        return data.length;
    }

    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    //makes boolean a checkbox-- returns class for boolean column using value
     public Class<? extends Object> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    //if col of 3 at row(s) != null || != 0, set col of 4 at row(s) to false and vice versa
    public boolean isCellEditable(int row, int col) {
        // Double col4Value = Double.parseDouble(getValueAt(row, 4).toString());
        // Double col5Value = Double.parseDouble(getValueAt(row, 5).toString());
        // if (col == 5 && col4Value != 0.0){
        //     return false;
        // } else if (col == 4 && col5Value != 0.0){
        //     return false;
        // }
        return true;
    }

    //set values
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}