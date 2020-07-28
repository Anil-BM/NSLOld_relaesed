package com.nsl.beejtantra.view;

/**
 * Saves the Service Location tv_managerName details
 * Created by arokalla on 1/6/2017.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.support.v7.widget.AppCompatSpinner;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.nsl.beejtantra.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MultiSelectionSpinner extends AppCompatSpinner implements
        OnMultiChoiceClickListener {

    private EditText commentsEt;
    private String comment = "";

    public interface OnMultipleItemsSelectedListener {
        void selectedIndices(List<Integer> indices);

        void selectedStrings(List<String> strings);
    }

    private OnMultipleItemsSelectedListener listener;

    String[] _items = new String[]{};
    boolean[] mSelection = null;
    boolean[] mSelectionAtStart = null;
    String _itemsAtStart = null;
    int itemWhich = -1;

    ArrayAdapter<String> simple_adapter;
    Context context;

    public MultiSelectionSpinner(Context context) {
        super(context);
        this.context = context;
        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        simple_adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void setListener(OnMultipleItemsSelectedListener listener) {
        this.listener = listener;
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            if (_items[which].equalsIgnoreCase("other") && commentsEt != null) {
                if (isChecked) {
                    this.itemWhich = which;
                    commentsEt.setVisibility(VISIBLE);
                } else {
                    this.itemWhich = -1;
                    comment = "";
                    commentsEt.setText("");
                    commentsEt.setVisibility(GONE);
                    mSelection[which] = isChecked;
                    simple_adapter.clear();
                    simple_adapter.add(buildSelectedItemString());
                }
            } else {
                mSelection[which] = isChecked;
                simple_adapter.clear();
                simple_adapter.add(buildSelectedItemString());
            }
        } else {
            throw new IllegalArgumentException(
                    "Argument 'itemWhich' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Please select");
        builder.setMultiChoiceItems(_items, mSelection, this);
        View view = inflate(context, R.layout.comments_text, null);
        builder.setView(view);
        commentsEt = view.findViewById(R.id.comments_et);
        _itemsAtStart = getSelectedItemsAsString();
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                System.arraycopy(mSelection, 0, mSelectionAtStart, 0, mSelection.length);
//                listener.selectedIndices(getSelectedIndices());
//                listener.selectedStrings(getSelectedStrings());
                if (itemWhich != -1 && commentsEt.getText().toString().trim().length() == 0) {
                    Toast.makeText(context, "Please Mention Other", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                simple_adapter.clear();
//                simple_adapter.add(_itemsAtStart);
//                System.arraycopy(mSelectionAtStart, 0, mSelection, 0, mSelectionAtStart.length);
            }
        });
        commentsEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0 && itemWhich != -1) {
                    comment = editable.toString().trim();
                    mSelection[itemWhich] = true;
                    simple_adapter.clear();
                    simple_adapter.add(buildSelectedItemString());
                }
            }
        });
        builder.show();
        return true;
    }


    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    public void setItems(String[] items) {
        _items = items;
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        // simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = false;
        mSelectionAtStart[0] = false;
    }

    public void setItems(List<String> items) {
        _items = items.toArray(new String[items.size()]);
        mSelection = new boolean[_items.length];
        mSelectionAtStart = new boolean[_items.length];
        simple_adapter.clear();
        //  simple_adapter.add(_items[0]);
        Arrays.fill(mSelection, false);
        mSelection[0] = false;
    }

    public void setSelection(String[] selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String cell : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(cell)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(List<String> selection) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (String sel : selection) {
            for (int j = 0; j < _items.length; ++j) {
                if (_items[j].equals(sel)) {
                    mSelection[j] = true;
                    mSelectionAtStart[j] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
            mSelectionAtStart[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(int[] selectedIndices) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
            mSelectionAtStart[i] = false;
        }
        for (int index : selectedIndices) {
            if (index >= 0 && index < mSelection.length) {
                mSelection[index] = true;
                mSelectionAtStart[index] = true;
            } else {
               /* throw new IllegalArgumentException("Index " + index
                        + " is out of bounds.");*/
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<String> getSelectedStrings() {
        List<String> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(_items[i]);
            }
        }
        return selection;
    }

    public String getOtherComments() {
        return commentsEt.getText().toString().trim();
    }

    public List<Integer> getSelectedIndices() {
        List<Integer> selection = new LinkedList<>();
        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                selection.add(i);
            }
        }
        return selection;
    }

    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items[i]);
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.length; ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;
                sb.append(_items[i]);
                if (_items[i].equalsIgnoreCase("other")) {
                    commentsEt.setVisibility(VISIBLE);
                    commentsEt.setText(comment);
                }
            } else {
                if (_items[i].equalsIgnoreCase("other"))
                    commentsEt.setVisibility(GONE);
            }
        }
        return sb.toString();
    }
}