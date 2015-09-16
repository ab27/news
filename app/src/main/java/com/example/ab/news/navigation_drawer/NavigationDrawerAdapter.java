package com.example.ab.news.navigation_drawer;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ab.news.R;

import java.util.List;

/**
 * Created by poliveira on 24/10/2014.
 */
public class NavigationDrawerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NavigationItem> mData;
    private NavigationDrawerCallbacks mNavigationDrawerCallbacks;
    private int mSelectedPosition;
    private int mTouchedPosition = -1;

    public NavigationDrawerAdapter(List<NavigationItem> data) {
        mData = data;
    }

    public NavigationDrawerCallbacks getNavigationDrawerCallbacks() {
        return mNavigationDrawerCallbacks;
    }

    public void setNavigationDrawerCallbacks(NavigationDrawerCallbacks navigationDrawerCallbacks) {
        mNavigationDrawerCallbacks = navigationDrawerCallbacks;
    }

    // unclickable main menu
    public static class ViewHolder0 extends RecyclerView.ViewHolder {
        public TextView textView;


        public ViewHolder0(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    // clickable main menu
    public static class ViewHolder1 extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder1(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
        }
    }

    // submenu
    public static class ViewHolder2 extends RecyclerView.ViewHolder {
        public TextView textView;
        public ImageView imageView;

        public ViewHolder2(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.item_name);
            imageView = (ImageView) itemView.findViewById(R.id.listIcon);

        }
    }


    /**
     * Called when RecyclerView needs a new {@link NavigationDrawerAdapter.ViewHolder0}
     * of the given type to represent an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(RecyclerView.ViewHolder, int)}.
     * Since it will be re-used to display different items in the data set, it is a good idea
     * to cache references to sub views of the View to avoid unnecessary
     * {@link android.view.View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(RecyclerView.ViewHolder, int)
     */
     @Override
     public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         switch (viewType) {
             case 0: {
                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_title, parent, false);
                 return new ViewHolder0(v);
             }
             case 1: {
                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row_title, parent, false);
                 return new ViewHolder1(v);
             }
             case 2: {
                 View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_row, parent, false);
                 return new ViewHolder2(v);
             }
         }

         // should not reach here
         return null;
     }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolderx, final int position) {
//        viewHolder.textView.setText(mData.get(position).getText());
//        viewHolder.textView.setCompoundDrawablesWithIntrinsicBounds(
//                mData.get(position).getDrawable(), null, null, null);


        switch (getItemViewType(position)) {
            case 0:
                ViewHolder0 viewHolder = (ViewHolder0) viewHolderx;
                viewHolder.textView.setText(mData.get(position).getText());
                break;
            case 1:
                ViewHolder1 viewHolder1 = (ViewHolder1) viewHolderx;
                viewHolder1.textView.setText(mData.get(position).getText());
//                viewHolder1.textView.setCompoundDrawablesWithIntrinsicBounds(
//                        mData.get(position).getDrawable(), null, null, null);

                viewHolder1.itemView.setOnTouchListener(new View.OnTouchListener() {
                                                            @Override
                                                            public boolean onTouch(View v, MotionEvent event) {

                                                                switch (event.getAction()) {
                                                                    case MotionEvent.ACTION_DOWN:
                                                                        touchPosition(position);
                                                                        return false;
                                                                    case MotionEvent.ACTION_CANCEL:
                                                                        touchPosition(-1);
                                                                        return false;
                                                                    case MotionEvent.ACTION_MOVE:
                                                                        return false;
                                                                    case MotionEvent.ACTION_UP:
                                                                        touchPosition(-1);
                                                                        return false;
                                                                }
                                                                return true;
                                                            }
                                                        }
                );
                viewHolder1.itemView.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View v) {
                                                                if (mNavigationDrawerCallbacks != null)
                                                                    mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(position);
                                                            }
                                                        }
                );

                //TODO: selected menu position, change layout accordingly
                if (mSelectedPosition == position || mTouchedPosition == position) {
                    viewHolder1.itemView.setBackgroundColor(viewHolder1.itemView.getContext().getResources().getColor(R.color.selected_grey));
                } else {
                    viewHolder1.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                break;
            case 2:
                ViewHolder2 viewHolder2 = (ViewHolder2) viewHolderx;
                viewHolder2.textView.setText(mData.get(position).getText());
                viewHolder2.imageView.setImageDrawable(mData.get(position).getDrawable());

//                viewHolder2.textView.setCompoundDrawablesWithIntrinsicBounds(
//                        mData.get(position).getDrawable(), null, null, null);
//
//                viewHolder2.itemView.setOnTouchListener(new View.OnTouchListener() {
//                                                           @Override
//                                                           public boolean onTouch(View v, MotionEvent event) {
//
//                                                               switch (event.getAction()) {
//                                                                   case MotionEvent.ACTION_DOWN:
//                                                                       touchPosition(position);
//                                                                       return false;
//                                                                   case MotionEvent.ACTION_CANCEL:
//                                                                       touchPosition(-1);
//                                                                       return false;
//                                                                   case MotionEvent.ACTION_MOVE:
//                                                                       return false;
//                                                                   case MotionEvent.ACTION_UP:
//                                                                       touchPosition(-1);
//                                                                       return false;
//                                                               }
//                                                               return true;
//                                                           }
//                                                       }
//                );
                viewHolder2.itemView.setOnClickListener(new View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View v) {
                                                               if (mNavigationDrawerCallbacks != null)
                                                                   mNavigationDrawerCallbacks.onNavigationDrawerItemSelected(position);
                                                           }
                                                       }
                );



                //TODO: selected menu position, change layout accordingly
                if (mSelectedPosition == position || mTouchedPosition == position) {
                    viewHolder2.itemView.setBackgroundColor(viewHolder2.itemView.getContext().getResources().getColor(R.color.selected_grey));
                } else {
                    viewHolder2.itemView.setBackgroundColor(Color.TRANSPARENT);
                }
                break;

        }





    }

    private void touchPosition(int position) {
        int lastPosition = mTouchedPosition;
        mTouchedPosition = position;
        if (lastPosition >= 0)
            notifyItemChanged(lastPosition);
        if (position >= 0)
            notifyItemChanged(position);
    }

    public void selectPosition(int position) {
        int lastPosition = mSelectedPosition;
        mSelectedPosition = position;
        notifyItemChanged(lastPosition);
        notifyItemChanged(position);
    }

    /**
     * Return the view type of the item at <code>position</code> for the purposes
     * of view recycling.
     * <p/>
     * <p>The default implementation of this method returns 0, making the assumption of
     * a single view type for the adapter. Unlike ListView adapters, types need not
     * be contiguous. Consider using id resources to uniquely identify item view types.
     *
     * @param position position to query
     * @return integer value identifying the type of the view needed to represent the item at
     * <code>position</code>. Type codes need not be contiguous.
     */
    @Override
    public int getItemViewType(int position) {
        //return super.getItemViewType(position);

        // Just as an example, return 0 or 2 depending on position
        // Note that unlike in ListView adapters, types don't have to be contiguous

        return mData.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }


}
