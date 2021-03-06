package com.wordpress.commonplayground.view;

import android.arch.lifecycle.MutableLiveData;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.wordpress.commonplayground.R;
import com.wordpress.commonplayground.model.Validator;
import com.wordpress.commonplayground.network.GetSearchResultRequest;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnSearch;
    private Spinner type_spinner, genre_spinner;
    private String place;
    private TextInputEditText placeView;
    private boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        type_spinner = findViewById(R.id.type_spinner);
        genre_spinner = findViewById(R.id.genre_spinner);
        placeView = findViewById(R.id.PlaceInputField);

        btnSearch = findViewById(R.id.ButtonPublish);
        btnSearch.setOnClickListener(this);

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                updateView(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        });
    }

    private void updateView(int item) {
        Log.d("Selection", Integer.toString(item));
        if (item==0){
            placeView.setVisibility(View.GONE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.search_online_genres, android.R.layout.simple_spinner_dropdown_item);
            genre_spinner.setAdapter(adapter);
        }else{
            placeView.setVisibility(View.VISIBLE);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.search_offline_genres, android.R.layout.simple_spinner_dropdown_item);
            genre_spinner.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View view) {
        if (view.equals(btnSearch)) {
            resetErrors();
            if (placeView.getVisibility() != View.GONE) {
                checkForValidPlace();
            }
            if(!cancel) {
                findSessions();
            }
        }
    }

    private void resetErrors() {
        placeView.setError(null);
        cancel = false;
    }

    private void checkForValidPlace() {
        place = placeView.getText().toString();
        if(!checkForAnyInput(place)) {
            placeView.setError(getString(R.string.error_field_required));
            cancel = true;
        } else if (!Validator.checkForValidPlace(place)) {
            placeView.setError(getString(R.string.error_wrong_place));
            cancel = true;
        }
    }

    private boolean checkForAnyInput(String input) {
        return input.trim().length() > 0;
    }

    private String getUrl() {
        String type = type_spinner.getSelectedItem().toString();
        String api = "findSessions";
        String url = api + "?isOnline=" + type + "&genre=" + genre_spinner.getSelectedItemId();

        if ("Offline".equals(type)) {
            url += "&place=" + place;
        }
        return url;
    }

    private void findSessions() {
        GetSearchResultRequest request = new GetSearchResultRequest(this);
        request.getJSONRequest(getUrl(), "Sessions", this.getApplication(), new MutableLiveData<>());
    }
}