package com.sophie.miller.portablecloset.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Colors {
    public final Map<Integer, String> colorsMap = new HashMap<Integer, String>() {{
        put(0, "White");
        put(1, "Black");
        put(2, "Blue");
        put(3,"Red");
        put(4,"Purple");
        put(5,"Pink");
        put(6,"Yellow");
        put(7,"Green");
        put(8,"Orange");
        put(9,"Brown");
        put(10,"Grey");
    }};
    public ArrayList<String> getAllColors() {
        ArrayList<String> colors = new ArrayList<>();
        for(int i = 0; i < colorsMap.size(); i++){
            colors.add(colorsMap.get(i));
        }
        return colors;
    }
}

