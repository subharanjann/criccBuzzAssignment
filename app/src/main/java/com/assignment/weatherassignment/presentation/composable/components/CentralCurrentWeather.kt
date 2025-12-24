package com.assignment.weatherassignment.presentation.composable.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assignment.weatherassignment.R
import com.assignment.weatherassignment.data.local.model.WeatherData
import com.assignment.weatherassignment.presentation.mapper.getWeatherIconRes
import com.assignment.weatherassignment.presentation.theme.DarkBgSecondary
import com.assignment.weatherassignment.presentation.theme.NeonBlue
import com.assignment.weatherassignment.presentation.theme.NeonPurple
import com.assignment.weatherassignment.presentation.theme.NeonPurpleSoft
import com.assignment.weatherassignment.presentation.theme.TextPrimary
import com.assignment.weatherassignment.presentation.theme.TextSecondary
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CentralCurrentWeather {
}
@Composable
fun CurrentWeatherShow(
    modifier: Modifier = Modifier,
    weather: WeatherData,
    isOnline: Boolean
) {
    val iconRes = getWeatherIconRes(
        condition = weather.condition,
        iconCode = weather.icon
    )

    Column(
        modifier = modifier.padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // --- Weather Icon ---
        Icon(
            painter = painterResource(iconRes),
            contentDescription = weather.condition,
            modifier = Modifier
                .size(140.dp)
                .shadow(
                    elevation = 32.dp,
                    shape = CircleShape,
                    ambientColor = NeonPurple,
                    spotColor = NeonPurple
                ),
            tint = NeonPurple
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Temperature ---
        Text(
            text = "${weather.temperature.toInt()}°",
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 88.sp,
                fontWeight = FontWeight.Bold
            ),
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- Location + Condition ---
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = TextPrimary
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = weather.city,
                color = TextPrimary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(TextSecondary, CircleShape)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Text(
                text = weather.condition,
                color = NeonPurpleSoft,
                style = MaterialTheme.typography.titleMedium
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- Date (NEW) ---
        Text(
            text = formatFullDate(weather.date),
            color = TextSecondary,
            style = MaterialTheme.typography.labelMedium
        )

        OfflineInfo(
            isOnline = isOnline,
            lastUpdated = weather.lastUpdated
        )
    }
}

@Composable
fun OfflineInfo(
    isOnline: Boolean,
    lastUpdated: Long
) {
    if (isOnline) return

    Row(
        modifier = Modifier
            .padding(top = 12.dp)
            .background(
                color = DarkBgSecondary.copy(alpha = 0.6f),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(R.drawable.fog),
            contentDescription = "Offline",
            modifier = Modifier.size(14.dp),
            tint = TextSecondary
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = "Offline data · Updated ${
                android.text.format.DateUtils.getRelativeTimeSpanString(
                    lastUpdated,
                    System.currentTimeMillis(),
                    android.text.format.DateUtils.MINUTE_IN_MILLIS
                )
            }",
            color = TextSecondary,
            style = MaterialTheme.typography.labelSmall
        )
    }
}


fun formatFullDate(date: String): String {
    val localDate = LocalDate.parse(date)
    val formatter = DateTimeFormatter.ofPattern("EEE, dd MMM", Locale.getDefault())
    return localDate.format(formatter)
}