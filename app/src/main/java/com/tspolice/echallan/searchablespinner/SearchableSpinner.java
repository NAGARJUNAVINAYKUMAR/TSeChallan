package com.tspolice.echallan.searchablespinner;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.tspolice.echallan.R;

import java.util.ArrayList;
import java.util.List;

public class SearchableSpinner extends AppCompatSpinner implements
        View.OnTouchListener,
        SearchableSpinnerDialog.SearchableItem {

    public static final int NO_ITEM_SELECTED = -1;
    private Context mContext;
    private List items;
    private String hint;
    private boolean isDirty, isFromInit;
    private SearchableSpinnerDialog spinnerDialog;
    private ArrayAdapter arrayAdapter;

    public SearchableSpinner(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchableSpinner);
        final int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; ++i) {
            int attr = typedArray.getIndex(i);
            if (attr == R.styleable.SearchableSpinner_hintText) {
                hint = typedArray.getString(attr);
            }
        }
        typedArray.recycle();
        init();
    }

    public SearchableSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        items = new ArrayList();
        spinnerDialog = SearchableSpinnerDialog.newInstance(items);
        spinnerDialog.setOnSearchableItemClickListener(this);
        setOnTouchListener(this);
        arrayAdapter = (ArrayAdapter) getAdapter();
        if (!TextUtils.isEmpty(hint)) {
            ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, new String[]{hint});
            isFromInit = true;
            setAdapter(arrayAdapter);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (null != arrayAdapter) {
                // Refresh content #6
                // Change Start
                // Description: The items were only set initially, not reloading the data in the
                // spinner every time it is loaded with items in the adapter.
                items.clear();
                for (int i = 0; i < arrayAdapter.getCount(); i++) {
                    items.add(arrayAdapter.getItem(i));
                }
                // Change end.
                spinnerDialog.show(scanForActivity(mContext).getFragmentManager(), "TAG");
            }
        }
        return true;
    }

    /*The method just below is executed when an item in the search list is tapped.
    This is where we store the value int string called selectedItem.*/
    @Override
    public void onSearchableItemClicked(Object item, int position) {
        setSelection(items.indexOf(item));
        if (!isDirty) {
            isDirty = true;
            setAdapter(arrayAdapter);
            setSelection(items.indexOf(item));
        }
        //item = getItemAtPosition(position).toString();
        //Toast.makeText(getContext(), "You selected " + selectedItem, Toast.LENGTH_LONG).show();
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        if (!isFromInit) {
            arrayAdapter = (ArrayAdapter) adapter;
            if (!TextUtils.isEmpty(hint) && !isDirty) {
                ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, new String[]{hint});
                super.setAdapter(arrayAdapter);
            } else {
                super.setAdapter(adapter);
            }
        } else {
            isFromInit = false;
            super.setAdapter(adapter);
        }
    }

    private Activity scanForActivity(Context context) {
        if (context == null) {
            return null;
        } else if (context instanceof Activity) {
            return (Activity) context;
        } else if (context instanceof ContextWrapper) {
            return scanForActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }

    @Override
    public int getSelectedItemPosition() {
        if (!TextUtils.isEmpty(hint) && !isDirty) {
            return NO_ITEM_SELECTED;
        } else {
            return super.getSelectedItemPosition();
        }
    }

    @Override
    public Object getSelectedItem() {
        if (!TextUtils.isEmpty(hint) && !isDirty) {
            return null;
        } else {
            return super.getSelectedItem();
        }
    }
}
