package com.example.pc24.cbohelp.PartyView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.MotionEvent;
import android.view.View;

import com.example.pc24.cbohelp.R;

import static android.support.v7.widget.helper.ItemTouchHelper.ACTION_STATE_SWIPE;


enum ButtonsState {
    GONE,
    LEFT_VISIBLE,
    RIGHT_VISIBLE
}

public class SwipeController extends Callback {

   private boolean swipeBack = false;

   private ButtonsState buttonShowedState = ButtonsState.GONE;

   private RectF buttonInstance = null;

   private RecyclerView.ViewHolder currentItemViewHolder = null;

   private SwipeControllerActions buttonsActions = null;

   private static final float buttonWidth = 250;

   private Context context;

   public SwipeController(Context context,SwipeControllerActions buttonsActions) {
       this.context = context;
       this.buttonsActions = buttonsActions;
   }

   @Override
   public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
       return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
   }

   @Override
   public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
       return false;
   }

   @Override
   public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

   }

   @Override
   public int convertToAbsoluteDirection(int flags, int layoutDirection) {
       if (swipeBack) {
           swipeBack = buttonShowedState != ButtonsState.GONE;
           return 0;
       }
       return super.convertToAbsoluteDirection(flags, layoutDirection);
   }

   @Override
   public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
       if (actionState == ACTION_STATE_SWIPE) {
           if (buttonShowedState != ButtonsState.GONE) {
               if (buttonShowedState == ButtonsState.LEFT_VISIBLE) dX = Math.max(dX, buttonWidth);
               if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) dX = Math.min(dX, -2*buttonWidth);
               super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
           }
           else {
               setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
           }
       }

       if (buttonShowedState == ButtonsState.GONE) {
           super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
       }
       currentItemViewHolder = viewHolder;
   }

   private void setTouchListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
       recyclerView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               swipeBack = event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP;
               if (swipeBack) {
                   if (dX < -buttonWidth) buttonShowedState = ButtonsState.RIGHT_VISIBLE;
                   else if (dX > buttonWidth) buttonShowedState  = ButtonsState.LEFT_VISIBLE;

                   if (buttonShowedState != ButtonsState.GONE) {
                       setTouchDownListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                       setItemsClickable(recyclerView, false);
                   }
               }
               return false;
           }
       });
   }

   private void setTouchDownListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
       recyclerView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   setTouchUpListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
               }
               return false;
           }
       });
   }

   private void setTouchUpListener(final Canvas c, final RecyclerView recyclerView, final RecyclerView.ViewHolder viewHolder, final float dX, final float dY, final int actionState, final boolean isCurrentlyActive) {
       recyclerView.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if (event.getAction() == MotionEvent.ACTION_UP) {
                   SwipeController.super.onChildDraw(c, recyclerView, viewHolder, 0F, dY, actionState, isCurrentlyActive);
                   recyclerView.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View v, MotionEvent event) {
                           return false;
                       }
                   });
                   setItemsClickable(recyclerView, true);
                   swipeBack = false;

                   if (buttonsActions != null && buttonInstance != null && buttonInstance.contains(event.getX(), event.getY())) {
                       if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
                           buttonsActions.onLeftClicked(viewHolder.getAdapterPosition());
                       }
                       else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {

                           buttonsActions.onRightClicked(viewHolder.getAdapterPosition());
                       }
                   }
                   buttonShowedState = ButtonsState.GONE;
                   currentItemViewHolder = null;
               }
               return false;
           }
       });
   }

   private void setItemsClickable(RecyclerView recyclerView, boolean isClickable) {
       for (int i = 0; i < recyclerView.getChildCount(); ++i) {
           recyclerView.getChildAt(i).setClickable(isClickable);
       }
   }

   private void drawButtons(Canvas c, RecyclerView.ViewHolder viewHolder) {
       float buttonWidthWithoutPadding = buttonWidth -20;
       float corners = 16;
       Bitmap icon;


       View itemView = viewHolder.itemView;
       Paint p = new Paint();

//       RectF leftButton = new RectF(itemView.getLeft(), itemView.getTop(), itemView.getLeft() +      buttonWidthWithoutPadding, itemView.getBottom());
//       p.setColor(Color.BLUE);
//       c.drawRoundRect(leftButton, corners, corners, p);
//       drawText("EDIT", c, leftButton, p);
//     /*  icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone_call);
//    */
//       RectF rightButton = new RectF(itemView.getRight() - buttonWidthWithoutPadding +5, itemView.getTop(), itemView.getRight(), itemView.getBottom());
//       p.setColor(Color.RED);
//       c.drawRoundRect(rightButton, corners, corners, p);
//       drawText("DELETE",c,rightButton,p);
//
//
//       RectF secondbutton = new RectF(itemView.getRight() - (2*buttonWidthWithoutPadding) +5, itemView.getTop(), itemView.getRight() - buttonWidthWithoutPadding, itemView.getBottom());
//       p.setColor(Color.GREEN);
//       c.drawRoundRect(secondbutton, corners, corners, p);
//       drawText("CALL",c,secondbutton,p);

     /*  icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.phone_call);
       RectF icon_dest = new RectF((float) itemView.getRight() + buttonWidth -150,(float) itemView.getTop() + buttonWidth,(float) itemView.getLeft()+ (buttonWidth-220),(float)itemView.getBottom() - buttonWidth);
       c.drawBitmap(icon,null,icon_dest,p);*/


       buttonInstance = null;
       if (buttonShowedState == ButtonsState.LEFT_VISIBLE) {
          // buttonInstance = leftButton;
       }
       else if (buttonShowedState == ButtonsState.RIGHT_VISIBLE) {
          // buttonInstance = rightButton;
          // buttonInstance=secondbutton;
       }
   }

   private void drawText(String text, Canvas c, RectF button, Paint p) {
       float textSize = 40;
       p.setColor(Color.WHITE);
       p.setAntiAlias(true);
       p.setTextSize(textSize);

       float textWidth = p.measureText(text);
       c.drawText(text, button.centerX()-(textWidth/2), button.centerY()+(textSize/2), p);
   }

   public void onDraw(Canvas c) {
       if (currentItemViewHolder != null) {
           drawButtons(c, currentItemViewHolder);
       }
   }
}

