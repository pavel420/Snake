package com.example.snake2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.concurrent.atomic.AtomicInteger;

public class GameActivity extends AppCompatActivity {

    private static int FPS = 80;
    private static int delay;
    private static final int SPEED = 25;

    private static final int STATUS_PAUSED = 1;
    private static final int STATUS_START = 2;
    private static final int STATUS_OVER = 3;
    private static final int STATUS_PLAYING = 4;

    private static GameView mGameView;
    private static TextView mGameStatusText;
    private TextView mGameScoreText;
    private static Button mGameBtn;

    private static final AtomicInteger mGameStatus = new AtomicInteger(STATUS_START);

    private static final Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGameView = findViewById(R.id.game_view);
        mGameStatusText = findViewById(R.id.game_status);
        mGameBtn = findViewById(R.id.game_control_btn);
        mGameScoreText = findViewById(R.id.game_score);
        mGameView.init();
        mGameView.setGameScoreUpdatedListener(score -> {
            mHandler.post(() -> mGameScoreText.setText("Score: " + score));
        });

        findViewById(R.id.topBtn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.UP);
            }
        });
        findViewById(R.id.bottomBtn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.DOWN);
            }
        });
        findViewById(R.id.leftBtn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.LEFT);
            }
        });
        findViewById(R.id.rightBtn).setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                mGameView.setDirection(Direction.RIGHT);
            }
        });

        mGameBtn.setOnClickListener(v -> {
            if (mGameStatus.get() == STATUS_PLAYING) {
                setGameStatus(STATUS_PAUSED);
            } else {
                setGameStatus(STATUS_PLAYING);
            }
        });

        setGameStatus(STATUS_START);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGameStatus.get() == STATUS_PLAYING) {
            setGameStatus(STATUS_PAUSED);
        }
    }

    private static void setGameStatus(int gameStatus) {
        int prevStatus = mGameStatus.get();
        mGameStatusText.setVisibility(View.VISIBLE);
        mGameBtn.setText("start");
        mGameStatus.set(gameStatus);
        switch (gameStatus) {
            case STATUS_OVER:
                mGameStatusText.setText("GAME OVER");
                break;
            case STATUS_START:
                mGameView.newGame();
                mGameStatusText.setText("START GAME");
                break;
            case STATUS_PAUSED:
                mGameStatusText.setText("GAME PAUSED");
                break;
            case STATUS_PLAYING:
                if (prevStatus == STATUS_OVER) {
                    mGameView.newGame();
                }
                startGame();
                mGameStatusText.setVisibility(View.INVISIBLE);
                mGameBtn.setText("pause");
                break;
        }
    }

    public static void startGame() {
        delay = 1000 / FPS;
        new Thread(() -> {
            int count = 0;
            while (!mGameView.isGameOver() && mGameStatus.get() != STATUS_PAUSED) {
                try {
                    Thread.sleep(delay);
                    if (count % SPEED == 0) {
                        mGameView.next();
                        mHandler.post(mGameView::invalidate);
                    }
                    count++;
                } catch (InterruptedException ignored) {
                }
            }
            if (mGameView.isGameOver()) {
                mHandler.post(() -> setGameStatus(STATUS_OVER));
            }
        }).start();
    }

    public static void setFPS(int FPS) {
        GameActivity.FPS = FPS;
    }
}