package src.view;
import java.awt.Component;
import java.awt.Font;
import java.awt.Color;

import javax.swing.JTable;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomTableCellRenderer extends DefaultTableCellRenderer{
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      Color myColor;
      table.setBackground(new ColorUIResource(150, 170, 240).brighter());
      if (row % 4 == 0) {
        myColor = new ColorUIResource(241, 129, 165);
        c.setBackground(myColor);
      } else if (row % 4 == 1){
        myColor = new ColorUIResource(253, 222, 248);
        c.setBackground(myColor);
      } else if (row % 4 == 2){
        myColor = new ColorUIResource(111, 214, 255);
        c.setBackground(myColor);
      } else {
        myColor = new ColorUIResource(152, 237, 222);
        c.setBackground(myColor);
      }

      if (hasFocus) {
        c.setFont(new Font("Sushi Sans", Font.BOLD, 20));
      }

      if (value.toString().contains("solenoid")){
        c.setFont(new Font("Sushi Sans", Font.ITALIC, 20));
      }

      if (row > 3) {
        c.setBackground(Color.WHITE);
      }

      return c;
    }
  
}
