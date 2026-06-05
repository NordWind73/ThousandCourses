package com.example.thousandcourses.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.thousandcourses.ComposeActivity
import com.example.thousandcourses.R
import kotlin.jvm.java

class SelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        SelectionScreen(
                            onXmlClick = {
                                findNavController().navigate(R.id.onboardingFragment)
                            },
                            onComposeClick = {
                                val intent = Intent(requireContext(), ComposeActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SelectionScreen(
    onXmlClick: () -> Unit,
    onComposeClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.choose_version),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onXmlClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.xml))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onComposeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.compose))
        }

    }
}