package com.stream.listener;

public interface ThreadEventListener {
    enum Event {
        MUSIC_STREAMING_STARTED,
        MUSIC_STREAMING_STOPPED,
        THREAD_STARTED,
        THREAD_STOPPED
    }
    void onThreadEvent(Thread thread, Event event);
}
