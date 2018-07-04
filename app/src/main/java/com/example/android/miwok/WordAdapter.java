package com.example.android.miwok;


import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;

/**
 * Created by IKENNA on 4/30/2018.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    /**Resource ID for backgroud color for this list of words*/
    private int mColorResourceId;

    public WordAdapter(Activity context, ArrayList<Word> words, int colorResourceId) {
        super(context, 0, words);
        mColorResourceId = colorResourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        //Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        //Find the TextView in the list_item.xml layout with the ID miwok_text_view
        TextView miwokTextView = (TextView) listItemView.findViewById(R.id.miwok_text_view);
        miwokTextView.setText(currentWord.getmIgboTranslation());

        //Find the TextView in the list_item.xml layout with the ID default_text_view
        TextView defaulTextView = (TextView) listItemView.findViewById(R.id.default_text_view);
        defaulTextView.setText(currentWord.getmDefaultTranslation());

        //Find the ImageView in the list_item.xml layout with the ID imageResourceId
        ImageView imageResourceId = (ImageView) listItemView.findViewById(R.id.image_view);

        if(currentWord.hasImage()) {
            //Set the ImageView to the image resource specified in the current Word
            imageResourceId.setImageResource(currentWord.getmImageResourceId());

            //Make sure the imageview is visible
            imageResourceId.setVisibility(View.VISIBLE);
        }
        else{
            imageResourceId.setVisibility(View.GONE);
        }

        //Set th theme color for the list item
        View textContainer = listItemView.findViewById(R.id.text_container);
        //Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResourceId);
        textContainer.setBackgroundColor(color);

        //Return th whole list item layout
        return listItemView;
    }
}
