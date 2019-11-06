package com.tspolice.echallan.models;

import com.tspolice.echallan.models.soap.PendingChallansModel;
import com.tspolice.echallan.multiselection.ViolationsModel;

import java.util.ArrayList;

public class GlobalModel {
    public static String challanType;
    public static ArrayList<PendingChallansModel> pendingChallansList;
    public static ArrayList<ViolationsModel> violationsArrayList;
}
