alter table applicant
    drop foreign key FK_applicant_member;

alter table applicant
    drop foreign key FK_applicant_recruiting;

alter table bookmark
    drop foreign key FK_bookmark_member;

alter table bookmark
    drop foreign key FK_bookmark_recruiting;

alter table comment
    drop foreign key FK_comment_member;

alter table comment
    drop foreign key FK_comment_recruiting;

alter table department
    drop foreign key FK_department_university;

alter table lecture_project
    drop foreign key FK_lecture_project_department;

alter table lecture_project
    drop foreign key FK_lecture_project_project;

alter table lecture_time
    drop foreign key FK_lecture_time_project;

alter table member
    drop foreign key FK_member_account;

alter table member
    drop foreign key FK_member_department;

alter table member
    drop foreign key FK_member_member_score;

alter table member
    drop foreign key FK_member_university;

alter table member_interest
    drop foreign key FK_member_interest_member;

alter table member_review
    drop foreign key FK_member_review_reviewee;

alter table member_review
    drop foreign key FK_member_review_reviewer;

alter table member_review_tag
    drop foreign key FK_member_review_tag_member_review;

alter table project_member
    drop foreign key FK_project_member_member;

alter table project_member
    drop foreign key FK_project_member_project;

alter table recruiting
    drop foreign key FK_recruiting_member;

alter table recruiting
    drop foreign key FK_recruiting_project;

alter table recruiting_activity_day_time
    drop foreign key FK_recruiting_activity_day_time_recruiting;

alter table recruiting_personality_adjective
    drop foreign key FK_recruiting_personality_adjective_recruiting;

alter table recruiting_personality_noun
    drop foreign key FK_recruiting_personality_noun_recruiting;

alter table review_tag_num
    drop foreign key FK_review_tag_num_member_score;

alter table side_project
    drop foreign key FK_side_project_project;

alter table vote
    drop foreign key FK_vote_vote_group;

alter table vote
    drop foreign key FK_vote_project_member;

alter table vote_group
    drop foreign key FK_vote_group_project;

alter table vote_group
    drop foreign key FK_vote_group_candidate;

drop table if exists account;

drop table if exists applicant;

drop table if exists bookmark;

drop table if exists comment;

drop table if exists department;

drop table if exists email_auth;

drop table if exists lecture_project;

drop table if exists lecture_time;

drop table if exists member;

drop table if exists member_interest;

drop table if exists member_review;

drop table if exists member_review_tag;

drop table if exists member_score;

drop table if exists project;

drop table if exists project_member;

drop table if exists recruiting;

drop table if exists recruiting_activity_day_time;

drop table if exists recruiting_personality_adjective;

drop table if exists recruiting_personality_noun;

drop table if exists review_tag_num;

drop table if exists side_project;

drop table if exists university;

drop table if exists vote;

drop table if exists vote_group;

create table account
(
    account_id         bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    use_yn             bit          not null,
    created_by         varchar(45)  not null,
    last_modified_by   varchar(45)  not null,
    deleted_at         datetime(6),
    email              varchar(65)  not null,
    password           varchar(105) not null,
    refresh_token      varchar(255),
    primary key (account_id)
) engine = InnoDB;

create table applicant
(
    applicant_id       bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    joined             bit         not null,
    member_id          bigint      not null,
    recruiting_id      bigint      not null,
    primary key (applicant_id)
) engine = InnoDB;

create table bookmark
(
    bookmark_id        bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    member_id          bigint      not null,
    recruiting_id      bigint      not null,
    primary key (bookmark_id)
) engine = InnoDB;

create table comment
(
    comment_id         bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    use_yn             bit          not null,
    created_by         varchar(45)  not null,
    last_modified_by   varchar(45)  not null,
    content            varchar(255) not null,
    parent_id          bigint       not null,
    member_id          bigint       not null,
    recruiting_id      bigint       not null,
    primary key (comment_id)
) engine = InnoDB;

create table department
(
    department_id    bigint      not null auto_increment,
    college_name     varchar(25) not null,
    main_branch_type varchar(15) not null,
    name             varchar(25) not null,
    region           varchar(15) not null,
    university_id    bigint      not null,
    primary key (department_id)
) engine = InnoDB;

create table email_auth
(
    email_auth_id      bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    auth_key           varchar(25) not null,
    authenticated      bit         not null,
    email              varchar(65) not null,
    primary key (email_auth_id)
) engine = InnoDB;

create table lecture_project
(
    professor     varchar(50) not null,
    project_id    bigint      not null,
    department_id bigint      not null,
    primary key (project_id)
) engine = InnoDB;

create table lecture_time
(
    project_id  bigint       not null,
    day_of_week varchar(255) not null,
    start_time  time         not null,
    primary key (project_id, day_of_week, start_time)
) engine = InnoDB;

create table member
(
    member_id             bigint       not null auto_increment,
    created_date          datetime(6)  not null,
    last_modified_date    datetime(6)  not null,
    use_yn                bit          not null,
    created_by            varchar(45)  not null,
    last_modified_by      varchar(45)  not null,
    admission_year        integer      not null,
    introduction          varchar(300) not null,
    introduction_url      varchar(255) not null,
    nickname              varchar(25)  not null,
    personality_adjective varchar(15)  not null,
    personality_noun      varchar(15)  not null,
    account_id            bigint       not null,
    department_id         bigint       not null,
    member_score_id       bigint       not null,
    university_id         bigint       not null,
    primary key (member_id)
) engine = InnoDB;

create table member_interest
(
    member_id bigint      not null,
    field     varchar(25) not null,
    primary key (member_id, field)
) engine = InnoDB;

create table member_review
(
    member_review_id    bigint      not null auto_increment,
    created_date        datetime(6) not null,
    last_modified_date  datetime(6) not null,
    use_yn              bit         not null,
    created_by          varchar(45) not null,
    last_modified_by    varchar(45) not null,
    participation_score integer,
    skipped             bit         not null,
    team_again_score    integer,
    reviewee_id         bigint      not null,
    reviewer_id         bigint      not null,
    primary key (member_review_id)
) engine = InnoDB;

create table member_review_tag
(
    member_review_tag_id bigint       not null auto_increment,
    created_date         datetime(6)  not null,
    last_modified_date   datetime(6)  not null,
    use_yn               bit          not null,
    created_by           varchar(45)  not null,
    last_modified_by     varchar(45)  not null,
    tag                  varchar(255) not null,
    member_review_id     bigint       not null,
    primary key (member_review_tag_id)
) engine = InnoDB;

create table member_score
(
    member_score_id           bigint           not null auto_increment,
    created_date              datetime(6)      not null,
    last_modified_date        datetime(6)      not null,
    use_yn                    bit              not null,
    created_by                varchar(45)      not null,
    last_modified_by          varchar(45)      not null,
    level                     integer          not null,
    review_num                integer          not null,
    total_feeds               double precision not null,
    total_participation_score integer          not null,
    total_team_again_score    integer          not null,
    primary key (member_score_id)
) engine = InnoDB;

create table project
(
    type               varchar(31)  not null,
    project_id         bigint       not null auto_increment,
    created_date       datetime(6)  not null,
    last_modified_date datetime(6)  not null,
    use_yn             bit          not null,
    created_by         varchar(45)  not null,
    last_modified_by   varchar(45)  not null,
    end_date           date         not null,
    member_count       integer      not null,
    name               varchar(255) not null,
    start_date         date         not null,
    status             varchar(255) not null,
    primary key (project_id)
) engine = InnoDB;

create table project_member
(
    project_member_id  bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    created_by         varchar(45) not null,
    last_modified_by   varchar(45) not null,
    expelled           bit         not null,
    member_id          bigint      not null,
    project_id         bigint      not null,
    primary key (project_member_id)
) engine = InnoDB;

create table recruiting
(
    recruiting_id           bigint       not null auto_increment,
    created_date            datetime(6)  not null,
    last_modified_date      datetime(6)  not null,
    use_yn                  bit          not null,
    created_by              varchar(45)  not null,
    last_modified_by        varchar(45)  not null,
    activity_area           integer      not null,
    bookmark_count          integer      not null,
    comment_count           integer      not null,
    content                 varchar(500),
    intro_link              varchar(255) not null,
    pool_up_count           integer      not null,
    pool_up_date            datetime(6),
    recruiting_end_date     date,
    recruiting_member_count integer      not null,
    recruiting_type         integer      not null,
    status                  varchar(255),
    title                   varchar(100) not null,
    member_id               bigint       not null,
    project_id              bigint       not null,
    primary key (recruiting_id)
) engine = InnoDB;

create table recruiting_activity_day_time
(
    recruiting_id bigint       not null,
    day_of_week   varchar(255) not null,
    end_time      time         not null,
    start_time    time         not null,
    primary key (recruiting_id, day_of_week, end_time, start_time)
) engine = InnoDB;

create table recruiting_personality_adjective
(
    recruiting_id bigint      not null,
    adjective     varchar(25) not null,
    primary key (recruiting_id, adjective)
) engine = InnoDB;

create table recruiting_personality_noun
(
    recruiting_id bigint      not null,
    noun          varchar(25) not null,
    primary key (recruiting_id, noun)
) engine = InnoDB;

create table review_tag_num
(
    member_score_id bigint      not null,
    num             integer     not null,
    review_tag      varchar(45) not null,
    primary key (member_score_id, review_tag)
) engine = InnoDB;

create table side_project
(
    field          varchar(25) not null,
    field_category varchar(25) not null,
    project_id     bigint      not null,
    primary key (project_id)
) engine = InnoDB;

create table university
(
    university_id bigint      not null auto_increment,
    email_domain  varchar(25) not null,
    name          varchar(25) not null,
    primary key (university_id)
) engine = InnoDB;

create table vote
(
    vote_id            bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    created_by         varchar(45) not null,
    last_modified_by   varchar(45) not null,
    choice             bit         not null,
    vote_group_id      bigint      not null,
    project_member_id  bigint      not null,
    primary key (vote_id)
) engine = InnoDB;

create table vote_group
(
    type               varchar(31) not null,
    vote_group_id      bigint      not null auto_increment,
    created_date       datetime(6) not null,
    last_modified_date datetime(6) not null,
    use_yn             bit         not null,
    created_by         varchar(45) not null,
    last_modified_by   varchar(45) not null,
    complete           bit         not null,
    project_id         bigint      not null,
    candidate_id       bigint      not null,
    primary key (vote_group_id)
) engine = InnoDB;

alter table account
    add constraint UK_account_email unique (email);

alter table member
    add constraint UK_member_account_id unique (account_id);

alter table member
    add constraint UK_member_member_score_id unique (member_score_id);

alter table member
    add constraint UK_member_nickname unique (nickname);

alter table project_member
    add constraint UK_project_member_project_id_member_id unique (project_id, member_id);

alter table applicant
    add constraint FK_applicant_member
        foreign key (member_id)
            references member (member_id);

alter table applicant
    add constraint FK_applicant_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table bookmark
    add constraint FK_bookmark_member
        foreign key (member_id)
            references member (member_id);

alter table bookmark
    add constraint FK_bookmark_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table comment
    add constraint FK_comment_member
        foreign key (member_id)
            references member (member_id);

alter table comment
    add constraint FK_comment_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table department
    add constraint FK_department_university
        foreign key (university_id)
            references university (university_id);

alter table lecture_project
    add constraint FK_lecture_project_department
        foreign key (department_id)
            references department (department_id);

alter table lecture_project
    add constraint FK_lecture_project_project
        foreign key (project_id)
            references project (project_id);

alter table lecture_time
    add constraint FK_lecture_time_project
        foreign key (project_id)
            references lecture_project (project_id);

alter table member
    add constraint FK_member_account
        foreign key (account_id)
            references account (account_id);

alter table member
    add constraint FK_member_department
        foreign key (department_id)
            references department (department_id);

alter table member
    add constraint FK_member_member_score
        foreign key (member_score_id)
            references member_score (member_score_id);

alter table member
    add constraint FK_member_university
        foreign key (university_id)
            references university (university_id);

alter table member_interest
    add constraint FK_member_interest_member
        foreign key (member_id)
            references member (member_id);

alter table member_review
    add constraint FK_member_review_reviewee
        foreign key (reviewee_id)
            references project_member (project_member_id);

alter table member_review
    add constraint FK_member_review_reviewer
        foreign key (reviewer_id)
            references project_member (project_member_id);

alter table member_review_tag
    add constraint FK_member_review_tag_member_review
        foreign key (member_review_id)
            references member_review (member_review_id);

alter table project_member
    add constraint FK_project_member_member
        foreign key (member_id)
            references member (member_id);

alter table project_member
    add constraint FK_project_member_project
        foreign key (project_id)
            references project (project_id);

alter table recruiting
    add constraint FK_recruiting_member
        foreign key (member_id)
            references member (member_id);

alter table recruiting
    add constraint FK_recruiting_project
        foreign key (project_id)
            references project (project_id);

alter table recruiting_activity_day_time
    add constraint FK_recruiting_activity_day_time_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table recruiting_personality_adjective
    add constraint FK_recruiting_personality_adjective_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table recruiting_personality_noun
    add constraint FK_recruiting_personality_noun_recruiting
        foreign key (recruiting_id)
            references recruiting (recruiting_id);

alter table review_tag_num
    add constraint FK_review_tag_num_member_score
        foreign key (member_score_id)
            references member_score (member_score_id);

alter table side_project
    add constraint FK_side_project_project
        foreign key (project_id)
            references project (project_id);

alter table vote
    add constraint FK_vote_vote_group
        foreign key (vote_group_id)
            references vote_group (vote_group_id);

alter table vote
    add constraint FK_vote_project_member
        foreign key (project_member_id)
            references project_member (project_member_id);

alter table vote_group
    add constraint FK_vote_group_project
        foreign key (project_id)
            references project (project_id);

alter table vote_group
    add constraint FK_vote_group_candidate
        foreign key (candidate_id)
            references project_member (project_member_id);
