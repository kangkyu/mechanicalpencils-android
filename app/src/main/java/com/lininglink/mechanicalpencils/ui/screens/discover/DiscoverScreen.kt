package com.lininglink.mechanicalpencils.ui.screens.discover

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.lininglink.mechanicalpencils.data.model.Influencer
import com.lininglink.mechanicalpencils.data.model.ItemGroup
import com.lininglink.mechanicalpencils.ui.components.LoadingIndicator
import com.lininglink.mechanicalpencils.ui.screens.groups.GroupsViewModel
import com.lininglink.mechanicalpencils.ui.screens.influencer.InfluencerListUiState
import com.lininglink.mechanicalpencils.ui.screens.influencer.InfluencerListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    influencerListViewModel: InfluencerListViewModel,
    groupsViewModel: GroupsViewModel,
    onInfluencerClick: (Int) -> Unit,
    onGroupClick: (Int) -> Unit
) {
    val influencerState by influencerListViewModel.uiState.collectAsState()
    val groupsState by groupsViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Discover") }
            )
        }
    ) { paddingValues ->
        val isLoading = influencerState is InfluencerListUiState.Loading && groupsState.isLoading
        val influencers = when (influencerState) {
            is InfluencerListUiState.Success -> (influencerState as InfluencerListUiState.Success).influencers
            else -> emptyList()
        }
        val groups = groupsState.groups

        if (isLoading && influencers.isEmpty() && groups.isEmpty()) {
            LoadingIndicator()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (influencers.isNotEmpty()) {
                    item {
                        Text(
                            text = "Influencers",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    items(
                        items = influencers,
                        key = { "influencer-${it.id}" }
                    ) { influencer ->
                        InfluencerCard(
                            influencer = influencer,
                            onClick = { onInfluencerClick(influencer.id) }
                        )
                    }
                }

                if (groups.isNotEmpty()) {
                    item {
                        Text(
                            text = "Groups",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }

                    items(
                        items = groups,
                        key = { "group-${it.id}" }
                    ) { group ->
                        GroupCard(
                            group = group,
                            onClick = { onGroupClick(group.id) }
                        )
                    }
                }

                if (influencers.isEmpty() && groups.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nothing to discover yet",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfluencerCard(
    influencer: Influencer,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = influencer.avatarUrl,
                contentDescription = influencer.name,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = influencer.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                influencer.platform?.let { platform ->
                    Text(
                        text = platform,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = "${influencer.itemsCount} items",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun GroupCard(
    group: ItemGroup,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = group.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${group.itemsCount} items",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View group",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
