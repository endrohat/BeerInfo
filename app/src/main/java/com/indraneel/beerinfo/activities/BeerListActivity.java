package com.indraneel.beerinfo.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.indraneel.beerinfo.models.Beer;
import com.indraneel.beerinfo.adapters.BeerListAdapter;
import com.indraneel.beerinfo.R;
import com.indraneel.beerinfo.dialogs.SortDialog;
import com.indraneel.beerinfo.utils.VolleySingleton;

/**
 * Activity class to display a list of beers
 */
public class BeerListActivity extends BaseActivity implements SortDialog.DialogItemClickListener
        , BeerListAdapter.OnItemClickListener {

    private RequestQueue mRequestQueue;
    private BeerListAdapter mAdapter;
    private int mCurrentSortIndex;
    private ProgressBar mProgressBar;
    private View mRetryContainer;
    private JsonArrayRequest mBeerRequest;

    private static final String BEER_API_URL = "https://api.punkapi.com/v2/beers";
    private static final String TAG = "BeerListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_list);

        if(savedInstanceState != null) {
            mCurrentSortIndex = savedInstanceState.getInt("currentSortIndex");
        }

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.beerList);

        getSupportActionBar().setTitle(R.string.beer_list_activity_name);

        mAdapter = new BeerListAdapter(LayoutInflater.from(this), this);
        recyclerView.setAdapter(mAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRetryContainer = findViewById(R.id.retryContainer);
        View retryButton = mRetryContainer.findViewById(R.id.retryButton);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retry();
            }
        });

        mRequestQueue = VolleySingleton.getInstance(this).getRequestQueue();
        // Request a JSON response
        mBeerRequest = new JsonArrayRequest(Request.Method.GET, BEER_API_URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseJSONResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(!fetchFromVolleyCache()) {
                    mRetryContainer.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    Log.e(TAG, error.getMessage());
                }
            }
        });

        mBeerRequest.setShouldCache(true);
        mRequestQueue.add(mBeerRequest);
    }

    private boolean fetchFromVolleyCache() {
        try {
            Cache cache = mRequestQueue.getCache();
            Cache.Entry entry = cache.get(BEER_API_URL);
            if(entry != null) {
                JSONArray cachedResponse = new JSONArray(new String(entry.data, "UTF-8"));
                parseJSONResponse(cachedResponse);
                return true;
            }
        } catch (UnsupportedEncodingException | JSONException e){
            mProgressBar.setVisibility(View.GONE);
            Log.e(TAG, e.getMessage());
        }

        return false;
    }

    private void retry() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRetryContainer.setVisibility(View.GONE);
        mRequestQueue.add(mBeerRequest);
    }

    private void parseJSONResponse (JSONArray response) {
        List<Beer> beerItems = new ArrayList<>();
        try {
            for (int i = 0; i < response.length(); i++) {

                JSONObject item = response.getJSONObject(i);
                Beer beerItem = new Beer();
                beerItem.setId(item.getInt("id"));
                beerItem.setName(item.getString("name"));
                beerItem.setTagline(item.getString("tagline"));
                String abvString = item.getString("abv");
                beerItem.setAbv(abvString.equals("null") ? 0.0 : Double.parseDouble(abvString));
                String ebcString = item.getString("ebc");
                beerItem.setEbc(ebcString.equals("null") ? 0.0 : Double.parseDouble(ebcString));
                String ibuString = item.getString("ibu");
                beerItem.setIbu(ibuString.equals("null")  ? 0.0 : Double.parseDouble(ibuString));
                beerItem.setDescription(item.getString("description"));
                beerItem.setFirstBrewed(item.getString("first_brewed"));
                JSONArray foodPairingJSON = item.getJSONArray("food_pairing");

                List<String> foodPairing = null;
                if(foodPairingJSON != null) {
                    foodPairing = new ArrayList<>();
                    for (int index = 0; index < foodPairingJSON.length(); index++) {
                        foodPairing.add(foodPairingJSON.getString(index));
                    }
                }
                beerItem.setFoodPairing(foodPairing);
                beerItem.setBrewersTips(item.getString("brewers_tips"));
                beerItems.add(beerItem);
            }

        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }

        mRetryContainer.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        mAdapter.setBeerItems(beerItems);
        sort(mCurrentSortIndex);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // cancel any pending network requests in the queue
        mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentSortIndex", mCurrentSortIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sort:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        DialogFragment dialogFragment = SortDialog.newInstance(
                R.string.sort_dialog_title, mCurrentSortIndex);
        dialogFragment.show(getSupportFragmentManager(), "sortDialog");
    }

    @Override
    public void onItemClick(DialogInterface dialogInterface, int index) {
        sort(index);
        mCurrentSortIndex = index;
    }

    private void sort(int index) {
        switch (index) {
            case 0: mAdapter.sort(BeerListAdapter.ABV_SORT_ASC); break;
            case 1: mAdapter.sort(BeerListAdapter.ABV_SORT_DESC); break;
            case 2: mAdapter.sort(BeerListAdapter.IBU_SORT_ASC); break;
            case 3: mAdapter.sort(BeerListAdapter.IBU_SORT_DESC); break;
            case 4: mAdapter.sort(BeerListAdapter.EBC_SORT_ASC); break;
            case 5: mAdapter.sort(BeerListAdapter.EBC_SORT_DESC); break;
        }
    }

    @Override
    public void onItemClick(View view, Beer beerItem) {
        Intent intent = new Intent(this, BeerDetailsActivity.class);
        intent.putExtra("beerData", beerItem);
        startActivity(intent);
    }
}
