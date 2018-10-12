package Backend.achievements;

import Gui.SceneManager;

public class Achievement {
    private String name;
    private int count;
    private int oneStar = 1;
    private int twoStar = 5;
    private int threeStar = 25;
    private String oneStarName;
    private String twoStarName;
    private String threeStarName;


    Achievement(String name, int oneStar, int twoStar, int threeStar, String oneStarName,
                String twoStarName, String threeStarName) {
        this.name = name;
        this.oneStar = oneStar;
        this.twoStar = twoStar;
        this.threeStar = threeStar;
        this.oneStarName = oneStarName;
        this.twoStarName = twoStarName;
        this.threeStarName = threeStarName;
    }

    void increment() {
        count++;
        checkAchievement();
    }

    public String getName() {
        return name;
    }

   public long getCount() {
        return count;
    }

    void setCount(long count) {
        this.count = (int) count;
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

    public String getStarName() {
        if (count == oneStar) {
            return oneStarName;
        } else if (count == twoStar) {
            return twoStarName;
        } else if (count == threeStar) {
            return threeStarName;
        }
        return "";
    }

    private void checkAchievement() {
        SceneManager sceneManager = SceneManager.getInstance();
        if (count == oneStar || count == twoStar || count == threeStar) {
            sceneManager.showDialog(name);
        }
    }


}
