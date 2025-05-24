package com.example.materialplayer.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.materialplayer.data.local.dto.ArtistWithAlbums
import com.example.materialplayer.data.local.entity.ArtistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(entity: ArtistEntity): Long

    /** Вставка списка артистов с заменой по уникальному имени */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(artists: List<ArtistEntity>)

    /** Получить артиста по имени */
    @Query("SELECT * FROM artists WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): ArtistEntity?

    /** Удалить всех артистов */
    @Query("DELETE FROM artists")
    suspend fun deleteAll()

    @Query("SELECT id FROM artists WHERE name = :name LIMIT 1")
    suspend fun findIdByName(name: String): Long?

    /** «Upsert»: вернёт существующий id или вставит нового артиста */
    @Transaction
    suspend fun getOrCreate(name: String): Long {
        val inserted = insertIgnore(ArtistEntity(id = 0, name = name.trim()))
        return if (inserted != -1L) inserted
        else findIdByName(name.trim())
            ?: error("Artist getOrCreate failed for $name")
    }

    @Query("SELECT * FROM artists ORDER BY name")
    fun allArtists(): Flow<List<ArtistEntity>>

    @Transaction
    @Query("SELECT * FROM artists WHERE id = :artistId")
    fun getArtistWithAlbums(artistId: Long): Flow<ArtistWithAlbums>
}