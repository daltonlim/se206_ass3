package Backend.achievements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AchievementManager {
    private static AchievementManager instance;
    private HashMap<String, Achievement> achievementHashMap;

    public static AchievementManager getInstance() {
        if (instance == null) {
            instance = new AchievementManager();
        }
        return instance;
    }

    private AchievementManager() {
        achievementHashMap = new HashMap<>();
        initalise();
    }

    private void initalise() {
        add("Play", 1, 10, 25, "Player One", "Two Streak", "God Player");
        add("Record", 1, 10, 25, "Player One", "Two Streak", "God Player");
        add("Time", 1, 10, 25, "Player One", "Two Streak", "God Player");
    }

    private void add(String name, int oneStar, int twoStar, int threeStar, String oneStarName,
                     String twoStarName, String threeStarName) {
        achievementHashMap.put(name, new Achievement(name, oneStar, twoStar, threeStar, oneStarName, twoStarName,
                threeStarName));
    }

    public List<String> retrieveAchievements() {
        List<String> stringList = new ArrayList<>();
        stringList =new ArrayList<>( achievementHashMap.keySet());
        Collections.sort(stringList);
        return stringList;
    }

    public void incrementAchievement(String achievement) {
        Achievement achievement1 = achievementHashMap.get(achievement);
        achievement1.increment();
    }

    public int getStar(String ach) {
        Achievement achievement = achievementHashMap.get(ach);
        return achievement.getStar();
    }

}

