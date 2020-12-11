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

    @Query("SELECT * FROM project ORDER BY id")
    LiveData<List<Project>> getAllProjects();

    @Query("SELECT * FROM project WHERE title LIKE '%' || :titleQuery || '%' " +
            "OR description LIKE '%' || :titleQuery || '%'" +
            "OR username LIKE '%' || :titleQuery || '%'" +
            "OR file LIKE '%' || :titleQuery || '%'")
    List<Project> getProjectsByTitle(String titleQuery);

    @Query("SELECT * FROM project WHERE (isProject = CASE WHEN :isProject <> 2 THEN :isProject ELSE (isProject = 1 OR isProject = 2) END)" +
            "AND (language_id IN (:languages))")
    List<Project> getProjects(int isProject, List<Integer> languages);
}
