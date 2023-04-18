pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        jcenter()
    }
    plugins {
        kotlin("multiplatform").version("1.8.10")
        id("org.jetbrains.compose").version("1.3.1")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

enableFeaturePreview("VERSION_CATALOGS")

include(
    ":utils:customisations",
    ":utils:interop-ribs",
    ":utils:interop-rx2",
    ":utils:interop-rx3",
    ":utils:testing-junit4",
    ":utils:testing-junit5",
    ":utils:testing-ui",
    ":utils:testing-ui-activity",
    ":utils:testing-unit-common",
    ":demos:appyx-navigation",
    ":demos:common",
    ":demos:navigation-compose",
    ":appyx-interactions:android",
    ":appyx-interactions:desktop",
    ":appyx-interactions:common",
    ":appyx-navigation",
    ":appyx-components:backstack:common",
    ":appyx-components:backstack:android",
    ":appyx-components:spotlight:common",
    ":appyx-components:spotlight:android",
    ":appyx-components:demos:common",
    ":appyx-components:demos:android",
    ":appyx-components:internal:common",
    ":appyx-components:internal:android",
    "demos:appyx-interactions"
)

includeBuild("plugins")
