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
public class PhrasesFragment extends Fragment {

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
                    } else if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //Set up the audio manager
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Adding Phrases Arraylist
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("good morning", "ututu oma",
                R.raw.phrases_good_morning));
        words.add(new Word("good afternoon", "ehihie oma",
                R.raw.phrases_good_afternoon));
        words.add(new Word("what?", "gini?", R.raw.phrases_what));
        words.add(new Word("why", "maka gini?", R.raw.phrases_why));
        words.add(new Word("how", "kedu?", R.raw.phrases_how));
        words.add(new Word("when", "Kee mgbe o", R.raw.phrases_when));
        words.add(new Word("please", "biko", R.raw.phrases_please));
        words.add(new Word("go", "gaa", R.raw.phrases_go));
        words.add(new Word("come here", "bia ebe a",
                R.raw.phrases_come));
        words.add(new Word("my name is", "aha m bu",
                R.raw.phrases_my_name));

        WordAdapter adapter = new WordAdapter(getActivity(),words, R.color.category_phrases);
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
                    //current word
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
