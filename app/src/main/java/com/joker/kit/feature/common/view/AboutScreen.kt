// Copyright 2025, compose-miuix-ui contributors
// SPDX-License-Identifier: Apache-2.0

@file:OptIn(ExperimentalScrollBarApi::class)

package com.joker.kit.feature.common.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode as ComposeBlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.joker.kit.BuildConfig
import com.joker.kit.R
import com.joker.kit.core.annotation.ScreenPreview
import com.joker.kit.core.annotation.ScreenPreviewDark
import com.joker.kit.core.designsystem.component.SpaceVerticalMedium
import com.joker.kit.core.designsystem.theme.AppTheme
import com.joker.kit.core.state.LocalAppSettings
import com.joker.kit.core.designsystem.theme.LogoIcon
import com.joker.kit.core.designsystem.theme.isInDarkTheme
import com.joker.kit.core.navigation.navigateBack
import com.joker.kit.core.ui.adaptive.isLG
import com.joker.kit.core.ui.adaptive.isMD
import com.joker.kit.core.ui.component.appbar.BackButton
import com.joker.kit.feature.common.component.BlurredBar
import com.joker.kit.feature.common.component.blend.ColorBlendToken
import com.joker.kit.feature.common.component.effect.BgEffectBackground
import com.joker.kit.feature.common.component.pageContentPadding
import com.joker.kit.feature.common.component.pageScrollModifiers
import com.joker.kit.feature.common.component.rememberBlurBackdrop
import com.joker.kit.feature.common.model.AboutLink
import com.joker.kit.feature.common.viewmodel.AboutViewModel
import top.yukonga.miuix.kmp.basic.BasicComponent
import top.yukonga.miuix.kmp.basic.Card
import top.yukonga.miuix.kmp.basic.CardDefaults
import top.yukonga.miuix.kmp.basic.MiuixScrollBehavior
import top.yukonga.miuix.kmp.basic.Scaffold
import top.yukonga.miuix.kmp.basic.ScrollBehavior
import top.yukonga.miuix.kmp.basic.SmallTitle
import top.yukonga.miuix.kmp.basic.SmallTopAppBar
import top.yukonga.miuix.kmp.basic.Text
import top.yukonga.miuix.kmp.basic.VerticalScrollBar
import top.yukonga.miuix.kmp.basic.rememberScrollBarAdapter
import top.yukonga.miuix.kmp.blur.BlendColorEntry
import top.yukonga.miuix.kmp.blur.BlurBlendMode
import top.yukonga.miuix.kmp.blur.BlurDefaults
import top.yukonga.miuix.kmp.blur.isRuntimeShaderSupported
import top.yukonga.miuix.kmp.blur.layerBackdrop
import top.yukonga.miuix.kmp.blur.textureBlur
import top.yukonga.miuix.kmp.interfaces.ExperimentalScrollBarApi
import top.yukonga.miuix.kmp.overlay.OverlayBottomSheet
import top.yukonga.miuix.kmp.preference.ArrowPreference
import top.yukonga.miuix.kmp.preference.OverlayDropdownPreference
import top.yukonga.miuix.kmp.preference.SwitchPreference
import top.yukonga.miuix.kmp.theme.MiuixTheme

/**
 * 关于页面路由入口。
 *
 * @param viewModel Hilt 注入的关于页面 ViewModel
 */
@Composable
internal fun AboutRoute(
    viewModel: AboutViewModel = hiltViewModel(),
) {
    // 系统 URI 打开器负责执行外部链接跳转。
    val uriHandler = LocalUriHandler.current

    AboutScreen(
        developerLink = viewModel.developerLink,
        projectLinks = viewModel.projectLinks,
        resourceLinks = viewModel.resourceLinks,
        onDeveloperClick = { link -> uriHandler.openUri(link.url) },
        onProjectLinkClick = { link -> uriHandler.openUri(link.url) },
        onResourceLinkClick = { link -> uriHandler.openUri(link.url) },
    )
}

/**
 * 关于页面骨架。
 *
 * @param developerLink 开发者主页链接
 * @param projectLinks 项目文档和代码仓库链接
 * @param resourceLinks 相关项目资源链接
 * @param onDeveloperClick 开发者链接点击回调
 * @param onProjectLinkClick 项目链接点击回调
 * @param onResourceLinkClick 资源链接点击回调
 * @param padding 页面外部安全区内边距
 */
@Composable
internal fun AboutScreen(
    developerLink: AboutLink = AboutLink(
        title = "Joker.X",
        url = "https://github.com/Joker-x-dev",
        description = "joker.x.dev@gmail.com",
    ),
    projectLinks: List<AboutLink> = emptyList(),
    resourceLinks: List<AboutLink> = emptyList(),
    onDeveloperClick: (AboutLink) -> Unit = {},
    onProjectLinkClick: (AboutLink) -> Unit = {},
    onResourceLinkClick: (AboutLink) -> Unit = {},
    padding: PaddingValues = PaddingValues(),
) {
    // 顶部栏滚动行为控制器。
    val topAppBarScrollBehavior = MiuixScrollBehavior()
    // 关于页面列表滚动状态。
    val lazyListState = rememberLazyListState()

    // 根据顶部 Logo 占位区域计算标题栏折叠进度。
    val scrollProgress by remember {
        derivedStateOf {
            when {
                lazyListState.firstVisibleItemIndex > 0 -> 1f

                else -> {
                    val spacer =
                        lazyListState.layoutInfo.visibleItemsInfo.firstOrNull { it.key == "logoSpacer" }
                    if (spacer != null && spacer.size > 0) {
                        (lazyListState.firstVisibleItemScrollOffset.toFloat() / spacer.size).coerceIn(
                            0f,
                            1f
                        )
                    } else {
                        0f
                    }
                }
            }
        }
    }

    // 页面背景图层，用于标题栏和内容卡片的模糊效果。
    val backdrop = rememberBlurBackdrop()
    // 标题栏完全折叠状态。
    val collapsed by remember { derivedStateOf { scrollProgress == 1f } }
    // 标题栏模糊效果启用状态。
    val blurActive by remember(backdrop) { derivedStateOf { backdrop != null && scrollProgress == 1f } }

    Scaffold(
        topBar = {
            val barColor = if (blurActive) {
                Color.Transparent
            } else {
                if (collapsed) MiuixTheme.colorScheme.surface else Color.Transparent
            }
            val titleColor = MiuixTheme.colorScheme.onSurface.copy(
                alpha = ((scrollProgress - 0.35f) / 0.65f).coerceIn(0f, 1f),
            )
            BlurredBar(backdrop, blurActive, topAppBarScrollBehavior) {
                SmallTopAppBar(
                    title = stringResource(R.string.about_title),
                    scrollBehavior = topAppBarScrollBehavior,
                    color = barColor,
                    titleColor = titleColor,
                    defaultWindowInsetsPadding = false,
                    navigationIcon = {
                        BackButton(
                            onClick = { navigateBack() },
                        )
                    },
                )
            }
        },
    ) { innerPadding ->
        Box(modifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier) {
            AboutContent(
                developerLink = developerLink,
                projectLinks = projectLinks,
                resourceLinks = resourceLinks,
                onDeveloperClick = onDeveloperClick,
                onProjectLinkClick = onProjectLinkClick,
                onResourceLinkClick = onResourceLinkClick,
                padding = PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    bottom = padding.calculateBottomPadding(),
                ),
                topAppBarScrollBehavior = topAppBarScrollBehavior,
                lazyListState = lazyListState,
                scrollProgressProvider = { scrollProgress },
            )
        }
    }
}

/**
 * 关于页面业务内容。
 *
 * @param developerLink 开发者主页链接
 * @param projectLinks 项目文档和代码仓库链接
 * @param resourceLinks 相关项目资源链接
 * @param onDeveloperClick 开发者链接点击回调
 * @param onProjectLinkClick 项目链接点击回调
 * @param onResourceLinkClick 资源链接点击回调
 * @param padding 页面内容安全区内边距
 * @param topAppBarScrollBehavior 顶部栏滚动行为
 * @param lazyListState 页面列表滚动状态
 * @param scrollProgressProvider 提供顶部内容滚动进度
 */
@Composable
private fun AboutContent(
    developerLink: AboutLink,
    projectLinks: List<AboutLink>,
    resourceLinks: List<AboutLink>,
    onDeveloperClick: (AboutLink) -> Unit,
    onProjectLinkClick: (AboutLink) -> Unit,
    onResourceLinkClick: (AboutLink) -> Unit,
    padding: PaddingValues,
    topAppBarScrollBehavior: ScrollBehavior,
    lazyListState: LazyListState,
    scrollProgressProvider: () -> Float,
) {
    val appSettings = LocalAppSettings.current
    // 当前窗口是否满足平板布局断点。
    val isWideScreen = isMD() || isLG()

    // 内容区域背景图层。
    val backdrop = rememberBlurBackdrop()
    // 背景效果变体选择状态。
    var isOs3Effect by remember { mutableStateOf(true) }
    // 背景效果配置面板显示状态。
    var showTextureSet by remember { mutableStateOf(false) }
    // 卡片纹理模糊半径。
    var blurRadius by remember { mutableFloatStateOf(60f) }
    // 卡片纹理噪声系数。
    var noiseCoefficient by remember { mutableFloatStateOf(BlurDefaults.NoiseCoefficient) }
    // 卡片纹理亮度系数。
    var brightness by remember { mutableFloatStateOf(0f) }
    // 卡片纹理对比度系数。
    var contrast by remember { mutableFloatStateOf(1f) }
    // 卡片纹理饱和度系数。
    var saturation by remember { mutableFloatStateOf(1f) }
    // 列表内容安全区内边距。
    val scrollPadding = pageContentPadding(
        padding,
        padding,
        isWideScreen,
        extraStart = if (isWideScreen) 0.dp else WindowInsets.displayCutout.asPaddingValues()
            .calculateLeftPadding(LayoutDirection.Ltr),
        extraEnd = WindowInsets.displayCutout.asPaddingValues()
            .calculateRightPadding(LayoutDirection.Ltr),
    )
    // 顶部 Logo 区域安全区内边距。
    val logoPadding = pageContentPadding(
        padding,
        padding,
        isWideScreen,
        extraTop = 40.dp,
        extraStart = if (isWideScreen) 0.dp else WindowInsets.displayCutout.asPaddingValues()
            .calculateLeftPadding(LayoutDirection.Ltr),
        extraEnd = WindowInsets.displayCutout.asPaddingValues()
            .calculateRightPadding(LayoutDirection.Ltr),
    )

    // 当前主题是否为深色模式。
    val isInDark = isInDarkTheme()
    // 动态背景开关状态。
    val dynamicBackground = remember { mutableStateOf(isRuntimeShaderSupported()) }
    // 全屏背景开关状态。
    val isFullScreenBackground = remember { mutableStateOf(true) }

    // 根据主题选择卡片混合颜色。
    val cardBlend =
        if (isInDark) ColorBlendToken.Overlay_Thin_Light else ColorBlendToken.Pured_Regular_Light
    // 根据主题选择 Logo 模糊混合颜色。
    val logoBlend = remember(isInDark) {
        if (isInDark) {
            listOf(
                BlendColorEntry(Color(0xe6a1a1a1), BlurBlendMode.ColorDodge),
                BlendColorEntry(Color(0x4de6e6e6), BlurBlendMode.LinearLight),
                BlendColorEntry(Color(0xff1af500), BlurBlendMode.Lab),
            )
        } else {
            listOf(
                BlendColorEntry(Color(0xcc4a4a4a), BlurBlendMode.ColorBurn),
                BlendColorEntry(Color(0xff4f4f4f), BlurBlendMode.LinearLight),
                BlendColorEntry(Color(0xff1af200), BlurBlendMode.Lab),
            )
        }
    }

    // 像素与 dp 的换算密度。
    val density = LocalDensity.current
    // 顶部 Logo 区域实际高度。
    var logoHeightDp by remember { mutableStateOf(300.dp) }

    BgEffectBackground(
        dynamicBackground = dynamicBackground.value,
        isOs3Effect = isOs3Effect,
        isFullSize = isFullScreenBackground.value,
        modifier = Modifier.fillMaxSize(),
        bgModifier = if (backdrop != null) Modifier.layerBackdrop(backdrop) else Modifier,
        alpha = { 1f - scrollProgressProvider() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = logoPadding.calculateTopPadding() + 52.dp,
                    start = logoPadding.calculateLeftPadding(LayoutDirection.Ltr),
                    end = logoPadding.calculateRightPadding(LayoutDirection.Ltr),
                )
                .onSizeChanged { size ->
                    with(density) { logoHeightDp = size.height.toDp() }
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(88.dp)
                    .graphicsLayer {
                        val iconProgress =
                            ((scrollProgressProvider() - 0.35f) / 0.15f).coerceIn(0f, 1f)
                        clip = true
                        shape = RoundedCornerShape(24.dp)
                        alpha = 1 - iconProgress
                        scaleX = 1 - (iconProgress * 0.05f)
                        scaleY = 1 - (iconProgress * 0.05f)
                    }
                    .background(Color.White),
            ) {
                LogoIcon(size = 74.dp)
            }
            Text(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 5.dp)
                    .graphicsLayer {
                        val projectNameProgress =
                            ((scrollProgressProvider() - 0.20f) / 0.15f).coerceIn(0f, 1f)
                        alpha = 1 - projectNameProgress
                        scaleX = 1 - (projectNameProgress * 0.05f)
                        scaleY = 1 - (projectNameProgress * 0.05f)
                    }
                    .then(
                        if (backdrop != null) {
                            Modifier
                                .textureBlur(
                                    backdrop = backdrop,
                                    shape = RoundedCornerShape(16.dp),
                                    blurRadius = 150f,
                                    noiseCoefficient = noiseCoefficient,
                                    colors = BlurDefaults.blurColors(
                                        blendColors = logoBlend,
                                    ),
                                    contentBlendMode = ComposeBlendMode.DstIn,
                                )
                        } else {
                            Modifier
                        },
                    ),
                text = stringResource(R.string.app_name),
                color = MiuixTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        val versionCodeProgress =
                            ((scrollProgressProvider() - 0.05f) / 0.15f).coerceIn(0f, 1f)
                        alpha = 1 - versionCodeProgress
                        scaleX = 1 - (versionCodeProgress * 0.05f)
                        scaleY = 1 - (versionCodeProgress * 0.05f)
                    },
                color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                text = "v" + BuildConfig.VERSION_NAME + " (" + BuildConfig.VERSION_CODE + ")",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
            )
        }

        // Scrollable content
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
                .pageScrollModifiers(
                    appSettings.enableScrollEndHaptic,
                    true,
                    topAppBarScrollBehavior,
                ),
            contentPadding = PaddingValues(
                top = scrollPadding.calculateTopPadding(),
                start = scrollPadding.calculateLeftPadding(LayoutDirection.Ltr),
                end = scrollPadding.calculateRightPadding(LayoutDirection.Ltr),
            ),
        ) {
            // Transparent spacer matching logo height
            item(key = "logoSpacer") {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(
                            logoHeightDp + 52.dp + logoPadding.calculateTopPadding() - scrollPadding.calculateTopPadding() + 126.dp,
                        )
                        .pointerInput(Unit) {
                            detectTapGestures {
                                showTextureSet = true
                            }
                        },
                    contentAlignment = Alignment.TopCenter,
                    content = { },
                )
            }

            item(key = "about") {
                Box {
                    Spacer(Modifier.fillParentMaxHeight())
                    Column(
                        modifier = Modifier.padding(bottom = scrollPadding.calculateBottomPadding()),
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .then(
                                    aboutCardBlur(
                                        backdrop,
                                        cardBlend,
                                        blurRadius,
                                        noiseCoefficient,
                                        brightness,
                                        contrast,
                                        saturation
                                    )
                                ),
                            colors = aboutCardColors(backdrop),
                        ) {
                            AboutInfoRow(
                                title = stringResource(R.string.about_app_name),
                                value = stringResource(R.string.app_name),
                            )
                            AboutInfoRow(
                                title = stringResource(R.string.about_version_name),
                                value = BuildConfig.VERSION_NAME,
                            )
                            AboutInfoRow(
                                title = stringResource(R.string.about_version_code),
                                value = BuildConfig.VERSION_CODE.toString(),
                            )
                        }
                        SpaceVerticalMedium()
                        SmallTitle(text = stringResource(R.string.about_developer))
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .then(
                                    aboutCardBlur(
                                        backdrop,
                                        cardBlend,
                                        blurRadius,
                                        noiseCoefficient,
                                        brightness,
                                        contrast,
                                        saturation
                                    )
                                ),
                            colors = aboutCardColors(backdrop),
                        ) {
                            ArrowPreference(
                                title = developerLink.titleResId?.let { stringResource(it) } ?: developerLink.title,
                                summary = developerLink.description.orEmpty(),
                                startAction = {
                                    Image(
                                        painter = painterResource(R.drawable.ic_avatar),
                                        contentDescription = "Joker.X",
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clip(CircleShape),
                                    )
                                },
                                onClick = { onDeveloperClick(developerLink) },
                            )
                        }
                        SpaceVerticalMedium()
                        SmallTitle(text = stringResource(R.string.about_project_links))
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .then(
                                    aboutCardBlur(
                                        backdrop,
                                        cardBlend,
                                        blurRadius,
                                        noiseCoefficient,
                                        brightness,
                                        contrast,
                                        saturation
                                    )
                                ),
                            colors = aboutCardColors(backdrop),
                        ) {
                            projectLinks.forEach { link ->
                                ArrowPreference(
                                    title = link.titleResId?.let { stringResource(it) } ?: link.title,
                                    endActions = {
                                        Text(
                                            text = link.description.orEmpty(),
                                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                                            color = MiuixTheme.colorScheme.onSurfaceVariantActions,
                                        )
                                    },
                                    onClick = { onProjectLinkClick(link) },
                                )
                            }
                        }
                        SpaceVerticalMedium()
                        SmallTitle(text = stringResource(R.string.about_resources))
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                                .then(
                                    aboutCardBlur(
                                        backdrop,
                                        cardBlend,
                                        blurRadius,
                                        noiseCoefficient,
                                        brightness,
                                        contrast,
                                        saturation
                                    )
                                ),
                            colors = aboutCardColors(backdrop),
                        ) {
                            resourceLinks.forEach { link ->
                                ArrowPreference(
                                    title = link.titleResId?.let { stringResource(it) } ?: link.title,
                                    onClick = { onResourceLinkClick(link) },
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 12.dp),
                            text = stringResource(R.string.about_copyright, stringResource(R.string.app_name)),
                            color = MiuixTheme.colorScheme.onSurfaceVariantSummary,
                            fontSize = MiuixTheme.textStyles.body2.fontSize,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
        VerticalScrollBar(
            adapter = rememberScrollBarAdapter(lazyListState),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .fillMaxHeight(),
            trackPadding = scrollPadding,
        )
    }

    OverlayBottomSheet(
        show = showTextureSet,
        title = stringResource(R.string.about_background_effect),
        onDismissRequest = {
            showTextureSet = false
        },
        insideMargin = DpSize(0.dp, 0.dp),
    ) {
        LazyColumn {
            item {
                val effectVariantOptions = listOf("OS2", "OS3")
                OverlayDropdownPreference(
                    title = stringResource(R.string.about_effect_variant),
                    items = effectVariantOptions,
                    selectedIndex = if (isOs3Effect) 1 else 0,
                    onSelectedIndexChange = { isOs3Effect = (it == 1) },
                )

                SwitchPreference(
                    title = stringResource(R.string.about_dynamic_background),
                    checked = dynamicBackground.value,
                    onCheckedChange = {
                        dynamicBackground.value = it
                    },
                )

                SwitchPreference(
                    title = stringResource(R.string.about_fullscreen_background),
                    checked = isFullScreenBackground.value,
                    onCheckedChange = {
                        isFullScreenBackground.value = it
                    },
                )
            }
            item {
                Spacer(
                    modifier = Modifier.height(
                        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
                )
            }
        }
    }
}

/**
 * 应用信息单行展示组件。
 *
 * @param title 信息名称
 * @param value 信息值
 */
@Composable
private fun AboutInfoRow(
    title: String,
    value: String,
) {
    BasicComponent(
        endActions = {
            Text(
                text = value,
                fontSize = MiuixTheme.textStyles.body2.fontSize,
                color = MiuixTheme.colorScheme.onSurfaceVariantActions,
            )
        },
    ) {
        Text(
            text = title,
            fontSize = MiuixTheme.textStyles.headline1.fontSize,
            fontWeight = FontWeight.Medium,
            color = MiuixTheme.colorScheme.onBackground,
        )
    }
}

/**
 * 计算关于页面卡片的背景模糊效果。
 *
 * @param backdrop 页面背景图层
 * @param cardBlend 卡片混合颜色
 * @param blurRadius 模糊半径
 * @param noiseCoefficient 模糊噪声系数
 * @param brightness 亮度调整值
 * @param contrast 对比度调整值
 * @param saturation 饱和度调整值
 * @return 卡片背景模糊 Modifier
 */
@Composable
private fun aboutCardBlur(
    backdrop: top.yukonga.miuix.kmp.blur.LayerBackdrop?,
    cardBlend: List<BlendColorEntry>,
    blurRadius: Float,
    noiseCoefficient: Float,
    brightness: Float,
    contrast: Float,
    saturation: Float,
): Modifier = if (backdrop != null) {
    Modifier.textureBlur(
        backdrop = backdrop,
        shape = RoundedCornerShape(16.dp),
        blurRadius = blurRadius,
        noiseCoefficient = noiseCoefficient,
        colors = BlurDefaults.blurColors(
            blendColors = cardBlend,
            brightness = brightness,
            contrast = contrast,
            saturation = saturation,
        ),
    )
} else {
    Modifier
}

/**
 * 提供关于页面卡片颜色。
 *
 * @param backdrop 页面背景图层
 * @return 卡片颜色配置
 */
@Composable
private fun aboutCardColors(
    backdrop: top.yukonga.miuix.kmp.blur.LayerBackdrop?,
) = CardDefaults.defaultColors(
    if (backdrop != null) Color.Transparent else MiuixTheme.colorScheme.surfaceContainer,
    Color.Transparent,
)

/**
 * 关于页面浅色预览。
 */
@ScreenPreview
@Composable
private fun AboutScreenPreview() {
    AppTheme {
        AboutScreen(
            developerLink = AboutLink(
                title = "Joker.X",
                url = "https://github.com/Joker-x-dev",
                description = "joker.x.dev@gmail.com",
            ),
            projectLinks = listOf(
                AboutLink("项目文档", "https://compose.dusksnow.top", "compose.dusksnow.top"),
                AboutLink("GitHub", "https://github.com/Joker-x-dev/AndroidProject-Compose", "Joker-x-dev"),
            ),
            resourceLinks = listOf(
                AboutLink("青商城 GitHub", "https://github.com/Joker-x-dev/CoolMallKotlin"),
            ),
        )
    }
}

/**
 * 关于页面深色预览。
 */
@ScreenPreviewDark
@Composable
private fun AboutScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        AboutScreen(
            developerLink = AboutLink(
                title = "Joker.X",
                url = "https://github.com/Joker-x-dev",
                description = "joker.x.dev@gmail.com",
            ),
            projectLinks = listOf(
                AboutLink("项目文档", "https://compose.dusksnow.top", "compose.dusksnow.top"),
                AboutLink("GitHub", "https://github.com/Joker-x-dev/AndroidProject-Compose", "Joker-x-dev"),
            ),
            resourceLinks = listOf(
                AboutLink("青商城 GitHub", "https://github.com/Joker-x-dev/CoolMallKotlin"),
            ),
        )
    }
}
