package project.app.notewise.presentation.loginScreen

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontFamily
import project.app.notewise.R
import project.app.notewise.baseUI.theme.ui.poppinsBold
import project.app.notewise.domain.models.MarqueeItem
import project.app.notewise.domain.models.dummyMarqueeItem
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlin.random.Random


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WOnBoardingScreen(
    paddingValues: PaddingValues,
    navHostController: NavController,
) {
    val isLoginVisible = remember { mutableStateOf(false) }
    val background = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isLoginVisible.value) {
                        Modifier.blur(6.dp)
                    } else {
                        Modifier
                    }
                )
                .drawWithCache {
                    val gradient =
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                background.copy(alpha = 0.5f),
                            ),
                            startY = size.height / 4.5f,
                            endY = size.height
                        )
                    onDrawWithContent {
                        drawContent()
                        drawRect(
                            gradient,
                            blendMode = BlendMode.Multiply
                        )
                    }
                },
        ) {
            itemsIndexed(dummyMarqueeItem) { _, _ ->
                Row(modifier = Modifier.basicMarquee()) {
                    repeat(30) {
                        val task = remember {
                            mutableStateOf(
                                dummyMarqueeItem[Random.nextInt(
                                0,
                                dummyMarqueeItem.size - 1
                            )])
                        }
                        MarqueeCard(taskItem = task.value)
                    }
                }
            }

        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_aichat),
                    contentDescription = null,
                    modifier = Modifier
                        .width(500.dp)
                        .height(650.dp),
                    tint = Color.Unspecified

                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {

                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.filledTonalButtonColors()
                        ) {
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_notewise),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(40.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.width(7.dp))
                                    Text(
                                        text = "Let's Start",
                                        fontSize = 19.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        softWrap = true,
                                        modifier = Modifier.fillMaxWidth(0.75f),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}



@Composable
fun MarqueeCard(
    modifier: Modifier = Modifier,
    taskItem: MarqueeItem,
    isSelected: Boolean = false,
    onClick: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    Card(
        modifier = modifier
            .width(200.dp)
            .height(90.dp)
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(interactionSource = interactionSource, indication = null) {
                onClick()
            },
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(
            1.dp,
            if (isSelected) Color.White else Color(0xFFABACAD).copy(alpha = 0.5f)
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (isSelected) 1f else 0.5f)
        ) {
            Row(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = taskItem.icon ?: R.drawable.ic_notewise),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(start = 10.dp),
                    tint = if (isSelected) taskItem.color else taskItem.color.copy(0.85f)
                )
                Spacer(modifier = Modifier.width(7.dp))
                Text(
                    text = taskItem.name,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(poppinsBold),
                    fontWeight = FontWeight.ExtraBold,
                    softWrap = true,
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .padding(horizontal = 10.dp),
                    textAlign = TextAlign.Center,
                    color = if (isSelected) taskItem.color else taskItem.color.copy(0.85f),
                )
            }
        }
    }
}
