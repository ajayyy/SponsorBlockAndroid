package app.ajay.sponsorblock;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.app.Service;
import android.content.Intent;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;
import android.widget.CheckBox;

import java.util.List;

public class SkippingService extends AccessibilityService {

    // TODO: Stop using a hardcoded video ID
    static String videoID = "iWSu6U0Ujs8";

    // TODO: Stop using hardcoded sponsor times
    static float[][] sponsorTimes = {{5, 15}};

    /** In seconds */
    int lastVideoTime = -1;
    /** When was this info received */
    long lastVideoTimeUpdate = -1;

    boolean playing = false;

    public SkippingService() {

    }

    @Override
    public void onServiceConnected() {
        System.out.println("SERVICE STARTED");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

        // Update timestamp
        List<AccessibilityNodeInfo> watchTime =
                getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/time_bar_current_time");

        if (watchTime.size() > 0) {
            CharSequence rawTime = watchTime.get(0).getText();

            if (rawTime != null) {
                int newTime = getSecondsFromStringTime(rawTime.toString());

                if (newTime != lastVideoTime) {
                    lastVideoTime = newTime;
                    lastVideoTimeUpdate = event.getEventTime();
                }
            }
        }

        AccessibilityNodeInfo ani = event.getSource();

        if (ani != null) {

            if (ani.getViewIdResourceName() != null) {
                if (event.getEventType() == AccessibilityEvent.TYPE_VIEW_CLICKED &&
                        ani.getViewIdResourceName().equals("com.google.android.youtube:id/player_control_play_pause_replay_button") &&
                        event.getText().size() > 0) {

                    // TODO: default to playing when new video is loaded (right now it defaults to paused
                    if (event.getText().get(0).toString().equals("Play video")) {
                        playing = true;

                        List<AccessibilityNodeInfo> watchTimeList =
                                getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/time_bar_current_time");

                        if (watchTime.size() > 0) {
                            CharSequence rawTime = watchTime.get(0).getText();

                            if (rawTime != null) {
                                // Update time even if the same because it just starting playing again and
                                // everything is reset

                                lastVideoTime = getSecondsFromStringTime(rawTime.toString());
                                lastVideoTimeUpdate = event.getEventTime();
                            }
                        }

                    } else if (event.getText().get(0).toString().equals("Pause video")) {
                        playing = false;
                    }
                }
            }

            System.out.println("PLAYING: " + playing);
            System.out.println("lastVideoTime: " + lastVideoTime);
            System.out.println("lastVideoTimeUpdate: " + lastVideoTimeUpdate);

            //TODO: Remove testing logs
//            System.out.println("SOURCE " + ani.getViewIdResourceName());
//            System.out.println("TEXT " + ani.getText());
//
//            // Find highest parent
//            AccessibilityNodeInfo root = ani;
//            while (root.getParent() != null) {
//                root = root.getParent();
//            }
//
//            System.out.println("Event text: " + event.getText());
//
//            System.out.println("Event type: " + event.getEventType());
//
//            System.out.println("Event: " + event.toString());
//
//            System.out.println("Root children: " + getRootInActiveWindow().getChild(0).getChildCount());
//
//            System.out.println("Specific element: " + getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/time_bar_current_time"));
//
//            System.out.println("Video id element: " + getRootInActiveWindow().findAccessibilityNodeInfosByText("xfQBkdLa6fo"));
//
//            // video id element
//            //com.google.android.youtube:id/video_id
//            System.out.println("Video id: " + getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/video_id"));
//
//            System.out.println("Video id 2: " + ani.findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/video_id"));








//            System.out.println("URL: " + getRootInActiveWindow().findAccessibilityNodeInfosByText("https://youtu.be/EYPs-ya_GDA"));

            // TODO: Get videoID using the stats for nerds button

//            List<AccessibilityNodeInfo> statsForNerdsButtons = getRootInActiveWindow().findAccessibilityNodeInfosByText("Captions");
//
//            List<AccessibilityNodeInfo> moreButtons = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/player_overflow_button");
//
////            List<AccessibilityNodeInfo> shareButton = getRootInActiveWindow().findAccessibilityNodeInfosByViewId("com.google.android.youtube:id/player_share_button");
//
////            if (shareButton.size() > 0) {
//////                shareButton.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
////            }
//
//            System.out.println("nerds: " + statsForNerdsButtons);
//
//            System.out.println("more button: " + moreButtons);
//
//            if (moreButtons.size() > 0) {
////                System.out.println("nerds buttons children size: " + moreButtons.get(0).getChildCount());
////                for (int i = 0; i < moreButtons.get(0).getChildCount(); i++) {
////                    AccessibilityNodeInfo child = moreButtons.get(0).getChild(i);
////                    if (child.getViewIdResourceName() != null) {
////                        System.out.println(i + " " + child.getViewIdResourceName());
////                    }
////                }
//
//                moreButtons.get(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            }
//
//            {
//                GestureDescription.Builder builder = new GestureDescription.Builder();
//                Rect bounds = new Rect();
//
//                Path p = new Path();
//                p.moveTo(1000, 1000);
//
//                builder.addStroke(new GestureDescription.StrokeDescription(p, 50, 100L));
//                GestureDescription gesture = builder.build();
//                dispatchGesture(gesture, null, null);
//
//                dispatchGesture(gesture, new GestureResultCallback() {
//                    @Override
//                    public void onCompleted(GestureDescription gestureDescription) {
//                        super.onCompleted(gestureDescription);
//
//                    }
//
//                    @Override
//                    public void onCancelled(GestureDescription gestureDescription) {
//                        super.onCancelled(gestureDescription);
//                    }
//                }, null);
//            }
//
//            if (statsForNerdsButtons.size() > 0) {
//
//                System.out.println("nerds parent parent children: " + statsForNerdsButtons.get(0).getParent().getParent().getChildCount());
//                System.out.println("nerds parent parent: " + statsForNerdsButtons.get(0).getParent().getParent());
//                System.out.println("nerds parent: " + statsForNerdsButtons.get(0).getParent());
//
//
//                System.out.println("nerds children: " + statsForNerdsButtons.get(0).getChildCount());
//                System.out.println("nerds id: " + statsForNerdsButtons.get(0).getParent().getViewIdResourceName());
//
//                GestureDescription.Builder builder = new GestureDescription.Builder();
//                Rect bounds = new Rect();
//
//                statsForNerdsButtons.get(0).getBoundsInScreen(bounds);
//
//                Path p = new Path();
//                p.moveTo(1000, 1000);
//
//                builder.addStroke(new GestureDescription.StrokeDescription(p, 50, 100L));
//                GestureDescription gesture = builder.build();
//                dispatchGesture(gesture, null, null);
//
//                dispatchGesture(gesture, new GestureResultCallback() {
//                    @Override
//                    public void onCompleted(GestureDescription gestureDescription) {
//                        super.onCompleted(gestureDescription);
//
//                    }
//
//                    @Override
//                    public void onCancelled(GestureDescription gestureDescription) {
//                        super.onCancelled(gestureDescription);
//                    }
//                }, null);
//
////                Bundle args = new Bundle();
////                args.putInt(AccessibilityNodeInfo.ARG, 3);
//
////                statsForNerdsButtons.get(0).addChild(new CheckBox(this));
////                statsForNerdsButtons.get(0).performAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SHOW_ON_SCREEN);
//            }

//            System.out.println(ani.getWindow().getRoot());

//            for (int i = 0; i < moreButtons.getChildCount(); i++) {
//                AccessibilityNodeInfo child = moreButtons.getChild(i);
//                if (child.getText() != null) {
//                    System.out.println(i + " " + child.getText());
//                }
//            }

//            ani.refresh(); // to fix issue with viewIdResName = null on Android 6+
        }

//        System.out.println(getRootInActiveWindow().getViewIdResourceName());

//        AccessibilityWindowInfo root = getRootInActiveWindow().getWindow();
//
//        System.out.println(root.getChildCount());

//        for (int i = 0; i < root.getChildCount(); i++) {
//            AccessibilityWindowInfo child = root.getChild(i);
//            if (child.getText() != null) {
//                System.out.println(i + " " + child.getText());
//            }
//        }

    }

    /**
     * Gets the raw seconds from a time formated with MM:SS.
     *
     * @param time MM:SS
     * @return
     */
    public int getSecondsFromStringTime(String time) {
        String[] sections = time.split(":");

        int minutes = Integer.parseInt(sections[0]);
        int seconds = Integer.parseInt(sections[1]);

        return minutes * 60 + seconds;
    }

    @Override
    public void onInterrupt() {

    }
}
