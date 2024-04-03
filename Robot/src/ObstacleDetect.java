import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;

public class ObstacleDetect implements Runnable {
    private EV3UltrasonicSensor ultrasonicSensor;
    private SampleProvider distance;
    private TransferObject transferObject;

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

            

            if (currentDistance < 20) { 
                transferObject.setFlag(true);
            } else {
                transferObject.setFlag(false);
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                
            }
        }
    }
}
