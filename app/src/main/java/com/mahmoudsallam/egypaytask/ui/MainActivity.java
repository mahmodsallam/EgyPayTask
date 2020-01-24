package com.mahmoudsallam.egypaytask.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mahmoudsallam.egypaytask.data.FieldPojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainMvpView {

    private MainPresenter mPresenter;
    private ArrayList<View> viewArrayList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout parentLayout = buildParentView(this);
        try {
            viewArrayList = buildingViewsFromFieldsList(this);
            //adding views to layout
            for (int i = 0; i < viewArrayList.size(); i++) {
                parentLayout.addView(viewArrayList.get(i));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            parentLayout.addView(buildSubmitButton(this, viewArrayList, getFieldsList(loadJsonFromAssets(this))));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        setContentView(parentLayout);
        mPresenter = new MainPresenter(this);


    }

    //getting data from views
    private Map<String, String> getDataFromViews(ArrayList<View> viewList, ArrayList<FieldPojo> dataList) {
        Map<String, String> map = new HashMap<>();
        ArrayList<String> valueList = new ArrayList<>();
        for (int i = 0; i < viewList.size(); i++) {
            int id = viewList.get(i).getId();
            String value = null;
            for (int j = i; j < dataList.size(); j++) {
                FieldPojo pojo = dataList.get(j);
                if (Integer.valueOf(pojo.getId()) == id) {
                    if (pojo.getType().equals("string") || pojo.getType().equals("number") ||
                            pojo.getType().equals("textarea") || pojo.getType().equals("date")) {
                        EditText editText = (EditText) viewList.get(i);
                        value = editText.getText().toString();
                        valueList.add(value);
                        map.put(String.valueOf(id), value);
                        break;

                    }

                    if (pojo.getType().equals("select")) {
                        Spinner spinner = (Spinner) viewList.get(i);
                        value = spinner.getSelectedItem().toString();
                        valueList.add(value);
                        map.put(String.valueOf(id), value);
                        break;
                    }


                }

            }


        }
        return map;
    }

    // getting the fields list
    private ArrayList<FieldPojo> getFieldsList(String result) throws JSONException {
        ArrayList<FieldPojo> fieldsList = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(result);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject object = jsonArray.getJSONObject(i);
            String id = object.getString("id");
            String name = object.getString("name");
            String required = object.getString("required");
            String type = object.getString("type");
            String defaultValue = object.getString("default_value");
            String multiple = object.getString("multiple");
            String sort = object.getString("sort");

            fieldsList.add(new FieldPojo(id, name, required, type, defaultValue, multiple, sort));
        }
        fieldsList = sortBySortField(fieldsList);
        return fieldsList;
    }

    // this method is used for building views from fields list
    private ArrayList<View> buildingViewsFromFieldsList(Context context) throws JSONException {
        ArrayList<FieldPojo> fieldPojoArrayList = getFieldsList(loadJsonFromAssets(context));
        ArrayList<View> viewArrayList = new ArrayList<>();

        for (int i = 0; i < fieldPojoArrayList.size(); i++) {
            FieldPojo fieldPojo = fieldPojoArrayList.get(i);
            if (fieldPojo.getType().equals("string") || fieldPojo.getType().equals("number") ||
                    fieldPojo.getType().equals("textarea") || fieldPojo.getType().equals("date")) {
                EditText editText = new EditText(context);
                editText.setWidth(420);
                editText.setHint(fieldPojo.getName());
                if (fieldPojo.getType().equals("number"))
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                else if (fieldPojo.getType().equals("date"))
                    editText.setInputType(InputType.TYPE_CLASS_DATETIME);

                editText.setId(Integer.valueOf(fieldPojoArrayList.get(i).getId()));
                viewArrayList.add(editText);

            }

            if (fieldPojo.getType().equals("select") && fieldPojo.getMultiple() !=
                    null && fieldPojo.getMultiple() != "") {
                ArrayList<String> spinnerData = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(fieldPojo.getMultiple());
                for (int j = 0; j < jsonArray.length(); j++) {
                    JSONObject obj = jsonArray.getJSONObject(j);
                    spinnerData.add(obj.getString("value"));
                }
                Spinner spinner = new Spinner(context);
                buildSpinner(spinnerData, context, spinner);
                spinner.setId(Integer.valueOf(fieldPojoArrayList.get(i).getId()));
                viewArrayList.add(spinner);
            }
        }
        return viewArrayList;

    }

    //sorting
    private ArrayList<FieldPojo> sortBySortField(ArrayList<FieldPojo> fieldPojoArrayList) {
        Collections.sort(fieldPojoArrayList);
        return fieldPojoArrayList;
    }

    // building the parent layout that holds all the sub views
    private LinearLayout buildParentView(Context context) {
        LinearLayout mParent = new LinearLayout(context);
        LinearLayout.LayoutParams mParentParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mParent.setLayoutParams(mParentParams);
        mParent.setOrientation(LinearLayout.VERTICAL);
        return mParent;
    }

    //read the json file and return it as a string
    private String loadJsonFromAssets(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }


    //this method is used for building spinner
    public void buildSpinner(ArrayList<String> data, Context context, Spinner spinner) {
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, data);
        spinnerArrayAdapter.notifyDataSetChanged();
        spinner.setAdapter(spinnerArrayAdapter);
    }

    //build the submitButton and make the request
    private Button buildSubmitButton(Context context, final ArrayList<View> viewsList, final ArrayList<FieldPojo> pojoList) {
        Button button = new Button(context);
        button.setText("Submit");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.submitDataToApi(getDataFromViews(viewsList, pojoList));

            }
        });
        return button;

    }

    //show the dialog with the response from the api
    @Override
    public void showDialog(String response) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Response");
        alertDialogBuilder.setMessage(response);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("ok", null);
        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

}
