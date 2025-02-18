package de.learneasy.repositories;

import de.learneasy.model.School;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

public interface SchoolsRepository {
    @NotNull School getExampleSchool();
    void save(@NotNull School school);
}

@Component
class SchoolsRepositoryImpl implements SchoolsRepository {
    @PersistenceContext
    private @NotNull EntityManager entityManager;

    @Override
    @Transactional
    public @NotNull School getExampleSchool() {
        @Nullable School exampleSchool = entityManager
                                            .createQuery("SELECT school FROM School school", School.class)
                                            .setMaxResults(1)
                                            .getResultStream()
                                            .findFirst()
                                            .orElse(null);

        if (exampleSchool == null) {
            exampleSchool = School.create("Christian-Wolfrum-Schule", "5573.Sekretariat@schule.bayern.de").getRight();
            entityManager.persist(exampleSchool);
        }

        return exampleSchool;
    }

    @Override
    @Transactional
    public void save(@NotNull School school) {
        entityManager.persist(school);
    }
}