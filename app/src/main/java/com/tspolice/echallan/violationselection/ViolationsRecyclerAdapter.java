package com.tspolice.echallan.violationselection;

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
import com.tspolice.echallan.multiselection.ViolationsModel;

import java.util.ArrayList;

public class ViolationsRecyclerAdapter extends RecyclerView.Adapter<ViolationsRecyclerAdapter.ViolationsViewHolder> {

    private ArrayList<ViolationsModel> violationsMainArrayList;
    private Context mContext;
    private String mSearchQuery = "";

    ViolationsRecyclerAdapter(Context context, ArrayList<ViolationsModel> violationsMainArrayList) {
        this.mContext = context;
        this.violationsMainArrayList = violationsMainArrayList;
    }

    class ViolationsViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rel_lyt_dialog_violation;
        private AppCompatCheckBox cb_dialog_violation_select;
        private AppCompatTextView tv_dialog_violation_description;

        ViolationsViewHolder(@NonNull View itemView) {
            super(itemView);
            rel_lyt_dialog_violation = itemView.findViewById(R.id.rel_lyt_dialog_violation);
            cb_dialog_violation_select = itemView.findViewById(R.id.cb_dialog_violation_select);
            tv_dialog_violation_description = itemView.findViewById(R.id.tv_dialog_violation_description);
        }
    }

    @NonNull
    @Override
    public ViolationsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_violations_select, viewGroup, false);
        return new ViolationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViolationsViewHolder holder, int i) {
        if (!mSearchQuery.equals("") && mSearchQuery.length() > 1) {
            setHighlightedText(i, holder.tv_dialog_violation_description);
        } else {
            holder.tv_dialog_violation_description.setText(violationsMainArrayList.get(i).getTotalViolation());
        }

        if (violationsMainArrayList.get(i).getSelected()) {
            if (!ViolationsSelectDialog.selectedIdsForCallback.contains(violationsMainArrayList.get(i).getId())) {
                ViolationsSelectDialog.selectedIdsForCallback.add(violationsMainArrayList.get(i).getId());
            }
        }

        if (checkForSelection(violationsMainArrayList.get(i).getId())) {
            holder.cb_dialog_violation_select.setChecked(true);
        } else {
            holder.cb_dialog_violation_select.setChecked(false);
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

        holder.rel_lyt_dialog_violation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!holder.cb_dialog_violation_select.isChecked()) {
                    ViolationsSelectDialog.selectedIdsForCallback.add(violationsMainArrayList.get(holder.getAdapterPosition()).getId());
                    holder.cb_dialog_violation_select.setChecked(true);
                    violationsMainArrayList.get(holder.getAdapterPosition()).setSelected(true);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    removeFromSelection(violationsMainArrayList.get(holder.getAdapterPosition()).getId());
                    holder.cb_dialog_violation_select.setChecked(false);
                    violationsMainArrayList.get(holder.getAdapterPosition()).setSelected(false);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return violationsMainArrayList.size();
    }

    private void setHighlightedText(int position, TextView textview) {
        String name = violationsMainArrayList.get(position).getTotalViolation();
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
        for (int i = 0; i < ViolationsSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(ViolationsSelectDialog.selectedIdsForCallback.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void removeFromSelection(Integer id) {
        for (int i = 0; i < ViolationsSelectDialog.selectedIdsForCallback.size(); i++) {
            if (id.equals(ViolationsSelectDialog.selectedIdsForCallback.get(i))) {
                ViolationsSelectDialog.selectedIdsForCallback.remove(i);
            }
        }
    }

    void setData(ArrayList<ViolationsModel> filteredList, String query, ViolationsRecyclerAdapter adapter) {
        this.violationsMainArrayList = filteredList;
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
