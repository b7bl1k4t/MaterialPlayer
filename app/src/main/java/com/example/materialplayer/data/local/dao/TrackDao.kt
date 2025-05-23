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

    /* ------------ вставка / обновление ------------ */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(tracks: List<TrackEntity>)

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
        SELECT 
          parent_dir AS path,
          CASE
             WHEN INSTR(SUBSTR(parent_dir, LENGTH(:basePath)+2), '/') > 0
             THEN SUBSTR(parent_dir, LENGTH(:basePath)+2, 
                         INSTR(SUBSTR(parent_dir, LENGTH(:basePath)+2), '/')-1)
             ELSE SUBSTR(parent_dir, LENGTH(:basePath)+2)
          END AS name,
          COUNT(*) AS trackCount
        FROM tracks
        WHERE parent_dir LIKE :basePath || '/%'
        GROUP BY parent_dir
        ORDER BY name
    """)
    fun subfolders(basePath: String): Flow<List<FolderItem>>

    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun countTracks(): Int

    @Query("UPDATE tracks SET play_count = play_count + 1 WHERE id = :trackId")
    suspend fun incrementPlayCount(trackId: Long)
}