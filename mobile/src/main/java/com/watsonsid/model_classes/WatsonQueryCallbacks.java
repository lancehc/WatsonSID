package com.watsonsid.model_classes;

/**
 * Created by Daniel on 11/13/2014.
 */
public interface WatsonQueryCallbacks{
    void onPreExecute();
    void onProgressUpdate(int percent);
    void onCancelled();
    void onPostExecute(String json);
}
