package com.example.materialplayer.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.materialplayer.data.local.dto.AlbumWithArtist
import com.example.materialplayer.data.local.dto.AlbumWithTracks
import com.example.materialplayer.data.local.entity.AlbumEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlbumDao {
    /** Вставка списка альбомов с заменой при конфликте */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(albums: List<AlbumEntity>)

    /** Найти альбом по заголовку и id артиста */
    @Query("SELECT * FROM albums WHERE title = :title AND artist_id = :artistId LIMIT 1")
    suspend fun findByTitleAndArtist(title: String, artistId: Long): AlbumEntity?

    @Transaction
    @Query("""
    SELECT al.*, ar.name AS artistName
    FROM albums al
    LEFT JOIN artists ar ON ar.id = al.artist_id
    ORDER BY al.title COLLATE NOCASE
    """)
    fun albumsWithArtist(): Flow<List<AlbumWithArtist>>

    /** Удалить все альбомы */
    @Query("DELETE FROM albums")
    suspend fun deleteAll()

    @Query("""
        SELECT * FROM albums
        WHERE (artist_id = :artistId IS NULL OR artist_id = :artistId)
        ORDER BY title
    """)
    fun albumsByArtist(artistId: Long?): PagingSource<Int, AlbumEntity>

    @Query("SELECT * FROM albums WHERE id = :albumId LIMIT 1")
    suspend fun albumById(albumId: Long): AlbumEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnore(album: AlbumEntity): Long      // вернёт id или -1

    @Query("SELECT id FROM albums WHERE title = :title LIMIT 1")
    suspend fun findIdByTitle(title: String): Long?

    @Transaction
    @Query("SELECT * FROM albums WHERE id = :id")
    fun albumWithTracks(id: Long): Flow<AlbumWithTracks>
    @Transaction
    @Query("SELECT * FROM albums WHERE id = :albumId")
    fun getAlbumWithTracks(albumId: Long): Flow<AlbumWithTracks>
}