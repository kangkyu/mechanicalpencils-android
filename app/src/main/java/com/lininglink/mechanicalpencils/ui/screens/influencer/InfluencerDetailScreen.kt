package com.lininglink.mechanicalpencils.ui.screens.influencer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lininglink.mechanicalpencils.data.model.InfluencerDetail
import com.lininglink.mechanicalpencils.ui.components.ItemCard
import com.lininglink.mechanicalpencils.ui.components.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfluencerDetailScreen(
    viewModel: InfluencerDetailViewModel,
    onNavigateBack: () -> Unit,
    onItemClick: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        when (uiState) {
                            is InfluencerDetailUiState.Success -> (uiState as InfluencerDetailUiState.Success).influencer.name
                            else -> "Influencer"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is InfluencerDetailUiState.Loading -> {
                    LoadingIndicator()
                }
                is InfluencerDetailUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = (uiState as InfluencerDetailUiState.Error).message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is InfluencerDetailUiState.Success -> {
                    val influencer = (uiState as InfluencerDetailUiState.Success).influencer
                    InfluencerDetailContent(
                        influencer = influencer,
                        onItemClick = onItemClick
                    )
                }
            }
        }
    }
}

@Composable
private fun InfluencerDetailContent(
    influencer: InfluencerDetail,
    onItemClick: (Int) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = influencer.avatarUrl,
                    contentDescription = influencer.name,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = influencer.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                influencer.platform?.let { platform ->
                    Text(
                        text = platform,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                influencer.bio?.let { bio ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bio,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (influencer.items.isNotEmpty()) {
                    Text(
                        text = "${influencer.items.size} Items",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        items(
            items = influencer.items,
            key = { it.id }
        ) { item ->
            ItemCard(
                item = item,
                onClick = { onItemClick(item.id) }
            )
        }
    }
}
