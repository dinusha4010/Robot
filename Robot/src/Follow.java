
import java.io.File;
import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.motor.Motor;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

/**
 * Follow class implements a behavior to follow a line or handle obstacles using light sensor data.
 * This class utilizes a light sensor thread to continuously monitor the light intensity.
 * It provides methods to handle line following and obstacle detection.
 * 
 * @author Dinusha Kaluarachchi
 * @author Nuwani Fernando
 * @author Supun Wathsana
 * @version 1.0
 * @since 04/04/2023
 */

public class Follow implements Runnable {

	private static final RegulatedMotor leftMotor = Motor.B;
	private static final RegulatedMotor rightMotor = Motor.A;
	private TransferObject transferObject;
	private long startTime;
	private int obstacleCount = 0;

	/**
	 * Constructs a Follow object with the specified TransferObject.
	 * 
	 * @param transferObject The TransferObject to be used by Follow
	 */
	public Follow(TransferObject transferObject) {
		this.transferObject = transferObject;
		startTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		LightSensorThread lightSensorThread = new LightSensorThread();
		lightSensorThread.start();

		while (!Button.ESCAPE.isDown()) {
			float currentIntensity = lightSensorThread.getCurrentIntensity();

			if (!transferObject.isObjectdetect()) {
				handleLineFollowing(currentIntensity);
			} else {
				handleObstacle();
			}

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Handles line following behavior based on the current light intensity.
	 * 
	 * @param currentIntensity The current light intensity detected by the sensor
	 */
	private void handleLineFollowing(float currentIntensity) {
		int defaultSpeed = 200;
		int targetIntensity1 = 30;
		int targetIntensity2 = 20;
		int targetIntensityLower = 15;
		int targetIntensityHigher = 55;

		if (currentIntensity < targetIntensityLower) {

			leftMotor.setSpeed(defaultSpeed);
			rightMotor.setSpeed(0);
		} else if (currentIntensity > targetIntensityLower && currentIntensity < targetIntensity1) {
			leftMotor.setSpeed(defaultSpeed);
			rightMotor.setSpeed(50);
		} else if (currentIntensity > targetIntensity1 && currentIntensity < targetIntensity2) {
			leftMotor.setSpeed(defaultSpeed);
			rightMotor.setSpeed(defaultSpeed);
		} else if (currentIntensity > targetIntensity2 && currentIntensity < targetIntensityHigher) {
			leftMotor.setSpeed(50);
			rightMotor.setSpeed(defaultSpeed);
		} else {
			leftMotor.setSpeed(0);
			rightMotor.setSpeed(defaultSpeed);
		}

		leftMotor.forward();
		rightMotor.forward();

	}

	/**
	 * Handles obstacle avoidance behavior.
	 */
	private void handleObstacle() {
		obstacleCount++;
		if (obstacleCount < 2) {
			leftMotor.setSpeed((int) (200));

			rightMotor.setSpeed((int) (120));
			leftMotor.forward();
			rightMotor.forward();
			Delay.msDelay(1000);
			leftMotor.setSpeed((int) (180));
			rightMotor.setSpeed((int) (200));

			leftMotor.forward();
			rightMotor.forward();
			Delay.msDelay(2500);

			leftMotor.setSpeed((int) (50));
			rightMotor.setSpeed((int) (100));

			leftMotor.forward();
			rightMotor.forward();
			Delay.msDelay(2000);
			leftMotor.setSpeed((int) (80));
			rightMotor.setSpeed((int) (100));

			leftMotor.forward();
			rightMotor.forward();
			Delay.msDelay(3000);


			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			leftMotor.stop();
			rightMotor.stop();
			play();

			long totalTime = System.currentTimeMillis() - startTime;
			System.out.println("Total Time: " + totalTime / 1000 + " s");

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			System.exit(0);
		}
	}

	/**
	 * Plays sound samples for obstacle detection.
	 */

	public void play() {
		Sound.playSample(new File("object.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("detected.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("stop.wav"), Sound.VOL_MAX);
	}
	public void play2() {
		Sound.playSample(new File("object.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("detected.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("stop.wav"), Sound.VOL_MAX);
	}
}
