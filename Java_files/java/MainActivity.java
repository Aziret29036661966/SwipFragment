package ltd.nickolay.swipfragment;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.TextView;

import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import ltd.nickolay.swipfragment.Jv.Block_Fragment;
import ltd.nickolay.swipfragment.Jv.SwipeDetector;

public class MainActivity extends AppCompatActivity implements Block_Fragment.OnFragmentEvent{

    private static final String TAG = "myLOG";
    private static final int MAX_W = 3, MAX_H = 3,
            MAX_BLOCK = MAX_W * MAX_H;

    //arguments
    private static final String ARG_SELECT = "ltd.nickolay.swipfragment.ARG_SELECT";
    private static final String ARG_CURRENT = "ltd.nickolay.swipfragment.ARG_CURRENT";
    private static final String ARG_WINCOUNT = "ltd.nickolay.swipfragment.ARG_WINCOUNT";
    private static final String ARG_BLOCKCOUNT = "ltd.nickolay.swipfragment.ARG_BLOCKCOUNT";





    final private Random random = new Random();

    private int
            bI = 1, bJ = 1,
            selectedBlock, //this is win block
            currentBlock = bJ * MAX_W + bI + 1,
            winCount = 0,
            blockCount = 0;

    private static SwipeDetector swipeDetector;

    TextView tv_Count_Val, tv_Win_Val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            selectedBlock = getWinBlock(currentBlock);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fl_Block,
                            Block_Fragment.newInstance(
                                    R.string.s_block_Start,
                                    currentBlock,
                                    false))
                    .commit();
        } else {
            currentBlock = savedInstanceState.getInt(ARG_CURRENT, 5);
            bJ = (currentBlock -1 ) / MAX_W;
            bI = (currentBlock - 1) % MAX_W;
            selectedBlock = savedInstanceState.getInt(ARG_SELECT, getWinBlock(currentBlock));
            blockCount = savedInstanceState.getInt(ARG_BLOCKCOUNT, 0);
            winCount = savedInstanceState.getInt(ARG_WINCOUNT, 0);
        }
        tv_Count_Val = findViewById(R.id.tv_Count_Val);
        tv_Count_Val.setText(String.valueOf(blockCount));
        tv_Win_Val = findViewById(R.id.tv_Win_Val);
        tv_Win_Val.setText(String.valueOf(winCount));

        swipeDetector = new SwipeDetector(ViewConfiguration.get(this).getScaledTouchSlop()) {
            @Override
            public void onSwipeDetected(Direction direction) {
                switch (direction){
                    case UP: bJ = (bJ > 0)? --bJ : 0;
                        break;
                    case DOWN: bJ = (bJ < MAX_H - 1)? ++bJ : MAX_H - 1;
                        break;
                    case RIGHT: bI = (bI < MAX_W - 1)? ++bI : MAX_W - 1;
                        break;
                    case LEFT: bI = (bI > 0)? --bI : 0;
                        break;
                    case UN_EXPT:
                        Log.d(TAG, "onSwipeDetected: detected UN_EXPT");
                        break;
                    default:
                        Log.d(TAG, "onSwipeDetected: something totaly wrong!!!");
                }
                if (currentBlock != bJ * MAX_W + bI + 1) {
                    currentBlock = bJ * MAX_W + bI + 1;
                    showBlock(
                            Block_Fragment.newInstance(
                                    (currentBlock == selectedBlock)? R.string.s_block_Win: R.string.s_block_Next,
                                    currentBlock,
                                    currentBlock == selectedBlock),
                            direction);

                }
            }
        };

    }

    private void showBlock(Fragment fragment, SwipeDetector.Direction direction, boolean... incBlock) {

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(direction.getEnterAnim(), direction.getExitAnim())
                .replace(R.id.fl_Block, fragment, null)
                .commit();
        if (incBlock.length < 1 || incBlock[0])
            tv_Count_Val.setText(String.valueOf(++blockCount));
    }

    private int getWinBlock(int skipthis) {
        if (MAX_BLOCK > 1) {
            int res = random.nextInt(MAX_BLOCK) + 1;
            while (res == skipthis)
                res = random.nextInt(MAX_BLOCK) + 1;
            return res;
        } else return 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return swipeDetector.onTouchEvent(event);
    }

    @Override
    public void onClickAgain(int blockIndex) {
        if (blockIndex != selectedBlock)
            Log.d(TAG, "onClickAgain: game is hacked!");

        selectedBlock = getWinBlock(blockIndex);
        tv_Win_Val.setText(String.valueOf(++winCount));

        showBlock(Block_Fragment.newInstance(R.string.s_block_Start, blockIndex, false),
                SwipeDetector.Direction.UN_EXPT,
                false);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(ARG_SELECT, selectedBlock);
        outState.putInt(ARG_CURRENT, currentBlock);
        outState.putInt(ARG_WINCOUNT, winCount);
        outState.putInt(ARG_BLOCKCOUNT, blockCount);

        super.onSaveInstanceState(outState);
    }
}
