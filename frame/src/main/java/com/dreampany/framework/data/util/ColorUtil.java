package com.dreampany.framework.data.util;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;

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
        color.setAccentId(accent);
        return color;
    }

    public static Color createRedColor() {
        Color color = new Color(R.color.colorRed500, R.color.colorRed700);
        color.setAccentId(R.color.colorRed900);
        return color;
    }

    public static Color createGreenColor() {
        Color color = new Color(R.color.colorGreen600, R.color.colorGreen700);
        color.setAccentId(R.color.colorGreen900);
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
            Color redColor = new Color(R.color.colorRed500, R.color.colorRed700).setAccentId(R.color.colorRed900);
            Color pinkColor = new Color(R.color.colorPink500, R.color.colorPink700).setAccentId(R.color.colorPink900);
            Color purpleColor = new Color(R.color.colorPurple500, R.color.colorPurple700).setAccentId(R.color.colorPurple900);

            Color deepPurpleColor = new Color(R.color.colorDeepPurple500, R.color.colorDeepPurple700).setAccentId(R.color.colorDeepPurple900);
            Color indigoColor = new Color(R.color.colorIndigo500, R.color.colorIndigo700).setAccentId(R.color.colorIndigo900);
            Color blueColor = new Color(R.color.colorBlue500, R.color.colorBlue700).setAccentId(R.color.colorBlue900);

            Color lightBlueColor = new Color(R.color.colorLightBlue600, R.color.colorLightBlue700).setAccentId(R.color.colorLightBlue900);
            Color cyanColor = new Color(R.color.colorCyan600, R.color.colorCyan700).setAccentId(R.color.colorCyan900);
            Color tealColor = new Color(R.color.colorTeal500, R.color.colorTeal700).setAccentId(R.color.colorTeal900);

            Color greenColor = new Color(R.color.colorGreen600, R.color.colorGreen700).setAccentId(R.color.colorGreen900);
            Color lightGreenColor = new Color(R.color.colorLightGreen600, R.color.colorLightGreen700).setAccentId(R.color.colorLightGreen900);
            Color limeColor = new Color(R.color.colorLime800, R.color.colorLime900).setAccentId(R.color.colorAccent);

            Color yellowColor = new Color(R.color.colorYellow600, R.color.colorYellow800).setAccentId(R.color.colorYellow900);
            Color amberColor = new Color(R.color.colorAmber600, R.color.colorAmber800).setAccentId(R.color.colorAmber900);
            Color orangeColor = new Color(R.color.colorOrange600, R.color.colorOrange800).setAccentId(R.color.colorOrange900);

            Color deepOrangeColor = new Color(R.color.colorDeepOrange600, R.color.colorDeepOrange800).setAccentId(R.color.colorDeepOrange900);
            Color brownColor = new Color(R.color.colorBrown600, R.color.colorBrown800).setAccentId(R.color.colorBrown900);
            Color greyColor = new Color(R.color.colorGrey600, R.color.colorGrey800).setAccentId(R.color.colorGrey900);

            Color blueGreyColor = new Color(R.color.colorBlueGrey600, R.color.colorBlueGrey800).setAccentId(R.color.colorBlueGrey900);

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
        return ContextCompat.getColor(context, color.getPrimaryId());
    }


    public static int getRandColor(Context context, int position) {
        Color color = getRandColor(position);
        return ContextCompat.getColor(context, color.getPrimaryDarkId());
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

    public static int lighter(int color, float factor) {
        int red = (int)(((float) android.graphics.Color.red(color) * (1.0F - factor) / 255.0F + factor) * 255.0F);
        int green = (int)(((float) android.graphics.Color.green(color) * (1.0F - factor) / 255.0F + factor) * 255.0F);
        int blue = (int)(((float) android.graphics.Color.blue(color) * (1.0F - factor) / 255.0F + factor) * 255.0F);
        return android.graphics.Color.argb(android.graphics.Color.alpha(color), red, green, blue);
    }

    public static int lighter(ColorStateList color, float factor) {
        return lighter(color.getDefaultColor(), factor);
    }

    public static int alpha(int color, int alpha) {
        return android.graphics.Color.argb(alpha, android.graphics.Color.red(color), android.graphics.Color.green(color), android.graphics.Color.blue(color));
    }

    public static boolean isColorDark(int color) {
        double darkness = 1.0D - (0.2126D * (double) android.graphics.Color.red(color) + 0.7152D * (double) android.graphics.Color.green(color) + 0.0722D * (double) android.graphics.Color.blue(color)) / 255.0D;
        return darkness >= 0.5D;
    }

    public static int getThemeAccentColor(Context context) {
        TypedValue value = new TypedValue();
        context.getTheme().resolveAttribute(com.pchmn.materialchips.R.attr.colorAccent, value, true);
        return value.data;
    }
}
