package Backend.achievements;

public class TimeAchievement extends Achievement {

    private final long start;
    private long minutes;

    TimeAchievement(int oneStar, int twoStar, int threeStar, String oneStarName, String twoStarName,
                    String threeStarName) {
        super("Time", oneStar, twoStar, threeStar, oneStarName, twoStarName, threeStarName);
        start = System.currentTimeMillis();
        minutes = 0;

    }

   public long getCount() {

       long seconds = (System.currentTimeMillis() - start )/ 1000;
       long minutes = seconds /60 + this.minutes;
        return minutes;
    }

    void setCount(long time) {
        minutes = time;
    }
}
