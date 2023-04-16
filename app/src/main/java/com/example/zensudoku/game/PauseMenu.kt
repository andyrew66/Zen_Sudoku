package com.example.zensudoku.game

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.DialogFragment
import com.example.zensudoku.R
import com.example.zensudoku.SettingsActivity
import com.example.zensudoku.view.MainMenu.MainMenuActivity

/**
 * A simple [Fragment] subclass.
 * Use the [PauseMenu.newInstance] factory method to
 * create an instance of this fragment.
 */
class PauseMenu : DialogFragment() {
    var resume: ImageButton? = null
    var exitBtn: ImageButton? = null
    var optnsBtn: ImageButton? = null

    // TODO: Rename and change types of parameters
    private val mParam1: String? = null
    private val mParam2: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pause_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resume = view.findViewById<View>(R.id.resumePause) as ImageButton
        exitBtn = view.findViewById<View>(R.id.exit) as ImageButton
        optnsBtn = view.findViewById<View>(R.id.optnsPause) as ImageButton
        resume!!.setOnClickListener { v: View? -> dialog!!.dismiss() }
        exitBtn!!.setOnClickListener { v: View? ->
            val i = Intent(activity, MainMenuActivity::class.java)
            startActivity(i)
            activity!!.finish()
        }
        optnsBtn!!.setOnClickListener { v: View? ->
            val i = Intent(activity, SettingsActivity::class.java)
            startActivity(i)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.alpha = 0.9f
    }

    companion object {
        /**
         * @return A new instance of fragment PauseMenu.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): PauseMenu {
            val fragment = PauseMenu()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}