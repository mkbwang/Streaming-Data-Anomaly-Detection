CREATE TABLE rainfall (
    inserttime            time,
    anomalytype         varchar(80),           
    points         int[][],           
    threshold       int
);

CREATE TABLE customer (
    inserttime            time,
    anomalytype         varchar(80),           
    userid          varchar(80),           
    threshold       int
);

CREATE TABLE others(
    inserttime          time,
    anomalytype         varchar(80),
    id                  varchar(80),
    threshold           float
);