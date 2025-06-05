package com.example.materialplayer.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.materialplayer.data.local.dto.FolderItemDto
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
    @Query("SELECT * FROM tracks ORDER BY title COLLATE NOCASE")
    fun allTracksByTitle(): Flow<List<TrackEntity>>

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
    fun tracksOfAlbum(albumId: Long): Flow<List<TrackEntity>>

    /** «Most-played» */
    @Query("SELECT * FROM tracks ORDER BY play_count DESC LIMIT :limit")
    fun mostPlayed(limit: Int): Flow<List<TrackEntity>>

    /* ------------ подпапки + количество ------------ */

    @Query(
        """
        SELECT * FROM tracks
        WHERE parent_dir = :docBase
        ORDER BY file_path COLLATE NOCASE
        """
    )
    fun getTracksInFolder(docBase: String): Flow<List<TrackEntity>>

    @Query(
        """
    WITH rel AS (
        SELECT
            parent_dir,
            SUBSTR(parent_dir, LENGTH(:docBase) + 4) AS rel_path
        FROM tracks
        WHERE parent_dir LIKE :docBase || '%'       -- внутри docBase
          AND parent_dir <>  :docBase               -- кроме самой папки
    )
    SELECT
        :docBase || '%2F' ||
        CASE
            WHEN INSTR(rel_path,'%2F') = 0
                 THEN rel_path
            ELSE SUBSTR(rel_path,1,INSTR(rel_path,'%2F')-1)
        END AS path,
        COUNT(*) AS trackCount,
        COUNT(DISTINCT
            CASE
                WHEN INSTR(rel_path,'%2F') = 0 THEN NULL
                ELSE SUBSTR(rel_path,1,INSTR(rel_path,'%2F')-1)
            END
        ) AS subfolderCount
    FROM rel
    GROUP BY path
    ORDER BY path
    """
    )
    fun getFolderInfos(docBase: String): Flow<List<FolderItemDto>>

    @Query("SELECT COUNT(*) FROM tracks")
    suspend fun countTracks(): Int

    @Query("""
    SELECT COUNT(*)
    FROM   tracks
    WHERE  parent_dir LIKE :docBase || '%'
    """)
    suspend fun countTracksRecursive(docBase: String): Int

    @Query("UPDATE tracks SET play_count = play_count + 1 WHERE id = :trackId")
    suspend fun incrementPlayCount(trackId: Long)

    @Query(
        """
    SELECT COUNT(*) FROM tracks
    WHERE parent_dir = :dir
    """
    )
    suspend fun countDirectTracks(dir: String): Int

    /** количество *уникальных* подпапок первого уровня (depth = 1) */
    @Query("""
    SELECT COUNT(DISTINCT
       CASE
         WHEN INSTR(rel, '%2F') = 0
              THEN rel
         ELSE SUBSTR(rel, 1, INSTR(rel, '%2F') - 1)
       END)
    FROM (
        SELECT SUBSTR(parent_dir, LENGTH(:dir) + 1) AS rel
        FROM   tracks
        WHERE  parent_dir LIKE :dir || '%'
              AND parent_dir <> :dir
    )
    """)
    suspend fun countDistinctSubDirs(dir: String): Int

}
