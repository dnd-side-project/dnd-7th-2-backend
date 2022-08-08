package com.dnd.niceteam.domain.review;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum MemberReviewTagName {

    책임감_굿("책임감 굿")
    , 마감을_칼같이("마감을 칼같이")
    , 분위기_메이커("분위기 메이커")
    , 시간매너_끝판왕("시간매너 끝판왕")
    ;

    private final String kor;

    public static final Map<String, MemberReviewTagName> map = new HashMap<>();

    static {
        for (MemberReviewTagName tagName : MemberReviewTagName.values()) {
            map.put(tagName.getKor(), tagName);
        }
    }

    public static MemberReviewTagName getByKor(String tagName) {
        return map.get(tagName);
    }

}
