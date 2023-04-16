import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.zensudoku.R
import com.example.zensudoku.view.SudokuActivity

class LevelComplete(levelDifficulty: String, timer: String) : DialogFragment() {
    private val mParam1: String? = null
    var emptyTimer = ""
    var nextLevelButton: ImageButton? = null
    var timerText: String
    var levelDifficulty: String

    init {
        this.levelDifficulty = levelDifficulty
        this.timerText = timer
    }

    override fun setEnterTransition(transition: Any?) {
        super.setEnterTransition(transition)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.attributes.alpha = 0.9f
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val timer = view.findViewById<TextView>(R.id.timer)
        val s: CharSequence = "Time Taken: " + timerText
        timer.text = s
        nextLevelButton = view.findViewById(R.id.nextLevelButton)
        nextLevelButton?.let { button ->
            button.setOnClickListener { nextLevelButtonClick() }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_level_complete, container, false)
    }

    private fun nextLevelButtonClick() {
        val i = Intent(activity, SudokuActivity::class.java)
        i.putExtra("Difficulty", levelDifficulty)
        startActivity(i)
        activity?.finish()

    }

    companion object {
        private const val ARG_PARAM2 = "param2"

        fun newInstance(levelDifficulty: String, timerText: String): LevelComplete {
            val fragment = LevelComplete(levelDifficulty, timerText)
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

}