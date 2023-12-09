pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Spotzify"
include(":app")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:ui")
include(":core:common")
include(":core:feature_api")
include(":core:network")
include(":feature:player:ui")
