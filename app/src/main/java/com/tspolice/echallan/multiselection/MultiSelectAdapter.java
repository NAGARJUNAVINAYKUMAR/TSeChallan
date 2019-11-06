package com.tspolice.echallan.multiselection;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tspolice.echallan.R;

import java.util.ArrayList;


public class MultiSelectAdapter extends RecyclerView.Adapter<MultiSelectAdapter.DialogViewHolder> {

    private ArrayList<ViolationsModel> violationsArrayList;
    private Context mContext;
    private String mSearchQuery = "";

    MultiSelectAdapter(ArrayList<ViolationsModel> mList, Context context) {
        violationsArrayList = mList;
        this.mContext = context;
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {

        private AppCompatTextView dialog_name_item;
        private AppCompatCheckBox dialog_item_checkbox;
        private RelativeLayout rel_lyt_violation;

        DialogViewHolder(@NonNull View itemView) {
            super(itemView);
            dialog_name_item = itemView.findViewById(R.id.tv_dialog_violation_description);
            dialog_item_checkbox = itemView.findViewById(R.id.cb_dialog_violation_select);
            rel_lyt_violation = itemView.findViewById(R.id.rel_lyt_dialog_violation);
        }
    }

    @NonNull
    @Override
    public DialogViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_violations_select, viewGroup, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DialogViewHolder dialogViewHolder, int i) {
        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(i, dialogViewHolder.dialog_name_item);
        } else {
            dialogViewHolder.dialog_name_item.setText(violationsArrayList.get(i).getTotalViolation());
        }

        if (violationsArrayList.get(i).getSelected()) {
            if (!MultiSelectDialog.selectedIdsForCallback.contains(violationsArrayList.get(i).getId())) {
                MultiSelectDialog.selectedIdsForCallback.add(violationsArrayList.get(i).getId());
            }
        }

        if (checkForSelection(violationsArrayList.get(i).getId())) {
            dialogViewHolder.dialog_item_checkbox.setChecked(true);
        } else {
            dialogViewHolder.dialog_item_checkbox.setChecked(false);
        }

        /*holder.dialog_item_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(true);
                } else {
                    removeFromSelection(mDataSet.get(holder.getAdapterPosition()).getId());
                    holder.dialog_item_checkbox.setChecked(false);
                }
            }
        });*/

        dialogViewHolder.rel_lyt_violation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dialogViewHolder.dialog_item_checkbox.isChecked()) {
                    MultiSelectDialog.selectedIdsForCallback.add(violationsArrayList.get(dialogViewHolder.getAdapterPosition()).getId());
                    dialogViewHolder.dialog_item_checkbox.setChecked(true);
                    violationsArrayList.get(dialogViewHolder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(dialogViewHolder.getAdapterPosition());
                } else {
                    removeFromSelection(violationsArrayList.get(dialogViewHolder.getAdapterPosition()).getId());
                    dialogViewHolder.dialog_item_checkbox.setChecked(false);
                    violationsArrayList.get(dialogViewHolder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(dialogViewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return violationsArrayList.size();
    }

    private void setHighlightedText(int position, TextView textview) {
        String name = violationsArrayList.get(position).getTotalViolation();
        SpannableString str = new SpannableString(name);
        int endLength = name.toLowerCase().indexOf(mSearchQuery) + mSearchQuery.length();
        ColorStateList highlightedColor = new ColorStateList(new int[][]{new int[]{}},
                new int[]{ContextCompat.getColor(mContext, R.color.colorSearchHighlight)});
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(null,
                Typeface.NORMAL, -1, highlightedColor, null);
        str.setSpan(textAppearanceSpan, name.toLowerCase().indexOf(mSearchQuery), endLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textview.setText(str);
    }

    private boolean checkForSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < MultiSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(MultiSelectDialog.selectedIdsForCallback.get(i))) {
                MultiSelectDialog.selectedIdsForCallback.remove(i);
            }
        }
    }

    void setData(ArrayList<ViolationsModel> data, String query, MultiSelectAdapter adapter) {
        this.violationsArrayList = data;
        this.mSearchQuery = query;
        adapter.notifyDataSetChanged();
    }

    /*//get selected name string seperated by coma
    public String getDataString() {
        String data = "";
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                data = data + ", " + mDataSet.get(i).getName();
            }
        }
        if (data.length() > 0) {
            return data.substring(1);
        } else {
            return "";
        }
    }
    //get selected name ararylist
    public ArrayList<String> getSelectedNameList() {
        ArrayList<String> names = new ArrayList<>();
        for (int i = 0; i < mDataSet.size(); i++) {
            if (checkForSelection(mDataSet.get(i).getId())) {
                names.add(mDataSet.get(i).getName());
            }
        }
        //  return names.toArray(new String[names.size()]);
        return names;
    }*/
}
