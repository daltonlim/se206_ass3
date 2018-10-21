package backend.achievements;

import backend.file.FileLogger;

import java.io.File;
import java.util.*;

public class AchievementManager {
    private static AchievementManager instance;
    private static String logFile = "Logs/stats.se206";
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
        add("Open",1,5,10,"Starter" ,"Runner","Consistent");
        add("Play", 1, 20, 50, "Player One", "Two Streak", "God Player");
        add("Recording", 1, 10, 25, "Spy Talk", "Low Flow", "Loud Crowd");
        add("Deletion", 1, 10, 25, "Fresh Blood", "file Murderer", "file Exterminator");
        add("Report", 1, 20, 50, "Snitch", "Enforcer", "Edward Snowden");
        achievementHashMap.put("Minute",new TimeAchievement(1, 30, 90, "Small timer", "Two Streak", "God Player"));
        readIn();
    }

    private void readIn() {
        File log = new File(logFile);
        if (log.exists()) {
            try {
                Scanner s = new Scanner(log);
                while (s.hasNext()) {
                    String[] strings = s.next().split(":");
                        int integer = Integer.valueOf(strings[1]);
                        achievementHashMap.get(strings[0]).setCount(integer);

                }
                s.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void add(String name, int oneStar, int twoStar, int threeStar, String oneStarName,
                     String twoStarName, String threeStarName) {
        achievementHashMap.put(name, new Achievement(name, oneStar, twoStar, threeStar, oneStarName, twoStarName,
                threeStarName));
    }

    public List<String> retrieveAchievements() {
        List<String> stringList = new ArrayList<>();
        stringList = new ArrayList<>(achievementHashMap.keySet());
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
    public Achievement retrieveAchievement(String achievement){
        return achievementHashMap.get(achievement);
    }

    public void saveState() {

        achievementHashMap.get("Minute").killThread();
        List<String> stringList = new ArrayList<>();
        for (Achievement achievement : achievementHashMap.values()) {
            stringList.add(achievement.getName() + ":" + achievement.getCount());
        }
        FileLogger.getInstance().writeToFile(logFile, stringList);
        achievementHashMap.get("Minute").killThread();
    }
}

