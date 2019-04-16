package com.example.android.speakigbo;


public class Word {
    /** Default translation for the word */
    private String mDefaultTranslation;
    /** Igbo translation for the word */
    private String mIgboTranslation;
    /** Image resource ID for the word */
    private int mImageResourceId = NO_IMAGE_PROVIDED;

    private  static  final int NO_IMAGE_PROVIDED = -1;

    private int mAudioResourceId;

    public Word(String defaultTranslation, String igboTranslation, int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mIgboTranslation = igboTranslation;
        mAudioResourceId = audioResourceId;
    }
    public Word(String defaultTranslation, String igboTranslation, int imageResourceId,
                int audioResourceId) {
        mDefaultTranslation = defaultTranslation;
        mIgboTranslation = igboTranslation;
        mImageResourceId = imageResourceId;
        mAudioResourceId = audioResourceId;
    }

    public String getmDefaultTranslation() {
        return mDefaultTranslation;
    }

    public  String getmIgboTranslation() {
        return mIgboTranslation;
    }

    public int getmImageResourceId(){
        return mImageResourceId;
    }
    /**
     * Returns whether or not there is an image for this word.
     */
    public boolean hasImage(){return  mImageResourceId != NO_IMAGE_PROVIDED;}

    /**
     * Returns the audio resource id for each word
     * @return
     */
    public int getAudioResourceId() {return mAudioResourceId;}
}
