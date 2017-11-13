package com.dreampany.framework.data.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by nuc on 11/11/2016.
 */

public class Profile extends Base {

    private String level;
    private String rank;
    private int points;
    private int wins;
    private int losses;

    public Profile() {
    }

    protected Profile(Parcel in) {
        super(in);
        level = in.readString();
        rank = in.readString();
        points = in.readInt();
        wins = in.readInt();
        losses = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(level);
        dest.writeString(rank);
        dest.writeInt(points);
        dest.writeInt(wins);
        dest.writeInt(losses);
    }

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getLevel() {
        return level;
    }

    public String getRank() {
        return rank;
    }

    public int getPoints() {
        return points;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }


    public Profile setLevel(String level) {
        this.level = level;
        return this;
    }

    public Profile setRank(String rank) {
        this.rank = rank;
        return this;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Profile setWins(int wins) {
        this.wins = wins;
        buildLevel();
        return this;
    }

    public Profile setLosses(int losses) {
        this.losses = losses;
        return this;
    }

    public Profile buildLevel() {
        //5  ( <= 5)
        //10 ( <= 15)
        //15 ( <= 30)
        //20 ( <= 50)
        //25 ( <= 75)

        int levelIndex = 1, range = 5, factor = 5;

        while (wins > range) {
            range += (++levelIndex) * factor;
        }

        if (wins == 0) {
            levelIndex = 0;
        }

        setLevel(String.valueOf(levelIndex));

        return this;
    }

    public Profile buildRank() {

        return this;
    }
}
