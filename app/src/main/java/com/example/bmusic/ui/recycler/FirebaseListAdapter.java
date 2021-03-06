


package com.example.bmusic.ui.recycler;

import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;

import com.example.bmusic.service.MediaPlaybackService;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * @author greg
 * @since 6/21/13
 *
 * This class is a generic way of backing an Android ListView with a Firebase location.
 * It handles all of the child events at the given Firebase location. It marshals received data into the given
 * class type. Extend this class and provide an implementation of <code>populateView</code>, which will be given an
 * instance of your list item mLayout and an instance your class that holds your data. Simply populate the view however
 * you like and this class will handle updating the list as the data changes.
 *
 * @param <T> The class type to use as a model for the data contained in the children of the given Firebase location
 */
public abstract class FirebaseListAdapter<T extends RecyclerData> extends BaseRecyclerAdapter<T> {

    private final Query mRef;
    private final Class<T> mModelClass;
//    private final int mLayout;
//    private final LayoutInflater mInflater;
    private final List<T> mModels;
    private final List<String> mKeys;
    private final ChildEventListener mListener;


    /**
     * @param mRef        The Firebase location to watch for data changes. Can also be a slice of a location, using some
     *                    combination of <code>limit()</code>, <code>startAt()</code>, and <code>endAt()</code>,
     * @param mModelClass Firebase will marshall the data at a location into an instance of a class that you provide
     */
    public FirebaseListAdapter(RecyclerActionListener actionListener, MediaPlaybackService service, Query mRef, Class<T> mModelClass) {
        super(actionListener, service);
        this.mRef = mRef;
        this.mModelClass = mModelClass;
//        this.mLayout = mLayout;
//        mInflater = LayoutInflater.from(context);
        mModels = new ArrayList<>();
        mKeys = new ArrayList<>();
        mRef.keepSynced(true);
        // Look for all child events. We will then map them to our own internal ArrayList, which backs ListView
        mListener = this.mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {

                T model = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                String key = dataSnapshot.getKey();

                Log.d("QuangNHe", "FirebaseListAdapter onChildAdded " + key + " | " + previousChildName + " | " + model);
                // Insert into the correct location, based on previousChildName
                if (previousChildName == null) {
                    mModels.add(0, model);
                    mKeys.add(0, key);
                    notifyItemInserted(0);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(model);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, model);
                        mKeys.add(nextIndex, key);
                    }
                    notifyItemInserted(nextIndex);
                }

                onContentChange();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // One of the mModels changed. Replace it in our list and name mapping
                String key = dataSnapshot.getKey();
                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);

                Log.d("QuangNHe", "FirebaseListAdapter onChildChanged " + key + " | " + newModel);
                mModels.set(index, newModel);

                onContentChange();
                notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                // A model was removed from the list. Remove it from our list and the name mapping
                String key = dataSnapshot.getKey();
                int index = mKeys.indexOf(key);

                Log.d("QuangNHe", "FirebaseListAdapter onChildRemoved " + key + " | " + dataSnapshot);
                mKeys.remove(index);
                mModels.remove(index);

                notifyItemRemoved(index);
                onContentChange();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

                // A model changed position in the list. Update our list accordingly
                String key = dataSnapshot.getKey();
                T newModel = dataSnapshot.getValue(FirebaseListAdapter.this.mModelClass);
                int index = mKeys.indexOf(key);
                Log.d("QuangNHe", "FirebaseListAdapter onChildMoved " + key + " | " + newModel);
                mModels.remove(index);
                mKeys.remove(index);
                if (previousChildName == null) {
                    mModels.add(0, newModel);
                    mKeys.add(0, key);
                    notifyItemInserted(0);
                } else {
                    int previousIndex = mKeys.indexOf(previousChildName);
                    int nextIndex = previousIndex + 1;
                    if (nextIndex == mModels.size()) {
                        mModels.add(newModel);
                        mKeys.add(key);
                    } else {
                        mModels.add(nextIndex, newModel);
                        mKeys.add(nextIndex, key);
                    }
                    notifyItemMoved(previousIndex, nextIndex);
                }
                onContentChange();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FirebaseListAdapter", "Listen was cancelled, no more updates will occur");
            }

        });
    }

    public void cleanup() {
        // We're being destroyed, let go of our mListener and forget about all of the mModels
        mRef.removeEventListener(mListener);
        mModels.clear();
        mKeys.clear();
    }

//    @Override
//    public int getCount() {
//        return mModels.size();
//    }
//
    @Nullable
    public String getKey(T model) {
        if (mModels.contains(model)) {
            return mKeys.get(mModels.indexOf(model));
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        if (view == null) {
//            view = mInflater.inflate(mLayout, viewGroup, false);
//        }
//
//        T model = mModels.get(i);
//        // Call out to subclass to marshall this model into the provided view
//        populateView(view, model);
//        return view;
//    }

    @CallSuper
    public void onContentChange() {
        update(mModels);
    }
    /**
     * Each time the data at the given Firebase location changes, this method will be called for each item that needs
     * to be displayed. The arguments correspond to the mLayout and mModelClass given to the constructor of this class.
     * <p/>
     * Your implementation should populate the view using the data contained in the model.
     *
     * @param v     The view to populate
     * @param model The object containing the data used to populate the view
     */
//    protected abstract void populateView(View v, T model);
}
