package com.dnd.niceteam.domain.comment;

import java.util.Optional;

public interface CommentRepositoryCustom {
    Optional<Long> findMaxGroupNo();
}
