package Backend.achievements;

public class TimeAchievement extends Achievement {

    private Thread thread;

    TimeAchievement(int oneStar, int twoStar, int threeStar, String oneStarName, String twoStarName,
                    String threeStarName) {
        super("Minute", oneStar, twoStar, threeStar, oneStarName, twoStarName, threeStarName);
        thread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(60000);
                    increment();
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });
        thread.start();
    }

    void killThread() {
        thread.stop();
    }
}
