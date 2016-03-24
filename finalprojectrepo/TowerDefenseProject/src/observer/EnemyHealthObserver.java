package observer;

import enemy.Enemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */
/**
 * This interface observes Enemies
 */
public interface EnemyHealthObserver {
	void notifyOfUpdateTo(Enemy e);
}
