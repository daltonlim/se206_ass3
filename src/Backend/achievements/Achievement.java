package Backend.achievements;

public class Achievement {
    private String name;
    private int count;
    private int oneStar = 1;
    private int twoStar = 5;
    private int threeStar = 25;

    Achievement(AchievementType achievementTypes) {
        this.name = achievementTypes.toString();
    }

    void increment() {
        count++;
    }

    int getStar() {
        if (count > threeStar) {
            return 3;
        } else if (count > twoStar) {
            return 2;
        } else if (count > oneStar) {
            return 1;
        } else {
            return 0;
        }
    }
    
    int getCount() {
		return count;    	
    }
}
