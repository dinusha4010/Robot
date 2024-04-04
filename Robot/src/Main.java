/**
 * Main class contains the main method to start the line following and obstacle
 * detection threads.
 * 
 * @author Dinusha Kaluarachchi
 * @author Nuwani Fernando
 * @author Supun Wathsana
 * @version 1.0
 * @since 04/04/2023
 */
public class Main {
	/**
	 * The main method to start the application.
	 * 
	 * @param args The command-line arguments
	 */
	public static void main(String[] args) {
		TransferObject transferObject = new TransferObject();

		Follow follow = new Follow(transferObject);
		ObstacleDetect obstacleDetect = new ObstacleDetect(transferObject);

		Thread threadFollow = new Thread(follow);
		Thread threadObstacleDetect = new Thread(obstacleDetect);

		threadFollow.start();
		threadObstacleDetect.start();
	}
}
