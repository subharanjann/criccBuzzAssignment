package com.assignment.weatherassignment.presentation.composable.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.assignment.weatherassignment.R
import com.assignment.weatherassignment.data.api.model.CityResponseItem
import com.assignment.weatherassignment.presentation.theme.DarkBgSecondary
import com.assignment.weatherassignment.presentation.theme.NeonOrange
import com.assignment.weatherassignment.presentation.theme.NeonPurple
import com.assignment.weatherassignment.presentation.theme.NeonPurpleSoft
import com.assignment.weatherassignment.presentation.theme.TextPrimary
import com.assignment.weatherassignment.presentation.theme.TextSecondary
import com.assignment.weatherassignment.util.Resource

class BasicElements {
}
@Composable
fun TopSearchBar(
    isExpanded: Boolean,
    searchText: String,
    onSearchTextChanged: (String) -> Unit,
    onExpand: () -> Unit,
    onCollapse: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.TopEnd
    ) {

        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInHorizontally { it },
            exit = fadeOut() + slideOutHorizontally { it }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(
                        color = DarkBgSecondary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .widthIn(max = 320.dp)
            ) {

                OutlinedTextField(
                    value = searchText,
                    onValueChange = onSearchTextChanged,
                    placeholder = { Text("Search city") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color.Transparent,
                        cursorColor = NeonPurple,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onCollapse) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close search",
                        tint = TextSecondary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = !isExpanded,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(onClick = onExpand) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonPurple,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
internal fun CitySuggestionPanel(
    resource: Resource<List<CityResponseItem>>?,
    onCitySelected: (CityResponseItem) -> Unit
) {
    if (resource !is Resource.Success || resource.data.isNullOrEmpty()) return

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkBgSecondary.copy(alpha = 0.95f)
        )
    ) {
        Column {
            resource.data.forEach { city ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCitySelected(city) }
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = "${city.name}, ${city.country}",
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun OfflineNetworkIndicator(
    isOnline: Boolean,
    onRetry: () -> Unit
) {
    if (isOnline) return

    IconButton(onClick = onRetry) {
        Icon(
            painter = painterResource(R.drawable.nonetwork),
            contentDescription = "Offline",
            tint = NeonOrange,
            modifier = Modifier.size(22.dp)
        )
    }
}


