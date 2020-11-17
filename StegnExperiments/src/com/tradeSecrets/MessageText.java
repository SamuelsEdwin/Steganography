package com.tradeSecrets;

public class MessageText {

    public String text;
    public MessageText() {
        text = "Using MediaPlayer can be straightforward in principle. However, it's important to keep in mind that a few more things are necessary to integrate it correctly with a typical Android application. For example, the call to prepare() can take a long time to execute, because it might involve fetching and decoding media data. So, as is the case with any method that may take long to execute, you should never call it from your application's UI thread. Doing that causes the UI to hang until the method returns, which is a very bad user experience and can cause an ANR (Application Not Responding) error. Even if you expect your resource to load quickly, remember that anything that takes more than a tenth of a second to respond in the UI causes a noticeable pause and gives the user the impression that your application is slow.\n" +
                "\n" +
                "To avoid hanging your UI thread, spawn another thread to prepare the MediaPlayer and notify the main thread when done. However, while you could write the threading logic yourself, this pattern is so common when using MediaPlayer that the framework supplies a convenient way to accomplish this task by using the prepareAsync() method. This method starts preparing the media in the background and returns immediately. When the media is done preparing, the onPrepared() method of the MediaPlayer.OnPreparedListener, configured through setOnPreparedListener() is called.\n" +
                "\n" +
                "Managing state\n" +
                "Another aspect of a MediaPlayer that you should keep in mind is that it's state-based. That is, the MediaPlayer has an internal state that you must always be aware of when writing your code, because certain operations are only valid when then player is in specific states. If you perform an operation while in the wrong state, the system may throw an exception or cause other undesirable behaviors.\n" +
                "\n" +
                "The documentation in the MediaPlayer class shows a complete state diagram, that clarifies which methods move the MediaPlayer from one state to another. For example, when you create a new MediaPlayer, it is in the Idle state. At that point, you should initialize it by calling setDataSource(), bringing it to the Initialized state. After that, you have to prepare it using either the prepare() or prepareAsync() method. When the MediaPlayer is done preparing, it enters the Prepared state, which means you can call start() to make it play the media. At that point, as the diagram illustrates, you can move between the Started, Paused and PlaybackCompleted states by calling such methods as start(), pause(), and seekTo(), amongst others. When you call stop(), however, notice that you cannot call start() again until you prepare the MediaPlayer again.\n" +
                "\n" +
                "Always keep the state diagram in mind when writing code that interacts with a MediaPlayer object, because calling its methods from the wrong state is a common cause of bugs.";
    }
}
