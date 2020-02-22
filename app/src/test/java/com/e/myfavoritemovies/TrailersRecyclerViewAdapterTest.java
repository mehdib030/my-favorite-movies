package com.e.myfavoritemovies;

import android.content.Context;
import android.view.LayoutInflater;

import com.e.myfavoritemovies.model.Trailer;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class TrailersRecyclerViewAdapterTest {

    private int mNumberOfItems;
    private List<Trailer> trailers;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private TrailersRecyclerViewAdapter.BtnClickListener mClickListener = null;

    @Test
    public void testGetItemCount(){

        TrailersRecyclerViewAdapter adapter = new TrailersRecyclerViewAdapter(context,trailers, 10, mClickListener);

        int itemCount = adapter.getItemCount();

        assertEquals("The get number of of items count failed",10,itemCount);

    }
}
