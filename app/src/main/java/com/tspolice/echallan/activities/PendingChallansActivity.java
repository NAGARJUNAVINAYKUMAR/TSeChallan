package com.tspolice.echallan.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.tspolice.echallan.R;
import com.tspolice.echallan.adapters.RecyclerItemClickListener;
import com.tspolice.echallan.adapters.RecyclerViewAdapter;
import com.tspolice.echallan.models.GlobalModel;
import com.tspolice.echallan.models.soap.PendingChallansModel;

import java.util.ArrayList;

public class PendingChallansActivity extends AppCompatActivity implements ActionMode.Callback {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private ArrayList<PendingChallansModel> pendingChallansList;
    private ArrayList<Integer> selectedIds = new ArrayList<>();
    private ActionMode actionMode;
    private boolean isMultiSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_challans);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pendingChallansList = (ArrayList<PendingChallansModel>)
                    getIntent().getExtras().getSerializable("pendingChallansList");
        } else {
            pendingChallansList = GlobalModel.pendingChallansList;
        }

        recyclerViewSetup();

        adapter = new RecyclerViewAdapter(PendingChallansActivity.this, pendingChallansList);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(PendingChallansActivity.this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (isMultiSelect) {
                            // if multiple selection is enabled then select item on single click else perform normal click on item.
                            multiSelect(position);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        if (!isMultiSelect) {
                            selectedIds = new ArrayList<>();
                            isMultiSelect = true;
                            if (actionMode == null) {
                                actionMode = startActionMode(PendingChallansActivity.this); // show ActionMode.
                            }
                        }
                        multiSelect(position);
                    }
                }));
    }

    private void multiSelect(int position) {
        PendingChallansModel item = adapter.getItem(position);
        if (item != null) {
            if (actionMode != null) {
                if (selectedIds.contains(item.getId())) {
                    selectedIds.remove(Integer.valueOf(item.getId()));
                } else {
                    selectedIds.add(item.getId());
                }
                if (selectedIds.size() > 0) {
                    actionMode.setTitle(String.valueOf(selectedIds.size())); // show selected item count on action mode.
                } else {
                    actionMode.setTitle(""); // remove item count from action mode.
                    actionMode.finish(); // hide action mode.
                }
                adapter.setSelectedIds(selectedIds);
            }
        }
    }

    private void recyclerViewSetup() {
        recyclerView = findViewById(R.id.recycler_view_pending_challans);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setSelected(true);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.menu_multi_select, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.action_confirm_challans) { // just to show selected items.
            StringBuilder stringBuilder = new StringBuilder();
            for (PendingChallansModel model : pendingChallansList) {
                if (selectedIds.contains(model.getId())) {
                    stringBuilder.append(model.getChallanNo()).append(", ").append(model.getTotalAmount()).append("\n");
                }
            }
            if (actionMode != null) {
                actionMode.finish();
            }
            Intent intent = new Intent();
            intent.putExtra("selectedChallansData", stringBuilder.toString());
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
        isMultiSelect = false;
        selectedIds = new ArrayList<>();
        adapter.setSelectedIds(new ArrayList<Integer>());
    }
}
