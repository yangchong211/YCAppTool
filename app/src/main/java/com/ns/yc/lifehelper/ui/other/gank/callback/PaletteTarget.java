package com.ns.yc.lifehelper.ui.other.gank.callback;

import android.support.v4.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;


public class PaletteTarget {

    @PicassoPalette.Profile.PaletteProfile
    protected int paletteProfile = PicassoPalette.Profile.VIBRANT;

    protected ArrayList<Pair<View, Integer>> targetsBackground = new ArrayList<>();
    protected ArrayList<Pair<TextView, Integer>> targetsText = new ArrayList<>();

    public PaletteTarget(@PicassoPalette.Profile.PaletteProfile int paletteProfile) {
        this.paletteProfile = paletteProfile;
    }

    public void clear(){
        targetsBackground.clear();
        targetsText.clear();

        targetsBackground = null;
        targetsText = null;
    }
}
