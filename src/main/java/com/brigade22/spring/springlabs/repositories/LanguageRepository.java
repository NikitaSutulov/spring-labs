package com.brigade22.spring.springlabs.repositories;

import com.brigade22.spring.springlabs.entities.Language;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LanguageRepository extends CrudRepository<Language, String> {

    @Query("SELECT l FROM Language l WHERE l.name = :name")
    Language findLanguageByName(@Param("name") String name);

    @Modifying
    @Query("UPDATE Language l SET l.code = :newCode, l.name = :newName WHERE l.code = :code")
    void updateLanguageByCode(@Param("code") String code, @Param("newCode") String newCode, @Param("newName") String newName);

    void deleteLanguageByCode(String code);
}
