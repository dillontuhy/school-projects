//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package observer;

/**
 * This is our custom Observable interface and follows the basic observer design pattern.
 */
public interface WumpObservable {

	void addObserver(WumpObserver observer);
	
	void notifyObservers();
	
	
	
	
}
