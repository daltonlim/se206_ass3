package Backend.achievements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AchievementManager {
    private static AchievementManager instance;
    private HashMap<AchievementTypes, Achievement> achievementHashMap;

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

    private void initalise(){
        AchievementTypes[] possibleValues = AchievementTypes.values();
        for (AchievementTypes possibleValue : possibleValues) {
            achievementHashMap.put(possibleValue   ,new Achievement(possibleValue));
        }
        //TODO setup reading to file and reading form file.
    }

    public List<String> retrieveAchievements(){
        List<String> stringList = new ArrayList<>();
        for (AchievementTypes achievementTypes : achievementHashMap.keySet()) {
            stringList.add(achievementTypes.toString());
        }
        Collections.sort(stringList);
        return stringList;
    }

    public void incrementAchievement(AchievementTypes achievement){
        Achievement achievement1 = achievementHashMap.get(achievement);
        achievement1.increment();
    }

    public int getStar(AchievementTypes ach){
        Achievement achievement = achievementHashMap.get(ach);
        return achievement.getStar();
    }

}
