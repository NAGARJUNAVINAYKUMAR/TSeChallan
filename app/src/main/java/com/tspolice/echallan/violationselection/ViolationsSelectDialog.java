package com.tspolice.echallan.violationselection;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tspolice.echallan.R;
import com.tspolice.echallan.multiselection.ViolationsModel;

import java.util.ArrayList;
import java.util.Objects;

public class ViolationsSelectDialog extends AppCompatDialogFragment implements
        SearchView.OnQueryTextListener,
        View.OnClickListener {

    AppCompatTextView violations_dialog_title;
    AppCompatButton btn_violations_dialog_cancel, btn_violations_dialog_done;

    public ArrayList<ViolationsModel> violationsList = new ArrayList<>();
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();
    private ArrayList<ViolationsModel> tempViolationsList = new ArrayList<>();
    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();
    public static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();
    private ViolationsRecyclerAdapter violationsRecyclerAdapter;

    private int minSelectionLimit = 1, maxSelectionLimit = 0;
    private String minSelectionMessage = null, maxSelectionMessage = null;

    private SubmitCallbackListener submitCallbackListener;

    public interface SubmitCallbackListener {
        void onSelected(ArrayList<Integer> selectedIds, ArrayList<String> selectedNames, String dataString);
        void onCancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.MultiSelectDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_violations_select);

        // recyclerView setup
        ViolationsCustomRecyclerView recyclerView = dialog.findViewById(R.id.violations_recycler_view);
        recyclerView.setEmptyView(dialog.findViewById(R.id.tv_no_items_to_display));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        violationsList = setCheckedIds(violationsList, previouslySelectedIdsList);
        violationsRecyclerAdapter = new ViolationsRecyclerAdapter(getContext(), violationsList);
        recyclerView.setAdapter(violationsRecyclerAdapter);

        // searchView setup
        SearchView searchView = dialog.findViewById(R.id.violations_search_view);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        violations_dialog_title = dialog.findViewById(R.id.violations_dialog_title);
        btn_violations_dialog_cancel = dialog.findViewById(R.id.btn_violations_dialog_cancel);
        btn_violations_dialog_done = dialog.findViewById(R.id.btn_violations_dialog_done);

        btn_violations_dialog_cancel.setOnClickListener(this);
        btn_violations_dialog_done.setOnClickListener(this);

        return dialog;
    }

    private ArrayList<ViolationsModel> setCheckedIds(
            ArrayList<ViolationsModel> violationsList, ArrayList<Integer> previouslySelectedIdsList) {
        for (int i = 0; i < violationsList.size(); i++) {
            violationsList.get(i).setSelected(false);
            for (int j = 0; j < previouslySelectedIdsList.size(); j++) {
                if (previouslySelectedIdsList.get(j) == violationsList.get(i).getId()) {
                    violationsList.get(i).setSelected(true);
                }
            }
        }
        return violationsList;
    }

    public ViolationsSelectDialog setMinSelectionLimit(int minSelectionLimit1) {
        this.minSelectionLimit = minSelectionLimit1;
        return this;
    }

    public ViolationsSelectDialog setMaxSelectionLimit(int maxSelectionLimit1) {
        this.maxSelectionLimit = maxSelectionLimit1;
        return this;
    }

    public ViolationsSelectDialog setMinSelectionMessage(String minSelectionMessage1) {
        this.minSelectionMessage = minSelectionMessage1;
        return this;
    }

    public ViolationsSelectDialog setMaxSelectionMessage(String maxSelectionMessage1) {
        this.maxSelectionMessage = maxSelectionMessage1;
        return this;
    }

    public ViolationsSelectDialog setViolationsList(ArrayList<ViolationsModel> violationsList1) {
        this.violationsList = violationsList1;
        this.tempViolationsList = new ArrayList<>(this.violationsList);
        if (maxSelectionLimit == 0) {
            maxSelectionLimit = violationsList1.size();
        }
        return this;
    }

    public ViolationsSelectDialog setPreviouslySelectedIdsList(ArrayList<Integer> previouslySelectedIdsList1) {
        this.previouslySelectedIdsList = previouslySelectedIdsList1;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(this.previouslySelectedIdsList);
        return this;
    }

    public void onSubmit(@NonNull SubmitCallbackListener callback) {
        this.submitCallbackListener = callback;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        violationsList = setCheckedIds(violationsList, selectedIdsForCallback);
        ArrayList<ViolationsModel> filteredList = filter(violationsList, newText);
        violationsRecyclerAdapter.setData(filteredList, newText.toLowerCase(), violationsRecyclerAdapter);
        return false;
    }

    private ArrayList<ViolationsModel> filter(ArrayList<ViolationsModel> violationsMainArrayList, String query) {
        query = query.toLowerCase();
        final ArrayList<ViolationsModel> filteredArrayList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredArrayList.addAll(violationsMainArrayList);
            return filteredArrayList;
        }
        for (ViolationsModel model : violationsMainArrayList) {
            final String name = model.getTotalViolation().toLowerCase();
            if (name.contains(query)) {
                filteredArrayList.add(model);
            }
        }
        return filteredArrayList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_violations_dialog_done:
                ArrayList<Integer> callBackListOfIds = selectedIdsForCallback;
                if (callBackListOfIds.size() >= minSelectionLimit) {
                    if (callBackListOfIds.size() <= maxSelectionLimit) {
                        // to remember last selected ids which were successfully done
                        tempPreviouslySelectedIdsList = new ArrayList<>(callBackListOfIds);
                        if (submitCallbackListener != null) {
                            submitCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                        }
                        dismiss();
                    } else {
                        String youCan = getResources().getString(R.string.you_can_only_select_upto);
                        String options = getResources().getString(R.string.options);
                        String option = getResources().getString(R.string.option);
                        String message;
                        if (this.maxSelectionMessage != null) {
                            message = maxSelectionMessage;
                        } else {
                            if (maxSelectionLimit > 1) {
                                message = youCan + " " + maxSelectionLimit + " " + options;
                            } else {
                                message = youCan + " " + maxSelectionLimit + " " + option;
                            }
                        }
                        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    String pleaseSelect = getResources().getString(R.string.please_select_atleast);
                    String options = getResources().getString(R.string.options);
                    String option = getResources().getString(R.string.option);
                    String message;
                    if (this.minSelectionMessage != null) {
                        message = minSelectionMessage;
                    } else {
                        if (minSelectionLimit > 1) {
                            message = pleaseSelect + " " + minSelectionLimit + " " + options;
                        } else {
                            message = pleaseSelect + " " + minSelectionLimit + " " + option;
                        }
                    }
                    Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_violations_dialog_cancel:
                if (submitCallbackListener != null) {
                    selectedIdsForCallback.clear();
                    selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                    submitCallbackListener.onCancel();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < tempViolationsList.size(); i++) {
            if (checkForSelection(tempViolationsList.get(i).getId())) {
                names.add(tempViolationsList.get(i).getTotalViolation());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < ViolationsSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(ViolationsSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    private String getSelectedDataString() {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < tempViolationsList.size(); i++) {
            if (checkForSelection(tempViolationsList.get(i).getId())) {
                data.append(", ").append(tempViolationsList.get(i).getTotalViolation());
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }
}
