package ltd.nickolay.swipfragment.Kt

import android.util.Log
import android.view.MotionEvent
import ltd.nickolay.swipfragment.R
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt


/**Created by JaKo coding (Nikolay) 14.02.2020
 * Translated to Kotlin 27.02.2020
 * движение определяется по вектору м/у точкой косания и точкой отрыва
 * в момент отрыва
 * */

private const val TAG: String = "myLOG"

abstract class SwipeDetectorK(minToachLen: Int) {

    private var startX = 0f
    private var startY = 0f
    private val minToachLen = minToachLen * 5

    //interactions
    abstract fun onSwipeDetected(direction: Direction)

    fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {}
            MotionEvent.ACTION_UP -> {
                val dX = event.x - startX
                val dY = event.y - startY
                if (calcDist(dX, dY) > minToachLen) {
                    onSwipeDetected(Direction[calcAngle(dX, dY)])
                }
                startX = 0f
                startY = 0f
            }
            else -> {
                startX = 0f
                startY = 0f
                Log.d(TAG, "onTouchEvent: Error")
            }

        }
        return false
    }

    private fun calcAngle(dx: Float, dy: Float): Int {
        return ((atan2(dy, dx) + PI) * 180 / PI + 180).toInt() % 360
    }

    private fun calcDist(dx: Float, dy: Float): Int {
        return sqrt(dx * dx + dy * dy).toInt()
    }


    enum class Direction(val enterAnim: Int, val exitAnim: Int){
        UN_EXPT(R.anim.stay_enter, R.anim.stay_exit), //stay in
        LEFT(R.anim.left_enter, R.anim.left_exit),
        RIGHT(R.anim.right_enter, R.anim.right_exit),
        UP(R.anim.up_enter, R.anim.up_exit),
        DOWN(R.anim.down_enter, R.anim.down_exit);

        companion object {
            operator fun get(angle: Int): Direction = when(angle){
                in 45..135 -> UP
                in 135..225 -> RIGHT
                in 225..315 -> DOWN
                in 315..360, in 0..45 -> LEFT
                else -> UN_EXPT
            }
        }
    }


}



