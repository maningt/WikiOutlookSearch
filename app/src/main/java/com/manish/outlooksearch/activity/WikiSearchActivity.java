package com.manish.outlooksearch.activity;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.manish.outlooksearch.R;
import com.manish.outlooksearch.adapter.SearchSuggestionAdapter;
import com.manish.outlooksearch.callbacks.OnNextCallListerner;
import com.manish.outlooksearch.model.PageValue;
import com.manish.outlooksearch.model.SearchSuggestionResponse;
import com.manish.outlooksearch.networkclient.ApiService;
import com.manish.outlooksearch.networkclient.ResponseListener;
import com.manish.outlooksearch.networkclient.RetrofitClient;
import com.manish.outlooksearch.utils.Constants;
import com.manish.outlooksearch.utils.Utils;

import java.util.ArrayList;

/**
 * Created by manishdewan on 04/06/16.
 */
public class WikiSearchActivity extends AppCompatActivity implements OnNextCallListerner {
    private ImageButton crossIcon;
    private RecyclerView searchSuggestionList;
    private EditText editTextSearch;
    private ProgressBar progressBar;
    private TextView textViewNoResult;
    private String previousSearch = "";
    private final String TAG = WikiSearchActivity.class.getSimpleName();
    private Handler handler;
    private SearchSuggestionAdapter searchSuggestionAdapter;
    private ArrayList<PageValue> pageValues;
    private int offset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_search);
        init();
        initListener();
    }


    /**
     * initialize the listerners and listen the view's event callbacks
     */
    private void initListener() {
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.length() > 0) {
                    if (!previousSearch.equalsIgnoreCase(text)) {
                        removeDelayedCalls(TAG);
                        //Thread added to handler request queue which calls api
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                offset = 0;
                                if (searchSuggestionAdapter != null) {
                                    searchSuggestionAdapter.updateListView(null);
                                }
                                callSearchSuggestionApi(false);
                            }
                        };
                        //add request to queue and handles it with some delay as the user types in search edit text
                        handler.postAtTime(r, TAG, SystemClock.uptimeMillis() + Constants.DELAY_SEARCH_SUGGESTION);
                        previousSearch = text;
                    }
                } else {
                    //clear adapter data and list if no text to search
                    clearSearchData();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        crossIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCrossClick();
            }
        });
    }

    /**
     * handles the visibility of horizontal progress bar
     */
    private void setProgressBarVisibilty(boolean show) {
        if (show)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    /**
     * @param isNextCall true if api call is for pagination
     * Search api call with success and error callbacks using retrofit client
     */
    private void callSearchSuggestionApi(final boolean isNextCall) {
        setProgressBarVisibilty(!isNextCall);
        ApiService apiService = RetrofitClient.getInstance().createRequest(ApiService.class);
        apiService.getSearchSuggestions("query", "pageimages", "json", "thumbnail", "200", "50", "prefixsearch", previousSearch, offset + "", new ResponseListener<SearchSuggestionResponse>(this) {
            @Override
            public void onSuccess(SearchSuggestionResponse result) {
                setProgressBarVisibilty(false);
                if (editTextSearch.getText() == null || editTextSearch.getText().toString().trim().length() == 0) {
                    clearSearchData();
                    return;
                }
                if (result != null && result.getQuery() != null) {
                    if (result.getQuery().getPages().size() > 0) {
                        pageValues = new ArrayList<PageValue>(result.getQuery().getPages().values());
                        showSearchResultUI(true);
                        searchSuggestionAdapter.updateListView((pageValues));
                        searchSuggestionAdapter.updateOffsetCount(offset + Constants.MAX_SIZE_LIST);
                    }
                } else {
                    showSearchResultUI(false);
                }
            }

            @Override
            public void onError(String error) {
                setProgressBarVisibilty(false);
                Utils.showToast(WikiSearchActivity.this, error);
            }
        });
    }

    /**
     * handles the visibility of default text and RecycleView
     */
    private void showSearchResultUI(boolean isVisible) {
        if (isVisible) {
            textViewNoResult.setVisibility(View.GONE);
            searchSuggestionList.setVisibility(View.VISIBLE);
        } else {
            textViewNoResult.setVisibility(View.VISIBLE);
            searchSuggestionList.setVisibility(View.GONE);
        }
    }

    /**
     * clears the RecycleView adapter's data , notifies the list and shows default error text on UI
     */
    private void clearSearchData() {
        if (searchSuggestionAdapter != null) {
            previousSearch = "";
            setProgressBarVisibilty(false);
            removeDelayedCalls(TAG);
            searchSuggestionAdapter.updateListView(null);
            showSearchResultUI(false);
        }
    }


    /**
     * @param tag
     * remove all the pending requests from handler's request queue
     */
    private void removeDelayedCalls(String tag) {
        handler.removeCallbacksAndMessages(tag);
    }

    @Override
    protected void onStart() {
        super.onStart();
        showKeyboard();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideKeyboard();
    }

    //make keyboard visible and edittext focus is requested
    private void showKeyboard() {
        editTextSearch.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    //make keyboard hidden and edittext focus is cleared
    private void hideKeyboard() {
        editTextSearch.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    /**
     * init method which initialize all the UI components of this activity
     * and initialize the adapter and handler
     */
    private void init() {
        crossIcon = (ImageButton) findViewById(R.id.ic_searchCross);
        searchSuggestionList = (RecyclerView) findViewById(R.id.search_suggestion_list);
        editTextSearch = (EditText) findViewById(R.id.edit_text_search);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textViewNoResult = (TextView) findViewById(R.id.txt_view_no_result);
        handler = new Handler();
        pageValues = new ArrayList<>();
        //setRecyclerView
        searchSuggestionList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        searchSuggestionList.setLayoutManager(linearLayoutManager);
        searchSuggestionAdapter = new SearchSuggestionAdapter(this, pageValues);
        searchSuggestionAdapter.setOnNextCallListerner(this);
        searchSuggestionList.setAdapter(searchSuggestionAdapter);
    }

    //click event for cross icon
    void onCrossClick() {
        if (editTextSearch != null) {
            editTextSearch.setText("");
        }
    }

    /**
     * callback when next page is to be loaded
     * updated the offset and make api call with new offset
     */
    @Override
    public void onNext() {
        offset = offset + Constants.MAX_SIZE_LIST;
        if (offset < Constants.MAX_COUNT_RESULT) {
            callSearchSuggestionApi(true);
        } else {
            setProgressBarVisibilty(false);
        }
    }
}
