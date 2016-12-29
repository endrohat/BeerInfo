package com.indraneel.beerinfo.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

import com.indraneel.beerinfo.models.Beer;
import com.indraneel.beerinfo.R;

/**
 * Activity class to display details about a particular beer
 */
public class BeerDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beer_details);

        getSupportActionBar().setTitle(R.string.beer_details_activity_name);

        TextView beerNameView = (TextView) findViewById(R.id.beerName);
        TextView beerTagLineView = (TextView) findViewById(R.id.beerTagLine);
        TextView beerAbvView = (TextView) findViewById(R.id.abv);
        TextView beerIbuView = (TextView) findViewById(R.id.ibu);
        TextView beerEbcView = (TextView) findViewById(R.id.ebc);
        TextView beerDescriptionView = (TextView) findViewById(R.id.description);
        TextView beerFirstBrewedView = (TextView) findViewById(R.id.firstBrewed);
        TextView beerFoodPairingView = (TextView) findViewById(R.id.foodPairing);
        TextView beerBrewersTipsView = (TextView) findViewById(R.id.brewersTips);

        Bundle bundle = getIntent().getExtras();
        Beer beer = bundle.getParcelable("beerData");

        beerNameView.setText(beer.getName());
        beerTagLineView.setText(beer.getTagline());
        beerAbvView.setText(Double.toString(beer.getAbv()));
        beerIbuView.setText(Double.toString(beer.getIbu()));
        beerEbcView.setText(Double.toString(beer.getEbc()));
        beerDescriptionView.setText(beer.getDescription());
        beerFirstBrewedView.setText(beer.getFirstBrewed());
        beerBrewersTipsView.setText(beer.getBrewersTips());

        List<String> beerFoodPairingList = beer.getFoodPairing();
        if(beerFoodPairingList != null) {
            beerFoodPairingView.setMaxLines(beerFoodPairingList.size());
            String beerFoodPairings = "";
            for(String foodPairing : beerFoodPairingList) {
                beerFoodPairings += foodPairing + "\n";
            }
            beerFoodPairingView.setText(beerFoodPairings);
        }

    }

    protected boolean isChildActivity() {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
