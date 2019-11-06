package com.tspolice.echallan.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.tspolice.echallan.R;
import com.tspolice.echallan.activities.SpotChallanActivity;
import com.tspolice.echallan.models.soap.PendingChallansModel;
import com.tspolice.echallan.utils.UiHelper;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private UiHelper mUiHelper;
    private ArrayList<PendingChallansModel> pendingChallansList;
    private ArrayList<Integer> selectedIds = new ArrayList<>();

    public RecyclerViewAdapter(Context context, ArrayList<PendingChallansModel> mList) {
        this.mContext = context;
        this.mUiHelper = new UiHelper(context);
        this.pendingChallansList = mList;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout rel_lyt_pend_challans;
        private AppCompatTextView tv_pend_challan_s_no, tv_pend_challan_eticket_no, tv_pend_challan_amount;
        private AppCompatImageView img_pend_challan_info;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);

            rel_lyt_pend_challans = itemView.findViewById(R.id.rel_lyt_pend_challans);

            tv_pend_challan_s_no = itemView.findViewById(R.id.tv_pend_challan_s_no);
            tv_pend_challan_eticket_no = itemView.findViewById(R.id.tv_pend_challan_eticket_no);
            tv_pend_challan_amount = itemView.findViewById(R.id.tv_pend_challan_amount);

            img_pend_challan_info = itemView.findViewById(R.id.img_pend_challan_info);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pending_challans2, viewGroup, false);
        return new RecyclerViewAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PendingChallansModel model = pendingChallansList.get(position);

        int id = model.getId();
        if (selectedIds.contains(id)) {
            holder.rel_lyt_pend_challans.setBackground(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorPrimaryTooLight)));
        } else {
            holder.rel_lyt_pend_challans.setBackground(new ColorDrawable(ContextCompat.getColor(mContext, R.color.colorWhite)));
        }

        holder.tv_pend_challan_s_no.setText((position + 1) + ".");
        holder.tv_pend_challan_eticket_no.setText(model.getChallanNo());
        holder.tv_pend_challan_amount.setText(mContext.getResources().getString(R.string.indian_rupee) + model.getTotalAmount());

        holder.img_pend_challan_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(mContext.getResources().getString(R.string.challan_information));
                builder.setMessage(model.getOffenceDescription());
                builder.setCancelable(true);
                builder.setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pendingChallansList.size();
    }

    public PendingChallansModel getItem(int position) {
        return pendingChallansList.get(position);
    }

    public void setSelectedIds(ArrayList<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }
}
