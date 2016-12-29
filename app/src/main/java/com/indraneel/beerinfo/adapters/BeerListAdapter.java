package com.indraneel.beerinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.indraneel.beerinfo.models.Beer;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.indraneel.beerinfo.R;

/**
 * Data adapter for beer list recyclerview
 */

public class BeerListAdapter extends RecyclerView.Adapter<BeerListAdapter.BeerItemHolder> {

    private List<Beer> mBeerItems;
    private final LayoutInflater mLayoutInflater;
    private final OnItemClickListener mOnItemClickListener;

    // out of the box comparators for beer sorting
    public static final Comparator<Beer> ABV_SORT_ASC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer1.getAbv() > beer2.getAbv() ? 1 : -1;
        }
    };

    public static final Comparator<Beer> ABV_SORT_DESC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer2.getAbv() > beer1.getAbv() ? 1 : -1;
        }
    };

    public static final Comparator<Beer> IBU_SORT_ASC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer1.getIbu() > beer2.getIbu() ? 1 : -1;
        }
    };

    public static final Comparator<Beer> IBU_SORT_DESC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer2.getIbu() > beer1.getIbu() ? 1 : -1;
        }
    };

    public static final Comparator<Beer> EBC_SORT_ASC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer1.getEbc() > beer2.getEbc() ? 1 : -1;
        }
    };

    public static final Comparator<Beer> EBC_SORT_DESC = new Comparator<Beer>() {
        @Override
        public int compare(Beer beer1, Beer beer2) {
            return beer2.getEbc() > beer1.getEbc() ? 1 : -1;
        }
    };

    public BeerListAdapter(LayoutInflater layoutInflater, OnItemClickListener onItemClickListener) {
        mLayoutInflater = layoutInflater;
        mOnItemClickListener = onItemClickListener;
    }

    /** sort the data list on the basis of comparator provided */
    public void sort(Comparator<Beer> comparator) {
        if(mBeerItems == null) {
            return;
        }
        Collections.sort(mBeerItems, comparator);
        notifyDataSetChanged();
    }

    public void setBeerItems(List<Beer> beerItems) {
        mBeerItems = beerItems;
    }

    @Override
    public BeerItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BeerItemHolder holder = new BeerItemHolder(mLayoutInflater.inflate(R.layout.beer_list_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(BeerItemHolder holder, int position) {
        Beer beerItem = mBeerItems.get(position);
        holder.mBeerNameView.setText(beerItem.getName());
        holder.mBeerTaglineView.setText(beerItem.getTagline());
        holder.mBeerAbvView.setText("Abv " + beerItem.getAbv());
        holder.mBeerIbuView.setText("Ibu " + beerItem.getIbu());
        holder.mBeerEbcView.setText("Ebc " + beerItem.getEbc());
    }

    @Override
    public int getItemCount() {
        if(mBeerItems==null) {
            return 0;
        }
        return mBeerItems.size();
    }

    protected class BeerItemHolder extends RecyclerView.ViewHolder {
        private TextView mBeerNameView;
        private TextView mBeerTaglineView;
        private TextView mBeerAbvView;
        private TextView mBeerIbuView;
        private TextView mBeerEbcView;

        BeerItemHolder (View itemView) {
            super(itemView);
            mBeerNameView = (TextView) itemView.findViewById(R.id.beerName);
            mBeerTaglineView = (TextView) itemView.findViewById(R.id.beerTagline);
            mBeerAbvView = (TextView) itemView.findViewById(R.id.abv);
            mBeerIbuView = (TextView) itemView.findViewById(R.id.ibu);
            mBeerEbcView = (TextView) itemView.findViewById(R.id.ebc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, mBeerItems.get(getAdapterPosition()));
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view , Beer beerItem);
    }
}
