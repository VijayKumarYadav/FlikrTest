/**
 * 
 */
package com.example.flickrtest;

import java.util.List;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Data List Loader
 * @author VijayK
 *
 */
public class DataListLoader extends AsyncTaskLoader<List<Model>>{
    
    List<Model> models;
    String param;
    
    public DataListLoader(Context context) {
        super(context);
    }
    
    public DataListLoader(Context context,String param) {
        super(context);
        this.param = param;
    }
    

    @Override
    public List<Model> loadInBackground() {
        FlickrManager manager = new FlickrManager();
        return manager.getData(param);
    }
     
    /**
     * Called when there is new data to deliver to the client. 
     */
    @Override public void deliverResult(List<Model> listOfData) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (listOfData != null) {
                onReleaseResources(listOfData);
            }
        }
        List<Model> oldApps = listOfData;
        models = listOfData;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(listOfData);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldApps != null) {
            onReleaseResources(oldApps);
        }
    }

    /**
     * Handles a request to start the Loader.
     */
    @Override protected void onStartLoading() {
        if (models != null) {
            // If we currently have a result available, deliver it
            // immediately.
            deliverResult(models);
        }


        if (takeContentChanged() || models == null) {
            // If the data has changed since the last time it was loaded
            // or is not currently available, start a load.
            forceLoad();
        }
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override 
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    /**
     * Handles a request to cancel a load.
     */
    @Override 
    public void onCanceled(List<Model> apps) {
        super.onCanceled(apps);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(apps);
    }

    /**
     * Handles a request to completely reset the Loader.
     */
    @Override 
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        // At this point we can release the resources associated with 'apps'
        // if needed.
        if (models != null) {
            onReleaseResources(models);
            models = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(List<Model> apps) {}
     

}
