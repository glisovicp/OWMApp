package rs.gecko.petar.owmapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import rs.gecko.petar.owmapp.R;
import rs.gecko.petar.owmapp.models.MyLocation;

/**
 * Created by Petar on 5/24/18.
 */

public class SavedLocationsAdapter extends RecyclerView.Adapter<SavedLocationsAdapter.SavedLocationsViewHolder> implements Filterable {

    private static final String TAG = "SaveLocAdapter";
    private List<MyLocation> savedLocations;
    private List<MyLocation> filteredSavedLocations;
    public Context context;

    private SavedLocationFilter savedLocationFilter;

    public SavedLocationsAdapter(List<MyLocation> savedLocations, Context context) {
        this.savedLocations = savedLocations;
        this.context = context;
    }

    private SavedLocationsViewHolder createSavedLocationsViewHolder(View view) {
        return new SavedLocationsViewHolder(view);
    }

    @Override
    public SavedLocationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mylocation_list_row, parent, false);
        return createSavedLocationsViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return savedLocations.size();
    }

    @Override
    public Filter getFilter() {
        if (savedLocationFilter == null)
            savedLocationFilter = new SavedLocationFilter(this, savedLocations);
        return savedLocationFilter;
    }

    @Override
    public void onBindViewHolder(final SavedLocationsViewHolder holder, final int position) {
        holder.item = savedLocations.get(position);

        if (holder.item.getLocationName() != null && holder.item.getLocationName().trim().length() > 0) {
            holder.title.setText(holder.item.getLocationName().toString());
        }

        holder.view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

//        if (context instanceof MainActivity) {
//
//        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "Place: " + holder.item.getLocationName());
            }
        });


    }

    class SavedLocationsViewHolder extends RecyclerView.ViewHolder {
        public final View view;
        final TextView title;
        MyLocation item;

        SavedLocationsViewHolder(View view) {
            super(view);
            this.view = view;
            title = (TextView) view.findViewById(R.id.title);
        }

    }

    private static class SavedLocationFilter extends Filter {

        private final SavedLocationsAdapter adapter;

        private final List<MyLocation> originalList;

        private final List<MyLocation> filteredList;

        private SavedLocationFilter(SavedLocationsAdapter adapter, List<MyLocation> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new ArrayList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().trim();

                for (final MyLocation location : originalList) {
                    if (location.getLocationName().toLowerCase().contains(filterPattern) || location.getLocationName().toUpperCase().contains(filterPattern) || location.getLocationName().contains(filterPattern)) {
                        filteredList.add(location);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.savedLocations.clear();
            adapter.savedLocations.addAll((List<MyLocation>) results.values);
            adapter.notifyDataSetChanged();

        }
    }

}
