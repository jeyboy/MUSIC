package entagged.tageditor.optionpanels;

import javax.swing.*;

public interface OptionPanelInterface{
	public String getOptionName();
	
	public JButton getButton();
	
	public JPanel getPanel();
	
	public boolean saveOptions();
}
	
