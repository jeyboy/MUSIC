package service;

import java.awt.event.ActionListener;

public class ActionBind {
	public ActionListener action = null;
	public String name = null;
	
	public ActionBind(String name, ActionListener action) {
		this.action = action;
		this.name = name;
	}
}
