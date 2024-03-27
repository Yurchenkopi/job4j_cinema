create table film_sessions
(
    id         serial primary key,
    film_id    int references films (id)     ON DELETE CASCADE          not null,
    hall_id   int references halls (id)      ON DELETE CASCADE          not null,
    start_time timestamp                                                not null,
    end_time   timestamp                                                not null,
    price      int                                                      not null
);