import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class LightSensorThread extends Thread {
    private EV3ColorSensor colorSensor;
    private SampleProvider lightIntensity;
    private float currentIntensity;

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

    public float getCurrentIntensity() {
        return currentIntensity;
    }
}

