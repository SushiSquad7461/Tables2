package src.view;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import java.awt.Font;

public class Buttons extends JButton {

  public Buttons(String text, int tableRowCount) {
    super(text);
    setFont(new Font("Sushi Sans", Font.PLAIN, 20));
    setVerticalTextPosition(AbstractButton.CENTER);
    setBounds(200, tableRowCount * 20 + 20,100,60);
  }

}
