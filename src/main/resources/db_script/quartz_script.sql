-- qrtz_job_details
create table qrtz_job_details
(
    sched_name        varchar(120) not null,
    job_name          varchar(200) not null,
    job_group         varchar(200) not null,
    description       varchar(250),
    job_class_name    varchar(250) not null,
    is_durable        boolean      not null,
    is_nonconcurrent  boolean      not null,
    is_update_data    boolean      not null,
    requests_recovery boolean      not null,
    job_data          mediumblob,
    primary key (sched_name, job_name, job_group)
);

-- qrtz_triggers
create table qrtz_triggers
(
    sched_name     varchar(120) not null,
    trigger_name   varchar(200) not null,
    trigger_group  varchar(200) not null,
    job_name       varchar(200) not null,
    job_group      varchar(200) not null,
    description    varchar(250),
    next_fire_time bigint(13),
    prev_fire_time bigint(13),
    priority       integer,
    trigger_state  varchar(16)  not null,
    trigger_type   varchar(8)   not null,
    start_time     bigint(13)   not null,
    end_time       bigint(13),
    calendar_name  varchar(200),
    misfire_instr  smallint(2),
    job_data       mediumblob,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, job_name, job_group) references qrtz_job_details (sched_name, job_name, job_group)
);

-- qrtz_blob_triggers
create table qrtz_blob_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    blob_data     mediumblob,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

-- qrtz_calendars
create table qrtz_calendars
(
    sched_name    varchar(120) not null,
    calendar_name varchar(200) not null,
    calendar      mediumtext   not null,
    primary key (sched_name, calendar_name)
);

-- qrtz_cron_triggers
create table qrtz_cron_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    cron_expression varchar(120) not null,
    time_zone_id    varchar(80),
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

-- qrtz_fired_triggers
create table qrtz_fired_triggers
(
    sched_name        varchar(120) not null,
    entry_id          varchar(95)  not null,
    trigger_name      varchar(200) not null,
    trigger_group     varchar(200) not null,
    instance_name     varchar(200) not null,
    fired_time        bigint(13)   not null,
    sched_time        bigint(13)   not null,
    priority          integer      not null,
    state             varchar(16)  not null,
    job_name          varchar(200),
    job_group         varchar(200),
    is_nonconcurrent  boolean,
    requests_recovery boolean,
    primary key (sched_name, entry_id)
);

-- qrtz_job_listeners
create table qrtz_job_listeners
(
    sched_name   varchar(120) not null,
    job_name     varchar(200) not null,
    job_group    varchar(200) not null,
    job_listener varchar(200) not null,
    primary key (sched_name, job_name, job_group, job_listener),
    foreign key (sched_name, job_name, job_group) references qrtz_job_details (sched_name, job_name, job_group)
);

-- qrtz_locks
create table qrtz_locks
(
    sched_name varchar(120) not null,
    lock_name  varchar(40)  not null,
    primary key (sched_name, lock_name)
);

-- qrtz_paused_trigger_grps
create table qrtz_paused_trigger_grps
(
    sched_name    varchar(120) not null,
    trigger_group varchar(200) not null,
    primary key (sched_name, trigger_group)
);

-- qrtz_scheduler_state
create table qrtz_scheduler_state
(
    sched_name        varchar(120) not null,
    instance_name     varchar(200) not null,
    last_checkin_time bigint(13)   not null,
    checkin_interval  bigint(13)   not null,
    primary key (sched_name, instance_name)
);

-- qrtz_simple_triggers
create table qrtz_simple_triggers
(
    sched_name      varchar(120) not null,
    trigger_name    varchar(200) not null,
    trigger_group   varchar(200) not null,
    repeat_count    bigint(7)    not null,
    repeat_interval bigint(12)   not null,
    times_triggered bigint(10)   not null,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

-- qrtz_simprop_triggers
create table qrtz_simprop_triggers
(
    sched_name    varchar(120) not null,
    trigger_name  varchar(200) not null,
    trigger_group varchar(200) not null,
    str_prop_1    varchar(512),
    str_prop_2    varchar(512),
    str_prop_3    varchar(512),
    int_prop_1    int,
    int_prop_2    int,
    long_prop_1   bigint,
    long_prop_2   bigint,
    dec_prop_1    numeric(13, 4),
    dec_prop_2    numeric(13, 4),
    bool_prop_1   boolean,
    bool_prop_2   boolean,
    primary key (sched_name, trigger_name, trigger_group),
    foreign key (sched_name, trigger_name, trigger_group) references qrtz_triggers (sched_name, trigger_name, trigger_group)
);

-- For App
CREATE TABLE IF NOT EXISTS scheduler_job_info
(
    job_id          INT AUTO_INCREMENT PRIMARY KEY,
    job_name        VARCHAR(255),
    job_group       VARCHAR(255),
    job_status      VARCHAR(255),
    job_class       VARCHAR(255),
    cron_expression VARCHAR(255),
    job_description VARCHAR(255),
    interface_name  VARCHAR(255),
    repeat_time     BIGINT,
    cron_job        BOOLEAN
);
