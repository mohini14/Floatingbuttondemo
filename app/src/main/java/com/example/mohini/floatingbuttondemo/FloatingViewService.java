package com.example.mohini.floatingbuttondemo;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.Image;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by mohini on 04/01/18.
 */

public class FloatingViewService extends Service implements View.OnTouchListener {

    private WindowManager mWindowManager;
    private View mFloatingView;

   private View collapsedView = null;
    private View expandedView = null;

    float initialX;
    float initialY;
    float initialTouchX;
    float initialTouchY;

    public FloatingViewService(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        super.onCreate();
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        expandedView = mFloatingView.findViewById(R.id.expanded_container);
        // adding view to window
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        );

        // specify the view position
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        // add the view to window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);


        setCloseButton();
        setPlayButton();
        setNextButon();
        setPreviousButton();
        setCloseBtn();
        setOpenButton();

        handleTouch(params);
    }

    // set buttons
    private void setCloseButton(){


        // setting the closed button
        ImageView closedButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_button);

        // set on click listner
        closedButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close the service and remove the from from the window
                stopSelf();
            }
        });
    }

    private void setPlayButton(){

        // set the view while view is expanded

        ImageView playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                Toast.makeText(FloatingViewService.this, "Playing button expanded", Toast.LENGTH_LONG);
            }
        });
    }

    private void setNextButon(){

        ImageView nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Toast.makeText(FloatingViewService.this, "Playing next song.", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setPreviousButton(){

        //Set the pause button.
        ImageView prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing previous song.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setCloseBtn(){
        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

    }

    private void setOpenButton(){

        ImageView openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the application  click.
                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


                //close the service and remove view from the view hierarchy
                stopSelf();
            }
        });
    }
    @Override
    public void onDestroy(){

        super.onDestroy();
        if(mFloatingView != null) mWindowManager.removeView(mFloatingView);
    }

    private void handleTouch(final WindowManager.LayoutParams params){
    mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener(){


        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:


                    //remember the initial position.
                    initialX = params.x;
                    initialY = params.y;


                    //get the touch location
                    initialTouchX = event.getRawX();
                    initialTouchY = event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    //Calculate the X and Y coordinates of the view.
                    params.x = (int) (initialX + (int) (event.getRawX() - initialTouchX));
                    params.y = (int) (initialY + (int) (event.getRawY() - initialTouchY));


                    //Update the layout with new X & Y coordinate
                    mWindowManager.updateViewLayout(mFloatingView, params);
                    return true;

                case MotionEvent.ACTION_UP:

                    int Xdiff = (int) (event.getRawX() - initialTouchX);
                    int Ydiff = (int) (event.getRawY() - initialTouchY);

                    if(Xdiff < 10 && Ydiff < 10){

                        if(isViewCollapsed()){

                            collapsedView.setVisibility(View.GONE);
                            expandedView.setVisibility(View.VISIBLE);
                        }
                    }
            }
            return false;
        }
    });
}

        private boolean isViewCollapsed(){
            return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
        }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
