package com.tspolice.echallan.multiselection;

import java.util.ArrayList;

public interface ViolationsSelectCallbackListener {

    void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String data);

    void onCancel();
}
