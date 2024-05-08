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

package publication

import BuildConfig.Publishing
import org.gradle.api.Project
import java.io.File

/**
 * Represents the configuration for signing artifacts, encapsulating necessary details.
 *
 * @property credentials The credentials for the signing configuration.
 */
sealed class SigningConfig {

  abstract val credentials: Credentials

  /**
   * Specific signing configuration for the release publication.
   *
   * @property credentials The credentials for the signing configuration.
   */
  class Release(override val credentials: Credentials) : SigningConfig() {

    companion object {
      /**
       * Get the [Release] [SigningConfig].
       *
       * @param project The root project
       * @return The release signing config or null if the environment is not setup to sign releases
       */
      operator fun get(project: Project): Release? {
        return try {
          val file = File(project.prop(Publishing.ENV_NAME_CREDENTIALS))
          val credentials = Credentials.from(file) ?: return null
          Release(credentials)
        } catch (e: Exception) {
          null
        }
      }
    }
  }

  /**
   * Singing credentials.
   *
   * @property keyId The ID of the signing key.
   * @property keyRing The location of the keyring containing the signing key.
   * @property password The password for accessing the signing key.
   */
  data class Credentials(
    val keyId: String,
    val keyRing: String,
    val password: String
  ) {
    companion object {
      /**
       * Load the credentials from a file.
       *
       * @param file The file containing the credentials
       * @return The [Credentials] or null if it failed to parse the credentials.
       */
      fun from(file: File): Credentials? {
        var keyId: String? = null
        var keyring: String? = null
        var password: String? = null
        file.forEachLine { line ->
          if (line.contains("=")) {
            val key = line.substringBefore("=")
            val value = line.substringAfter("=")
            when (key) {
              KEY_ID -> keyId = value
              KEYRING -> keyring = value
              PASSWORD -> password = value
            }
          }
        }
        keyring = "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"
        return let(keyId, keyring, password) { id, key, pass ->
          Credentials(id, key, pass)
        }
      }
    }
  }

  private companion object {
    private const val KEY_ID = "SIGNING_KEY_ID"
    private const val KEYRING = "SIGNING_KEYRING"
    private const val PASSWORD = "SIGNING_PASSWORD"

    private fun <A, B, C, D> let(a: A?, b: B?, c: C?, block: (a: A, b: B, c: C) -> D): D? {
      return if (a != null && b != null && c != null) block(a, b, c) else null
    }

    /**
     * Retrieves a project property or environment variable value by name.
     *
     * @param name The name of the property or environment variable to retrieve.
     * @return The value of the property or environment variable.
     */
    private infix fun Project.prop(name: String): String {
      return (System.getenv(name) ?: findProperty(name)) as String
    }
  }
}
