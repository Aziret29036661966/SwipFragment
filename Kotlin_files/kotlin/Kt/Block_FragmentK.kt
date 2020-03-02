package ltd.nickolay.swipfragment.Kt

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_block.view.*
import ltd.nickolay.swipfragment.R


/**
 * Created by JaKo coding (Nikolay) 14.02.2020
 * Translated 27.02.2020*/

private const val ARG_PARAM_TEXT  = "ltd.nickolay.swipfragment.Kt.ARG_PARAM_TEXT"
private const val ARG_PARAM_INDEX = "ltd.nickolay.swipfragment.Kt.ARG_PARAM_INDEX"
private const val ARG_PARAM_WIN   = "ltd.nickolay.swipfragment.Kt.ARG_PARAM_WIN"

/**
 * A simple [Fragment] subclass.
 * Use the [Block_FragmentK.newInstance] factory method to
 * create an instance of this fragment.
 */
class Block_FragmentK : Fragment() {
    // TODO: Rename and change types of parameters
    private var param_Text = R.string.s_error_param
    private var param_Index = -1
    private var param_Win = false

    private var mListener: OnFragmentEvent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param_Text =  it.getInt(ARG_PARAM_TEXT, R.string.s_error_param)
            param_Index = it.getInt(ARG_PARAM_INDEX, -1)
            param_Win = it.getBoolean(ARG_PARAM_WIN, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_block, container, false)
        var bgColor = ResourcesCompat.getColor(resources,R.color.colorNotWin,null)
        if (param_Win) {
            val b_Again = view.b_Again
            b_Again.visibility = View.VISIBLE
            b_Again.setOnClickListener {
                mListener?.onClickAgain(param_Index)
            }
            //TODO add versions control
            bgColor = ResourcesCompat.getColor(resources,R.color.colorWin,null)
        }
        view.setBackgroundColor(bgColor)
        view.tv_Info.setText(param_Text)
        view.tv_Index.text = param_Index.toString()
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentEvent) {
            mListener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentEvent")
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /** Activities that contain this fragment must implement the
     * {@link OnFragmentEvent} interface
     * to handle interaction events.
     * Use the {@link Block_Fragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    interface OnFragmentEvent {
        fun onClickAgain(blockIndex: Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param paramText show this text for User.
         * @param paramIndex labirint block Index.
         * @param paramWin is this block Win?
         * @return A new instance of fragment Block_FragmentK.
         */
        @JvmStatic
        fun newInstance(paramText: Int, paramIndex: Int, paramWin: Boolean) =
                Block_FragmentK().apply {
                    arguments = Bundle().apply {
                        putInt(ARG_PARAM_TEXT, paramText)
                        putInt(ARG_PARAM_INDEX, paramIndex)
                        putBoolean(ARG_PARAM_WIN, paramWin)
                    }
                }
    }
}
