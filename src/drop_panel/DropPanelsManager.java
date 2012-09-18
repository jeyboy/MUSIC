package drop_panel;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import components.ArrowButton;
import components.ArrowButton.Turn;

public class DropPanelsManager {
	public Container wnd;
	
	public DropPanel drop_left = new DropPanel(270);
	public DropPanel drop_top = new DropPanel();
	public DropPanel drop_bottom = new DropPanel();
	public DropPanel drop_right = new DropPanel(90);
	
	public ArrowButton arrow_left = new ArrowButton(Turn.LEFT);
	public ArrowButton arrow_top = new ArrowButton(Turn.TOP);
	public ArrowButton arrow_bottom = new ArrowButton(Turn.BOTTOM);
	public ArrowButton arrow_right = new ArrowButton(Turn.RIGHT);		
	
	public DropPanelsManager(Container parent) {
		wnd = parent;
		initializeArrowButtons();
		LoadDropPanels();
	}
	
	public void LoadDropPanels() {
		drop_left.Load(service.Settings.drop_left_path());
		drop_top.Load(service.Settings.drop_top_path());
		drop_bottom.Load(service.Settings.drop_bottom_path());
		drop_right.Load(service.Settings.drop_right_path());	
	}
	
	public void saveDropPanels() {
		drop_left.Save(service.Settings.drop_left_path());
		drop_top.Save(service.Settings.drop_top_path());
		drop_bottom.Save(service.Settings.drop_bottom_path());
		drop_right.Save(service.Settings.drop_right_path());		
	}
	
	public void ToogleLeftDrop() {
		drop_left.setVisible(!drop_left.isVisible());
		wnd.revalidate();
	}
	public void ToogleRightDrop() {
		drop_right.setVisible(!drop_right.isVisible());
		wnd.revalidate();
	}
	public void ToogleTopDrop() {
		drop_top.setVisible(!drop_top.isVisible());
		wnd.revalidate();
	}
	public void ToogleBottomDrop() {
		drop_bottom.setVisible(!drop_bottom.isVisible());
		wnd.revalidate();
	}
	
	public void CloseAll() {
		drop_left.setVisible(false);
		drop_top.setVisible(false);
		drop_bottom.setVisible(false);
		drop_right.setVisible(false);
		wnd.revalidate();
	}
	
	void initializeArrowButtons() {
		arrow_left.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) { ToogleLeftDrop(); }
		});
		
		arrow_right.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) { ToogleRightDrop(); }
		});		
		
		arrow_top.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) { ToogleTopDrop(); }
		});
		
		arrow_bottom.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) { ToogleBottomDrop(); }
		});		
	}	
}
