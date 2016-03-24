package observer;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 * 
 */

/**
 * This interface is observed by GameConditionObserver
 */
public interface GameConditionObservable {
	void addConditionObserver(GameConditionObserver o);

	void notifyObservers(int panelInt);
}
