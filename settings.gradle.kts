plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "study.spring"
include("common")
// include("reactor")
include("reactive:reactor")
include("reactive:reactive-queue")
include("reactive:reactive-queue:website")
include("reactive:reactive-queue:waiting-room")
include("redis")
