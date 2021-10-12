package dev.jx.pokedex.ui

import android.R.color.transparent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import dev.jx.pokedex.databinding.FragmentFilterDialogBinding
import dev.jx.pokedex.model.FilterOptions
import dev.jx.pokedex.model.FilterOptions.OrderBy.NUMBER_ASC
import dev.jx.pokedex.util.setWidthPercent

const val TYPE_TAG = "TypeError"
const val MAX_HEIGHT = 880
const val MAX_WEIGHT = 460

class FilterDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentFilterDialogBinding
    private lateinit var callback: FilterDialog
    private val options by lazy {
        // Set default values
        FilterOptions(NUMBER_ASC, listOf(), 0..MAX_HEIGHT, 0..MAX_WEIGHT, listOf())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            callback = parentFragment as FilterDialog
        } catch (e: ClassCastException) {
            throw ClassCastException("Calling fragment must implement Callback interface")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFilterDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply.setOnClickListener {
            setOptions()
            callback.setFilter(options)
            dismiss()
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }

        setDialogSize()
        setupSpinnerOrderBy()
        setupRanges()
    }

    private fun setupSpinnerOrderBy() {
        binding.orderSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            FilterOptions.OrderBy.values()
        )
    }

    private fun setOptions() = with(binding) {
        val types = with(typesLayout) {
            listOf(
                opNormal, opFighting, opFlying, opPoison, opGround, opRock, opBug,
                opGhost, opSteel, opFire, opWater, opGrass, opElectric, opPsychic, opDark, opIce,
                opDragon,
                opFairy
            )
        }
        val weakTypes = with(weaknessLayout) {
            listOf(
                opNormal, opFighting, opFlying, opPoison, opGround, opRock, opBug,
                opGhost, opSteel, opFire, opWater, opGrass, opElectric, opPsychic, opDark, opIce,
                opDragon,
                opFairy
            )
        }

        options.orderBy = orderSpinner.selectedItem as FilterOptions.OrderBy
        options.height = heightSelect.values.first().toInt()..heightSelect.values.last().toInt()
        options.weight = weightSelect.values.first().toInt()..weightSelect.values.last().toInt()

        try {
            options.types = types.filter { it.isChecked }
                .map { FilterOptions.Type.getType(it.text.toString()) }
            options.weakness = weakTypes.filter { it.isChecked }
                .map { FilterOptions.Type.getType(it.text.toString()) }
        } catch (e: NoSuchElementException) {
            Log.e(TYPE_TAG, e.message.toString())
        }
    }

    private fun setDialogSize() {
        dialog?.window?.setBackgroundDrawableResource(transparent)
        setWidthPercent(90)
    }

    private fun setupRanges() {
        val minHeight = 0f
        val maxHeight = MAX_HEIGHT.toFloat()
        binding.heightSelect.valueFrom = minHeight
        binding.heightSelect.valueTo = maxHeight
        binding.heightSelect.values = listOf(minHeight, maxHeight)
        binding.heightSelect.stepSize = 1f

        val minWeight = 0f
        val maxWeight = MAX_WEIGHT.toFloat()
        binding.weightSelect.valueFrom = minWeight
        binding.weightSelect.valueTo = maxWeight
        binding.weightSelect.values = listOf(minWeight, maxWeight)
        binding.weightSelect.stepSize = 1f
    }

    interface FilterDialog {
        fun setFilter(options: FilterOptions)
    }
}
