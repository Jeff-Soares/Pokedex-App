package dev.jx.pokedex.ui

import android.R.color.transparent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dev.jx.pokedex.databinding.FragmentFilterDialogBinding
import dev.jx.pokedex.model.FilterOptions
import dev.jx.pokedex.ui.viewmodel.PokemonListViewModel
import dev.jx.pokedex.util.setWidthPercent

const val TYPE_TAG = "TypeError"
const val MAX_HEIGHT = 880
const val MAX_WEIGHT = 460

class FilterDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentFilterDialogBinding
    private lateinit var callback: FilterDialog
    private val viewModel: PokemonListViewModel by viewModels(
        ownerProducer = { requireParentFragment() })

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

    override fun onResume() {
        viewModel.filterOptions.let {
            binding.heightSelect.values =
                listOf(it.height.first().toFloat(), it.height.last().toFloat())
            binding.weightSelect.values =
                listOf(it.weight.first().toFloat(), it.weight.last().toFloat())
            binding.typesLayout.root.children.forEach { view ->
                val checkbox = view as AppCompatCheckBox
                checkbox.isChecked =
                    it.types.contains(FilterOptions.Type.getType(checkbox.text.toString()))
            }
            binding.weaknessLayout.root.children.forEach { view ->
                val checkbox = view as AppCompatCheckBox
                checkbox.isChecked =
                    it.weakness.contains(FilterOptions.Type.getType(checkbox.text.toString()))
            }
        }
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setDialogSize()
        setupSpinnerOrderBy()
        setupRanges()

        binding.apply.setOnClickListener { filter() }
        binding.cancel.setOnClickListener { dismiss() }
        binding.clear.setOnClickListener { clearValues() }
    }

    private fun filter() {
        setOptions()
        callback.setFilter(viewModel.filterOptions)
        dismiss()
    }

    private fun clearValues() {
        binding.orderSpinner.setSelection(0)
        binding.typesLayout.root.children.forEach { checkbox ->
            (checkbox as AppCompatCheckBox).isChecked = false
        }
        binding.weaknessLayout.root.children.forEach { checkbox ->
            (checkbox as AppCompatCheckBox).isChecked = false
        }
        binding.heightSelect.values = listOf(0f, MAX_HEIGHT.toFloat())
        binding.weightSelect.values = listOf(0f, MAX_WEIGHT.toFloat())

        filter()
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

        viewModel.filterOptions.orderBy = orderSpinner.selectedItem as FilterOptions.OrderBy
        viewModel.filterOptions.height =
            heightSelect.values.first().toInt()..heightSelect.values.last().toInt()
        viewModel.filterOptions.weight =
            weightSelect.values.first().toInt()..weightSelect.values.last().toInt()

        try {
            viewModel.filterOptions.types = types.filter { it.isChecked }
                .map { FilterOptions.Type.getType(it.text.toString()) }
            viewModel.filterOptions.weakness = weakTypes.filter { it.isChecked }
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
