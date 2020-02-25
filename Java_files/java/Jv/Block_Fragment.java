package ltd.nickolay.swipfragment.Jv;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import ltd.nickolay.swipfragment.R;

/**
 * Created by JaKo coding (Nikolay) 14.02.2020
 * Activities that contain this fragment must implement the
 * {@link OnFragmentEvent} interface
 * to handle interaction events.
 * Use the {@link Block_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Block_Fragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_INDEX = "ltd.nickolay.swipfragment.Jv.ARG_PARAM_INDEX";
    private static final String ARG_PARAM_TEXT = "ltd.nickolay.swipfragment.Jv.ARG_PARAM_TEXT";
    private static final String ARG_PARAM_WIN = "ltd.nickolay.swipfragment.Jv.ARG_PARAM_WIN";

    private int param_Index;
    private int param_Text;
    private boolean param_Win;

    private OnFragmentEvent mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param_Text show this text for User.
     * @param param_Index labirint block Index.
     * @param param_Win is this block Win?
     * @return A new instance of fragment Block_Fragment.
     */

    public static Block_Fragment newInstance(int param_Text, int param_Index, boolean param_Win) {
        Block_Fragment fragment = new Block_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM_TEXT, param_Text);
        args.putInt(ARG_PARAM_INDEX, param_Index);
        args.putBoolean(ARG_PARAM_WIN, param_Win);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            param_Index = getArguments().getInt(ARG_PARAM_INDEX, -1);
            param_Text = getArguments().getInt(ARG_PARAM_TEXT, R.string.s_error_param);
            param_Win = getArguments().getBoolean(ARG_PARAM_WIN, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_block, container, false);
        if (param_Win) {
            Button b_Again = view.findViewById(R.id.b_Again);
            b_Again.setVisibility(View.VISIBLE);
            b_Again.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        mListener.onClickAgain(param_Index);
                    }
                }
            });
            //TODO add versions control
            /*switch (Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.O:..*/
            view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorWin,null));
        } else {view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.colorNotWin,null));}
        ((TextView) view.findViewById(R.id.tv_Info)).setText(param_Text);
        ((TextView) view.findViewById(R.id.tv_Index)).setText(String.valueOf(param_Index));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentEvent) {
            mListener = (OnFragmentEvent) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentEvent");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentEvent {
        void onClickAgain(int blockIndex);
    }
}
