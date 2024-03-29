package com.sidharth.lgconnect.ui.codeeditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.sidharth.lgconnect.R
import com.sidharth.lgconnect.databinding.FragmentCodeEditorBinding
import com.sidharth.lgconnect.util.LGDialogs
import com.sidharth.lgconnect.util.LGManager
import com.sidharth.lgconnect.util.NetworkUtils
import com.sidharth.lgconnect.util.ResourceProvider
import com.sidharth.lgconnect.util.ToastUtils
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class CodeEditorFragment : Fragment() {
    private val patternOpeningTags = Pattern.compile("<[A-Za-z][^<>]*?>")
    private val patternClosingTags = Pattern.compile("</[A-Za-z][^<>]*?>")
    private val patternSelfClosingTags = Pattern.compile("</?[A-Za-z][^<>]*?/>")
    private val patternComment = Pattern.compile("<!--[\\s\\S]*?-->")
    private val patternAttribute = Pattern.compile("\\s[a-zA-Z0-9_-]+\\s*=")
    private val patternOperation = Pattern.compile(":|>|<|!=|>=|<=|=|%|-|\\+|\\*|/")
    private val patternNumbers = Pattern.compile("\\b(\\d*[.]?\\d+)\\b")
    private val patternChar = Pattern.compile("'(.*?)'")
    private val patternString = Pattern.compile("[\"'][^\"']*?[\"']")

    private lateinit var resourceProvider: ResourceProvider
    private lateinit var dialog: LGDialogs

    private var lgDialogs: LGDialogs? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        resourceProvider = ResourceProvider(requireContext())
        lgDialogs = LGDialogs()
    }

    override fun onPause() {
        super.onPause()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs = null
    }

    override fun onDestroy() {
        super.onDestroy()
        lgDialogs?.dismissNoConnectionDialog()
        lgDialogs = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentCodeEditorBinding.inflate(inflater)

        val pairCompleteMap = mutableMapOf<Char, Char>()
        pairCompleteMap['{'] = '}'
        pairCompleteMap['['] = ']'
        pairCompleteMap['('] = ')'
        pairCompleteMap['<'] = '>'
        pairCompleteMap['"'] = '"'
        pairCompleteMap['\''] = '\''

        val syntaxPatternMap = mutableMapOf<Pattern, Int>()
        syntaxPatternMap[patternOpeningTags] = resourceProvider.getColor(R.color.xml_tags)
        syntaxPatternMap[patternClosingTags] = resourceProvider.getColor(R.color.xml_tags)
        syntaxPatternMap[patternSelfClosingTags] = resourceProvider.getColor(R.color.xml_tags)
        syntaxPatternMap[patternComment] = resourceProvider.getColor(R.color.xml_comment)
        syntaxPatternMap[patternAttribute] = resourceProvider.getColor(R.color.xml_attribute)
        syntaxPatternMap[patternOperation] = resourceProvider.getColor(R.color.xml_operation)
        syntaxPatternMap[patternNumbers] = resourceProvider.getColor(R.color.xml_numbers)
        syntaxPatternMap[patternChar] = resourceProvider.getColor(R.color.xml_char)
        syntaxPatternMap[patternString] = resourceProvider.getColor(R.color.xml_string)

        binding.codeView.setSyntaxPatternsMap(syntaxPatternMap)
        binding.codeView.setHighlightWhileTextChanging(true)

        binding.codeView.setPairCompleteMap(pairCompleteMap)
        binding.codeView.enablePairComplete(true)
        binding.codeView.enablePairCompleteCenterCursor(true)

        binding.codeView.setEnableAutoIndentation(true)
        binding.codeView.setTabLength(2)

        binding.codeView.setEnableLineNumber(true)
        binding.codeView.setEnableRelativeLineNumber(true)
        binding.codeView.setEnableHighlightCurrentLine(true)

        binding.fabSendKml.setOnClickListener {
            if (NetworkUtils.isNetworkConnected(requireContext())) {
                lifecycleScope.launch {
                    if (LGManager.getInstance()?.connected == true) {
                        LGManager.getInstance()?.sendKml(binding.codeView.text.toString())
                    } else {
                        lgDialogs?.showNoConnectionDialog(requireContext())
                    }
                }
            } else {
                ToastUtils.showToast(requireContext(), "No Network Connection")
            }
        }

        return binding.root
    }
}