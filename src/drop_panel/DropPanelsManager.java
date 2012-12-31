package drop_panel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;

import components.ArrowButton;

import service.Constants;

public class DropPanelsManager {
	public Container wnd;
	
	public DropPanel drop_left = new DropPanel(270);
	public DropPanel drop_top = new DropPanel();
	public DropPanel drop_bottom = new DropPanel();
	public DropPanel drop_right = new DropPanel(90);
	
	public ArrowButton arrow_left = new ArrowButton(SwingConstants.WEST); //LEFT
	public ArrowButton arrow_top = new ArrowButton(SwingConstants.NORTH); //TOP
	public ArrowButton arrow_bottom = new ArrowButton(SwingConstants.SOUTH); //BOTTOM
	public ArrowButton arrow_right = new ArrowButton(SwingConstants.EAST); // RIGHT
	
	public DropPanelsManager(Container parent) {
		wnd = parent;
		initializeArrowButtons();
		LoadDropPanels();
	}
	
	void initializeArrowButtons() {
		arrow_left.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { ToogleDrop(drop_left); }
		});
		
		arrow_right.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { ToogleDrop(drop_right); }
		});		
		
		arrow_top.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { ToogleDrop(drop_top); }
		});
		
		arrow_bottom.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) { ToogleDrop(drop_bottom); }
		});		
	}		
	
	public void LoadDropPanels() {
		drop_left.Load(Constants.drop_left_path);
		drop_top.Load(Constants.drop_top_path);
		drop_bottom.Load(Constants.drop_bottom_path);
		drop_right.Load(Constants.drop_right_path);	
	}
	
	public void saveDropPanels() {
		drop_left.Save(Constants.drop_left_path);
		drop_top.Save(Constants.drop_top_path);
		drop_bottom.Save(Constants.drop_bottom_path);
		drop_right.Save(Constants.drop_right_path);		
	}
	
	void ToogleDrop(DropPanel pan) {
		pan.setVisible(!pan.isVisible());
		wnd.revalidate();		
	}
	
	public void CloseAll() {
		drop_left.setVisible(false);
		drop_top.setVisible(false);
		drop_bottom.setVisible(false);
		drop_right.setVisible(false);
		wnd.revalidate();
	}
}