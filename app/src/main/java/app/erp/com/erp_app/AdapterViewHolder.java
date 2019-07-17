package app.erp.com.erp_app;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import app.erp.com.erp_app.vo.UnitList;

import static android.text.InputType.TYPE_CLASS_NUMBER;

/**
 * Created by hsra on 2019-06-24.
 */

public class AdapterViewHolder {

    public TextView index_text_name;
    public TextView area_text_name;
    public TextView unit_text_name;
    public TextView text_input;
    public TextView barcode_text_name;
    public CheckBox delete_check;

}
