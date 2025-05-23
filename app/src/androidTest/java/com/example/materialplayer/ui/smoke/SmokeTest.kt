package com.example.materialplayer.ui.smoke

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Инструментальный Smoke-тест без кастомных матчеров
 */
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SmokeTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        runBlocking {
            // сразу заливаем тестовые данные
            TestDatabaseModule.provideSmokeDataSeeder(
                TestDatabaseModule.provideInMemoryDb(composeRule.activity)
            ).seedIfEmpty()
        }
    }

    @get:Rule
    val composeRule = createAndroidComposeRule<SmokeTestActivity>()

    @Test
    fun smokeSectionsAreVisible() {
        composeRule.onNodeWithText("Tracks by Title").assertExists()
        composeRule.onNodeWithText("Recently Played").assertExists()
        composeRule.onNodeWithText("Playlists").assertExists()
    }

    @Test
    fun clickingTrackAddsToHistory() {
        // До клика история пуста (нет элементов, начинающихся с '#')
        composeRule.onNodeWithText("#", substring = true).assertDoesNotExist()

        // Кликаем по первому треку
        composeRule.onNodeWithText("Adele - Rolling In The Deep")
            .performScrollTo()
            .performClick()

        // После клика в истории появляется запись вида "#1 at"
        composeRule.onNodeWithText("#1 at", substring = true).assertExists()
    }

    @Test
    fun addPlaylistViaUi() {
        // Вводим название в поле
        composeRule.onNodeWithText("New playlist", substring = true)
            .performClick()
            .performTextInput("My Test PL")

        // Нажимаем Add
        composeRule.onNodeWithText("Add").performClick()

        // Проверяем, что появился плейлист с этим именем
        composeRule.onNodeWithText("My Test PL").assertExists()
    }
}
