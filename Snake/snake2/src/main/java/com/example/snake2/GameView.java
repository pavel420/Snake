package com.example.snake2;

import static com.example.snake2.GameActivity.setFPS;

import android.app.Notification;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.Random;

public class GameView extends View{

    public GameView(Context context) {
        super(context);
    }

    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GameView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public static Colors colorApple = new Colors(Color.RED);

    public static int mScore;

    private static final String TAG = "GameView";

    private static final int MAP_SIZE = 20;
    private static final int START_X = 5;
    private static final int START_Y = 10;


    private int score = -1;
    private int scoreApple=0;

    private final Point[][] mPoints = new Point[MAP_SIZE][MAP_SIZE];
    private final LinkedList<Point> mSnake = new LinkedList<>();
    private Direction mDir;

    private ScoreUpdatedListener mScoreUpdatedListener;

    private boolean mGameOver = false;

    private int mBoxSize;
    private int mBoxPadding;

    private final Paint mPaint = new Paint();

    public void init() {
        mBoxSize = getContext().getResources()
                .getDimensionPixelSize(R.dimen.game_size) / MAP_SIZE;
        mBoxPadding = mBoxSize / 10;
    }

    public void newGame() {
        mGameOver = false;
        mDir = Direction.RIGHT;
        initMap();
        updateScore();
    }

    public void setGameScoreUpdatedListener(ScoreUpdatedListener scoreUpdatedListener) {
        mScoreUpdatedListener = scoreUpdatedListener;
    }

    private void initMap() {
        for (int i = 0; i < MAP_SIZE; i++) {
            for (int j = 0; j < MAP_SIZE; j++) {
                mPoints[i][j] = new Point(j, i);
            }
        }
        mSnake.clear();
        for (int i = 0; i < 3; i++) {
            Point point = getPoint(START_X + i, START_Y);
            point.type = PointType.SNAKE;
            mSnake.addFirst(point);
        }
        randomApple();
    }

    private void randomApple() {
        Random random = new Random();
        int t;
        while (true) {
            Point point = getPoint(random.nextInt(MAP_SIZE),
                    random.nextInt(MAP_SIZE));
            if (point.type == PointType.EMPTY) {
                point.type = PointType.APPLE;
                t=random.nextInt(100);
                if(t<=70){
                    point.typeApple=TypeApple.FIRST;
                }
                if(t>70&&t<=90){
                    point.typeApple=TypeApple.SECOND;
                }
                if(t>90){
                    point.typeApple=TypeApple.THIRD;
                }
                break;
            }
        }
    }

    private Point getPoint(int x, int y) {
        return mPoints[y][x];
    }

    public void next() {
        Point first = mSnake.getFirst();
        Log.d(TAG, "first: " + first.x + " " + first.y);
        Point next = getNext(first);
        Log.d(TAG, "next: " + next.x + " " + next.y);

        switch (next.type) {
            case EMPTY:
                Log.d(TAG, "next: empty");
                next.type = PointType.SNAKE;
                mSnake.addFirst(next);
                mSnake.getLast().type = PointType.EMPTY;
                mSnake.removeLast();
                break;
            case APPLE:
                Log.d(TAG, "next: apple");
                next.type = PointType.SNAKE;
                mSnake.addFirst(next);
                randomApple();
                updateScore();
                break;
            case SNAKE:
                Log.d(TAG, "next: snake");
                mGameOver = true;
                break;
        }
    }

    public void updateScore() {
        if (mScoreUpdatedListener != null) {
            score++;
            score=score+scoreApple;
            mScoreUpdatedListener.onScoreUpdated(score);
            mScore=score;
            int record = Integer.parseInt(RecordActivity.readFile());
            if(record<score){
                RecordActivity.writeFile(score);
            }

        }
    }

    public void setDirection(Direction dir) {
        if ((dir == Direction.LEFT || dir == Direction.RIGHT) &&
                (mDir == Direction.LEFT || mDir == Direction.RIGHT)) {
            return;
        }
        if ((dir == Direction.UP || dir == Direction.DOWN) &&
                (mDir == Direction.UP || mDir == Direction.DOWN)) {
            return;
        }
        mDir = dir;
    }

    private Point getNext(Point point) {
        int x = point.x;
        int y = point.y;

        switch (mDir) {
            case UP:
                y = y == 0 ? MAP_SIZE - 1 : y - 1;
                break;
            case DOWN:
                y = y == MAP_SIZE - 1 ? 0 : y + 1;
                break;
            case LEFT:
                x = x == 0 ? MAP_SIZE - 1 : x - 1;
                break;
            case RIGHT:
                x = x == MAP_SIZE - 1 ? 0 : x + 1;
                break;
        }
        return getPoint(x, y);
    }

    public boolean isGameOver() {
        return mGameOver;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                int left = mBoxSize * x;
                int right = left + mBoxSize;
                int top = mBoxSize * y;
                int bottom = top + mBoxSize;
                switch (getPoint(x, y).type) {
                    case APPLE:
                        switch (getPoint(x, y).typeApple) {
                            case FIRST:
                                mPaint.setColor(Color.RED);
                                scoreApple=0;
                                break;
                            case SECOND:
                                mPaint.setColor(Color.YELLOW);
                                scoreApple=2;
                                break;
                            case THIRD:
                                mPaint.setColor(Color.WHITE);
                                scoreApple=6;
                                break;
                        }
                        break;
                    case SNAKE:
                        mPaint.setColor(Color.BLACK);
                        canvas.drawRect(left, top, right, bottom, mPaint);
                        mPaint.setColor(SkinActivity.getColors());
                        left += mBoxPadding;
                        right -= mBoxPadding;
                        top += mBoxPadding;
                        bottom -= mBoxPadding;
                        break;
                    case EMPTY:
                        mPaint.setColor(Color.BLACK);
                        break;
                }
                canvas.drawRect(left, top, right, bottom, mPaint);
            }
        }
    }


    public static int getScore(){
        return mScore;
    }
}