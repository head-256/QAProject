package com.dubhad.qaproject.dao;

import com.dubhad.qaproject.ProjectException;
import com.dubhad.qaproject.entity.Tag;

import java.util.List;

/**
 * Interface, that provides methods for TagDaos. All concrete TagDaos must implement it
 */
public interface TagDao extends AbstractDao<Long, Tag> {
    /**
     * Initializes id in tag entity
     * @param entity entity to initialize id
     * @return true, if new tag was created, false otherwise
     * @throws ProjectException in the case of unexpected error on lower level
     */
    boolean initTagIdByText(Tag entity) throws ProjectException;

    /**
     * Finds tags by question id
     * @param questionId id of question
     * @return list of all found tags
     * @throws ProjectException in the case of unexpected error on lower level
     */
    List<Tag> findTagsByQuestionId(long questionId) throws ProjectException;
}
