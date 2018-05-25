package rs.gecko.petar.owmapp.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.adapters.SavedLocationsAdapter;
import rs.gecko.petar.owmapp.data.LocalCache;
import rs.gecko.petar.owmapp.helpers.RecyclerItemTouchHelper;
import rs.gecko.petar.owmapp.models.MyLocation;

/**
 * Created by Petar on 5/23/18.
 */

public class SavedLocationsFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {

    private static final String TAG = SavedLocationsFragment.class.getSimpleName();

    private List<MyLocation> myLocations;
    private SavedLocationsAdapter adapter;

    RecyclerView locationsRecyclerView;

    private SearchView searchView;

    public SavedLocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedLocationsFragment.
     */
    public static SavedLocationsFragment newInstance() {
        SavedLocationsFragment fragment = new SavedLocationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_savedlocations, container, false);
        locationsRecyclerView = (RecyclerView) v.findViewById(R.id.saved_locations_rv);

        getActivity().setTitle(getString(R.string.app_name));

        // adding item touch helper
        // only ItemTouchHelper.LEFT added to detect Right to Left swipe
        // if you want both Right -> Left and Left -> Right
        // add pass ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT as param
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(locationsRecyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.UP) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(locationsRecyclerView);

        setHasOptionsMenu(true);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        // get my saved locations
        myLocations = LocalCache.getInstance(getActivity().getApplication()).getMyLocations();
    }

    @Override
    public void onResume() {
        super.onResume();
        
        if (myLocations != null) {
            // show recyclerView with locations
            refreshRecyclerView();
        }
    }

    public void refreshRecyclerView() {
        adapter = new SavedLocationsAdapter(myLocations, getActivity());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        locationsRecyclerView.setLayoutManager(mLayoutManager);
        locationsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        locationsRecyclerView.setAdapter(adapter);
        locationsRecyclerView.invalidate();
    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction, final int position) {
        if (viewHolder instanceof SavedLocationsAdapter.SavedLocationsViewHolder) {

            // get the removed item name to display it in snack bar
            String name = myLocations.get(viewHolder.getAdapterPosition()).getLocationName();

            // backup of removed item for undo purpose
            final MyLocation deletedItem = myLocations.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            adapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(getActivity().findViewById(R.id.coordinator_layout), name + " removed from bookmarked locations!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    adapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });



        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
