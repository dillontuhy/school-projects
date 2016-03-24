package observer;

import model.Clickable;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This interface observes clickableObserverable
 */
public interface ClickableObserver {
	void notifyOfClick(Clickable c);
}
