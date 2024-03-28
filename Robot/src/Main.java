
public class Main {
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

