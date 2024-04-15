import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

/**
 * ObstacleDetect class implements a thread for detecting obstacles using an EV3
 * ultrasonic sensor. It continuously updates the transferObject based on the
 * distance to detected obstacles.
 * 
 * @author Dinusha Kaluarachchi
 * @author Nuwani Fernando
 * @author Supun Wathsana
 * @version 1.0
 * @since 04/04/2023
 */
public class ObstacleDetect implements Runnable {
	private EV3UltrasonicSensor ultrasonicSensor;
	private SampleProvider distance;
	private TransferObject transferObject;

	/**
	 * Constructs an ObstacleDetect object with the specified TransferObject.
	 * 
	 * @param transferObject The TransferObject to be updated based on obstacle
	 *                       detection
	 */
	public ObstacleDetect(TransferObject transferObject) {
		this.transferObject = transferObject;
		ultrasonicSensor = new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"));
		distance = ultrasonicSensor.getDistanceMode();
	}

	@Override
	public void run() {
		float[] distanceSample = new float[distance.sampleSize()];

		while (!Button.ESCAPE.isDown()) {
			distance.fetchSample(distanceSample, 0);
			float currentDistance = distanceSample[0] * 100;

			if (currentDistance < 18) {
				transferObject.setFlag(true);
			} else {
				transferObject.setFlag(false);
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
