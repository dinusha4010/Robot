import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

/**
 * LightSensorThread class implements a thread for reading light intensity using
 * an EV3 color sensor. It continuously updates the current light intensity
 * detected by the sensor.
 * 
 * @author [Dinusha Kaluarachchi], [Nuwani Fernando], [Supun Wathsana]
 * @version 1.0
 * @since [04/04/2023]
 */
public class LightSensorThread extends Thread {
	private EV3ColorSensor colorSensor;
	private SampleProvider lightIntensity;
	private float currentIntensity;

	/**
	 * Constructs a LightSensorThread object.
	 */
	public LightSensorThread() {
		colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
		lightIntensity = colorSensor.getRedMode();
	}

	@Override
	public void run() {
		float[] sample = new float[lightIntensity.sampleSize()];

		while (!Button.ESCAPE.isDown()) {
			lightIntensity.fetchSample(sample, 0);
			currentIntensity = sample[0] * 100;
		}
	}

	/**
	 * Gets the current light intensity detected by the sensor.
	 * 
	 * @return The current light intensity
	 */
	public float getCurrentIntensity() {
		return currentIntensity;
	}
}
