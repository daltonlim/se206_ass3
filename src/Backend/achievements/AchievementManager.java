package Backend.achievements;

import Backend.File.FileLogger;

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
        add("Play", 1, 10, 25, "Player One", "Two Streak", "God Player");
        add("Record", 1, 10, 25, "Player One", "Two Streak", "God Player");
        achievementHashMap.put("Time",new TimeAchievement(1, 10, 25, "Player One", "Two Streak", "God Player"));
        readIn();
    }

    private void readIn() {
        //TODO read in fiels to the manager
        File log = new File(logFile);
        if (log.exists()) {
            try {
                Scanner s = new Scanner(log);
                while (s.hasNext()) {
                    String[] strings = s.next().split(":");
                        Long integer = Long.valueOf(strings[1]);
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

    public void saveState() {
        List<String> stringList = new ArrayList<>();
        for (Achievement achievement : achievementHashMap.values()) {
            stringList.add(achievement.getName() + ":" + achievement.getCount());
        }
        FileLogger.getInstance().writeToFile(logFile, stringList);
    }
}

