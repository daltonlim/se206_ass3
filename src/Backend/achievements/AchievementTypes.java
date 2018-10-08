package Backend.achievements;

import Backend.File.FileNameParser;

public enum AchievementTypes {
    PLAY, RECORD, TIME;

    @Override
    public String toString() {
        String toReturn =  super.toString();
       return FileNameParser.sentenceCase(toReturn);
    }
}

