package rs.gecko.petar.owmapp.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
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

    public SavedLocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SavedLocationsFragment.
     */
    public static SavedLocationsFragment newInstance(LocalCache localCache) {
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

        setSearchListener();

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

    private void setSearchListener() {

    }

    /**
     * callback when recycler view is swiped
     * item will be removed on swiped
     * undo option will be provided in snackbar to restore the item
     */
    @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder, final int direction, final int position) {
        if (viewHolder instanceof SavedLocationsAdapter.SavedLocationsViewHolder) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage(getString(R.string.dialog_message_delete_location));

            alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
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
            });

            alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    adapter.notifyDataSetChanged();
                }
            });

            Dialog d = alertDialog.show();
        }
    }
}
