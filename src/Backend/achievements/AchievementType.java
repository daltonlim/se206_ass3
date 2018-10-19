package Backend.achievements;

import Backend.File.FileNameParser;

public enum AchievementType {
    PLAY, RECORD, TIME;

    @Override
    public String toString() {
        String toReturn =  super.toString();
       return FileNameParser.sentenceCase(toReturn);
    }
}

