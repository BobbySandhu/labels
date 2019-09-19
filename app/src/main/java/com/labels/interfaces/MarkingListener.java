package com.labels.interfaces;

import com.labels.model.MarkingRect;

import java.util.ArrayList;

public interface MarkingListener {
    void onMarking(ArrayList<MarkingRect> markedRectsArray);
}
