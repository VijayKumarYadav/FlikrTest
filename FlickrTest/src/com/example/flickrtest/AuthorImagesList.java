/**
 * 
 */
package com.example.flickrtest;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.ListView;

/**
 * Class to Show image List of an Owner
 * @author VijayK
 *
 */
public class AuthorImagesList extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.author_image_frag);
		// to give support on lower android version
		FragmentManager mFragManager = getSupportFragmentManager();

		// Create the list fragment 
		if (mFragManager.findFragmentById(R.id.list_fragment) == null) {
			AuthorImageListFragment listFrag = new AuthorImageListFragment();
			Bundle bundle = new Bundle();
			bundle.putString("data",getIntent().getStringExtra("authorName"));
			listFrag.setArguments(bundle);
			mFragManager.beginTransaction().add(R.id.list_fragment, listFrag).commit();
		}
	
		// back button handling
		findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
				
			}
		});
	}
	
	/**
	 * Author ImageList Fragment
	 *
	 */
	public static class AuthorImageListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<List<Model>> {

		CustomArrayAdapter mAdapter;
		
		Loader<List<Model>> loader;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// Initially there is no data
			setEmptyText("No Data Available");

			// Create an empty adapter, use to display the loaded data.
			mAdapter = new CustomArrayAdapter(getActivity(), getListView());
			setListAdapter(mAdapter);

			// Start out with a progress indicator.
			setListShown(false);

			// Prepare the loader. Either re-connect with an existing one,
			// or start a new one.
			loader = getLoaderManager().initLoader(0, null, this);
		}

		@Override
		public void onListItemClick(ListView listview, View v, int position, long id) {
			// do nothing
		}

		@Override
		public Loader<List<Model>> onCreateLoader(int arg0, Bundle arg1) {
			return new DataListLoader(getActivity(),getArguments().getString("data"));
		}

		@Override
		public void onLoadFinished(Loader<List<Model>> arg0, List<Model> data) {
			mAdapter.setData(data);
			// The list should now be shown.
			if (isResumed()) {
				setListShown(true);
			} else {
				setListShownNoAnimation(true);
			}
		}

		@Override
		public void onLoaderReset(Loader<List<Model>> arg0) {
			mAdapter.setData(null);
		}
	}
}
