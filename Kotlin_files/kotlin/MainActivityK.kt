package ltd.nickolay.swipfragment

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ltd.nickolay.swipfragment.Kt.Block_FragmentK
import ltd.nickolay.swipfragment.Kt.SwipeDetectorK
import kotlin.random.Random


private const val TAG = "myLOG"
//arguments
private const val ARG_SELECT = "ltd.nickolay.swipfragment.ARG_SELECT_K"
private const val ARG_CURRENT = "ltd.nickolay.swipfragment.ARG_CURRENT_K"
private const val ARG_WINCOUNT = "ltd.nickolay.swipfragment.ARG_WINCOUNT_K"
private const val ARG_BLOCKCOUNT = "ltd.nickolay.swipfragment.ARG_BLOCKCOUNT_K"

class MainActivityK: AppCompatActivity(), Block_FragmentK.OnFragmentEvent {

    companion object {
        private const val MAX_W = 3
        private const val MAX_H = 3
        private const val MAX_BLOCK = MAX_W * MAX_H
        //TODO Create Setting menu

        private var bI = 1
        private var bJ = 1
        private var selectedBlock = 0 //this is win block
        private var currentBlock = bJ * MAX_W + bI + 1
        private var winCount = 0
        private var blockCount = 0
        private lateinit var swipeDetector: SwipeDetectorK
    }


    private lateinit var tv_Count_Val: TextView
    private lateinit var tv_Win_Val: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            selectedBlock = getWinBlock(currentBlock)
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fl_Block,
                            Block_FragmentK.newInstance(
                                    R.string.s_block_Start,
                                    currentBlock,
                                    false))
                    .commit()
        } else {
            savedInstanceState?.let {
                currentBlock = it.getInt(ARG_CURRENT, 5)
                selectedBlock = it.getInt(ARG_SELECT, getWinBlock(currentBlock));
                blockCount = it.getInt(ARG_BLOCKCOUNT, 0);
                winCount = it.getInt(ARG_WINCOUNT, 0);
            }
            bJ = (currentBlock -1 ) / MAX_W
            bI = (currentBlock - 1) % MAX_W
        }

        tv_Count_Val = findViewById(R.id.tv_Count_Val)
        tv_Count_Val.text = blockCount.toString()
        tv_Win_Val = findViewById(R.id.tv_Win_Val);
        tv_Win_Val.text = "$winCount"

        swipeDetector = object: SwipeDetectorK(ViewConfiguration.get(this).scaledTouchSlop){
            override fun onSwipeDetected(direction: Direction) {
                when (direction) {
                    Direction.UP -> bJ = if (bJ > 0) --bJ else 0
                    Direction.DOWN -> bJ = if (bJ < MAX_H - 1) ++bJ else MAX_H - 1
                    Direction.RIGHT -> bI = if (bI < MAX_W - 1) ++bI else MAX_W - 1
                    Direction.LEFT -> bI = if (bI > 0) --bI else 0
                    Direction.UN_EXPT -> Log.d(TAG, "onSwipeDetected: detected UN_EXPT")
                }
                if (currentBlock != bJ * MAX_W + bI + 1) {
                    currentBlock = bJ * MAX_W + bI + 1
                    showBlock(
                            Block_FragmentK.newInstance(
                                    if (currentBlock == selectedBlock) R.string.s_block_Win else R.string.s_block_Next,
                            currentBlock,
                            currentBlock == selectedBlock),
                    direction)

                }
            }
        }
    }

    fun showBlock(fragment: Fragment,  direction: SwipeDetectorK.Direction, incBlock: Boolean = true) {
        supportFragmentManager
                .beginTransaction()
                .setCustomAnimations(direction.enterAnim, direction.exitAnim)
                .replace(R.id.fl_Block, fragment, null)
                .commit()
        if (incBlock)
            tv_Count_Val.text = "${++blockCount}"
    }

    private fun getWinBlock(skipthis: Int) = if (MAX_BLOCK > 1) {
            var res: Int = Random.nextInt(MAX_BLOCK) + 1
            while (res == skipthis) res = Random.nextInt(MAX_BLOCK) + 1
            res
        } else 0

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return swipeDetector.onTouchEvent(event!!)
        //TODO check all links
    }

    override fun onClickAgain(blockIndex: Int) {
        if (blockIndex != selectedBlock)
            Log.d(TAG, "onClickAgain: game is hacked!")
        selectedBlock = getWinBlock(blockIndex)
        tv_Win_Val.text = "${++winCount}"
        showBlock(Block_FragmentK.newInstance(R.string.s_block_Start, blockIndex, false),
                SwipeDetectorK.Direction.UN_EXPT,
                false)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.apply {
            putInt(ARG_SELECT, selectedBlock)
            putInt(ARG_CURRENT, currentBlock)
            putInt(ARG_WINCOUNT, winCount)
            putInt(ARG_BLOCKCOUNT, blockCount)
        }
        super.onSaveInstanceState(outState)
    }
}