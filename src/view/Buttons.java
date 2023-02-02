package src.view;

import javax.swing.AbstractButton;
import javax.swing.JButton;

public class Buttons extends JButton {

  public Buttons(String text, int tableRowCount) {
    super(text);
    setVerticalTextPosition(AbstractButton.CENTER);
    setBounds(200, tableRowCount * 20 + 20,100,60);
  }

}
