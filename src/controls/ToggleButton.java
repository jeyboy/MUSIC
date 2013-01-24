package controls;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Icon;

import Media.ActionObserver;


public class ToggleButton extends RoundButton {
	private static final long serialVersionUID = -1724640532322995296L;
	
	Icon alt_image;
	boolean default_value = true;
	int [] states;
	
    private ArrayList<ActionObserver> observers = new ArrayList<ActionObserver>();
    
    public void registerObserver(ActionObserver observer) { observers.add(observer); }
    public void notifyListeners() {
    	int state = default_value ? states[0] : states[1];
  		setVisible(state != ActionObserver.HIDDEN);
  		
        for(ActionObserver observer : observers)
            observer.notify(state);
    } 	
	
    public ToggleButton(Icon icon, Icon alt_icon, ActionObserver observer, int ... elem_states) {
		super(icon);
		alt_image = alt_icon;
		states = elem_states;
		addActionListener(new ActionListener() {  public void actionPerformed(ActionEvent arg0) { ToggleWithNotify(); } });
		if (observer != null) registerObserver(observer);
	}
	
	public void Toggle() {
		Icon temp = getIcon();
		setIcon(alt_image);
		alt_image = temp;
		default_value = !default_value;
	}
	
	public void ToggleWithNotify() {
		Toggle();
		notifyListeners();
	}	
	
	public boolean IsDefaultState() { return default_value; }
}