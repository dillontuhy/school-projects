package model;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

import observer.ClickableObservable;
import observer.ClickableObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * Abstract class Clickable
 */
public abstract class Clickable extends JPanel implements ClickableObservable, Serializable {

	private Border selectedBorder;
	private boolean isSelected;
	private MouseListener mouse;
	private boolean selectable, hoverable;
	private List<ClickableObserver> clickableObservers;
	private Border defaultBorder, highlightedBorder;
	
	/**
	 * Constructor initializes the Clickable variables
	 * 
	 * @param  clickObservers
	 */
	public Clickable(List<ClickableObserver> clickObservers) {

		selectedBorder = BorderFactory.createLineBorder(Color.PINK, 5);
		isSelected = false;
		mouse = new ClickableMouseListener();
		this.addMouseListener(mouse);
		clickableObservers = clickObservers;
		defaultBorder = BorderFactory.createLineBorder(Color.BLACK, 2);
		highlightedBorder = BorderFactory.createLineBorder(Color.ORANGE, 3);
	}

	/**
	 * Returns the list of clickable observers
	 */
	public List<ClickableObserver> getClickableObservers() {
		return clickableObservers;
	}

	/**
	 * abstract method to getInstanceOf for clickable
	 */
	public abstract Clickable getNewInstanceOf();

	/**
	 * Method sets selectable as true or false
	 * 
	 * @param  isSelectable
	 */
	public void setSelectable(boolean isSelectable) {
		selectable = isSelectable;
	}
	
	/**
	 * Method set hoverable as true or false
	 * 
	 * @param  isHoverable
	 */
	public void setHoverable(boolean isHoverable) {
		hoverable = isHoverable;
	}

	/**
	 * Returns hoverable boolean value
	 */
	public boolean isHoverable() {
		return hoverable;
	}

	/**
	 * Returns selectable boolean value
	 */
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * Abstract method getPrice
	 */
	public abstract int getPrice();

	/**
	 * Abstract method toString
	 */
	public abstract String toString();

	/**
	 * When tile or object is hovered over it lets the user know
	 * 
	 * @param  hovering
	 */
	public void hover(boolean hovering) {
		if (!isSelected()) {
			if (hovering) {
				this.setBorder(highlightedBorder);
			} else {
				this.setBorder(this.getDefaultBorder());
			}
		}
	}
	
	/**
	 * highlights the border of the object
	 * 
	 * @param  highlight
	 */
	public void setHighlightedBorder(Border highlight){
		this.highlightedBorder = highlight;
	}

	/**
	 * returns the boolean isSelected. Lets clickable know if the item is selected or not.
	 */
	public boolean isSelected() {
		return isSelected;
	}
	
	/**
	 * sets the border of tiles when selected to be default border
	 * 
	 * @param  newBorder
	 */
	public void setDefaultBorder(Border newBorder)
	{
		defaultBorder = newBorder;
	}
	
	/**
	 * returns the default border
	 */
	public Border getDefaultBorder()
	{
		return defaultBorder;
	}

	/**
	 * when something/tile is selected toggles isSelected
	 * 
	 * @param  newIsSelected
	 */
	public void setSelected(boolean newIsSelected) {
		isSelected = newIsSelected && isSelectable();
		if (isSelected) {
			this.setBorder(selectedBorder);
		} else {
			//TODO: make sure this is correct
			this.setBorder(defaultBorder);
		}
	}

	/**
	 * adds co to clickableObservers list
	 * 
	 * @param  co
	 */
	@Override
	public void addObserver(ClickableObserver co) {
		clickableObservers.add(co);
	}

	/**
	 * inner class makes the mouse a listener
	 */
	private class ClickableMouseListener implements MouseListener, Serializable{

		@Override
		public void mouseClicked(MouseEvent arg0) {
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			hover(true);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			hover(false);
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			setSelected(!isSelected);
			notifyClickableObservers();
		}

	}

	/**
	 * abstract method to get the location of an object/item on the map
	 */
	public abstract Point getGameLocation();

	/**
	 * Notifies when click is observed
	 */
	public void notifyClickableObservers() {
		for (ClickableObserver co : clickableObservers) {
			co.notifyOfClick(this);
		}
	}

}
