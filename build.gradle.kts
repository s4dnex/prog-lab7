subprojects {
    tasks.withType<Jar> {
        destinationDirectory.set(rootProject.layout.buildDirectory.dir("libs"))
    }
}
