package observer;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This interface is observed by clickableObserver
 */
public interface ClickableObservable {
	void addObserver(ClickableObserver co);

	void notifyClickableObservers();
}
