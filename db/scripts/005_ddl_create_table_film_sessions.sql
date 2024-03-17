create table film_sessions
(
    id         serial primary key,
    film_id    int references films (id) not null,
    halls_id   int references halls (id) not null,
    start_time time without time zone    not null,
    end_time   time without time zone    not null,
    price      int                       not null
);