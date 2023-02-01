package src.view;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import src.logic.MotorInfo;

public class RowInputOutput extends AbstractTableModel{

    String[] columnNames = {"Subsystem", "MotorNum", "Speed", //preset
    "Coast", "Flip Encoder",
    "Current Limit",
    "Encoder Min",
    "Encoder Max"};
    
    Object[][] data = new Object[3][columnNames.length]; //keep constant for now

    public RowInputOutput(String[] inputs) {
        for (int i = 0; i < inputs.length; i++){//read network table string array
            MotorInfo motorInfo = new MotorInfo(inputs[i]);
            data[i] = new Object[]{motorInfo.subSystem, motorInfo.motorName, motorInfo.speed, motorInfo.coast, motorInfo.encoderDir, motorInfo.currLimit, 
            motorInfo.encoderMin, motorInfo.encoderMax};
        }
    }

    public static String[] sendValues(String[] output, JTable table){
        for (int rows = 0; rows < table.getRowCount(); rows++){
            for (int cols = 0; cols < table.getColumnCount(); cols++){
                Object value = table.getModel().getValueAt(rows, cols).toString();
                if (value != "null"){
                    output[rows] += (value) + " ";
                }
            }
            output[rows] = output[rows].substring(4);
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

    public boolean isCellEditable(int row, int col) {
        return true;
    }

    //set values
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}