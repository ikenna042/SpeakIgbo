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
public class NumbersFragment extends Fragment {

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

    @Override
    public void onStop() {
        super.onStop();

        //When the activity is stopped, release the media player resources because we won't be
        //playing anymore sounds.
        releaseMediaPlayer();
    }

    public NumbersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        //Set up the audio manager
        mAudioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        //Adding number Arraylist
        final ArrayList<Word> words = new ArrayList<>();
        words.add(new Word("one", "otu", R.drawable.number_one,
                R.raw.number_one));
        words.add(new Word("two", "abuo", R.drawable.number_two,
                R.raw.number_two));
        words.add(new Word("three", "ato", R.drawable.number_three,
                R.raw.number_three));
        words.add(new Word("four", "ano", R.drawable.number_four,
                R.raw.number_four));
        words.add(new Word("five", "ise", R.drawable.number_five,
                R.raw.number_five));
        words.add(new Word("six", "isii", R.drawable.number_six,
                R.raw.number_six));
        words.add(new Word("seven", "asaa", R.drawable.number_seven,
                R.raw.number_seven));
        words.add(new Word("eight", "asato", R.drawable.number_eight,
                R.raw.number_eight));
        words.add(new Word("nine", "itoolu", R.drawable.number_nine,
                R.raw.number_nine));
        words.add(new Word("ten", "iri", R.drawable.number_ten,
                R.raw.number_ten));

        WordAdapter adapter = new WordAdapter(getActivity(),words, R.color.category_numbers);
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

                    // Creates and sets up the media player for audio resource associated with the
                    //current word
                    mPlayer = MediaPlayer.create(getActivity(), word.getAudioResourceId());
                    //Start the audio file
                    mPlayer.start();
                    //Set up a listener on the media, so that we can stop and release the media
                    //player once the sound has finished playing.
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });
        return rootView;
    }

}
