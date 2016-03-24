package observer;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This interface is observed by CurrencyObserver
 */
public interface CurrencyObservable {
	
	void addObserver(CurrencyObserver o);
	
	void notifyObservers();
}
