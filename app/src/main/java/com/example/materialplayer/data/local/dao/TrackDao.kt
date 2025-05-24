package com.example.materialplayer.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.materialplayer.data.local.dto.FolderItem
import com.example.materialplayer.data.local.entity.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {

    /** Удалить все треки */
    @Query("DELETE FROM tracks")
    suspend fun deleteAll()

    /** Вставить список треков: replace при конфликте по primary key */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(tracks: List<TrackEntity>)

    @Query("UPDATE tracks SET play_count = play_count + 1 WHERE id = :trackId")
    suspend fun incPlayCount(trackId: Long)

    /* ------------ выборки для экранов ------------ */

    /** Треки по названию */
    @Query("SELECT * FROM tracks ORDER BY title")
    fun tracksByTitle(): PagingSource<Int, TrackEntity>

    /** Путь в файловой системе */
    @Query("SELECT * FROM tracks ORDER BY parent_dir, file_path")
    fun tracksByPath(): PagingSource<Int, TrackEntity>

    /** Треки по артисту + вложенная сортировка по альбому/номеру трека */
    @Query("""
        SELECT * FROM tracks
        WHERE artist_id = :artistId
        ORDER BY album_id, track_no
    """)
    fun tracksByArtist(artistId: Long): PagingSource<Int, TrackEntity>

    /** Треки по альбому */
    @Query("""
        SELECT * FROM tracks
        WHERE album_id = :albumId
        ORDER BY track_no
    """)
    fun tracksByAlbum(albumId: Long): PagingSource<Int, TrackEntity>

    /** «Most-played» */
    @Query("SELECT * FROM tracks ORDER BY play_count DESC LIMIT :limit")
    fun mostPlayed(limit: Int): Flow<List<TrackEntity>>

    /* ------------ подпапки + количество ------------ */
    @Query("""
        WITH folders AS (
          SELECT DISTINCT
            parent_dir AS path,
            SUBSTR(parent_dir, INSTR(REVERSE(parent_dir), '/') + 1) AS name,
            SUBSTR(parent_dir, 1, LENGTH(parent_dir) - LENGTH(SUBSTR(parent_dir, INSTR(REVERSE(parent_dir), '/') + 1)) - 1) AS parentDir
          FROM tracks
        )
        SELECT
          f.path,
          f.name,
          f.parentDir,
          (SELECT COUNT(DISTINCT 
             SUBSTR(t.parent_dir, LENGTH(f.path) + 2, 
                    INSTR(SUBSTR(t.parent_dir, LENGTH(f.path) + 2), '/') - 1)
           )
           FROM tracks AS t
           WHERE t.parent_dir LIKE f.path || '/%') AS subfolderCount,
          (SELECT COUNT(*) FROM tracks AS t2 WHERE t2.parent_dir = f.path) AS trackCount
        FROM folders AS f
    """)
    fun getFolderInfos(): Flow<List<FolderItem>>

    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun countTracks(): Int

    @Query("UPDATE tracks SET play_count = play_count + 1 WHERE id = :trackId")
    suspend fun incrementPlayCount(trackId: Long)
}