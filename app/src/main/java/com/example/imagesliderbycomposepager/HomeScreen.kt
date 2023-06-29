package com.example.imagesliderbycomposepager

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.imagesliderbycomposepager.ui.theme.ImageSliderByComposePagerTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen() {
    val images = remember {
        mutableStateListOf(
            "https://images.pexels.com/photos/10770234/pexels-photo-10770234.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
            "https://images.pexels.com/photos/17303514/pexels-photo-17303514.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
            "https://images.pexels.com/photos/17080957/pexels-photo-17080957/free-photo-of-love-summer-leaf-blur.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
            "https://images.pexels.com/photos/14833456/pexels-photo-14833456.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
            "https://images.pexels.com/photos/17054022/pexels-photo-17054022/free-photo-of-wood-sea-beach-vacation.jpeg?auto=compress&cs=tinysrgb&w=600&lazy=load",
        )
    }
    val pagerState = rememberPagerState()
    val colorMatrix = remember { ColorMatrix() }
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(vertical = 32.dp)
            .fillMaxSize(),
    ) {
        HorizontalPager(
            modifier = Modifier.align(Alignment.Center),
            pageCount = images.size,
            state = pagerState
        ) { index ->
            val pageOffset = (pagerState.currentPage - index) + pagerState.currentPageOffsetFraction
            val imageSizebyScale by animateFloatAsState(
                targetValue = if (pageOffset.equals(0.0f)) 1f else 0.75f,
                animationSpec = tween(durationMillis = 300)
            )
            LaunchedEffect(key1 = imageSizebyScale) {
                if (pageOffset.equals(0.0f)) {
                    colorMatrix.setToSaturation(1f)
                } else {
                    colorMatrix.setToSaturation(0f)
                }
            }
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize(0.8f)
                    .padding(16.dp)
                    .graphicsLayer {
                        scaleX = imageSizebyScale
                        scaleY = imageSizebyScale
                    }
                    .clip(RoundedCornerShape(16.dp)),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(images[index])
                    .build(),
                contentDescription = "Pexels Image Loaded From Internet",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(colorMatrix)
            )
        }

        Box(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(0.5f)
                .clip(RoundedCornerShape(100))
                .align(Alignment.BottomCenter)
                .background(MaterialTheme.colorScheme.onBackground)
                .padding(8.dp),
        ) {
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )
                    }
                },
                modifier = Modifier.align(
                    Alignment.CenterStart
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Go Back"
                )
            }

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                },
                modifier = Modifier.align(
                    Alignment.CenterEnd
                )
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Go Forward"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    ImageSliderByComposePagerTheme {
        HomeScreen()
    }
}