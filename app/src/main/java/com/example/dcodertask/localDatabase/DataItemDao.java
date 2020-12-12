package com.example.dcodertask.localDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DataItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Project project);

    @Update
    void update(Project project);

    @Delete
    void delete(Project project);

    @Query("DELETE FROM project")
    void deleteAllProjects();

    @Query("SELECT * FROM project")
    LiveData<List<Project>> getAllProjects();

    @Query("SELECT * FROM project WHERE " +
            "(title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR file LIKE '%' || :query || '%')" +
            " AND CASE WHEN :isProject IS NULL THEN 1 ELSE isProject = :isProject END" +
            " AND language_id IN (:languages)")
    List<Project> getProjects(String query, Integer isProject, List<Integer> languages);
}
