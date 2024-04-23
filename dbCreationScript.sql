create table bad_words
(
    id   bigint auto_increment
        primary key,
    word varchar(255) null
);

create table inns
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);

create table articles
(
    id        bigint auto_increment
        primary key,
    abstract  text collate utf8mb4_unicode_ci null,
    bad_words bit                             null,
    snippet   text collate utf8mb4_unicode_ci null,
    title     text collate utf8mb4_unicode_ci null,
    url       text                            null,
    inn_id    bigint                          null,
    author    text collate utf8mb4_unicode_ci null,
    constraint FK5ssn1b93w0yya9l03yn8wvnfq
        foreign key (inn_id) references inns (id)
);

create table queries
(
    id           bigint auto_increment
        primary key,
    client_url   text   null,
    server_query text   null,
    inn_id       bigint null,
    constraint FK7brpv5pnuh0yip9xhhecjw8yh
        foreign key (inn_id) references inns (id)
);


