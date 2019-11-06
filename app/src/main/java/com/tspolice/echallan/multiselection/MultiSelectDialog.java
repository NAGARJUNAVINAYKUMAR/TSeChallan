package com.tspolice.echallan.multiselection;

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
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.tspolice.echallan.R;

import java.util.ArrayList;
import java.util.Objects;

public class MultiSelectDialog extends AppCompatDialogFragment implements
        SearchView.OnQueryTextListener,
        View.OnClickListener {

    private AppCompatTextView multi_dialog_title;
    private AppCompatButton multi_dialog_cancel, multi_dialog_done;

    private ViolationsSelectCallbackListener selectCallbackListener;

    private String title, positiveText = "DONE", negativeText = "CANCEL", minSelectionMessage = null, maxSelectionMessage = null;
    private float titleTextSize = 25;
    private int minSelectionLimit = 1, maxSelectionLimit = 0;
    private MultiSelectAdapter multiSelectAdapter;
    public static ArrayList<Integer> selectedIdsForCallback = new ArrayList<>();
    public ArrayList<ViolationsModel> violationsArrayList = new ArrayList<>();
    private ArrayList<ViolationsModel> tempViolationsArrayList = new ArrayList<>();
    private ArrayList<Integer> previouslySelectedIdsList = new ArrayList<>();
    private ArrayList<Integer> tempPreviouslySelectedIdsList = new ArrayList<>();

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()), R.style.MultiSelectDialogTheme);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dialog.setContentView(R.layout.dialog_violations_select);

        multi_dialog_title = dialog.findViewById(R.id.violations_dialog_title);
        multi_dialog_cancel = dialog.findViewById(R.id.btn_violations_dialog_cancel);
        multi_dialog_done = dialog.findViewById(R.id.btn_violations_dialog_done);

        CustomRecyclerView recyclerView = dialog.findViewById(R.id.violations_recycler_view);
        recyclerView.setEmptyView(dialog.findViewById(R.id.tv_no_items_to_display));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        settingValues();

        violationsArrayList = setCheckedIds(violationsArrayList, previouslySelectedIdsList);
        multiSelectAdapter = new MultiSelectAdapter(violationsArrayList, getContext());
        recyclerView.setAdapter(multiSelectAdapter);

        SearchView searchView = dialog.findViewById(R.id.violations_search_view);
        searchView.setOnQueryTextListener(this);
        searchView.onActionViewExpanded();
        searchView.clearFocus();

        multi_dialog_cancel.setOnClickListener(this);
        multi_dialog_done.setOnClickListener(this);

        return dialog;
    }

    private void settingValues() {
        multi_dialog_title.setText(title);
        multi_dialog_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, titleTextSize);
        multi_dialog_title.setVisibility(View.GONE);
        multi_dialog_done.setText(positiveText.toUpperCase());
        multi_dialog_cancel.setText(negativeText.toUpperCase());
    }

    private ArrayList<ViolationsModel> setCheckedIds(
            ArrayList<ViolationsModel> violationsArrayList1, ArrayList<Integer> listOfIdsSelected) {
        for (int i = 0; i < violationsArrayList1.size(); i++) {
            violationsArrayList1.get(i).setSelected(false);
            for (int j = 0; j < listOfIdsSelected.size(); j++) {
                if (listOfIdsSelected.get(j).equals(violationsArrayList1.get(i).getId())) {
                    violationsArrayList1.get(i).setSelected(true);
                }
            }
        }
        return violationsArrayList1;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        selectedIdsForCallback = previouslySelectedIdsList;
        violationsArrayList = setCheckedIds(violationsArrayList, selectedIdsForCallback);
        ArrayList<ViolationsModel> filteredList = filter(violationsArrayList, newText);
        multiSelectAdapter.setData(filteredList, newText.toLowerCase(), multiSelectAdapter);
        return false;
    }

    private ArrayList<ViolationsModel> filter(ArrayList<ViolationsModel> list, String query) {
        query = query.toLowerCase();
        final ArrayList<ViolationsModel> filteredArrayList = new ArrayList<>();
        if (query.equals("") | query.isEmpty()) {
            filteredArrayList.addAll(list);
            return filteredArrayList;
        }
        for (ViolationsModel model : list) {
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
                        if (selectCallbackListener != null) {
                            selectCallbackListener.onSelected(callBackListOfIds, getSelectNameList(), getSelectedDataString());
                        }
                        dismiss();
                    } else {
                        String youCan = "You can only select up to ";
                        String options = "options";
                        String option = "option";
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
                    String pleaseSelect = "Please select at least";
                    String options = "options";
                    String option = "option";
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
                if (selectCallbackListener != null) {
                    selectedIdsForCallback.clear();
                    selectedIdsForCallback.addAll(tempPreviouslySelectedIdsList);
                    selectCallbackListener.onCancel();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private ArrayList<String> getSelectNameList() {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<tempViolationsArrayList.size();i++){
            if(checkForSelection(tempViolationsArrayList.get(i).getId())){
                names.add(tempViolationsArrayList.get(i).getTotalViolation());
            }
        }
        return names;
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    private String getSelectedDataString() {
        StringBuilder data = new StringBuilder();
        for (int i = 0; i < tempViolationsArrayList.size(); i++) {
            if (checkForSelection(tempViolationsArrayList.get(i).getId())) {
                data.append(", ").append(tempViolationsArrayList.get(i).getTotalViolation());
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }

    public MultiSelectDialog setTitle(String title) {
        this.title = title;
        return this;
    }

    public MultiSelectDialog setTitleTextSize(float titleTextSize) {
        this.titleTextSize = titleTextSize;
        return this;
    }

    public MultiSelectDialog setPositiveText(@NonNull String message) {
        this.positiveText = message;
        return this;
    }

    public MultiSelectDialog setNegativeText(@NonNull String message) {
        this.negativeText = message;
        return this;
    }

    public MultiSelectDialog setMinSelectionLimit(int limit) {
        this.minSelectionLimit = limit;
        return this;
    }

    public MultiSelectDialog setMinSelectionMessage(String message) {
        this.minSelectionMessage = message;
        return this;
    }

    public MultiSelectDialog setMaxSelectionLimit(int limit) {
        this.maxSelectionLimit = limit;
        return this;
    }

    public MultiSelectDialog setMaxSelectionMessage(String message) {
        this.maxSelectionMessage = message;
        return this;
    }

    public MultiSelectDialog setPreviouslySelectedIdsList(ArrayList<Integer> list) {
        this.previouslySelectedIdsList = list;
        this.tempPreviouslySelectedIdsList = new ArrayList<>(previouslySelectedIdsList);
        return this;
    }

    public MultiSelectDialog setMultiSelectList(ArrayList<ViolationsModel> list) {
        this.violationsArrayList = list;
        this.tempViolationsArrayList = new ArrayList<>(violationsArrayList);
        if (maxSelectionLimit == 0) {
            maxSelectionLimit = list.size();
        }
        return this;
    }

    public MultiSelectDialog onSubmit(@NonNull ViolationsSelectCallbackListener callback) {
        this.selectCallbackListener = callback;
        return this;
    }
}
