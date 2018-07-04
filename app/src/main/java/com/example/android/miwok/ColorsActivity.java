package com.example.android.miwok;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);

        //Set up the audio manager
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //Adding colors Arraylist
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word("white", "ocha", R.drawable.color_white,
                R.raw.color_white));
        words.add(new Word("black", "oji", R.drawable.color_black,
                R.raw.color_black));
        words.add(new Word("red", "uhie", R.drawable.color_red,
                R.raw.color_red));
        words.add(new Word("yellow", "edo",
                R.drawable.color_mustard_yellow, R.raw.color_yellow));
        words.add(new Word("orange", "oroma",
                R.drawable.color_dusty_yellow, R.raw.color_orange));
        words.add(new Word("green", "akwukwo ndu",
                R.drawable.color_green, R.raw.color_green));
        words.add(new Word("brown", "uri", R.drawable.color_brown,
                R.raw.color_brown));
        words.add(new Word("gray", "ntu ntu", R.drawable.color_gray,
                R.raw.color_gray));

        WordAdapter adapter = new WordAdapter(this,words, R.color.category_colors);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        //Set a click listener to play the audio file when the list item is clicked
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
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
                    mPlayer = MediaPlayer.create(ColorsActivity.this,
                            word.getAudioResourceId());
                    //Start the audio file
                    mPlayer.start();
                    //Set up a listener on the media, so that we can stop and release the media player
                    //once the sound has finished playing.
                    mPlayer.setOnCompletionListener(mCompletionListener);
                }
            }
        });

    }

    /**
     * Clean up media player by releasing its resources.
     */
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

    /**
     * Method to clean up resources when user leaves the activity
     */
    @Override
    protected void onStop() {
        super.onStop();
        releaseMediaPlayer();
    }
}
