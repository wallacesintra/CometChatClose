pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            // specify the url of the maven repository
//            url = uri("https://repo.maven.apache.org/maven2")
            url = uri("https://dl.cloudsmith.io/public/cometchat/cometchat/maven/")
        }
    }
}

rootProject.name = "CometChatClose"
include(":app")
 