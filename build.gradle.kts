/*
 * Copyright 2024 GoatBytes.IO
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

BuildConfig.initialize(rootProject)

plugins {
  kotlin("jvm") version libs.versions.kotlin
  alias(libs.plugins.detekt)
  alias(libs.plugins.dokka)
  id("publication.s01-oss-sonatype")
}

repositories {
  mavenCentral()
}

dependencies {
  compileOnly(libs.elasticsearch)
  compileOnly(libs.elasticsearch.plugin.parent.join.client)
  testImplementation(kotlin("test"))
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

dependencies {
  detektPlugins(libs.detekt.formatting)
}

detekt {
  buildUponDefaultConfig = true
  config.setFrom(BuildConfig.Detekt.CONFIG)
}

tasks.withType<Detekt>().configureEach {
  reports {
    html.required.set(true)
    md.required.set(true)
    sarif.required.set(false)
    txt.required.set(false)
    xml.required.set(false)
  }
}

tasks.withType<Detekt>().configureEach {
  jvmTarget = BuildConfig.Detekt.jvmTarget
}

tasks.withType<DetektCreateBaselineTask>().configureEach {
  jvmTarget = BuildConfig.Detekt.jvmTarget
}

val detektAll by tasks.registering(Detekt::class) {
  description = "Run detekt analysis on entire project"
  parallel = true
  buildUponDefaultConfig = true
  config.setFrom(BuildConfig.Detekt.CONFIG)
  setSource(files(projectDir))

  include("**/*.kt", "**/*.kts")
  exclude("resources/", "*/build/*")
}

val javadocJar by tasks.registering(Jar::class) {
  archiveClassifier.set("javadoc")
  from(tasks["javadoc"])
}

val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(project.sourceSets.main.get().allSource)
}
