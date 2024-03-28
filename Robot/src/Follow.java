import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.motor.Motor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;

public class Follow implements Runnable {
    private EV3ColorSensor colorSensor;
    private SampleProvider lightIntensity;
    private float targetIntensity1 = 30;
    private float targetIntensity2 = 20;
    private float targetIntensityLower = 15;
    private float targetIntensityHigher = 55;
    private int defaultSpeed = 200;
    private TransferObject transferObject;

    public Follow(TransferObject transferObject) {
        this.transferObject = transferObject;
        colorSensor = new EV3ColorSensor(LocalEV3.get().getPort("S1"));
        lightIntensity = colorSensor.getRedMode();
    }

    @Override
    public void run() {
        float[] sample = new float[lightIntensity.sampleSize()];

        while (!Button.ESCAPE.isDown()) {
            lightIntensity.fetchSample(sample, 0);
            float currentIntensity = sample[0] * 100;

            System.out.println("Light Intensity: " + currentIntensity);

            if (!transferObject.isFlag()) {
                if (currentIntensity < targetIntensityLower) {
                    Motor.B.setSpeed(defaultSpeed);
                    Motor.A.setSpeed(0);
                } else if (currentIntensity > targetIntensityLower && currentIntensity < targetIntensity1) {
                    Motor.B.setSpeed(defaultSpeed);
                    Motor.A.setSpeed(50);
                } else if (currentIntensity > targetIntensity1 && currentIntensity < targetIntensity2) {
                    Motor.B.setSpeed(defaultSpeed);
                    Motor.A.setSpeed(defaultSpeed);
                } else if (currentIntensity > targetIntensity2 && currentIntensity < targetIntensityHigher) {
                    Motor.B.setSpeed(50);
                    Motor.A.setSpeed(defaultSpeed);
                } else {
                    Motor.B.setSpeed(0);
                    Motor.A.setSpeed(defaultSpeed);
                }

                Motor.A.forward();
                Motor.B.forward();
            } else {
                // If obstacle detected, stop the robot
                Motor.A.stop();
                Motor.B.stop();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

