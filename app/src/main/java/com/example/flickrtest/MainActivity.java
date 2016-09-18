package com.example.flickrtest;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * Launcher Activity - Responsible for displaying UI in a Fragment
 * @author VijayK
 *
 */
public class MainActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// to give support on lower android version
		FragmentManager fragManager = getSupportFragmentManager();

		// Create the list fragment 
		if (fragManager.findFragmentById(android.R.id.content) == null) {
			DataListFragment list = new DataListFragment();
			fragManager.beginTransaction().add(android.R.id.content, list).commit();
		}
	}

	/**
	 * Class DataListFragment - A List Fragment for an Activity
	 *
	 */
	public static class DataListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Model>> {

		/** Adapter for List */
		CustomArrayAdapter adapter;
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Initially there is no data
			setEmptyText("No Data Available");

			// Create an empty adapter, use to display the loaded data.
			adapter = new CustomArrayAdapter(getActivity(), getListView());
			setListAdapter(adapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the mLoader. Either re-connect with an existing one,
			// or start a new one.
			getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onListItemClick(ListView listview, View v, int position, long id) {
			Log.i("DataListFragment", "Item clicked: " + id);
			Intent intent = new Intent(getActivity(),AuthorImagesList.class);
			intent.putExtra("authorName", adapter.getItem(position).getAuthorName());
			startActivity(intent);
		}

		@Override
		public Loader<List<Model>> onCreateLoader(int arg0, Bundle arg1) {
			return new DataListLoader(getActivity());
		}

		@Override
		public void onLoadFinished(Loader<List<Model>> arg0, List<Model> data) {
			adapter.setData(data);
			// The list should now be shown.
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Model>> arg0) {
			adapter.setData(null);
		}
	}
}
