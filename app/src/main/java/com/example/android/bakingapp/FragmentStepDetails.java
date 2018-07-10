package com.example.android.bakingapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingapp.Model.StepsModel;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

/**
 * Created by azza anter on 4/9/2018.
 */

public class FragmentStepDetails extends Fragment implements ExoPlayer.EventListener {
    TextView step_short_description, step_description;
    SimpleExoPlayerView videoPlayerView;
    ProgressBar pbBuffering;
    public static StepsModel stepModel;
    private SimpleExoPlayer mExoPlayer;
    private boolean playWhenReady;
    private PlaybackStateCompat.Builder mStateBuilder;
    private MediaSessionCompat mediaSessionCompat;
    private long currentVideoPosition;
    private String videoUrl;
    public ImageView imageView;

    public FragmentStepDetails() {

    }

    public static FragmentStepDetails newInstance(StepsModel step) {
        FragmentStepDetails fragment = new FragmentStepDetails();
        Bundle bundle = new Bundle();
        bundle.putParcelable("step", step);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        playWhenReady = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View RootView = inflater.inflate(R.layout.fragment_step_details, container, false);

        step_description = (TextView) RootView.findViewById(R.id.step_description);
        videoPlayerView = (SimpleExoPlayerView) RootView.findViewById(R.id.video_player_view);
        pbBuffering = (ProgressBar) RootView.findViewById(R.id.pb_buffering);
        imageView = (ImageView) RootView.findViewById(R.id.imageView2);
       imageView.setVisibility(View.INVISIBLE);

        stepModel = getArguments().getParcelable("step");
        if (stepModel != null) {
            step_description.setText(stepModel.getDescription());
            if ((stepModel.getVideoURL() == null) || (stepModel.getThumbnailURL() == null)) {
                videoPlayerView.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);

            } else {
                if (stepModel.getThumbnailURL() != null
                        && stepModel.getThumbnailURL().length() >= 3
                        && !com.example.android.bakingapp.TextUtils.getExtension(stepModel.getThumbnailURL()).equalsIgnoreCase("mp4")) {
                    // based on the reviewer comment i added " Picasso.with(getContext()).load(stepModel.getThumbnailURL());"
                     Picasso.with(getContext()).load(stepModel.getThumbnailURL());
                    imageView.setVisibility(View.VISIBLE);


                }

                if (stepModel.getVideoURL() != null && !stepModel.getVideoURL().equalsIgnoreCase("")) {
                    videoUrl = stepModel.getVideoURL();
                } else {
                    videoPlayerView.setVisibility(View.GONE);
                    imageView.setVisibility(View.VISIBLE);

                }
            }
        }
        return RootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.SDK_INT <= 23 || videoPlayerView == null) {
            initializePlayer();

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("CurrentPosition", currentVideoPosition);
    }

    @Override
    public void onPause() {
        super.onPause();

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        // intializing media session
        if (mediaSessionCompat == null) {
            mediaSessionCompat = new MediaSessionCompat(getActivity(), "Recipe");
            mediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
            mediaSessionCompat.setMediaButtonReceiver(null);
            mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                    PlaybackStateCompat.ACTION_PLAY |
                            PlaybackStateCompat.ACTION_PAUSE |
                            PlaybackStateCompat.ACTION_PLAY_PAUSE |
                            PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
            mediaSessionCompat.setPlaybackState(mStateBuilder.build());
            mediaSessionCompat.setCallback(new MediaSessionCompat.Callback() {
                @Override
                public void onPlay() {
                    playWhenReady = true;
                }

                @Override
                public void onPause() {
                    playWhenReady = false;
                }

                @Override
                public void onSkipToPrevious() {
                    mExoPlayer.seekTo(0);
                }
            });
            mediaSessionCompat.setActive(true);
        }


        // intializing video player
        if (mExoPlayer == null && videoUrl != null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            mExoPlayer.seekTo(currentVideoPosition);
            videoPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getActivity(), "Recipe");
            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl), new DefaultDataSourceFactory(
                    getActivity(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(playWhenReady);
        }
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            playWhenReady = mExoPlayer.getPlayWhenReady();
            currentVideoPosition = mExoPlayer.getCurrentPosition();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
            pbBuffering.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
            pbBuffering.setVisibility(View.GONE);
        } else if (playbackState == ExoPlayer.STATE_BUFFERING) {
            pbBuffering.setVisibility(View.VISIBLE);
        } else {
            pbBuffering.setVisibility(View.GONE);
        }

        // pass to mediaSession
        mediaSessionCompat.setPlaybackState(mStateBuilder.build());


    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
