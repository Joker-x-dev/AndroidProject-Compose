package com.joker.kit.feature.main.view

import com.joker.kit.core.designsystem.theme.AppColors
import com.joker.kit.core.designsystem.theme.AppTextStyles
import top.yukonga.miuix.kmp.basic.Text
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.R
import com.joker.kit.core.designsystem.component.CenterColumn
import com.joker.kit.core.designsystem.component.FullScreenBox
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.designsystem.theme.ArrowRightIcon
import com.joker.kit.core.designsystem.theme.LogoIcon
import com.joker.kit.core.designsystem.theme.ShapeExtraLarge
import com.joker.kit.core.designsystem.theme.SpacePaddingLarge
import com.joker.kit.core.designsystem.theme.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.SpaceVerticalSmall
import com.joker.kit.core.designsystem.theme.SpaceVerticalXLarge
import com.joker.kit.core.util.`package`.PackageUtils
import com.joker.kit.feature.main.model.LinkItem
import com.joker.kit.feature.main.viewmodel.AboutViewModel
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.HorizontalDivider
import top.yukonga.miuix.kmp.basic.SmallTitle

/**
 * About 页面路由
 *
 * @param viewModel About 页面 ViewModel
 * @author Joker.X
 */
@Composable
internal fun AboutRoute(
    viewModel: AboutViewModel = hiltViewModel()
) {
    // 系统 URI 打开器
    val uriHandler = LocalUriHandler.current

    AboutScreen(
        developerLink = viewModel.developerLink,
        projectLinks = viewModel.projectLinks,
        resourceLinks = viewModel.resourceLinks,
        onDeveloperClick = { link -> uriHandler.openUri(link.url) },
        onProjectLinkClick = { link -> uriHandler.openUri(link.url) },
        onResourceLinkClick = { link -> uriHandler.openUri(link.url) }
    )
}

/**
 * About 页面
 *
 * @param developerLink 开发者主页
 * @param projectLinks 项目地址列表
 * @param resourceLinks 资源列表
 * @param onDeveloperClick 开发者点击回调
 * @param onProjectLinkClick 项目地址点击回调
 * @param onResourceLinkClick 资源点击回调
 * @author Joker.X
 */
@Composable
internal fun AboutScreen(
    developerLink: LinkItem = LinkItem(
        title = "开发作者",
        url = "https://github.com/Joker-x-dev",
        description = "https://github.com/Joker-x-dev"
    ),
    projectLinks: List<LinkItem> = emptyList(),
    resourceLinks: List<LinkItem> = emptyList(),
    onDeveloperClick: (LinkItem) -> Unit = {},
    onProjectLinkClick: (LinkItem) -> Unit = {},
    onResourceLinkClick: (LinkItem) -> Unit = {}
) {
    AboutContent(
        developerLink = developerLink,
        projectLinks = projectLinks,
        resourceLinks = resourceLinks,
        onDeveloperClick = onDeveloperClick,
        onProjectLinkClick = onProjectLinkClick,
        onResourceLinkClick = onResourceLinkClick
    )
}

/**
 * About 页面内容
 *
 * @param developerLink 开发者主页
 * @param projectLinks 项目地址列表
 * @param resourceLinks 资源列表
 * @param onDeveloperClick 开发者点击回调
 * @param onProjectLinkClick 项目地址点击回调
 * @param onResourceLinkClick 资源点击回调
 * @author Joker.X
 */
@Composable
private fun AboutContent(
    developerLink: LinkItem,
    projectLinks: List<LinkItem>,
    resourceLinks: List<LinkItem>,
    onDeveloperClick: (LinkItem) -> Unit,
    onProjectLinkClick: (LinkItem) -> Unit,
    onResourceLinkClick: (LinkItem) -> Unit
) {
    // 当前 Android 上下文
    val context = LocalContext.current
    // 应用显示名称
    val appName = PackageUtils.getCurrentAppName(context)
    // 应用版本名称
    val appVersionName = PackageUtils.getCurrentVersionName(context)
    // 应用版本号
    val appVersionCode = PackageUtils.getCurrentVersionCode(context).toString()
    // 页面滚动状态
    val scrollState = rememberScrollState(0)
    // 顶部内容完全淡出所需的滚动距离
    val scrollFadeThresholdPx = with(LocalDensity.current) { 220.dp.toPx() }
    // 顶部内容随滚动变化的归一化进度
    val scrollFraction = (scrollState.value / scrollFadeThresholdPx).coerceIn(0f, 1f)

    // 首次进入页面的动画启动状态
    var isAnimationReady by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(100)
        isAnimationReady = true
    }

    // 顶部内容入场位移
    val entranceTranslationY by animateDpAsState(
        targetValue = if (isAnimationReady) 0.dp else 30.dp,
        animationSpec = tween(durationMillis = 1000, easing = EaseOutCubic),
        label = "about_entrance_translation"
    )
    // 页面入场透明度
    val entranceAlpha by animateFloatAsState(
        targetValue = if (isAnimationReady) 1f else 0f,
        animationSpec = tween(durationMillis = 800),
        label = "about_entrance_alpha"
    )
    // 顶部内容滚动透明度
    val contentAlpha by animateFloatAsState(
        targetValue = 1f - scrollFraction,
        label = "about_content_alpha"
    )
    // 顶部内容滚动缩放比例
    val contentScale by animateFloatAsState(
        targetValue = 1f - scrollFraction * 0.1f,
        label = "about_content_scale"
    )

    FullScreenBox(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(modifier = Modifier.graphicsLayer(alpha = contentAlpha * entranceAlpha)) {
            AnimatedAuroraBackground()
        }

        Box(
            modifier = Modifier.graphicsLayer {
                alpha = contentAlpha * entranceAlpha
                scaleX = contentScale
                scaleY = contentScale
                translationY = entranceTranslationY.toPx()
            }
        ) {
            AboutTopSection(
                projectName = appName,
                versionLabel = "Version $appVersionName"
            )
        }

        AboutBottomScrollableContent(
            scrollState = scrollState,
            appName = appName,
            appVersionName = appVersionName,
            appVersionCode = appVersionCode,
            projectName = appName,
            developerLink = developerLink,
            projectLinks = projectLinks,
            resourceLinks = resourceLinks,
            onDeveloperClick = onDeveloperClick,
            onProjectLinkClick = onProjectLinkClick,
            onResourceLinkClick = onResourceLinkClick
        )
    }
}

/**
 * 动态极光背景
 *
 * @author Joker.X
 */
@Composable
private fun AnimatedAuroraBackground() {
    // 极光背景无限动画控制器
    val infiniteTransition = rememberInfiniteTransition(label = "about_aurora_transition")
    // 极光圆心运动使用的周期时间值
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 15000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "about_aurora_time"
    )

    // 当前主题是否为深色模式
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    // 页面背景颜色
    val backgroundColor = MaterialTheme.colorScheme.background
    // 四组极光渐变颜色
    val (color1, color2, color3, color4) = if (isDarkTheme) {
        listOf(
            Color(0.0f, 0.31f, 0.58f),
            Color(0.53f, 0.29f, 0.15f),
            Color(0.46f, 0.06f, 0.27f),
            Color(0.16f, 0.12f, 0.45f)
        )
    } else {
        listOf(
            Color(0.57f, 0.76f, 0.98f),
            Color(0.98f, 0.85f, 0.68f),
            Color(0.98f, 0.75f, 0.93f),
            Color(0.73f, 0.7f, 0.98f)
        )
    }
    // 主色对应的第五组极光颜色
    val color5 = MaterialTheme.colorScheme.primary
    // 极光背景顶部颜色
    val topColor = if (isDarkTheme) Color.Black else backgroundColor

    FullScreenBox(
        modifier = Modifier.background(topColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(150.dp)
                .drawBehind {
                    // 当前绘制区域宽度
                    val width = size.width
                    // 当前绘制区域高度
                    val height = size.height

                    // 第一组极光圆半径
                    val radius1 = width * 0.7f
                    // 第一组极光圆心
                    val center1 = Offset(
                        x = width * (0.3f + 0.2f * cos(time)),
                        y = height * (0.4f + 0.2f * sin(time))
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color1.copy(alpha = 0.9f), Color.Transparent),
                            center = center1,
                            radius = radius1
                        ),
                        radius = radius1,
                        center = center1
                    )

                    // 第二组极光圆半径
                    val radius2 = width * 0.6f
                    // 第二组极光圆心
                    val center2 = Offset(
                        x = width * (0.7f + 0.2f * cos(time * 1.5f + PI.toFloat())),
                        y = height * (0.6f + 0.15f * sin(time * 1.5f + PI.toFloat()))
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color2.copy(alpha = 0.9f), Color.Transparent),
                            center = center2,
                            radius = radius2
                        ),
                        radius = radius2,
                        center = center2
                    )

                    // 第三组极光圆半径
                    val radius3 = width * 0.8f
                    // 第三组极光圆心
                    val center3 = Offset(
                        x = width * (0.6f + 0.2f * cos(time * 0.8f + PI.toFloat() / 2)),
                        y = height * (0.3f + 0.2f * sin(time * 0.8f + PI.toFloat() / 2))
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color3.copy(alpha = 0.85f), Color.Transparent),
                            center = center3,
                            radius = radius3
                        ),
                        radius = radius3,
                        center = center3
                    )

                    // 第四组极光圆半径
                    val radius4 = width * 0.5f
                    // 第四组极光圆心
                    val center4 = Offset(
                        x = width * (0.2f + 0.1f * cos(time * 1.2f + PI.toFloat() / 4)),
                        y = height * (0.7f + 0.1f * sin(time * 1.2f + PI.toFloat() / 4))
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color4.copy(alpha = 0.9f), Color.Transparent),
                            center = center4,
                            radius = radius4
                        ),
                        radius = radius4,
                        center = center4
                    )

                    // 第五组极光圆半径
                    val radius5 = width * 0.7f
                    // 第五组极光圆心
                    val center5 = Offset(
                        x = width * (0.8f + 0.15f * cos(time * 0.9f + PI.toFloat() * 1.5f)),
                        y = height * (0.2f + 0.15f * sin(time * 0.9f + PI.toFloat() * 1.5f))
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(color5.copy(alpha = 0.9f), Color.Transparent),
                            center = center5,
                            radius = radius5
                        ),
                        radius = radius5,
                        center = center5
                    )
                }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.1f to Color.Transparent,
                            1.0f to backgroundColor
                        )
                    )
                )
        )
    }
}

/**
 * 顶部内容区域
 *
 * @param projectName 项目名称
 * @param versionLabel 版本文案
 * @author Joker.X
 */
@Composable
private fun AboutTopSection(
    projectName: String,
    versionLabel: String
) {
    CenterColumn {
        Spacer(modifier = Modifier.height(130.dp))

        Card(
            modifier = Modifier.size(120.dp),
            shape = ShapeExtraLarge,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LogoIcon(size = 100.dp)
            }
        }

        Spacer(modifier = Modifier.height(SpaceVerticalXLarge))

        Text(
            text = projectName,
            style = AppTextStyles.title1,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(SpaceVerticalSmall))

        Text(
            text = versionLabel,
            style = AppTextStyles.body2,
            color = AppColors.onSurfaceContainerVariant
        )
    }
}

/**
 * 底部滚动内容区域
 *
 * @param scrollState 滚动状态
 * @param appName 应用名称
 * @param appVersionName 应用版本名称
 * @param appVersionCode 应用版本号
 * @param projectName 项目名称
 * @param developerLink 开发者主页
 * @param projectLinks 项目地址列表
 * @param resourceLinks 资源列表
 * @param onDeveloperClick 开发者点击回调
 * @param onProjectLinkClick 项目地址点击回调
 * @param onResourceLinkClick 资源点击回调
 * @author Joker.X
 */
@Composable
private fun AboutBottomScrollableContent(
    scrollState: ScrollState,
    appName: String,
    appVersionName: String,
    appVersionCode: String,
    projectName: String,
    developerLink: LinkItem,
    projectLinks: List<LinkItem>,
    resourceLinks: List<LinkItem>,
    onDeveloperClick: (LinkItem) -> Unit,
    onProjectLinkClick: (LinkItem) -> Unit,
    onResourceLinkClick: (LinkItem) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(state = scrollState)
            .padding(horizontal = SpacePaddingLarge)
    ) {
        Spacer(modifier = Modifier.height(360.dp))

        SmallTitle(text = "应用信息")
        Spacer(modifier = Modifier.height(SpaceVerticalMedium))
        Card {
            BasicComponent(
                title = "应用名称",
                endActions = {
                    Text(text = appName, style = AppTextStyles.body2)
                },
            )
            HorizontalDivider()
            BasicComponent(
                title = "版本名称",
                endActions = {
                    Text(text = appVersionName, style = AppTextStyles.body2)
                },
            )
            HorizontalDivider()
            BasicComponent(
                title = "版本号",
                endActions = {
                    Text(text = appVersionCode, style = AppTextStyles.body2)
                },
            )
        }

        Spacer(modifier = Modifier.height(SpaceVerticalXLarge))

        SmallTitle(text = "开发者")
        Spacer(modifier = Modifier.height(SpaceVerticalMedium))
        Card {
            BasicComponent(
                title = "Joker.X",
                summary = "joker.x.dev@gmail.com",
                startAction = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_avatar),
                        contentDescription = "Joker.X",
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape),
                    )
                },
                onClick = { onDeveloperClick(developerLink) },
            )
        }

        Spacer(modifier = Modifier.height(SpaceVerticalXLarge))

        SmallTitle(text = "项目地址")
        Spacer(modifier = Modifier.height(SpaceVerticalMedium))
        AboutLinkSection(
            links = projectLinks,
            onLinkClick = onProjectLinkClick
        )

        Spacer(modifier = Modifier.height(SpaceVerticalXLarge))

        SmallTitle(text = "相关资源")
        Spacer(modifier = Modifier.height(SpaceVerticalMedium))
        AboutLinkSection(
            links = resourceLinks,
            onLinkClick = onResourceLinkClick
        )

        Spacer(modifier = Modifier.height(SpaceVerticalXLarge))

        Text(
            text = "© 2026 Joker.X & $projectName Contributors",
            style = AppTextStyles.body2,
            color = AppColors.onSurfaceContainerVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = SpacePaddingLarge)
        )
    }
}

/**
 * About 链接分组卡片
 *
 * @param links 链接列表
 * @param onLinkClick 点击回调
 * @author Joker.X
 */
@Composable
private fun AboutLinkSection(
    links: List<LinkItem>,
    onLinkClick: (LinkItem) -> Unit
) {
    Card {
        links.forEachIndexed { index, link ->
            BasicComponent(
                title = link.title,
                summary = link.description,
                endActions = {
                    ArrowRightIcon()
                },
                onClick = { onLinkClick(link) },
            )
            if (index != links.lastIndex) HorizontalDivider()
        }
    }
}

/**
 * About 页面浅色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun AboutPreview() {
    AppTheme {
        AboutScreen()
    }
}

/**
 * About 页面深色预览
 *
 * @author Joker.X
 */
@Preview(showBackground = true)
@Composable
private fun AboutPreviewDark() {
    AppTheme(darkTheme = true) {
        AboutScreen()
    }
}
