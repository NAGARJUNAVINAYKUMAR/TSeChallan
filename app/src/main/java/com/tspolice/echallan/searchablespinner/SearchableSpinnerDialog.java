package com.tspolice.echallan.searchablespinner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.tspolice.echallan.R;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SearchableSpinnerDialog extends DialogFragment implements
        SearchView.OnQueryTextListener,
        SearchView.OnCloseListener {

    private static final String ITEMS = "items";
    private SearchableItem searchableItem;
    private OnSearchTextChanged onSearchTextChanged;
    private SearchView searchView;
    private ListView listView;
    private ArrayAdapter listAdapter;
    private DialogInterface.OnClickListener dialogOnClickListener;
    private String positiveButtonText, title;

    public SearchableSpinnerDialog() {
        // default constructor
    }

    public static SearchableSpinnerDialog newInstance(List items) {
        SearchableSpinnerDialog spinnerDialog = new SearchableSpinnerDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ITEMS, (Serializable) items);
        spinnerDialog.setArguments(bundle);
        return spinnerDialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog().getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        // Crash on orientation change #7
        // Change Start
        // Description: As the instance was re initializing to null on rotating the device,
        // getting the instance from the saved instance
        if (savedInstanceState != null) {
            searchableItem = (SearchableItem) savedInstanceState.getSerializable("item");
        }
        // Change End
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.dialog_searchable_spinner, null);
        setData(view);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton(positiveButtonText == null ? getResources().getString(R.string.close) : positiveButtonText,
                dialogOnClickListener);
        builder.setTitle(title == null ? getResources().getString(R.string.select_item) : title);
        final AlertDialog dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        return dialog;
    }

    private void setData(View view) {
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =  view.findViewById(R.id.searchView);
        listView =  view.findViewById(R.id.listView);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setOnCloseListener(this);
        searchView.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);

        List items = (List) getArguments().getSerializable(ITEMS);
        //create the adapter by passing your ArrayList data  and attach the adapter to the list
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(listAdapter);
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                searchableItem.onSearchableItemClicked(listAdapter.getItem(position), position);
                getDialog().dismiss();
            }
        });
    }

    // Crash on orientation change #7
    // Change Start
    // Description: Saving the instance of searchable item instance.
    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("item", searchableItem);
        super.onSaveInstanceState(outState);
    }
    // Change End

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        //listAdapter.filterData(s);
        if (TextUtils.isEmpty(newText)) {
            //listView.clearTextFilter();
            ((ArrayAdapter) listView.getAdapter()).getFilter().filter(null);
        } else {
            ((ArrayAdapter) listView.getAdapter()).getFilter().filter(newText);
        }
        if (null != onSearchTextChanged) {
            onSearchTextChanged.onSearchTextChanged(newText);
        }
        return true;
    }

    @Override
    public boolean onClose() {
        return false;
    }

    public interface SearchableItem<T> extends Serializable {
        void onSearchableItemClicked(T item, int position);
    }

    public interface OnSearchTextChanged {
        void onSearchTextChanged(String newText);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPositiveButton(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setPositiveButton(String positiveButtonText, DialogInterface.OnClickListener onClickListener) {
        this.positiveButtonText = positiveButtonText;
        this.dialogOnClickListener = onClickListener;
    }

    public void setOnSearchableItemClickListener(SearchableItem searchableItem) {
        this.searchableItem = searchableItem;
    }

    public void setOnSearchTextChangedListener(OnSearchTextChanged onSearchTextChanged) {
        this.onSearchTextChanged = onSearchTextChanged;
    }
}
