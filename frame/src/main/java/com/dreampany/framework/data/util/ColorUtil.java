package com.dreampany.framework.data.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.dreampany.framework.R;
import com.dreampany.framework.data.model.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by nuc on 5/3/2016.
 */
public class ColorUtil {
    private ColorUtil() {
    }

    public static Color createColor(int primary, int primaryDark, int accent) {
        Color color = new Color(primary, primaryDark);
        color.setColorAccentId(accent);
        return color;
    }

    public static Color createRedColor() {
        Color color = new Color(R.color.colorRed500, R.color.colorRed700);
        color.setColorAccentId(R.color.colorRed900);
        return color;
    }

    public static Color createGreenColor() {
        Color color = new Color(R.color.colorGreen600, R.color.colorGreen700);
        color.setColorAccentId(R.color.colorGreen900);
        return color;
    }

    public static int getColor(Context context, int colorId) {
        return ContextCompat.getColor(context, colorId);
    }

    private static final List<Color> colors = new ArrayList<>();
    //private static Color color;

    public static Color getBlackColor() {
        Color blackColor = new Color(R.color.colorBlack, R.color.colorBlack);
        return blackColor;
    }

    public static Color getRandColor(int position) {
        if (colors.isEmpty()) {
            Color redColor = new Color(R.color.colorRed500, R.color.colorRed700).setColorAccentId(R.color.colorRed900);
            Color pinkColor = new Color(R.color.colorPink500, R.color.colorPink700).setColorAccentId(R.color.colorPink900);
            Color purpleColor = new Color(R.color.colorPurple500, R.color.colorPurple700).setColorAccentId(R.color.colorPurple900);

            Color deepPurpleColor = new Color(R.color.colorDeepPurple500, R.color.colorDeepPurple700).setColorAccentId(R.color.colorDeepPurple900);
            Color indigoColor = new Color(R.color.colorIndigo500, R.color.colorIndigo700).setColorAccentId(R.color.colorIndigo900);
            Color blueColor = new Color(R.color.colorBlue500, R.color.colorBlue700).setColorAccentId(R.color.colorBlue900);

            Color lightBlueColor = new Color(R.color.colorLightBlue600, R.color.colorLightBlue700).setColorAccentId(R.color.colorLightBlue900);
            Color cyanColor = new Color(R.color.colorCyan600, R.color.colorCyan700).setColorAccentId(R.color.colorCyan900);
            Color tealColor = new Color(R.color.colorTeal500, R.color.colorTeal700).setColorAccentId(R.color.colorTeal900);

            Color greenColor = new Color(R.color.colorGreen600, R.color.colorGreen700).setColorAccentId(R.color.colorGreen900);
            Color lightGreenColor = new Color(R.color.colorLightGreen600, R.color.colorLightGreen700).setColorAccentId(R.color.colorLightGreen900);
            Color limeColor = new Color(R.color.colorLime800, R.color.colorLime900).setColorAccentId(R.color.colorAccent);

            Color yellowColor = new Color(R.color.colorYellow600, R.color.colorYellow800).setColorAccentId(R.color.colorYellow900);
            Color amberColor = new Color(R.color.colorAmber600, R.color.colorAmber800).setColorAccentId(R.color.colorAmber900);
            Color orangeColor = new Color(R.color.colorOrange600, R.color.colorOrange800).setColorAccentId(R.color.colorOrange900);

            Color deepOrangeColor = new Color(R.color.colorDeepOrange600, R.color.colorDeepOrange800).setColorAccentId(R.color.colorDeepOrange900);
            Color brownColor = new Color(R.color.colorBrown600, R.color.colorBrown800).setColorAccentId(R.color.colorBrown900);
            Color greyColor = new Color(R.color.colorGrey600, R.color.colorGrey800).setColorAccentId(R.color.colorGrey900);

            Color blueGreyColor = new Color(R.color.colorBlueGrey600, R.color.colorBlueGrey800).setColorAccentId(R.color.colorBlueGrey900);

            colors.add(redColor);
            colors.add(pinkColor);
            colors.add(purpleColor);

            colors.add(deepPurpleColor);
            colors.add(indigoColor);
            colors.add(blueColor);

            colors.add(lightBlueColor);
            colors.add(cyanColor);
            colors.add(tealColor);

            colors.add(greenColor);
            colors.add(lightGreenColor);
            colors.add(limeColor);

            colors.add(yellowColor);
            colors.add(amberColor);
            colors.add(orangeColor);

            colors.add(deepOrangeColor);
            colors.add(brownColor);
            colors.add(greyColor);

            colors.add(blueGreyColor);
        }

        if (position == -1) {
            int min = 1;
            int max = colors.size();

            Random r = new Random();
            int rand = r.nextInt((max - min) + 1) + min;

            Color color = colors.get(rand - 1);
            return color;
        }

        int size = colors.size();
        Color color = colors.get((position % size));

        return color;

    }


    public static Color getRandColor() {
        return getRandColor(-1);
    }

    public static int getRandCompatColor(Context context) {
        Color color = getRandColor();
        return ContextCompat.getColor(context, color.getColorPrimaryId());
    }


    public static int getRandColor(Context context, int position) {
        Color color = getRandColor(position);
        return ContextCompat.getColor(context, color.getColorPrimaryDarkId());
    }

    private static int[] particleColors;

    public static int[] getParticleColors(Context context) {
        if (particleColors == null) {
            int goldDark = ColorUtil.getColor(context, R.color.gold_dark);
            int goldMed = ColorUtil.getColor(context, R.color.gold_med);
            int gold = ColorUtil.getColor(context, R.color.gold);
            int goldLight = ColorUtil.getColor(context, R.color.gold_light);
            particleColors = new int[]{goldDark, goldMed, gold, goldLight};
        }
        return particleColors;
    }
}
