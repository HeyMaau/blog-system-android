# CLAUDE.md

本文档为 Claude Code (claude.ai/code) 在此仓库中编写代码提供指引。

## 构建命令

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK（含 ProGuard + 资源压缩）
./gradlew assembleRelease

# 运行单元测试
./gradlew testDebugUnitTest

# 运行插桩测试（需要模拟器或真机）
./gradlew connectedDebugAndroidTest

# 运行单个单元测试类
./gradlew app:testDebugUnitTest --tests "top.manpok.blog.ExampleUnitTest"

# 清理构建
./gradlew clean

# 查看依赖
./gradlew app:dependencies
```

- Gradle: 8.4, AGP: 8.3.0, Kotlin: 1.9.0, KSP: 1.9.20-1.0.14
- JDK: 11（sourceCompatibility/targetCompatibility = JVM 11）

## 设备与部署

使用 `android` CLI 管理模拟器并将应用部署到设备。

```bash
# 列出可用模拟器
android emulator list

# 创建模拟器（如 API 35）
android emulator create --name "pixel_35" --sdk "system-images/android-35/google_apis/arm64-v8a"

# 启动模拟器（等待启动完成）
android emulator start --name "pixel_35" --wait

# 停止模拟器
android emulator stop --name "pixel_35"

# 部署 APK 到已连接的设备/模拟器
android run --apks app/build/outputs/apk/debug/app-debug.apk

# 查看 SDK 信息
android info sdk

# 安装/更新 SDK 包（当前项目 targetSdk=35, minSdk=24）
android sdk install platforms/android-35 build-tools/35.0.0
```

## 调试与截图

```bash
# 截取设备屏幕
android screen capture --output screenshot.png

# 截取带标注的屏幕（自动给 UI 元素编号，便于定位）
android screen capture --annotate --output screenshot.png

# 将标注编号解析为坐标
android screen resolve --screen screenshot.png --string "#3"

# 导出 UI 布局树（JSON 格式，包含 bounds、center、交互状态等）
android layout --pretty

# 仅输出自上次调用以来变化的 UI 元素（减少上下文噪音）
android layout --diff --pretty

# 点击某个 UI 元素（使用 layout 输出的 center 坐标）
adb shell input tap 152 23

# 滑动操作（scrollable 列表等）
adb shell input swipe 250 400 600 500 500
```

> **选择原则：** 优先使用 `layout` 检查 UI 结构（更轻量），截图作为辅助手段（尤其在处理 WebView 或动画时）。`layout --diff` 可在执行操作后聚焦变化，减少上下文占用。

## 项目架构

单模块 `app/` 项目，采用 **MVVM** 模式，使用 Jetpack Compose + Material 3。

### 包结构

| 包 | 用途 |
|---|---|
| `activity/` | 9 个 Activity，均继承 `BaseActivity`。`MainActivity` 承载 Compose UI 及底部导航栏；其余为独立页面（详情、搜索、反馈等） |
| `api/` | Retrofit 服务接口 + `BlogRetrofit` 单例（通过 `TempData.currentEnv` 切换生产/开发双实例模式） |
| `viewmodel/` | 每个页面一个 ViewModel，使用 `androidx.lifecycle.ViewModel`。`GlobalViewModelManager` 持有跨页面共享的单例（`AudioViewModel`、`ListStateViewModel`）|
| `page/` | 顶层 Compose 页面：`HomePage`、`CategoryPage`、`ToolsPage`、`AboutPage`，以及列表子页面 |
| `component/` | 可复用的 Compose 组件：文章列表项、评论 UI、音频播放控件、搜索栏、弹窗等 |
| `db/` | Room 数据库、DAO 和实体类（文章缓存、搜索历史） |
| `pojo/` | API 反序列化的数据/响应类 |
| `service/` | `AudioService` — 媒体播放前台服务 |
| `view/` | `AudioFloatingWindowManager` — 系统悬浮窗 |
| `webview/` | WebView Chrome 客户端及 JS 接口 |
| `utils/` | 工具类（`Constants`、`LogUtil`、`NetworkUtil`、`ScrollUtil` 等） |
| `ui/theme/` | Material 3 主题（Color、Typography、Theme）— 支持动态取色 + 深色模式 |
| `ds/` | DataStore 偏好设置（应用设置持久化） |
| `base/` | `BaseApplication`、`BaseActivity`、`CustomExceptionHandler`、生命周期回调 |

### 导航

`BlogScaffold` 中基于索引的简单 Tab 导航：
- 索引 0：HomePage（子标签：推荐 + 想法）
- 索引 1：CategoryPage
- 索引 2：ToolsPage
- 索引 3：AboutPage

子页面通过独立的 Activity 打开（如 `ArticleDetailActivity`、`SearchActivity`）。

### 网络

- `BlogRetrofit` 对象创建生产环境（`https://m.manpok.top`）和开发环境（`http://192.168.31.136:8080`）两个 Retrofit 实例。
- API 调用使用 Retrofit `Call<>` 返回类型 + `enqueue()` 回调（非挂起函数）。
- 自定义 User-Agent 请求头：`Android/${release} ${brand}/${model} manpok_app/${versionName}`
- 所有请求设置 Referer 请求头。
- 超时时间：连接 20s，读/写 30s。

### 状态管理

- 无依赖注入框架 — 通过 `object` + `by lazy` 手动管理单例。
- `TempData.currentEnv` 在生产/开发环境间切换。
- Compose 页面通过 `viewModel()` 观察 ViewModel。
- `GlobalViewModelManager` 提供音频播放和列表滚动状态的跨页面 ViewModel 访问。

### 主要依赖库

- **UI：** Jetpack Compose（BOM 2025.03.01）、Material 3、Accompanist Zoomable
- **网络：** Retrofit 2.11.0 + OkHttp 4.12.0 + Gson
- **图片：** Coil Compose 2.7.0（自定义 OkHttp 客户端设置 Referer 请求头）
- **数据库：** Room 2.6.0（KSP 注解处理）
- **音频：** Media3 ExoPlayer 1.4.1（前台服务 + 通知）
- **其他：** Jsoup 1.17.2（HTML 解析）、AndroidX Palette、DataStore Preferences

### 构建配置

- `versionCode = 9`，`versionName = "2.0"`
- `minSdk = 24`，`targetSdk = 35`，`compileSdk = 35`
- Release 构建：启用 ProGuard + 资源压缩
- APK 命名规则：`manpok_blog_V${versionName}_${timestamp}`
