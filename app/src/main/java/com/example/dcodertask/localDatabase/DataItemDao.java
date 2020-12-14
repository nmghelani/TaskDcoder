package com.example.dcodertask.localDatabase;

import com.example.dcodertask.model.DataItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DataItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(DataItem project);

    @Update
    void update(DataItem project);

    @Delete
    void delete(DataItem project);

    @Query("DELETE FROM DataItem")
    void deleteAllProjects();

    @Query("SELECT * FROM DataItem")
    LiveData<List<DataItem>> getAllProjects();

    @Query("SELECT * FROM DataItem WHERE " +
            "(title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%' OR username LIKE '%' || :query || '%' OR file LIKE '%' || :query || '%')" +
            " AND CASE WHEN :isProject IS NULL THEN 1 ELSE isProject = :isProject END" +
            " AND languageId IN (:languages)")
    DataSource.Factory<Integer, DataItem> getProjects(String query, Integer isProject, List<Integer> languages);
}
