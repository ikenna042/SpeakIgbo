package com.example.android.speakigbo;


import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {

    //Handles media player
    private MediaPlayer mPlayer;

    //Handles audio focus when playing a sound file
    private AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener mAudioFocusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {

                public void onAudioFocusChange(int focusChange) {
                    if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                            focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
                        mPlayer.pause();
                        mPlayer.seekTo(0);
                    } else if(focusChange == AudioManager.AUDIOFOCUS_GAIN) {
                        //Resume playback
                    } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
                        releaseMediaPlayer();
                    }
                }
            };

    //When the music has finished, call the release method to release the media player
    //resources
    private MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            releaseMediaPlayer();
        }
    };

    /**
     * Clean up media player by releasing its resources.
     */
    @TargetApi(11)
    private void releaseMediaPlayer() {
        //If the media player is not null, then it may be playing a sound.
        if (mPlayer != null) {
            //Regardless of the current state of the media player, release its resources
            //because we no longer need it.
            mPlayer.release();

            //Set the media player to null, For our code, we've decided that
            //setting the media player to null is an easy way to tell that media player
            //is not configured to play an audio file at the moment.
            mPlayer = null;

            //Abandon audio focus when playback completed
            mAudioManager.abandonAudioFocus(mAudioFocusChangeListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        //When the activity is stopped, release the media player resources because we won't be
        //playing anymore sounds.
        releaseMediaPlayer();
    }

    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //Set up the audio manager
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Adding family Arraylist
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("father", "nna", R.drawable.family_father,
                R.raw.family_father));
        words.add(new Word("mother", "nne", R.drawable.family_mother,
                R.raw.family_mother));
        words.add(new Word("son", "nwa nwoke", R.drawable.family_son,
                R.raw.family_son));
        words.add(new Word("daughter", "nwa nwanyi",
                R.drawable.family_daughter, R.raw.family_daughter));
        words.add(new Word("sibling", "nwanne",
                R.drawable.family_older_brother, R.raw.family_sibling));
        words.add(new Word("brother", "nwanne nwoke",
                R.drawable.family_older_brother, R.raw.family_brother));
        words.add(new Word("sister", "nwanne nwanyi",
                R.drawable.family_younger_sister, R.raw.family_sister));
        words.add(new Word("grandchild", "nwa nwa",
                R.drawable.family_younger_brother, R.raw.family_grandchild));
        words.add(new Word("husband", "di", R.drawable.family_father,
                R.raw.family_husband));
        words.add(new Word("wife", "nwunye",
                R.drawable.family_older_sister, R.raw.family_wife));

        WordAdapter adapter = new WordAdapter(getActivity(),words, R.color.category_family);
        ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(adapter);

        //Set a click listener to play the audio file when the list item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            @TargetApi(11)
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                //Get the Object at the given position the user clicked on
                Word word = words.get(position);
                //release audio resources before initializing the media player
                releaseMediaPlayer();

                //Request Audio for playback
                int result = mAudioManager.requestAudioFocus(mAudioFocusChangeListener,
                        //Use the music stream.
                        AudioManager.STREAM_MUSIC,
                        //Request permanent focus.
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                if (result== AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                    //We have audio focus now

                    //Creates and sets up the media player for audio resource associated with the
                    // current word
                    mPlayer = MediaPlayer.create(getActivity(),
                            word.getAudioResourceId());
                    //Start the audio file
                    mPlayer.start();
                    //Set up a listener on the media, so that we can stop and release the media player
                    //once the sound has finished playing.
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

}
