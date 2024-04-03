import java.io.File;

import lejos.hardware.Button;
import lejos.hardware.Sound;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.robotics.RegulatedMotor;

public class Follow implements Runnable {
	public EV3ColorSensor colorSensor;
	public SampleProvider lightIntensity;
	private float targetIntensity1 = 30;
	private float targetIntensity2 = 20;
	private float targetIntensityLower = 15;
	private float targetIntensityHigher = 55;
	private int defaultSpeed = 200;

	private static final RegulatedMotor leftMotor = Motor.B;
	private static final RegulatedMotor rightMotor = Motor.A;
	private TransferObject transferObject;
	private long startTime;
	private int obstacleCount = 0;

	public Follow(TransferObject transferObject) {
		this.transferObject = transferObject;
		colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
		lightIntensity = colorSensor.getRedMode();
		startTime = System.currentTimeMillis();
	}

	@Override
	public void run() {
		float[] sample = new float[lightIntensity.sampleSize()];

		while (!Button.ESCAPE.isDown()) {
			lightIntensity.fetchSample(sample, 0);
			float currentIntensity = sample[0] * 100;

			if (!transferObject.isFlag()) {
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
			} else {
				// If obstacle detected, stop the robot
				handleObstacle();
				lightIntensity.fetchSample(sample, 0);
				currentIntensity = sample[0] * 100;
				while (currentIntensity > 45) {
					lightIntensity.fetchSample(sample, 0);
					currentIntensity = sample[0] * 100;
					leftMotor.setSpeed(100);
					rightMotor.setSpeed(80);

					leftMotor.forward();
					rightMotor.forward();
					Delay.msDelay(250);

				}

			}
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}
	}

	private void handleObstacle() {
		obstacleCount++;
		if (obstacleCount < 2) {

			//play();

			// If obstacle detected, stop the robot
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

		} else {
			leftMotor.stop();
			rightMotor.stop();

			play();

			long totalTime = System.currentTimeMillis() - startTime;

			LCD.drawString("Total Time: " + totalTime / 1000 + " s", 0, 0);

			Delay.msDelay(10000);
			System.exit(0);
		}
	}

	public void play() {
		Sound.playSample(new File("object.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("detected.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("speed_down.wav"), Sound.VOL_MAX);
		Sound.playSample(new File("stop.wav"), Sound.VOL_MAX);

	}
}
