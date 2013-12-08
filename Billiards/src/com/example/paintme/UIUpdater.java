package com.example.paintme;

import android.os.Handler;
import android.os.Looper;
/**
 * A class used to perform periodical updates,
 * specified inside a runnable object. An update interval
 * may be specified (otherwise, the class will perform the 
 * update every 2 seconds).
 * 
 * @author Carlos Sim�es
 * 
 * found at http://stackoverflow.com/questions/6242268/repeat-a-task-with-a-time-delay/6242292#6242292
 * on 10/27/2013
 */
public class UIUpdater {
        // Create a Handler that uses the Main Looper to run in
        private Handler mHandler = new Handler(Looper.getMainLooper());

        private Runnable mStatusChecker;
        private int UPDATE_INTERVAL = 20;

        /**
         * Creates an UIUpdater object, that can be used to
         * perform UIUpdates on a specified time interval.
         * 
         * @param uiUpdater A runnable containing the update routine.
         */
        public UIUpdater(final Runnable uiUpdater) {
            mStatusChecker = new Runnable() {
                @Override
                public void run() {
                    // Run the passed runnable
                    uiUpdater.run();
                    // Re-run it after the update interval
                    mHandler.postDelayed(this, UPDATE_INTERVAL);
                }
            };
        }
        
        /**
         * Starts the periodical update routine (mStatusChecker 
         * adds the callback to the handler).
         */
        public synchronized void startUpdates(){
            mStatusChecker.run();
        }

        /**
         * Stops the periodical update routine from running,
         * by removing the callback.
         */
        public synchronized void stopUpdates(){
            mHandler.removeCallbacks(mStatusChecker);
        }
}


