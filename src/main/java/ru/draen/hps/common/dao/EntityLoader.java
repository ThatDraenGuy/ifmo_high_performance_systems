package ru.draen.hps.common.dao;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import ru.draen.hps.I18n;
import ru.draen.hps.common.entity.ADeletableEntity;
import ru.draen.hps.common.entity.IEntity;
import ru.draen.hps.common.exception.ProcessingException;
import ru.draen.hps.common.label.ILabelService;

import static java.util.Objects.isNull;

@Component
public class EntityLoader {
    private static final ILabelService lbs = I18n.getLabelService();

    @PersistenceContext
    protected EntityManager entityManager;

    @SuppressWarnings("unchecked")
    public <E extends IEntity<ID>, ID> E load(@Nullable E entity) {
        if (isNull(entity)) return null;
        E result = (E) entityManager.find(entity.getClass(), entity.getId());
        if (isNull(result)) {
            throw new ProcessingException(
                    lbs.msg("ProcessingException.EntityLoader.notFound", entity.getClass().getSimpleName(), entity.getId()));
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <E extends ADeletableEntity<ID>, ID> E load(@Nullable E entity) {
        if (isNull(entity)) return null;
        E result = (E) entityManager.find(entity.getClass(), entity.getId());
        if (isNull(result) || result.isDeleted()) {
            throw new ProcessingException(
                    lbs.msg("ProcessingException.EntityLoader.notFound", entity.getClass().getSimpleName(), entity.getId()));
        }
        return result;
    }
}
